package de.emn4tor;

import org.bukkit.World;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ScriptManager {
    private final AquilaCine plugin;
    private final ScriptParser scriptParser;
    private final File scriptsDir;

    public ScriptManager(AquilaCine plugin) {
        this.plugin = plugin;
        this.scriptParser = new ScriptParser(plugin);
        this.scriptsDir = new File(plugin.getDataFolder(), "scripts");

        // Create scripts directory if it doesn't exist
        if (!scriptsDir.exists()) {
            scriptsDir.mkdirs();
        }
    }

    /**
     * Load all camera drives from script files
     */
    public void loadCameraDrives() {
        // Clear existing drives
        plugin.getDriveManager().clearDrives();
        plugin.getDriveManager().stopAllDrives();

        // Create example scripts if directory is empty
        if (scriptsDir.listFiles() == null || scriptsDir.listFiles().length == 0) {
            createExampleScripts();
        }

        // Load all script files
        File[] scriptFiles = scriptsDir.listFiles((dir, name) -> name.endsWith(".cdrive"));
        if (scriptFiles != null) {
            for (File scriptFile : scriptFiles) {
                try {
                    CameraDrive drive = scriptParser.parseScript(scriptFile);
                    if (drive != null) {
                        plugin.getDriveManager().registerDrive(drive);
                        plugin.getServer().getConsoleSender().sendMessage(
                                plugin.formatMessage("<aqua>Loaded camera drive: " + drive.getName() + "</aqua>")
                        );
                    }
                } catch (Exception e) {
                    plugin.getServer().getConsoleSender().sendMessage(
                            plugin.formatMessage("<red>Error loading script " + scriptFile.getName() + ": " + e.getMessage() + "</red>")
                    );
                }
            }
        }
    }

    /**
     * Create example script files
     */
    private void createExampleScripts() {
        createBasicExampleScript();
        createDemoCameraScript();
    }

    /**
     * Create a basic example script
     */
    private void createBasicExampleScript() {
        try {
            File exampleScript = new File(scriptsDir, "ultimate-example.cdrive");
            if (!exampleScript.exists()) {
                exampleScript.createNewFile();
                try (FileWriter writer = new FileWriter(exampleScript)) {
                    writer.write("# Ultimate Showcase Camera Drive\n");
                    writer.write("# This script demonstrates all features of the CameraDrives system\n\n");
                    writer.write("name: UltimateShowcase\n\n");
                    writer.write("# Specify the world (optional, defaults to the first world if not specified)\n");
                    writer.write("world: world\n\n");
                    writer.write("# Starting point - ground level, looking straight ahead\n");
                    writer.write("waypoint: 0,64,0,0,0\n\n");
                    writer.write("# Slow ascent with a slight turn\n");
                    writer.write("waypoint: 5,70,5,15,5\n");
                    writer.write("waypoint: 10,75,10,30,10\n\n");
                    writer.write("# Quick rise to a high point\n");
                    writer.write("waypoint: 15,100,15,45,30\n\n");
                    writer.write("# Dramatic top-down view\n");
                    writer.write("waypoint: 20,120,20,0,90\n\n");
                    writer.write("# Rapid descent with spin\n");
                    writer.write("waypoint: 25,100,25,90,45\n");
                    writer.write("waypoint: 30,80,30,180,0\n");
                    writer.write("waypoint: 35,60,35,270,-10\n\n");
                    writer.write("# Low tracking shot\n");
                    writer.write("waypoint: 40,66,40,315,0\n");
                    writer.write("waypoint: 50,66,50,315,0\n\n");
                    writer.write("# Curve around an imaginary structure\n");
                    writer.write("waypoint: 60,70,55,270,10\n");
                    writer.write("waypoint: 65,75,60,225,15\n");
                    writer.write("waypoint: 70,80,65,180,20\n\n");
                    writer.write("# Spiral ascent\n");
                    writer.write("waypoint: 75,85,65,135,25\n");
                    writer.write("waypoint: 80,90,60,90,30\n");
                    writer.write("waypoint: 85,95,55,45,35\n");
                    writer.write("waypoint: 90,100,50,0,40\n\n");
                    writer.write("# Quick side-to-side movement\n");
                    writer.write("waypoint: 95,100,55,0,0\n");
                    writer.write("waypoint: 100,100,45,0,0\n");
                    writer.write("waypoint: 105,100,55,0,0\n\n");
                    writer.write("# Dive towards ground\n");
                    writer.write("waypoint: 110,70,50,0,-45\n\n");
                    writer.write("# Pull up at last second\n");
                    writer.write("waypoint: 115,66,45,0,0\n\n");
                    writer.write("# Slow crawl to finish\n");
                    writer.write("waypoint: 120,66,40,0,0\n\n");
                    writer.write("# Speed changes throughout the drive\n");
                    writer.write("# Start slow\n");
                    writer.write("speed: 2\n\n");
                    writer.write("# Speed up for the rise\n");
                    writer.write("@125,66,35: speed 8\n\n");
                    writer.write("# Slow down for the top-down view\n");
                    writer.write("@20,120,20: speed 1\n\n");
                    writer.write("# Speed up for the rapid descent\n");
                    writer.write("@25,100,25: speed 10\n\n");
                    writer.write("# Normal speed for tracking shot\n");
                    writer.write("@40,66,40: speed 5\n\n");
                    writer.write("# Slow for spiral\n");
                    writer.write("@75,85,65: speed 3\n\n");
                    writer.write("# Fast for side-to-side\n");
                    writer.write("@95,100,55: speed 15\n\n");
                    writer.write("# Slow for final approach\n");
                    writer.write("@115,66,45: speed 2\n\n");
                    writer.write("# End of camera drive\n");
                }
                plugin.getLogger().info("Created ultimate example script file");
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to create example script: " + e.getMessage());
        }
    }

    /**
     * Create a demo camera script with a more complex path
     */
    private void createDemoCameraScript() {
        try {
            File demoScript = new File(scriptsDir, "demo_cinematic.cdrive");
            if (!demoScript.exists()) {
                demoScript.createNewFile();
                try (FileWriter writer = new FileWriter(demoScript)) {
                    writer.write("# Demo Cinematic Camera Drive\n");
                    writer.write("# This demonstrates a more complex camera path\n");
                    writer.write("name: DemoCinematic\n\n");

                    writer.write("# Starting point - looking at a building\n");
                    writer.write("waypoint: 100,70,100,45,0\n\n");

                    writer.write("# Slowly rise up while panning right\n");
                    writer.write("waypoint: 105,75,105,90,10\n");
                    writer.write("waypoint: 110,85,110,135,15\n\n");

                    writer.write("# Dramatic overhead shot\n");
                    writer.write("waypoint: 115,95,115,180,30\n\n");

                    writer.write("# Quick descent while spinning\n");
                    writer.write("waypoint: 120,80,120,270,20\n");
                    writer.write("waypoint: 125,70,125,360,10\n\n");

                    writer.write("# Final tracking shot along the ground\n");
                    writer.write("waypoint: 130,65,130,45,0\n");
                    writer.write("waypoint: 140,65,140,45,0\n");
                    writer.write("waypoint: 150,65,150,45,0\n\n");

                    writer.write("# Slower speed for cinematic effect\n");
                    writer.write("speed: 3.5\n");
                }
                plugin.getLogger().info("Created demo cinematic camera script");
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to create demo script: " + e.getMessage());
        }
    }
}

