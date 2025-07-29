package tokarotik.giftapi.NBT.savers;

import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class WorldNBTSynchronizer {

    /**
     * Finds the name of the world that contains the given player and where the player's NBT
     * has a valid "giftapi" tag.
     *
     * @param entityPlayer the player entity to check
     * @return the world name if found, otherwise null
     */
    @Nullable
    public static String getWorldContainingPlayerWithGiftApiNBT(EntityPlayer entityPlayer) {
        Objects.requireNonNull(entityPlayer, "entityPlayer cannot be null");

        for (World world : Bukkit.getWorlds()) {
            String worldName = world.getName();
            if (isPlayerInWorldWithGiftApiNBT(entityPlayer, worldName)) {
                return worldName;
            }
        }

        return null;
    }

    /**
     * Checks whether the player with the specified name exists in the world and
     * if their NBT contains the "giftapi" tag.
     *
     * @param entityPlayer the player entity
     * @param worldName    the world name
     * @return true if player exists in world and has valid giftapi NBT, false otherwise
     */
    public static boolean isPlayerInWorldWithGiftApiNBT(EntityPlayer entityPlayer, String worldName) {
        if (!isPlayerOnlineInWorld(entityPlayer.getName(), worldName)) {
            return false;
        }
        return doesPlayerNBTContainGiftApi(entityPlayer, worldName);
    }

    /**
     * Checks if the player with the given name is currently online in the specified world.
     *
     * @param playerName the player name to check
     * @param worldName  the world name
     * @return true if player is online in the world, false otherwise
     */
    private static boolean isPlayerOnlineInWorld(String playerName, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return false;
        }

        // Use stream for clarity and short-circuiting
        return world.getPlayers().stream()
                .anyMatch(player -> player.getName().equals(playerName));
    }

    /**
     * Checks if the player's NBT data in the specified world contains a valid "giftapi" NBTTagList.
     *
     * @param entityPlayer the player entity
     * @param worldName    the world name
     * @return true if valid giftapi tag exists, false otherwise
     */
    private static boolean doesPlayerNBTContainGiftApi(EntityPlayer entityPlayer, String worldName) {
        BasicNBT basicNBT = new BasicNBT(worldName);
        NBTTagCompound tag = basicNBT.readPlayerNBT(entityPlayer);

        if (tag == null || !tag.hasKey("giftapi")) {
            return false;
        }

        try {
            // Check if "giftapi" is an NBTTagList
            tag.getList("giftapi");
            return true;
        } catch (Exception e) {
            // Swallow exception since the tag exists but is invalid type
            return false;
        }
    }
}

