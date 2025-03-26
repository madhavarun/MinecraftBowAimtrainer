package me.aurniox.bowtrainer.util;

import me.aurniox.bowtrainer.BowTrainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    /**
     * Fetches a stored location from the plugin's configuration.
     *
     * @param key The configuration key for the location (e.g., 'game' or 'lobby').
     * @return The corresponding location object, or null if invalid.
     */

    public static Location getLocationFromConfig(String key) {
        BowTrainer plugin = BowTrainer.getInstance();
        String worldName = plugin.getConfig().getString(key + ".world");

        if (worldName == null) {
            return null;
        }

        double x = plugin.getConfig().getDouble(key + ".x");
        double y = plugin.getConfig().getDouble(key + ".y");
        double z = plugin.getConfig().getDouble(key + ".z");
        float yaw = (float) plugin.getConfig().getDouble(key + ".yaw");
        float pitch = (float) plugin.getConfig().getDouble(key + ".pitch");

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
