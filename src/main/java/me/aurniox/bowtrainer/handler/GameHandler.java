package me.aurniox.bowtrainer.handler;

import me.aurniox.bowtrainer.BowTrainer;
import me.aurniox.bowtrainer.util.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class GameHandler {

    // locations
    public static Location lobbyLocation = LocationUtil.getLocationFromConfig("lobby");
    public static Location gameLocation = LocationUtil.getLocationFromConfig("game");

    private static final int maxChickenCount = 20;
    public static int playerScore = 0;
    private static int chickenCount = 0;

    // tasks
    private static BukkitTask gameTask;
    private static BukkitTask countdownTask;

    // radius
    public static double minRadius = 3;
    public static double maxRadius = 30;
    private static double radius = 15;


    public static void startGame(Player player) {
        playerScore = 0;
        chickenCount = 0;

        player.teleport(gameLocation);
        player.setMetadata("playing", new FixedMetadataValue(BowTrainer.getInstance(), true));
        player.sendMessage(ChatColor.GREEN + "The game has started.");

        spawnChicken(player);
        gameTask = new BukkitRunnable() {
            public void run() {
                // Add code to make chickens spawn until 10
                System.out.println(chickenCount);
                if (chickenCount < maxChickenCount) {
                    spawnChicken(player);
                }
            }
        }.runTaskTimer(BowTrainer.getInstance(), 0, 15L);

        countdownTask = new BukkitRunnable() {
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
                }
            }
        }.runTaskTimer(BowTrainer.getInstance(), 0, 20L);
    }

    public static void stopGame(Player player) {
        countdownTask.cancel();
        gameTask.cancel();

        player.teleport(lobbyLocation);
        player.sendMessage(ChatColor.GREEN + "The game has ended.");
        player.sendMessage("Your score was " + playerScore);
        player.removeMetadata("playing", BowTrainer.getInstance());
    }

    private static void spawnChicken(Player player) {
        Random rand = new Random();

        // Spawn the chicken around the player at varying heights
        Location spawnLocation = player.getLocation();
        spawnLocation.setY(spawnLocation.getY() + rand.nextDouble(2.5) + 1);
        Chicken chicken = player.getWorld().spawn(spawnLocation, Chicken.class);

        int chickenScore = rand.nextInt(3) + 1;
        // Set metadata used for score, speed, and game entity marker
        chicken.setMetadata("playing", new FixedMetadataValue(BowTrainer.getInstance(), true));
        chicken.setMetadata("score", new FixedMetadataValue(BowTrainer.getInstance(), chickenScore));

        chicken.setAI(false);
        chicken.setGravity(false);
        chicken.setHealth(1);

        Location center = spawnLocation.clone(); // Center point of the circle
        chickenCount ++;

        // Separate for each chicken instance, stops whenever game stops or individual chicken dies
        new BukkitRunnable() {
            double angle = 0; // Angle in radians

            public void run() {
                // Stop if the chicken is removed, dies, or player doesn't have the correct metadata
                // DO NOT HANDLE SCORE as this runs regardless at end of game
                if (chicken.isDead() || !chicken.isValid() || !player.hasMetadata("playing")) {
                    chicken.remove();
                    chickenCount--;
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
                angle += Math.toRadians(3 * chickenScore); // Adjust speed here

            }
        }.runTaskTimer(BowTrainer.getInstance(), 0L, 2L); // Runs every 2 ticks (adjust for smoothness)
    }

    public static void setRadius(double newRadius) {
        if (newRadius < minRadius || newRadius > maxRadius) {
            return;
        }
        radius = newRadius;
    }
}