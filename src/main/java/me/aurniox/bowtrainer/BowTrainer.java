package me.aurniox.bowtrainer;

import me.aurniox.bowtrainer.commands.StartGameCommand;
import me.aurniox.bowtrainer.commands.StopGameCommand;
import me.aurniox.bowtrainer.handler.GameHandler;
import me.aurniox.bowtrainer.listeners.GameListener;
import org.bukkit.plugin.java.JavaPlugin;

public class BowTrainer extends JavaPlugin {

    private static BowTrainer instance;
    private GameHandler gameHandler;

    public void onEnable() {
        instance = this;

        System.out.println("");
        System.out.println("[BowTrainer] Plugin loaded successfully");
        System.out.println("");

        loadHandlers();
        loadListeners();
        loadCommands();
    }

    public void onDisable() {
        System.out.println("");
        System.out.println("[BowTrainer] Disabling plugin");
        System.out.println("");
    }

    public void loadHandlers() {
        gameHandler = new GameHandler();
    }

    public void loadListeners() {
        getServer().getPluginManager().registerEvents(new GameListener(), this);
    }

    public void loadCommands() {
        getCommand("startgame").setExecutor(new StartGameCommand());
        getCommand("stopgame").setExecutor(new StopGameCommand());
    }

    public static BowTrainer getInstance() {
        return instance;
    }
}
