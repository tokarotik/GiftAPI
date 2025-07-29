package tokarotik.giftapi.NBT.item;

import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;
import net.minecraft.server.v1_6_R3.NBTTagString;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ItemNBT {

    private ItemNBT() {
        // Utility class; prevent instantiation
    }

    /**
     * Converts a Bukkit ItemStack into an NBTTagCompound representing the item.
     *
     * @param item the ItemStack to convert
     * @return the NBTTagCompound representing the item or null if the item is null
     */
    public static NBTTagCompound getTag(ItemStack item) {
        if (item == null) {
            return null;
        }

        NBTTagCompound tag = new NBTTagCompound("item");

        setBasic(item, tag);
        setMetaTag(item, tag);

        return tag;
    }

    private static void setBasic(ItemStack item, NBTTagCompound tag) {
        short count = (short) item.getAmount();
        if (count != 1) {
            tag.setShort("count", count);
        }

        int id = item.getTypeId();
        if (id != 0) {
            tag.setInt("id", id);
        }

        short damage = item.getDurability();
        if (damage != 0) {
            tag.setShort("damage", damage);
        }
    }

    private static void setMetaTag(ItemStack item, NBTTagCompound baseTag) {
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        NBTTagCompound metaTag = new NBTTagCompound();

        if (meta.hasDisplayName()) {
            metaTag.setString("name", meta.getDisplayName());
        }

        if (meta.hasLore()) {
            metaTag.set("lore", stringListToNBTList(meta.getLore()));
        }

        if (meta.hasEnchants()) {
            metaTag.setCompound("enchantments", enchantsToNBTCompound(meta.getEnchants()));
        }

        if (!metaTag.isEmpty()) {
            baseTag.setCompound("meta", metaTag);
        }
    }

    private static NBTTagList stringListToNBTList(List<String> list) {
        NBTTagList nbtList = new NBTTagList();
        if (list == null || list.isEmpty()) {
            return nbtList;
        }

        for (String str : list) {
            // Null-safe insertion of strings
            nbtList.add(new NBTTagString(null, Objects.toString(str, "")));
        }
        return nbtList;
    }

    private static NBTTagCompound enchantsToNBTCompound(Map<Enchantment, Integer> enchantments) {
        NBTTagCompound enchantmentsNBT = new NBTTagCompound();

        if (enchantments == null || enchantments.isEmpty()) {
            return enchantmentsNBT;
        }

        enchantments.forEach((enchantment, level) -> {
            if (enchantment == null) return; // defensive check

            NBTTagCompound enchantmentNBT = new NBTTagCompound();
            enchantmentNBT.setInt("level", level != null ? level : 0);

            enchantmentsNBT.setCompound(String.valueOf(enchantment.getId()), enchantmentNBT);
        });

        return enchantmentsNBT;
    }
}

