package tokarotik.giftapi.inventory.inventoryitems;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tokarotik.giftapi.NBT.pages.PagesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for rendering item pages into inventories.
 */
public final class InventoryItemsUtil {

    private static final int INVENTORY_COLUMNS = 9;
    private static final int MAX_ROW_WIDTH = 10;

    private InventoryItemsUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * Returns a sublist of the items for the current page from the PagesManager.
     *
     * @param pagesManager the source of item pages
     * @param slotsPerPage how many item slots are available per page
     * @return list of items to render on the current page
     */
    public static List<ItemStack> getArrayToRender(PagesManager pagesManager, int slotsPerPage) {
        if (pagesManager == null || slotsPerPage <= 0) return new ArrayList<>();

        ItemStack[] allItems = pagesManager.getList();
        int totalItems = allItems.length;
        int currentPage = pagesManager.getCurrentPage();

        int startIndex = getStartItemIndex(currentPage, slotsPerPage);
        int endIndex = getEndItemIndex(currentPage, slotsPerPage, totalItems);

        return Arrays.asList(allItems).subList(startIndex, endIndex);
    }

    /**
     * Renders the list of items into the inventory based on a coordinate system.
     *
     * @param itemsToRender list of items to place into the inventory
     * @param inventory     the target Bukkit inventory
     * @param slotsPerPage  number of item slots available for rendering
     */
    public static void renderList(List<ItemStack> itemsToRender, Inventory inventory, int slotsPerPage) {
        if (itemsToRender == null || inventory == null) return;

        int x = 0, y = 0;
        int maxSlot = slotsPerPage - 1;

        for (ItemStack item : itemsToRender) {
            if (x >= MAX_ROW_WIDTH) {
                x = 0;
                y++;
            }

            int slot = getSlotFromCoordinates(slotsPerPage + 9, x, y); // 9 added for controls

            if (slot > maxSlot) {
                return; // Exceeded usable inventory area
            }

            inventory.setItem(slot, item);
            x++;
        }
    }

    /**
     * Calculates the start index for a page.
     */
    private static int getStartItemIndex(int currentPage, int slotsPerPage) {
        return currentPage * slotsPerPage;
    }

    /**
     * Calculates the end index for a page, ensuring it doesn't exceed item count.
     */
    private static int getEndItemIndex(int currentPage, int slotsPerPage, int totalItems) {
        int paddedPageSize = slotsPerPage + 4; // May account for GUI spacing or reserved rows
        int endIndex = (currentPage + 1) * paddedPageSize;

        return Math.min(endIndex, totalItems);
    }

    /**
     * Maps 2D coordinates into a 1D inventory slot index.
     * Accounts for skipped rows or padding.
     *
     * @param totalInventorySlots total size of the inventory
     * @param x column index
     * @param y row index
     * @return calculated 1D slot index
     */
    public static int getSlotFromCoordinates(int totalInventorySlots, int x, int y) {
        // Adjust X if the row is beyond the first (some layouts may shift items right)
        if (y > 0 && y != totalInventorySlots / INVENTORY_COLUMNS) {
            x += y;
        }

        int slot = (y * INVENTORY_COLUMNS) + x;
        return slot % totalInventorySlots;
    }
}

