package net.radsteve.tgttos.commands;

import net.radsteve.tgttos.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;


public class Colour implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player player = Bukkit.getPlayer(sender.getName());
        String[] colours = {"red", "orange", "yellow", "lime", "green", "cyan", "aqua", "blue", "purple", "pink"};
        if(args.length != 1 || !Arrays.stream(colours).anyMatch(args[0]::equals)) {
            String currentColour = Main.getPlugin(Main.class).getConfig().getString(player.getName() + ".colour");
            if(currentColour != null) {
                sender.sendMessage("Your currently selected colour is " + currentColour);
            }
            sender.sendMessage("Available colours are: red, orange, yellow, lime, green, cyan, aqua, blue, purple and pink");
            return true;
        }

        Main.getPlugin(Main.class).setConfig(player.getName() + ".colour", args[0]);
        sender.sendMessage("Your colour has been changed to " + Main.getPlugin(Main.class).getConfig().getString(player.getName() + ".colour"));
        return true;
    }
}
