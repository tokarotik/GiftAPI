package tokarotik.giftapi.inventory.inventoryitems;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tokarotik.giftapi.NBT.pages.PagesManager;

import java.util.List;

/**
 * Handles rendering of item pages into a Bukkit Inventory.
 */
public class InventoryItems {

    private final int itemSlotLimit;

    /**
     * @param itemSlotLimit Number of item slots available for rendering (typically inventory size minus control slots).
     */
    public InventoryItems(int itemSlotLimit) {
        if (itemSlotLimit <= 0) {
            throw new IllegalArgumentException("Item slot limit must be greater than 0.");
        }
        this.itemSlotLimit = itemSlotLimit;
    }

    /**
     * Renders a page of items from the PagesManager into the provided inventory.
     *
     * @param pagesManager the source of paginated items
     * @param inventory    the Bukkit inventory to be populated
     */
    public void renderPage(PagesManager pagesManager, Inventory inventory) {
        if (pagesManager == null || inventory == null) return;

        List<ItemStack> itemsToRender = InventoryItemsUtil.getArrayToRender(pagesManager, itemSlotLimit);
        InventoryItemsUtil.renderList(itemsToRender, inventory, itemSlotLimit);
    }
}
