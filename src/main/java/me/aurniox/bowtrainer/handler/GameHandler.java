package me.aurniox.bowtrainer.handler;

import me.aurniox.bowtrainer.BowTrainer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

// TODO: allow player to turn while in game/disable moving to other blocks
// TODO: Create a loop that spawns chickens until count >= 30
public class GameHandler {

    public static final HashMap<UUID, Chicken> activeChickens = new HashMap<>();
    public static BukkitTask gameTask;

    public static void startGame(Player player) {
        player.teleport(player.getWorld().getSpawnLocation());
        player.setMetadata("playing", new FixedMetadataValue(BowTrainer.getInstance(), true));
        spawnChicken(player);
        player.sendMessage(ChatColor.GREEN + "The game has started.");


        gameTask = new BukkitRunnable() {
            int timeLeft = 30;
            public void run() {
                // Countdown for aesthetic (wow!)
                if (timeLeft == 30 || timeLeft == 20 || timeLeft == 10 || timeLeft <= 5) {
                    player.sendMessage(ChatColor.WHITE + "The game will end in " + ChatColor.YELLOW + timeLeft + ChatColor.WHITE + " seconds.");
                }

                timeLeft--;

                if (timeLeft <= 0) {
                    stopGame(player);
                    this.cancel();
                    player.sendMessage(ChatColor.GREEN + "The game has ended.");
                }
            }
        }.runTaskTimer(BowTrainer.getInstance(), 0, 20L);
    }

    public static void stopGame(Player player) {
        player.teleport(player.getWorld().getSpawnLocation());
        player.sendMessage(ChatColor.RED + "The game has stopped.");
        gameTask.cancel();

        for (Chicken chicken : activeChickens.values()) {
            chicken.remove(); // Remove all chickens
        }

        activeChickens.clear();
        player.removeMetadata("playing", BowTrainer.getInstance());
    }

    private static void spawnChicken(Player player) {
        // Spawn the chicken at the player's location
        Location spawnLocation = player.getLocation();
        Chicken chicken = player.getWorld().spawn(spawnLocation, Chicken.class);

        for (Chicken chicken1 : activeChickens.values()) {
            chicken1.setMetadata("playing", new FixedMetadataValue(BowTrainer.getInstance(), true));
        }

        UUID chickenId = chicken.getUniqueId();

        // Disable chicken AI
        chicken.setAI(false);
        chicken.setGravity(false); // Optional, prevents falling

        activeChickens.put(chickenId, chicken);
        double radius = 10.0; // Radius of the circular motion
        Location center = spawnLocation.clone(); // Center point of the circle

        new BukkitRunnable() {
            double angle = 0; // Angle in radians

            @Override
            public void run() {
                // Stop if the chicken is removed, dies, or player doesn't have the correct metadata
                if (chicken.isDead() || !chicken.isValid() || !player.hasMetadata("playing")) {
                    activeChickens.remove(chickenId);
                    this.cancel();
                    return;
                }

                // Calculate the next position in the circle
                double x = center.getX() + radius * Math.cos(angle);
                double z = center.getZ() + radius * Math.sin(angle);
                Location newLoc = chicken.getLocation();
                newLoc.setX(x);
                newLoc.setZ(z);

                // Update the chicken's position
                chicken.teleport(newLoc);

                // Increase angle for the next movement
                angle += Math.toRadians(5); // Adjust speed here

            }
        }.runTaskTimer(BowTrainer.getInstance(), 0L, 2L); // Runs every 2 ticks (adjust for smoothness)
    }
}
