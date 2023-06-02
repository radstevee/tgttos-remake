package net.radsteve.tgttos.commands;

import net.radsteve.tgttos.Config;
import net.radsteve.tgttos.Main;
import net.radsteve.tgttos.handlers.PlayerHandler;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;

public class StartGame implements CommandExecutor {
    int time;
    BukkitTask task;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }
        Main.players = (Collection<Player>) Bukkit.getOnlinePlayers();
        ((Player) sender).getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
        for (Player player : Main.players) {
            player.setGameMode(GameMode.ADVENTURE);
            player.addPotionEffect(PotionEffectType.SATURATION.createEffect(PotionEffect.INFINITE_DURATION, 255));

            // this is still a very temporary and janky method to do it
            if (Main.getPlugin(Main.class).getConfig().getString(player.getName() + ".colour") == null) {
                Main.getPlugin(Main.class).setConfig(player.getName() + ".colour", "red");
            }
        }
        ;

        setCountdown(3);
        startTimer(((Player) sender).getWorld());
        return true;
    }

    public void setCountdown(int amount) {
        time = amount;
    }

    public void stopCountdown() {
        task.cancel();
    }

    public void startTimer(World world) {
        task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
            if (time == -1) {
                task.cancel();
                return;
            }
            if (time == 0) {
                for (Player player : Main.players) {
                    Material material = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString(player.getName() + ".colour").replace("aqua", "light_blue").toUpperCase() + "_WOOL");
                    player.getInventory().setItemInOffHand(new ItemStack(material, 64));
                    player.getInventory().setItem(0, new ItemStack(Material.SHEARS, 1));
                    player.getInventory().getItem(0).addEnchantment(Enchantment.DIG_SPEED, 3);
                    player.getInventory().getItem(0).addEnchantment(Enchantment.DURABILITY, 3);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setLevel(0);
                    player.sendTitle(ChatColor.GREEN + "GOOOO!!!", "", 5, 20, 0);
                }
                if (world.getBlockAt(62, 58, -11).getType() == Material.BARRIER) {
                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                    String[] barrierArea = Config.getBarrierArea(Main.getPlugin(Main.class), world);
                    Bukkit.dispatchCommand(console, "fill " + barrierArea[0] + " " + barrierArea[1] + " air");
                }
                Main.startTimer();
                Main.isRunning = true;
                stopCountdown();
                PlayerHandler.showTime();
                task.cancel();
                setCountdown(-1);
                return;
            }

            if (time > 0) {
                ChatColor colour = ChatColor.RED;
                switch (time) {
                    case 2:
                        colour = ChatColor.GOLD;
                        break;
                    case 1:
                        colour = ChatColor.YELLOW;
                        break;
                }
                Bukkit.getLogger().info(String.valueOf(time));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setLevel(time);
                    player.sendTitle(colour + String.valueOf(time), "", 5, 20, 0);
                }
            }

            if (time != -1) setCountdown(time - 1);
        }, 0L, 20L);
    }
}
