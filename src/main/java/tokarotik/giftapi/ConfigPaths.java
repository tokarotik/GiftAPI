package tokarotik.giftapi;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Centralized configuration path keys and utility methods for retrieving
 * and colorizing string values from the plugin's configuration.
 */
public final class ConfigPaths {

    private ConfigPaths() {
        // Utility class; prevent instantiation
        throw new UnsupportedOperationException("ConfigPaths is a utility class and cannot be instantiated.");
    }

    // === Config Path Constants ===
    public static final String GUI_TITLE     = "gui.title-menu";
    public static final String GUI_BUTTON_RIGHT = "gui.buttons.right-name";
    public static final String GUI_BUTTON_LEFT  = "gui.buttons.left-name";
    public static final String GUI_BUTTON_EXIT  = "gui.buttons.exit-name";

    /**
     * Retrieves a string from the config and translates alternate color codes (& -> §).
     *
     * @param config The plugin's configuration object.
     * @param path   The path in the config file.
     * @param fallback The default value to return if the path is missing or null.
     * @return A colorized string from the config or the provided fallback.
     */
    public static String getWithColor(FileConfiguration config, String path, String fallback) {
        if (config == null || path == null) return ChatColor.translateAlternateColorCodes('&', fallback);
        String raw = config.getString(path, fallback);
        return ChatColor.translateAlternateColorCodes('&', raw != null ? raw : fallback);
    }
}

