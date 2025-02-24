package me.aurniox.bowtrainer.listeners;

import me.aurniox.bowtrainer.handler.GameHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("playing")) {
            event.setCancelled(true);
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
        Entity chicken = event.getEntity();

        if (!chicken.hasMetadata("playing")) {
            return;
        }

        event.getDrops().clear();
        event.setDroppedExp(0);
        chicken.remove();

        chicken.getLocation().getWorld().strikeLightningEffect(chicken.getLocation());
    }
}
