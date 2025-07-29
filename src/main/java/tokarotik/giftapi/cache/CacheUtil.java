package tokarotik.giftapi.cache;

import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tokarotik.giftapi.NBT.savers.BasicNBT;
import tokarotik.giftapi.NBT.pages.PagesManager;

import java.util.*;

/**
 * Handles caching and persistent storage of PagesManager instances
 * for players across different world environments.
 */
public class CacheUtil {

    private static final String OVERWORLD = "world";
    private static final String NETHER = "world_nether";
    private static final String END = "world_the_end";

    private final Map<Player, PagesManager> overworldCache = new HashMap<>();
    private final Map<Player, PagesManager> netherCache = new HashMap<>();
    private final Map<Player, PagesManager> endCache = new HashMap<>();

    private final int inventorySlots;

    public CacheUtil(int inventorySlots) {
        if (inventorySlots <= 0) {
            throw new IllegalArgumentException("Inventory slots must be greater than zero.");
        }
        this.inventorySlots = inventorySlots;
    }

    /**
     * Saves the player's data across all three known dimensions.
     *
     * @param player the player whose data to save
     */
    public synchronized void saveHard(Player player) {
        if (player == null) return;

        EntityPlayer entityPlayer = BasicNBT.getPlayer(player);

        for (Map.Entry<String, Map<Player, PagesManager>> entry : getWorldCaches().entrySet()) {
            String worldName = entry.getKey();
            Map<Player, PagesManager> cache = entry.getValue();

            PagesManager pages = cache.get(player);
            if (pages == null) continue;

            NBTTagList tag = pages.getTag();
            BasicNBT basicNBT = new BasicNBT(worldName);
            NBTTagCompound compound = basicNBT.readPlayerNBT(entityPlayer);

            compound.set("giftapi", tag);
            basicNBT.writePlayerNBT(compound, entityPlayer);
        }
    }

    /**
     * Saves all player data in all world caches.
     */
    public synchronized void saveAllHard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveHard(player);
        }
    }

    /**
     * Retrieves the proper world-specific player cache map.
     *
     * @param worldName name of the world
     * @return the map of players and their PagesManager for that world
     */
    public Map<Player, PagesManager> getWorlMap(String worldName) {
        switch (worldName) {
            case OVERWORLD:
                return overworldCache;
            case NETHER:
                return netherCache;
            case END:
                return endCache;
            default:
                return null;
        }
    }

    /**
     * Loads and caches a PagesManager instance from NBT data for the given player.
     *
     * @param player     the player
     * @param world      the world name
     * @param entityPlayer the NMS player instance
     * @param worldMap   the cache map where the data will be stored
     * @return loaded and cached PagesManager instance
     */
    public synchronized PagesManager loadAndCachePageManager(Player player, String world, EntityPlayer entityPlayer, Map<Player, PagesManager> worldMap) {
        NBTTagCompound compound = new BasicNBT(world).readPlayerNBT(entityPlayer);
        NBTTagList giftList = compound.getList("giftapi");
        PagesManager pagesManager = new PagesManager(giftList, inventorySlots);

        worldMap.put(player, pagesManager);
        return pagesManager;
    }

    /**
     * Utility to aggregate all internal world caches.
     *
     * @return map of worldName -> (player, pagesManager map)
     */
    private Map<String, Map<Player, PagesManager>> getWorldCaches() {
        Map<String, Map<Player, PagesManager>> worldCaches = new HashMap<>();
        worldCaches.put(OVERWORLD, overworldCache);
        worldCaches.put(NETHER, netherCache);
        worldCaches.put(END, endCache);
        return worldCaches;
    }
}

