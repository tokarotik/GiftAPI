package tokarotik.giftapi.NBT.pages;

import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;
import org.bukkit.inventory.ItemStack;
import tokarotik.giftapi.NBT.item.ItemLoad;
import tokarotik.giftapi.NBT.item.ItemNBT;

import java.util.ArrayList;
import java.util.List;

public final class PagesUtil {

    private PagesUtil() {
        // Utility class - prevent instantiation
    }

    public static synchronized void add(ItemStack item, NBTTagList tag) {
        if (item == null || tag == null) return;

        NBTTagCompound nbt = ItemNBT.getTag(item);
        if (nbt != null) {
            tag.add(nbt);
        }
    }

    public static synchronized NBTTagList remove(ItemStack item, NBTTagList tag) {
        if (item == null || tag == null) return tag;

        // Convert NBTTagList -> List<ItemStack>
        List<ItemStack> oldList = getListFromNBT(tag);
        List<ItemStack> newList = new ArrayList<>(oldList.size());

        for (ItemStack stack : oldList) {
            if (!equalsItem(stack, item)) {
                newList.add(stack);
            }
        }

        return itemStackListToNBTList(newList);
    }

    private static boolean equalsItem(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        return a.isSimilar(b);
    }

    public static synchronized NBTTagList itemStackListToNBTList(List<ItemStack> items) {
        NBTTagList list = new NBTTagList();

        for (ItemStack item : items) {
            if (item != null) {
                NBTTagCompound compound = ItemNBT.getTag(item);
                if (compound != null) {
                    list.add(compound);
                }
            }
        }

        return list;
    }

    public static synchronized ItemStack[] getRawList(NBTTagList tag) {
        if (tag == null) return new ItemStack[0];

        ItemStack[] list = new ItemStack[tag.size()];

        for (int i = 0; i < tag.size(); i++) {
            NBTTagCompound compound = (NBTTagCompound) tag.get(i);
            list[i] = ItemLoad.getItem(compound);
        }

        return list;
    }

    private static synchronized List<ItemStack> getListFromNBT(NBTTagList tag) {
        ItemStack[] array = getRawList(tag);
        List<ItemStack> list = new ArrayList<>(array.length);
        for (ItemStack item : array) {
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }
}
