package net.radsteve.tgttos.commands;

import net.radsteve.tgttos.Config;
import net.radsteve.tgttos.Main;
import net.radsteve.tgttos.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collection;

public class StopGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }
        PlayerHandler.removeBlocks();
        if (Main.players == null) Main.players = (Collection<Player>) Bukkit.getOnlinePlayers();
        for (Player player : Main.players) {
            player.setExp(0);
            player.getInventory().clear();
            player.setFlying(false);
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(Config.getSpawnLocation(Main.getPlugin(Main.class), player.getWorld()));

            for (Entity e : player.getWorld().getEntities()) {
                if (e.getType().toString() == "CHICKEN" || e.getType().toString() == "ITEM") e.remove();
            }
            player.getInventory().clear();
        }

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String[] barrierArea = Config.getBarrierArea(Main.getPlugin(Main.class), ((Player) sender).getWorld());
        Bukkit.dispatchCommand(console, "fill " + barrierArea[0] + " " + barrierArea[1] + " barrier");

        for (int i = 0; i <= 15; i++) {
            ((Player) sender).getWorld().spawnEntity(Config.getChickenLocation(Main.getPlugin(Main.class), ((Player) sender).getWorld()), EntityType.CHICKEN);
        }

        Main.isRunning = false;
        return true;
    }
}
