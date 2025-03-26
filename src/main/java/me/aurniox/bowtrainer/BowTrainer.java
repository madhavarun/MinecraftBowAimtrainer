package me.aurniox.bowtrainer;

import lombok.Getter;
import me.aurniox.bowtrainer.commands.SetLocCommand;
import me.aurniox.bowtrainer.commands.SetRadiusCommand;
import me.aurniox.bowtrainer.commands.StartGameCommand;
import me.aurniox.bowtrainer.commands.StopGameCommand;
import me.aurniox.bowtrainer.handler.GameHandler;
import me.aurniox.bowtrainer.listeners.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BowTrainer extends JavaPlugin {

    @Getter private static BowTrainer instance;
    @Getter private GameHandler gameHandler;

    public void onEnable() {
        instance = this;

        saveDefaultConfig();


        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("[BowTrainer] Plugin loaded successfully");
        Bukkit.getLogger().info("");

        loadListeners();
        loadCommands();
    }

    public void onDisable() {
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("[BowTrainer] Disabling plugin");
        Bukkit.getLogger().info("");
    }

    public void loadListeners() {
        getServer().getPluginManager().registerEvents(new GameListener(), this);
    }

    public void loadCommands() {
        getCommand("startgame").setExecutor(new StartGameCommand());
        getCommand("stopgame").setExecutor(new StopGameCommand());
        getCommand("setradius").setExecutor(new SetRadiusCommand());
        getCommand("setloc").setExecutor(new SetLocCommand());
    }

}
