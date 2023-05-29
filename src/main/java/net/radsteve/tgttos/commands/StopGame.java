package net.radsteve.tgttos.commands;

import net.radsteve.tgttos.Main;
import net.radsteve.tgttos.handlers.PlayerHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class StopGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }
        PlayerHandler.removeBlocks();
        Player player = ((Player) sender).getPlayer();
        player.setExp(0);
        player.getInventory().clear();
        //placing barriers
        for(int x = 41; x <= 62; x++) {
            for(int y = 55; y <= 58; y++) {
                for(int z = -11; z == -11; z++) {
                    player.getWorld().getBlockAt(x, y, z).setType(Material.BARRIER);
                }
            }
        }
        player.setFlying(false);
        Location location = new Location(player.getWorld(), 52, 55, -12);
        player.teleport(location);

        for(Entity e : player.getWorld().getEntities()) {
            if(e.getType().toString() == "CHICKEN" || e.getType().toString() == "ITEM") e.remove();
        }

        for(int i = 0; i <= 15; i++) {
            player.getWorld().spawnEntity(new Location(player.getWorld(), 49, -19, 70), EntityType.CHICKEN);
        }

        Main.isRunning = false;
        return true;
    }
}
