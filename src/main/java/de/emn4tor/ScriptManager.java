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
            File exampleScript = new File(scriptsDir, "example.cdrive");
            if (!exampleScript.exists()) {
                exampleScript.createNewFile();
                try (FileWriter writer = new FileWriter(exampleScript)) {
                    writer.write("# Example Camera Drive Script\n");
                    writer.write("name: ExampleDrive\n");
                    writer.write("# Define waypoints as x,y,z,yaw,pitch\n");
                    writer.write("waypoint: 100,64,100,0,0\n");
                    writer.write("waypoint: 110,70,110,45,10\n");
                    writer.write("waypoint: 120,75,120,90,20\n");
                    writer.write("# Speed in blocks per second\n");
                    writer.write("speed: 5\n");
                }
                plugin.getLogger().info("Created example script file");
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

