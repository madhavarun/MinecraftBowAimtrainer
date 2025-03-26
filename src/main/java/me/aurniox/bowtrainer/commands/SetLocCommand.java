package me.aurniox.bowtrainer.commands;

import me.aurniox.bowtrainer.BowTrainer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLocCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Player checks
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /setloc <game/lobby>");
            return true;
        }

        String type = args[0].toLowerCase();

        if (!type.equals("game") && !type.equals("lobby")) {
            sender.sendMessage(ChatColor.RED + "Invalid argument! Use 'game' or 'lobby'.");
            return true;
        }

        Location location = player.getLocation();

        saveLocationToConfig(type, location);

        sender.sendMessage(ChatColor.GREEN + "The location for the " + type + " area has been set!");

        return true;
    }

    private void saveLocationToConfig(String key, Location location) {
        BowTrainer plugin = BowTrainer.getInstance();
        plugin.getConfig().set(key + ".world", location.getWorld().getName());
        plugin.getConfig().set(key + ".x", location.getX());
        plugin.getConfig().set(key + ".y", location.getY());
        plugin.getConfig().set(key + ".z", location.getZ());
        plugin.getConfig().set(key + ".yaw", location.getYaw());
        plugin.getConfig().set(key + ".pitch", location.getPitch());
        plugin.saveConfig();
    }


}
