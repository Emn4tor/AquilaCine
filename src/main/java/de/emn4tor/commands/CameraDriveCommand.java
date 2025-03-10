package de.emn4tor.commands;

/*
 *  @author: Emn4tor
 *  @created: 10.03.2025
 */

import de.emn4tor.AquilaCine;
import de.emn4tor.CameraDrive;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CameraDriveCommand implements CommandExecutor, TabCompleter {
    private final AquilaCine plugin;

    public CameraDriveCommand(AquilaCine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "list":
                listDrives(sender);
                break;
            case "start":
                if (args.length < 2) {
                    sender.sendMessage(plugin.formatMessage("<red>Usage: /cameradrive start <drive-name></red>"));
                    return true;
                }
                startDrive(sender, args[1]);
                break;
            case "stop":
                stopDrive(sender);
                break;
            case "reload":
                reloadDrives(sender);
                break;
            case "help":
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.formatMessage("<gold>===== Camera Drives Help =====</gold>"));
        sender.sendMessage(plugin.formatMessage("<yellow>/cameradrive list</yellow> - List all available camera drives"));
        sender.sendMessage(plugin.formatMessage("<yellow>/cameradrive start <name></yellow> - Start a camera drive"));
        sender.sendMessage(plugin.formatMessage("<yellow>/cameradrive stop</yellow> - Stop your current camera drive"));
        sender.sendMessage(plugin.formatMessage("<yellow>/cameradrive reload</yellow> - Reload all camera drive scripts"));
    }

    private void listDrives(CommandSender sender) {
        List<CameraDrive> drives = new ArrayList<>(plugin.getDriveManager().getAllDrives());

        if (drives.isEmpty()) {
            sender.sendMessage(plugin.formatMessage("<yellow>No camera drives available. Add script files to the 'scripts' folder.</yellow>"));
            return;
        }

        sender.sendMessage(plugin.formatMessage("<gold>===== Available Camera Drives =====</gold>"));
        for (CameraDrive drive : drives) {
            sender.sendMessage(plugin.formatMessage("<aqua>" + drive.getName() + "</aqua> - " +
                    drive.getWaypoints().size() + " waypoints, " + drive.getSpeed() + " blocks/sec"));
        }
    }

    private void startDrive(CommandSender sender, String driveName) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.formatMessage("<red>Only players can use camera drives</red>"));
            return;
        }

        Player player = (Player) sender;
        CameraDrive drive = plugin.getDriveManager().getDrive(driveName);

        if (drive == null) {
            sender.sendMessage(plugin.formatMessage("<red>Camera drive not found: " + driveName + "</red>"));
            return;
        }

        if (plugin.getDriveManager().isInDriveSession(player)) {
            sender.sendMessage(plugin.formatMessage("<red>You are already in a camera drive. Use /cameradrive stop first.</red>"));
            return;
        }

        drive.startDrive(player, plugin);
    }

    private void stopDrive(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.formatMessage("<red>Only players can use camera drives</red>"));
            return;
        }

        Player player = (Player) sender;

        if (!plugin.getDriveManager().isInDriveSession(player)) {
            sender.sendMessage(plugin.formatMessage("<red>You are not in a camera drive</red>"));
            return;
        }

        plugin.getDriveManager().stopDrive(player);
        sender.sendMessage(plugin.formatMessage("<green>Camera drive stopped</green>"));
    }

    private void reloadDrives(CommandSender sender) {
        if (!sender.hasPermission("cameradrives.reload")) {
            sender.sendMessage(plugin.formatMessage("<red>You don't have permission to reload camera drives</red>"));
            return;
        }

        plugin.getScriptManager().loadCameraDrives();
        sender.sendMessage(plugin.formatMessage("<green>Camera drives reloaded!</green>"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // First argument - subcommands
            List<String> subCommands = List.of("list", "start", "stop", "reload", "help");
            return filterCompletions(subCommands, args[0]);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            // Second argument for 'start' - drive names
            return filterCompletions(
                    plugin.getDriveManager().getAllDrives().stream()
                            .map(CameraDrive::getName)
                            .collect(Collectors.toList()),
                    args[1]
            );
        }

        return completions;
    }

    private List<String> filterCompletions(List<String> options, String input) {
        if (input.isEmpty()) {
            return options;
        }

        String lowerInput = input.toLowerCase();
        return options.stream()
                .filter(option -> option.toLowerCase().startsWith(lowerInput))
                .collect(Collectors.toList());
    }
}

