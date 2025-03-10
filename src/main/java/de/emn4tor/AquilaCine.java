package de.emn4tor;

import de.emn4tor.commands.CameraDriveCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;

public final class AquilaCine extends JavaPlugin {
    private CameraDriveManager driveManager;
    private ScriptManager scriptManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();

        // Initialize managers
        this.driveManager = new CameraDriveManager(this);
        this.scriptManager = new ScriptManager(this);

        // Register commands
        registerCommands();

        // Load camera drives
        scriptManager.loadCameraDrives();

        getServer().getConsoleSender().sendMessage(
                miniMessage.deserialize("<green>Camera Drives Plugin enabled!</green>")
        );
    }

    @Override
    public void onDisable() {
        if (driveManager != null) {
            driveManager.stopAllDrives();
        }

        getServer().getConsoleSender().sendMessage(
                miniMessage.deserialize("<red>Camera Drives Plugin disabled!</red>")
        );
    }

    private void registerCommands() {
        PluginCommand command = getCommand("cameradrive");
        if (command != null) {
            CameraDriveCommand commandExecutor = new CameraDriveCommand(this);
            command.setExecutor(commandExecutor);
            command.setTabCompleter(commandExecutor);
        }
    }

    public CameraDriveManager getDriveManager() {
        return driveManager;
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public Component formatMessage(String message) {
        return miniMessage.deserialize(message);
    }
}

