package me.aurniox.spinnyChicken.listeners;

import me.aurniox.spinnyChicken.SpinnyChicken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class actionListener implements Listener {
    private final HashMap<UUID, Chicken> activeChickens = new HashMap<>();

    // Join Message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.AQUA + "Welcome to the server " + ChatColor.LIGHT_PURPLE + event.getPlayer().getName());
    }

    // Check for player damaging entity
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Bukkit.broadcastMessage("Entity Damaged Event Triggered by " + event.getDamager());
        if (!(event.getDamager() instanceof Player)) {
            Bukkit.broadcastMessage("Damager is not a player");
            return;
        }

        Bukkit.broadcastMessage(event.getDamage() + " to " + event.getEntity().getName());
    }

    // Spawn chicken on right click hoe
    @EventHandler
    public void onWoodenHoeRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        spawnChicken(event.getPlayer());
    }

    // Spinning Chicken
    private void spawnChicken(Player player) {
        // Spawn the chicken at the player's location
        Location spawnLocation = player.getLocation();
        Chicken chicken = player.getWorld().spawn(spawnLocation, Chicken.class);
        UUID chickenId = chicken.getUniqueId();

        // Disable chicken AI (Only works in newer versions)
        chicken.setAI(false);
        chicken.setGravity(false); // Optional, prevents falling

        activeChickens.put(chickenId, chicken);
        double radius = 2.0; // Radius of the circular motion
        Location center = spawnLocation.clone(); // Center point of the circle

        new BukkitRunnable() {
            double angle = 0; // Angle in radians

            @Override
            public void run() {
                // Stop if the chicken is removed or dies
                if (chicken.isDead() || !chicken.isValid()) {
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
                angle += Math.toRadians(15); // Adjust speed here
                Bukkit.broadcastMessage(angle + "");

                // Stop after one full circle (360 degrees)
                if (angle >= 2 * Math.PI) { // 2π radians = 360 degrees
                    activeChickens.remove(chickenId);
                    this.cancel();
                }
            }
        }.runTaskTimer(SpinnyChicken.getInstance(), 0L, 2L); // Runs every 2 ticks (adjust for smoothness)
    }
}
