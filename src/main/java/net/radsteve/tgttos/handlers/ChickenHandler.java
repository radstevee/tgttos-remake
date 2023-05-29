package net.radsteve.tgttos.handlers;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.radsteve.tgttos.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class ChickenHandler implements Listener {
    public ChickenHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onChickenHit(EntityDamageByEntityEvent event) {
        if(event.getDamager().getType().toString() == "PLAYER" && event.getEntity().getType().toString() == "CHICKEN") {
            Player player = Bukkit.getPlayer(event.getDamager().getName());
            if(player.getAllowFlight()) {
                event.setCancelled(true);
                return;
            }
            event.getEntity().remove();
            Main.stopTimer();
            player.sendTitle(ChatColor.GREEN + "Yay!", ChatColor.GREEN + "You finished in " + Main.formatTime(Main.timeElapsed) + "!", 5, 100, 5);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 100, 0);
            player.setGameMode(GameMode.ADVENTURE);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.getInventory().clear();
        }
    }
}
