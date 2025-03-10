package de.emn4tor;

/*
 *  @author: Emn4tor
 *  @created: 10.03.2025
 */

import org.bukkit.Location;
import org.bukkit.World;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ScriptParser {
    private final AquilaCine plugin;

    public ScriptParser(AquilaCine plugin) {
        this.plugin = plugin;
    }

    /**
     * Parse a camera drive script file
     * @param scriptFile The script file to parse
     * @return The parsed CameraDrive, or null if parsing failed
     */
    public CameraDrive parseScript(File scriptFile) throws Exception {
        String name = scriptFile.getName().replace(".cdrive", "");
        List<Location> waypoints = new ArrayList<>();
        double speed = 5.0; // Default speed
        World world = plugin.getServer().getWorlds().get(0); // Default world

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(":", 2);
                if (parts.length != 2) {
                    continue;
                }

                String key = parts[0].trim().toLowerCase();
                String value = parts[1].trim();

                switch (key) {
                    case "name":
                        name = value;
                        break;
                    case "world":
                        World foundWorld = plugin.getServer().getWorld(value);
                        if (foundWorld != null) {
                            world = foundWorld;
                        } else {
                            plugin.getLogger().warning("World '" + value + "' not found, using default world");
                        }
                        break;
                    case "waypoint":
                        waypoints.add(parseWaypoint(value, world));
                        break;
                    case "speed":
                        try {
                            speed = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            plugin.getLogger().warning("Invalid speed value: " + value + ", using default");
                        }
                        break;
                }
            }
        }

        if (waypoints.isEmpty()) {
            throw new Exception("No waypoints defined in script");
        }

        return new CameraDrive(name, waypoints, speed);
    }

    /**
     * Parse a waypoint string in the format "x,y,z,yaw,pitch"
     * @param waypointStr The waypoint string
     * @param world The world for this waypoint
     * @return The parsed Location
     */
    private Location parseWaypoint(String waypointStr, World world) throws Exception {
        String[] coords = waypointStr.split(",");
        if (coords.length < 3) {
            throw new Exception("Waypoint must have at least x,y,z coordinates");
        }

        try {
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            double z = Double.parseDouble(coords[2]);

            float yaw = 0f;
            float pitch = 0f;

            if (coords.length >= 4) {
                yaw = Float.parseFloat(coords[3]);
            }

            if (coords.length >= 5) {
                pitch = Float.parseFloat(coords[4]);
            }

            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid coordinate format: " + waypointStr);
        }
    }
}

