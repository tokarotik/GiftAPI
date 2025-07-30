GiftAPI
======

[Russian](README_ru.md)

A plugin for Minecraft servers using the Bukkit 1.6.4 core.  
This is both an API and a custom inventory system.

Usage
-----

1. Download the release or build the plugin yourself.
2. Move it to the `ROOT-SERVER-FOLDER/plugins` directory.
3. Copy it into the root folder of your plugin.
4. In your IDE, import `giftapi.jar`.
5. Use the usage example and documentation to work with the API.
6. Build your plugin into a JAR file.
7. Copy the resulting JAR file to `ROOT-SERVER-FOLDER/plugins`.
8. Start the server or run the `/reload` command.

Documentation
-------------

### `add(player, item)`
- **Description**: Adds an item to the player's custom inventory.
- **Parameters**:
  - `player` (`Player`): The player who will receive the item.
  - `item` (`ItemStack`): The item to be added to the inventory.

### `remove(player, item)`
- **Description**: Removes an item from the player's custom inventory.
- **Parameters**:
  - `player` (`Player`): The player whose item will be removed.
  - `item` (`ItemStack`): The item to be removed from the inventory.

### `openGUI(player)`
- **Description**: Opens the custom inventory for the player.
- **Parameters**:
  - `player` (`Player`): The player whose custom inventory will be opened.

Example Usage
-------------

```java
import tokarotik.giftapi.GiftAPI;

// Get plugin instance
Plugin plugin = Bukkit.getPluginManager().getPlugin("GiftAPI");

if (plugin != null && plugin.isEnabled() && plugin instanceof GiftAPI) {
    // Set up API
    GiftAPI giftAPI = (GiftAPI) plugin;

    // Get the first online player
    Player player = Bukkit.getOnlinePlayers()[0];

    // Create an item (stick)
    ItemStack item = new ItemStack(Material.STICK);

    // Add item to player's custom inventory
    giftAPI.add(player, item);

    // Open the custom inventory GUI
    giftAPI.openGUI(player);
}
```

Requirements
------------
- Bukkit 1.6.4

Developer
---------
- Developer:
	- [GitHub](https://github.com/tokarotik)
	- [Gitea](https://git.artberry.xyz/ToKarotik)
	- Discord: tokarotik

- Client:
	- [Gitea](https://git.artberry.xyz/Miriko)
	- Discord: jetstreambase

License
-------
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).