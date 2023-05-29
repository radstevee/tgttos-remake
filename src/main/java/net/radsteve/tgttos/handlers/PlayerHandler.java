package net.radsteve.tgttos.handlers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.radsteve.tgttos.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandler implements Listener {

    public PlayerHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    public static List<Location> placedBlocks = new ArrayList<Location>();

    //show time in actionbar
    public static void showTime() throws InterruptedException {
        if(!Main.isRunning) return;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        Player player = Bukkit.getPlayer("radstevee");

        BukkitTask task = scheduler.runTaskTimer(Main.getPlugin(Main.class), () -> {
            if(!Main.isRunning || !Main.isTimerRunning) return;
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + Main.formatTime(Main.getTime())));
            for(Entity e : player.getWorld().getEntities()) {
                if(e.getType().toString() == "ITEM") e.remove();
            }
        }, 0L, 1L);
    }

    //remove placed blocks
    public static void removeBlocks() {
        if(PlayerHandler.placedBlocks != null) {
            for(Location loc : PlayerHandler.placedBlocks) {
                loc.getBlock().setType(Material.AIR);
            }
        }
    }

    //teleport player to spawn if below y-25
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(event.getTo().getBlockY() <= -25) {
            Location location = new Location(player.getWorld(), 52, 55, -12);
            player.teleport(location);
        }
    }

    //no fall damage
    @EventHandler
    public void ifPlayerDamage(EntityDamageByBlockEvent event) {
        if(event.getEntity().getType().toString() == "PLAYER") {
            event.setCancelled(true);
        }
    }

    //resupply blocks and add block to list to clear later on
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!Main.getRunning()) event.setCancelled(true);
        if(Main.getPlugin(Main.class).getConfig().getString(event.getPlayer().getName() + ".colour") == null) {
            Main.getPlugin(Main.class).setConfig(event.getPlayer().getName() + ".colour", "red");
        }
        String colour = Main.getPlugin(Main.class).getConfig().getString(event.getPlayer().getName() + ".colour").replace("aqua", "light_blue").toUpperCase() + "_WOOL";
        Material material = Material.valueOf(colour);
        placedBlocks.add(new Location(event.getBlock().getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()));
        event.getPlayer().getInventory().setItemInOffHand(new ItemStack(material, 64));
    }

}
