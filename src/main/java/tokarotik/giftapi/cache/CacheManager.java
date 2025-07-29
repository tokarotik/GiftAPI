package tokarotik.giftapi.cache;

import net.minecraft.server.v1_6_R3.EntityPlayer;
import org.bukkit.entity.Player;
import tokarotik.giftapi.NBT.savers.BasicNBT;
import tokarotik.giftapi.NBT.savers.WorldNBTSynchronizer;
import tokarotik.giftapi.NBT.pages.PagesManager;

import java.util.Map;

/**
 * Manages player-specific cache of PagesManager instances across worlds.
 * Handles loading from NBT or initializing defaults.
 */
public class CacheManager {

    private final CacheUtil cacheUtil;
    private final int inventorySlots;

    public CacheManager(int inventorySlots) {
        if (inventorySlots <= 0) {
            throw new IllegalArgumentException("Inventory slots must be positive.");
        }

        this.inventorySlots = inventorySlots;
        this.cacheUtil = new CacheUtil(inventorySlots);
    }

    /**
     * Loads the PagesManager for a given player.
     * If already cached, returns immediately.
     * Attempts to load from NBT, falling back to default if necessary.
     *
     * @param player the player to load data for
     * @return PagesManager instance associated with the player
     */
    public PagesManager load(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        synchronized (getLockFor(player)) {
            String worldName = player.getWorld().getName();
            EntityPlayer entityPlayer = BasicNBT.getPlayer(player);
            Map<Player, PagesManager> worldMap = cacheUtil.getWorlMap(worldName);

            // Use cached instance if present
            if (worldMap.containsKey(player)) {
                return worldMap.get(player);
            }

            // Load from NBT if available in current or alternate world
            if (WorldNBTSynchronizer.isPlayerInWorldWithGiftApiNBT(entityPlayer, worldName)) {
                return cacheUtil.loadAndCachePageManager(player, worldName, entityPlayer, worldMap);
            }

            String alternateWorld = WorldNBTSynchronizer.getWorldContainingPlayerWithGiftApiNBT(entityPlayer);
            if (alternateWorld != null) {
                return cacheUtil.loadAndCachePageManager(player, alternateWorld, entityPlayer, worldMap);
            }

            // Default fallback: new empty manager
            PagesManager defaultPages = new PagesManager(inventorySlots);
            worldMap.put(player, defaultPages);
            return defaultPages;
        }
    }

    /**
     * Saves all cached player data to disk.
     * Call during plugin disable.
     */
    public void disable() {
        cacheUtil.saveAllHard();
    }

    /**
     * Saves data for a player when they leave.
     *
     * @param player the player who quit
     */
    public void playerQuit(Player player) {
        if (player == null) return;

        synchronized (getLockFor(player)) {
            cacheUtil.saveHard(player);
        }
    }

    /**
     * Simple locking strategy for per-player concurrency without coarse-grained locking.
     */
    private Object getLockFor(Player player) {
        // Optional improvement: use a WeakHashMap<Player, Object> for real-world concurrency handling
        return player.getUniqueId();
    }
}
