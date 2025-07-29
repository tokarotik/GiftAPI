package tokarotik.giftapi.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tokarotik.giftapi.cache.CacheManager;
import tokarotik.giftapi.NBT.pages.PagesManager;

/**
 * Manages interactions with the custom inventory GUI, including
 * rendering, handling clicks, and modifying player item pages.
 */
public class InventoryManager implements Listener {

    private final String guiTitle;
    private final int inventorySlots;
    private final CacheManager cacheManager;
    private final InventoryUtil inventoryUtil;

    /**
     * Constructs an InventoryManager responsible for handling a custom inventory UI.
     *
     * @param cacheManager   player cache handler
     * @param inventorySlots total number of inventory slots
     * @param guiTitle       title of the GUI
     * @param arrowRight     text for the right-arrow button
     * @param arrowLeft      text for the left-arrow button
     * @param exitButton     text for the exit button
     */
    public InventoryManager(CacheManager cacheManager, int inventorySlots, String guiTitle,
                            String arrowRight, String arrowLeft, String exitButton) {
        this.guiTitle = guiTitle;
        this.inventorySlots = inventorySlots;
        this.cacheManager = cacheManager;

        int itemSlots = inventorySlots - 9; // reserve 9 for controls
        this.inventoryUtil = new InventoryUtil(cacheManager, guiTitle, arrowRight, arrowLeft, exitButton, itemSlots);
    }

    /**
     * Handles player clicking within a managed custom inventory.
     *
     * @param event the inventory click event
     */
    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        inventoryUtil.onInventoryClick(event);
    }

    /**
     * Adds an item to the player's PagesManager.
     *
     * @param player the target player
     * @param item   the item to add
     */
    public void add(Player player, ItemStack item) {
        if (player == null || item == null) return;

        PagesManager pages = cacheManager.load(player);
        pages.add(item);
    }

    /**
     * Removes an item from the player's PagesManager.
     *
     * @param player the target player
     * @param item   the item to remove
     */
    public void remove(Player player, ItemStack item) {
        if (player == null || item == null) return;

        PagesManager pages = cacheManager.load(player);
        pages.remove(item);
    }

    /**
     * Opens the custom GUI for the specified player.
     * Resets to the first page before rendering.
     *
     * @param player the player to open the GUI for
     */
    public void openCustomInventory(Player player) {
        if (player == null) return;

        PagesManager pages = cacheManager.load(player);
        pages.setCurrentPage(0);

        Inventory inventory = Bukkit.createInventory(null, inventorySlots, guiTitle);
        inventoryUtil.updateInventory(inventory, player);

        player.openInventory(inventory);
    }
}

