package me.aurniox.spinnyChicken;

import me.aurniox.spinnyChicken.listeners.actionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SpinnyChicken extends JavaPlugin {
    private static SpinnyChicken instance;

    public void onEnable() {
        instance = this;
        System.out.println("[SpinnyChicken] Plugin loaded successfully");

        Bukkit.getServer().getPluginManager().registerEvents(new actionListener(), this);
    }

    public void onDisable() {
        System.out.println("[SpinnyChicken] Disabling plugin");
    }

    public static SpinnyChicken getInstance() {
        return instance;
    }
}
