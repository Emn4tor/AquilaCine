# CameraDrives Plugin

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Installation](#installation)
4. [Usage](#usage)
    - [Commands](#commands)
    - [Permissions](#permissions)
5. [Script Format](#script-format)
6. [Tutorials](#tutorials)
    - [Creating Your First Camera Drive](#creating-your-first-camera-drive)
    - [Advanced Camera Techniques](#advanced-camera-techniques)
7. [Configuration](#configuration)
8. [API Documentation](#api-documentation)
9. [Troubleshooting](#troubleshooting)
10. [Contributing](#contributing)
11. [License](#license)

## Introduction

CameraDrives is a powerful Minecraft plugin for Paper 1.21 that allows server administrators and content creators to create smooth, cinematic camera movements within the game. With an easy-to-use scripting system, you can design complex camera paths, control movement speeds, and create stunning visual experiences for your players.

## Features

- **Script-Based Camera Paths**: Create camera drives using simple, human-readable script files.
- **Dynamic Speed Control**: Adjust camera movement speed at different points in the path.
- **World-Specific Paths**: Specify which world each camera drive should operate in.
- **Smooth Interpolation**: Enjoy smooth transitions between waypoints for fluid camera movement.
- **In-Game Commands**: Start, stop, and manage camera drives with easy-to-use commands.
- **Permission System**: Control who can use and manage camera drives.
- **Hot Reloading**: Reload camera drive scripts without restarting the server.
- **MiniMessage Support**: Use MiniMessage format for all text, allowing for rich, formatted messages.

## Installation

1. Download the latest \`CameraDrives.jar\` from the releases page.
2. Place the jar file in your server's \`plugins\` folder.
3. Restart your server or use a plugin manager to load the plugin.
4. The plugin will generate a default configuration and example scripts on first run.

## Usage

### Commands

- `cameradrive list\` - List all available camera drives
- `cameradrive start <name>\` - Start a camera drive
- `cameradrive stop\` - Stop your current camera drive
- `cameradrive reload\` - Reload all camera drive scripts

### Permissions

- \`cameradrives.use\` - Allows use of basic camera drive commands (default: true)
- \`cameradrives.reload\` - Allows reloading of camera drive scripts (default: op)

## Script Format

Camera drive scripts use the \`.cdrive\` extension and should be placed in the \`plugins/CameraDrives/scripts/\` directory.

Basic script structure:

```
# Camera Drive Name
name: MyCameraDrive

# Optional: Specify world
world: world

# Waypoints (x, y, z, yaw, pitch)
waypoint: 0,64,0,0,0
waypoint: 10,70,10,45,15

# Speed in blocks per second
speed: 5

# Change speed at a specific point
@15,75,15: speed 10
```

## Tutorials

### Creating Your First Camera Drive

1. Navigate to \`plugins/CameraDrives/scripts/\`
2. Create a new file named \`my_first_drive.cdrive\`
3. Open the file and add the following content:

   ```
   name: MyFirstDrive
   waypoint: 0,64,0,0,0
   waypoint: 10,70,10,45,15
   waypoint: 20,75,20,90,30
   speed: 3
   ```

4. Save the file and run `cameradrive reload\` in-game
5. Start your drive with `cameradrive start MyFirstDrive\`

### Advanced Camera Techniques

1. **Spiral Ascent**:
   ```
   waypoint: 0,64,0,0,0
   waypoint: 5,70,5,90,15
   waypoint: 10,76,0,180,30
   waypoint: 5,82,-5,270,45
   waypoint: 0,88,0,360,60
   ```

2. **Dynamic Speed Changes**:
   ```
   speed: 2
   waypoint: 0,64,0,0,0
   @10,70,10: speed 5
   waypoint: 20,76,20,90,15
   @30,82,30: speed 1
   waypoint: 40,88,40,180,30
   ```

3. **Simulating a Drone Shot**:
   ```
   speed: 3
   waypoint: 0,70,0,0,0
   waypoint: 10,72,10,45,5
   waypoint: 20,68,20,90,-5
   waypoint: 30,74,30,135,10
   waypoint: 40,70,40,180,0
   ```

## Configuration

The main configuration file is located at \`plugins/CameraDrives/config.yml\`.

Key configuration options:
- \`default-speed\`: Default speed for camera drives if not specified in the script
- \`max-speed\`: Maximum allowed speed for camera drives
- \`return-to-original-location\`: Whether to return players to their original location after a drive ends

## API Documentation

For developers looking to integrate CameraDrives into their plugins:

```java
CameraDrivesPlugin plugin = (CameraDrivesPlugin) Bukkit.getPluginManager().getPlugin("CameraDrives");

// Get a camera drive
CameraDrive drive = plugin.getDriveManager().getDrive("MyDrive");

// Start a drive for a player
drive.startDrive(player, plugin);

// Check if a player is in a drive
boolean isInDrive = plugin.getDriveManager().isInDriveSession(player);

// Stop a player's current drive
plugin.getDriveManager().stopDrive(player);
```

## Troubleshooting

- **Scripts not loading**: Ensure scripts are in the correct directory and have the \`.cdrive\` extension.
- **Jerky movement**: Try reducing the speed or adding more waypoints for smoother transitions.
- **Players getting stuck**: Make sure waypoints are in accessible locations and not inside blocks.

## Contributing

We welcome contributions to the CameraDrives plugin! Please see our [CONTRIBUTING.md](CONTRIBUTING.md) file for guidelines on how to submit issues, feature requests, and pull requests.

## License

CameraDrives is released under the MIT License. See the [LICENSE](LICENSE) file for details.

