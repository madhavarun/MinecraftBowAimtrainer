package me.aurniox.bowtrainer.commands;

import me.aurniox.bowtrainer.handler.GameHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Player checks
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }

            Player player = (Player) sender;
            if (player.hasMetadata("playing")) {
                sender.sendMessage(ChatColor.RED + "You are already in a game");
                return true;
            }
            GameHandler.startGame(player);
            sender.sendMessage("§aYou have started your own game!");
            return true;
        }

        // Start game for another player
        if (args.length == 1) {
            if (!sender.hasPermission("bowtrainer.startgame.others")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to start a game for others!");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }

            if (target.hasMetadata("playing")) {
                sender.sendMessage(ChatColor.RED + "This player is already in a game");
                return true;
            }

            GameHandler.startGame(target);
            sender.sendMessage(ChatColor.GREEN + "You have started a game for " + target.getName() + "!");
            target.sendMessage(ChatColor.GREEN + "§aYour game has been started by " + sender.getName() + "!");
            return true;
        }

        // Send usage
        return false;
    }
}
