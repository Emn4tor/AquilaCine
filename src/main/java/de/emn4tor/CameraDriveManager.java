package de.emn4tor;

/*
 *  @author: Emn4tor
 *  @created: 10.03.2025
 */

import org.bukkit.entity.Player;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class CameraDriveManager {
    private final AquilaCine plugin;
    private final Map<String, CameraDrive> drives = new HashMap<>();
    private final Map<Player, CameraDrive.CameraDriveSession> activeSessions = new HashMap<>();

    public CameraDriveManager(AquilaCine plugin) {
        this.plugin = plugin;
    }

    /**
     * Register a new camera drive
     * @param drive The drive to register
     */
    public void registerDrive(CameraDrive drive) {
        drives.put(drive.getName().toLowerCase(), drive);
    }

    /**
     * Get a camera drive by name
     * @param name The name of the drive
     * @return The camera drive, or null if not found
     */
    public CameraDrive getDrive(String name) {
        return drives.get(name.toLowerCase());
    }

    /**
     * Get all registered camera drives
     * @return Collection of all camera drives
     */
    public Collection<CameraDrive> getAllDrives() {
        return drives.values();
    }

    /**
     * Clear all registered drives
     */
    public void clearDrives() {
        drives.clear();
    }

    /**
     * Register an active drive session
     * @param player The player in the session
     * @param session The drive session
     */
    public void registerActiveSession(Player player, CameraDrive.CameraDriveSession session) {
        activeSessions.put(player, session);
    }

    /**
     * Unregister an active drive session
     * @param player The player to unregister
     */
    public void unregisterActiveSession(Player player) {
        activeSessions.remove(player);
    }

    /**
     * Check if a player is in an active drive session
     * @param player The player to check
     * @return True if the player is in a session
     */
    public boolean isInDriveSession(Player player) {
        return activeSessions.containsKey(player);
    }

    /**
     * Stop all active drive sessions
     */
    public void stopAllDrives() {
        for (CameraDrive.CameraDriveSession session : activeSessions.values()) {
            try {
                session.stop();
            } catch (Exception e) {
                plugin.getLogger().warning("Error stopping drive session: " + e.getMessage());
            }
        }
        activeSessions.clear();
    }

    /**
     * Stop a player's active drive session
     * @param player The player whose session to stop
     */
    public void stopDrive(Player player) {
        CameraDrive.CameraDriveSession session = activeSessions.get(player);
        if (session != null) {
            session.stop();
            activeSessions.remove(player);
        }
    }
}

