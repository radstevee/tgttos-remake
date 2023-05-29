package net.radsteve.tgttos.commands;

import net.radsteve.tgttos.Main;
import net.radsteve.tgttos.handlers.PlayerHandler;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class StartGame implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }
        Player player = Bukkit.getPlayer(sender.getName());
        player.addPotionEffect(PotionEffectType.SATURATION.createEffect(PotionEffect.INFINITE_DURATION, 255));
        player.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
        player.sendTitle(ChatColor.RED + "3", "", 5, 20, 0);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.sendTitle(ChatColor.GOLD + "2", "", 5, 20, 0);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.sendTitle(ChatColor.YELLOW + "1", "", 5, 20, 0);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int x = 41; x <= 62; x++) {
            for(int y = 55; y <= 58; y++) {
                for(int z = -11; z == -11; z++) {
                    player.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
        player.sendTitle(ChatColor.GREEN + "GOOOO!!!", "", 5, 20, 0);
        if(Main.getPlugin(Main.class).getConfig().getString(player.getName() + ".colour") == null) {
            Main.getPlugin(Main.class).setConfig(player.getName() + ".colour", "red");
        }


        String colour = Main.getPlugin(Main.class).getConfig().getString(player.getName() + ".colour").replace("aqua", "light_blue").toUpperCase() + "_WOOL";

        Material material = Material.valueOf(colour);
        player.getInventory().setItemInOffHand(new ItemStack(material, 64));
        player.getInventory().setItem(0, new ItemStack(Material.SHEARS, 1));
        player.getInventory().getItem(0).addEnchantment(Enchantment.DIG_SPEED, 3);
        player.getInventory().getItem(0).addEnchantment(Enchantment.DURABILITY, 3);
        player.setGameMode(GameMode.SURVIVAL);
        Main.startTimer();
        Main.isRunning = true;
        try {
            PlayerHandler.showTime();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
