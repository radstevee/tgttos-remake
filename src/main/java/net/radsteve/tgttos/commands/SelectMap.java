package net.radsteve.tgttos.commands;

import net.radsteve.tgttos.Config;
import net.radsteve.tgttos.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SelectMap implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0 || !Arrays.stream(Config.availableMaps).anyMatch(args[0]::equalsIgnoreCase)) {
            sender.sendMessage("Available maps are: " + Arrays.stream(Config.availableMaps).collect(Collectors.joining(", ")));
            return true;
        }

        Config.setCurrentMap(Main.getPlugin(Main.class), args[0]);
        sender.sendMessage("You set the map to " + Config.getCurrentMap(Main.getPlugin(Main.class)));
        ((Player) sender).teleport(Config.getSpawnLocation(Main.getPlugin(Main.class), ((Player) sender).getWorld()));
        return true;
    }
}
