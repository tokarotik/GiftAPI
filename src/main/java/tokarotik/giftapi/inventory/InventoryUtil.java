package tokarotik.giftapi.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tokarotik.giftapi.cache.CacheManager;
import tokarotik.giftapi.NBT.pages.PagesManager;
import tokarotik.giftapi.inventory.inventoryitems.InventoryItems;
import tokarotik.giftapi.inventory.inventoryitems.InventoryItemsUtil;

/**
 * Utility class for managing the inventory GUI elements and user interaction.
 */
public class InventoryUtil {

    private final CacheManager cacheManager;
    private final InventoryItems inventoryItems;
    private final String guiTitle;
    private final String arrowRightLabel;
    private final String arrowLeftLabel;
    private final String exitLabel;
    private final int inventorySlots;

    public InventoryUtil(CacheManager cacheManager, String guiTitle,
                         String arrowRightLabel, String arrowLeftLabel,
                         String exitLabel, int inventorySlots) {

        this.cacheManager = cacheManager;
        this.guiTitle = guiTitle;
        this.arrowRightLabel = arrowRightLabel;
        this.arrowLeftLabel = arrowLeftLabel;
        this.exitLabel = exitLabel;
        this.inventorySlots = inventorySlots;
        this.inventoryItems = new InventoryItems(inventorySlots);
    }

    /**
     * Handles clicks in the custom GUI and controls pagination or exiting.
     */
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!event.getView().getTitle().equals(this.guiTitle)) return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String displayName = clickedItem.getItemMeta().getDisplayName();
        if (displayName == null) return;

        Player player = (Player) event.getWhoClicked();
        PagesManager pagesManager = cacheManager.load(player);

        if (displayName.equals(arrowRightLabel)) {
            if (pagesManager.getCurrentPage() < pagesManager.getCountPages()) {
                pagesManager.nextPage();
                updateInventory(inventory, player);
            }

        } else if (displayName.equals(arrowLeftLabel)) {
            if (pagesManager.getCurrentPage() > 0) {
                pagesManager.backPage();
                updateInventory(inventory, player);
            }

        } else if (displayName.equals(exitLabel)) {
            player.closeInventory();
        }
    }

    /**
     * Refreshes the inventory GUI with items and controls for the current page.
     */
    public void updateInventory(Inventory inventory, Player player) {
        inventory.clear();
        PagesManager pagesManager = cacheManager.load(player);

        inventoryItems.renderPage(pagesManager, inventory);
        renderControls(inventory, pagesManager);
    }

    /**
     * Adds navigation and exit controls to the inventory UI.
     */
    private void renderControls(Inventory inventory, PagesManager pagesManager) {
        int lastRow = inventorySlots / 9;

        // Exit button in the center
        addItem(inventory, Material.REDSTONE, exitLabel, 4, lastRow);

        int currentPage = pagesManager.getCurrentPage();
        int totalPages = pagesManager.getCountPages();

        if (currentPage < totalPages) {
            addItem(inventory, Material.ARROW, arrowRightLabel, 8, lastRow);
        }

        if (currentPage > 0) {
            addItem(inventory, Material.ARROW, arrowLeftLabel, 0, lastRow);
        }
    }

    /**
     * Places an item at a specific coordinate within the inventory.
     */
    private void addItem(Inventory inventory, Material material, String name, int x, int y) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        int slot = InventoryItemsUtil.getSlotFromCoordinates(inventorySlots, x, y);
        inventory.setItem(slot, item);
    }
}
