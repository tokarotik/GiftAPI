package tokarotik.giftapi.NBT.pages;

import net.minecraft.server.v1_6_R3.NBTTagList;
import org.bukkit.inventory.ItemStack;

public final class PagesManager {

    private volatile NBTTagList tag;
    private volatile int currentPage = 0;
    private final int itemsSlots;

    public PagesManager(NBTTagList tag, int itemsSlots) {
        this.itemsSlots = itemsSlots;
        this.tag = tag != null ? tag : new NBTTagList("giftapi");
    }

    public PagesManager(int itemsSlots) {
        this.itemsSlots = itemsSlots;
        this.tag = new NBTTagList("giftapi");
    }

    public synchronized void add(ItemStack item) {
        PagesUtil.add(item, this.tag);
    }

    public synchronized void remove(ItemStack item) {
        this.tag = PagesUtil.remove(item, this.tag);
        // Optionally reset currentPage if it's now out of range
        int maxPage = getCountPages() - 1;
        if (currentPage > maxPage) {
            currentPage = Math.max(0, maxPage);
        }
    }

    public synchronized NBTTagList getTag() {
        return tag;
    }

    public synchronized ItemStack[] getList() {
        return PagesUtil.getRawList(tag);
    }

    public synchronized int getCurrentPage() {
        return currentPage;
    }

    public synchronized void setCurrentPage(int currentPage) {
        if (currentPage < 0) {
            this.currentPage = 0;
        } else if (currentPage >= getCountPages()) {
            this.currentPage = Math.max(0, getCountPages() - 1);
        } else {
            this.currentPage = currentPage;
        }
    }

    /**
     * Gets the total number of pages, accounting for partial pages.
     */
    public synchronized int getCountPages() {
        int size = tag.size();
        if (size == 0) return 1;
        return (int) Math.ceil((double) size / itemsSlots);
    }

    public synchronized void nextPage() {
        if (currentPage < getCountPages() - 1) {
            currentPage++;
        }
    }

    public synchronized void backPage() {
        if (currentPage > 0) {
            currentPage--;
        }
    }
}

