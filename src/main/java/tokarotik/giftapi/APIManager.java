package tokarotik.giftapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import tokarotik.giftapi.inventory.InventoryManager;

/**
 * Handles API-facing logic such as adding, removing, and opening
 * gift inventories for players. Acts as a middle layer between plugin
 * logic and internal inventory mechanics.
 */
public class APIManager implements Listener {
    private final InventoryManager inventoryManager;

    public APIManager(GiftAPI plugin, FileConfiguration config, int inventorySlots) {
        if (plugin == null || config == null) {
            throw new IllegalArgumentException("Plugin and config must not be null.");
        }

        this.inventoryManager = new InventoryManager(
                plugin.getCacheManager(),
                inventorySlots,
                ConfigPaths.getWithColor(config, ConfigPaths.GUI_TITLE, "GiftAPI Menu"),
                ConfigPaths.getWithColor(config, ConfigPaths.GUI_BUTTON_RIGHT, "<<right"),
                ConfigPaths.getWithColor(config, ConfigPaths.GUI_BUTTON_LEFT, "left>>"),
                ConfigPaths.getWithColor(config, ConfigPaths.GUI_BUTTON_EXIT, "quit")
        );

        // Register inventory click listener
        Bukkit.getPluginManager().registerEvents(this.inventoryManager, plugin);
    }

    /**
     * Adds an item to the player's gift inventory.
     *
     * @param player the target player
     * @param item   the item to add
     */
    public void add(Player player, ItemStack item) {
        if (player == null || item == null) return;
        inventoryManager.add(player, item);
    }

    /**
     * Removes an item from the player's gift inventory.
     *
     * @param player the target player
     * @param item   the item to remove
     */
    public void remove(Player player, ItemStack item) {
        if (player == null || item == null) return;
        inventoryManager.remove(player, item);
    }

    /**
     * Opens the custom inventory GUI for the specified player.
     *
     * @param player the player to open the inventory for
     */
    public void openInventory(Player player) {
        if (player == null) return;
        inventoryManager.openCustomInventory(player);
    }

    /**
     * Delegates inventory click events to the inventory manager.
     *
     * @param event the inventory click event
     */
    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        inventoryManager.handleInventoryClick(event);
    }
}

