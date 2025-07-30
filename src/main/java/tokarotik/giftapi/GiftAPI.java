package tokarotik.giftapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import tokarotik.giftapi.cache.CacheManager;

public class GiftAPI extends JavaPlugin implements Listener {

    private static final int INVENTORY_SLOTS = 54;
    private static final int CACHED_SLOTS = INVENTORY_SLOTS - 9;

    private CacheManager cacheManager;
    private APIManager apiManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.cacheManager = new CacheManager(CACHED_SLOTS);
        this.apiManager = new APIManager(this, getConfig(), INVENTORY_SLOTS);

        registerEvents();

        getLogger().info("GiftAPI has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving GiftAPI state...");
        if (cacheManager != null) {
            cacheManager.disable();
        }
        getLogger().info("GiftAPI has been disabled.");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> cacheManager.playerQuit(player));
    }

    /**
     * Adds an item to the player's gift cache.
     * This operation is asynchronous.
     *
     * @param player the player to add the item to
     * @param item   the item to be added
     */
    public void add(Player player, ItemStack item) {
        if (player == null || item == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> apiManager.add(player, item));
    }

    /**
     * Removes an item from the player's gift cache.
     * This operation is asynchronous.
     *
     * @param player the player to remove the item from
     * @param item   the item to be removed
     */
    public void remove(Player player, ItemStack item) {
        if (player == null || item == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> apiManager.remove(player, item));
    }

    /**
     * Opens the GiftAPI GUI for the specified player.
     *
     * @param player the player for whom to open the GUI
     */
    public void openGUI(Player player) {
        if (player == null) return;
        Bukkit.getScheduler().runTask(this, () -> apiManager.openInventory(player));
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
