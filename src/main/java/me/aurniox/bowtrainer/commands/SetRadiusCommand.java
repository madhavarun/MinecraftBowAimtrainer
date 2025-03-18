package me.aurniox.bowtrainer.commands;

import me.aurniox.bowtrainer.handler.GameHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetRadiusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        double newRadius;
        if (args.length != 1) {
            return false;
        }
        try {
            newRadius = Double.parseDouble(args[0]);
        }
        catch (NumberFormatException e) {
            sender.sendMessage("The argument should be a valid integer or float");
            return true;
        }
        if (newRadius < GameHandler.minRadius || newRadius > GameHandler.maxRadius) {
            sender.sendMessage("The entered value is not valid. You can only use a value from 3 to 30.");
            return true;
        }
        GameHandler.setRadius(newRadius);
        return true;
    }
}
