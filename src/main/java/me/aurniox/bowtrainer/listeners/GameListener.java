package me.aurniox.bowtrainer.listeners;

import me.aurniox.bowtrainer.BowTrainer;
import me.aurniox.bowtrainer.handler.GameHandler;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;

public class GameListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("playing")) {
            Location gameLoc = GameHandler.gameLocation;
            Location playerLoc = player.getLocation();

            double maxDistance = 0.5;

            if (Math.abs(gameLoc.getX() - playerLoc.getX()) > maxDistance ||
                    Math.abs(gameLoc.getZ() - playerLoc.getZ()) > maxDistance) {

                // Move player back to the closest valid position instead of just cancelling movement
                double clampedX = Math.max(gameLoc.getX() - maxDistance, Math.min(playerLoc.getX(), gameLoc.getX() + maxDistance));
                double clampedZ = Math.max(gameLoc.getZ() - maxDistance, Math.min(playerLoc.getZ(), gameLoc.getZ() + maxDistance));

                Location newLoc = new Location(player.getWorld(), clampedX, playerLoc.getY(), clampedZ, playerLoc.getYaw(), playerLoc.getPitch());
                player.teleport(newLoc);
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("playing")) {
            GameHandler.stopGame(player);
        }
    }

    @EventHandler
    public void onChickenDeath(EntityDeathEvent event) {
        System.out.println("Entity Died");
        if (!(event.getEntity() instanceof Chicken)) {
            return;
        }
        System.out.println("Chicken Died");
        Chicken chicken = (Chicken) event.getEntity();

        if (!chicken.hasMetadata("playing")) {
            System.out.println("Not game chicken");
            return;
        }

        chicken.getLocation().getWorld().strikeLightningEffect(chicken.getLocation());
        event.getDrops().clear();
        event.setDroppedExp(0);
        chicken.remove();
        int chickenScore = getMetadataInt(chicken, "score");
        System.out.println(chickenScore);
        GameHandler.playerScore += chickenScore;
    }

    private int getMetadataInt(Entity chicken, String key) {
        if (chicken.hasMetadata(key)) {
            for (MetadataValue value : chicken.getMetadata(key)) {
                if (value.getOwningPlugin().equals(BowTrainer.getInstance())) {
                    return value.asInt(); // Convert metadata to integer
                }
            }
        }
        return 0; // Default if metadata is not found
    }
}
