package de.emn4tor;

/*
 *  @author: Emn4tor
 *  @created: 10.03.2025
 */

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;
import java.util.ArrayList;

public class CameraDrive {
    private final String name;
    private final List<Location> waypoints;
    private final double speed; // blocks per second

    public CameraDrive(String name, List<Location> waypoints, double speed) {
        this.name = name;
        this.waypoints = waypoints;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public List<Location> getWaypoints() {
        return waypoints;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * Starts the camera drive for a player
     * @param player The player to start the drive for
     * @param plugin Reference to the main plugin
     */
    public void startDrive(Player player, AquilaCine plugin) {
        if (waypoints.size() < 2) {
            player.sendMessage(plugin.formatMessage("<red>This camera drive doesn't have enough waypoints!</red>"));
            return;
        }

        // Create a new drive session
        new CameraDriveSession(player, this, plugin).start();
    }

    /**
     * Inner class to handle an active camera drive session
     */
    public static class CameraDriveSession {
        private final Player player;
        private final CameraDrive drive;
        private final AquilaCine plugin;
        private int currentWaypointIndex = 0;
        private int taskId = -1;
        private final Location originalLocation;

        public CameraDriveSession(Player player, CameraDrive drive, AquilaCine plugin) {
            this.player = player;
            this.drive = drive;
            this.plugin = plugin;
            this.originalLocation = player.getLocation().clone();
        }

        public void start() {
            // Teleport to first waypoint
            player.teleport(drive.getWaypoints().get(0));

            // Schedule the movement task
            taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (currentWaypointIndex >= drive.getWaypoints().size() - 1) {
                    // End of drive
                    stop();
                    player.sendMessage(plugin.formatMessage("<green>Camera drive complete!</green>"));
                    return;
                }

                // Get current and next waypoints
                Location current = drive.getWaypoints().get(currentWaypointIndex);
                Location next = drive.getWaypoints().get(currentWaypointIndex + 1);

                // Calculate distance and direction
                double distance = current.distance(next);
                Vector direction = next.toVector().subtract(current.toVector()).normalize();

                // Calculate steps based on speed
                double stepsTotal = distance / (drive.getSpeed() / 20); // 20 ticks per second

                // Move player along the path
                Location playerLoc = player.getLocation();
                Vector movement = direction.multiply(drive.getSpeed() / 20);
                playerLoc.add(movement);

                // Interpolate yaw and pitch
                float yawDiff = next.getYaw() - current.getYaw();
                float pitchDiff = next.getPitch() - current.getPitch();

                float yawStep = yawDiff / (float)stepsTotal;
                float pitchStep = pitchDiff / (float)stepsTotal;

                playerLoc.setYaw(playerLoc.getYaw() + yawStep);
                playerLoc.setPitch(playerLoc.getPitch() + pitchStep);

                // Teleport player
                player.teleport(playerLoc);

                // Check if we've reached the next waypoint
                if (playerLoc.distance(next) < 0.5) {
                    currentWaypointIndex++;
                }
            }, 0L, 1L); // Run every tick

            // Register this session with the drive manager
            plugin.getDriveManager().registerActiveSession(player, this);

            player.sendMessage(plugin.formatMessage("<green>Started camera drive: " + drive.getName() + "</green>"));
        }

        public void stop() {
            if (taskId != -1) {
                plugin.getServer().getScheduler().cancelTask(taskId);
                taskId = -1;
                plugin.getDriveManager().unregisterActiveSession(player);

                // Option to return to start location
                if (plugin.getConfig().getBoolean("return-to-original-location", false)) {
                    player.teleport(originalLocation);
                }
            }
        }
    }
}

