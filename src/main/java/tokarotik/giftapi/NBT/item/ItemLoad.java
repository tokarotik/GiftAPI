package tokarotik.giftapi.NBT.item;

import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;
import net.minecraft.server.v1_6_R3.NBTTagString;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ItemLoad {

    private ItemLoad() {
        // Utility class; prevent instantiation
    }

    /**
     * Constructs an ItemStack from the given NBTTagCompound.
     *
     * @param tag the NBTTagCompound containing item data
     * @return an ItemStack representing the item data from NBT
     */
    public static ItemStack getItem(NBTTagCompound tag) {
        int id = getId(tag);
        short count = getCount(tag);
        short durability = getDurability(tag);

        ItemStack item = new ItemStack(id, count, durability);

        // Set item meta after item creation
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta = applyMeta(tag, meta);
            item.setItemMeta(meta);
        }

        return item;
    }

    private static int getId(NBTTagCompound tag) {
        return tag.hasKey("id") ? tag.getInt("id") : 0;
    }

    private static short getCount(NBTTagCompound tag) {
        // Default count 1 for valid items
        return tag.hasKey("count") ? tag.getShort("count") : 1;
    }

    private static short getDurability(NBTTagCompound tag) {
        // Default durability 0 (undamaged)
        return tag.hasKey("damage") ? tag.getShort("damage") : 0;
    }

    /**
     * Applies metadata to an ItemMeta from the NBT tag.
     *
     * @param tag  the NBTTagCompound containing meta data
     * @param meta the ItemMeta to apply changes to
     * @return the modified ItemMeta
     */
    private static ItemMeta applyMeta(NBTTagCompound tag, ItemMeta meta) {
        if (tag.hasKey("meta")) {
            NBTTagCompound metaNBT = tag.getCompound("meta");

            if (metaNBT.hasKey("name")) {
                meta.setDisplayName(metaNBT.getString("name"));
            }

            if (metaNBT.hasKey("lore")) {
                NBTTagList loreList = metaNBT.getList("lore");
                List<String> lore = nbtListToStringList(loreList);
                meta.setLore(lore);
            }
        }
        return meta;
    }

    /**
     * Converts an NBTTagList of strings to a List<String>.
     *
     * @param nbt the NBTTagList containing NBTTagStrings
     * @return a List of Strings extracted from the NBT list
     */
    private static List<String> nbtListToStringList(NBTTagList nbt) {
        if (nbt == null || nbt.size() == 0) {
            return Collections.emptyList();
        }

        String[] list = new String[nbt.size()];
        for (int i = 0; i < nbt.size(); i++) {
            // Defensive cast, check instance before casting if possible
            if (nbt.get(i) instanceof NBTTagString) {
                NBTTagString base = (NBTTagString) nbt.get(i);
                list[i] = base.data != null ? base.data : "";
            } else {
                list[i] = "";
            }
        }
        return Arrays.asList(list);
    }
}

