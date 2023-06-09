package net.radsteve.tgttos;

import net.radsteve.tgttos.commands.Colour;
import net.radsteve.tgttos.commands.SelectMap;
import net.radsteve.tgttos.commands.StartGame;
import net.radsteve.tgttos.commands.StopGame;
import net.radsteve.tgttos.handlers.ChickenHandler;
import net.radsteve.tgttos.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public final class Main extends JavaPlugin {
    public static boolean isRunning = false;
    public static Instant timer;
    public static Collection<Player> players;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getCommand("stopgame").setExecutor(new StopGame());
        getCommand("startgame").setExecutor(new StartGame());
        getCommand("colour").setExecutor(new Colour());
        getCommand("selectmap").setExecutor(new SelectMap());
        new ChickenHandler(this);
        new PlayerHandler(this);
        Config.setDefaultConfig(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (PlayerHandler.placedBlocks != null) {
            for (Location loc : PlayerHandler.placedBlocks) {
                loc.getBlock().setType(Material.AIR);
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getConfig().getString(player.getName() + ".isFinished") != null) getConfig().set(player.getName(), "");
        }
        saveConfig();
        Bukkit.getLogger().info("[tgttos] Shutting down TGTTOS");
    }

    public static boolean getRunning() {
        return isRunning;
    }

    public static void setRunning(boolean bool) {
        isRunning = bool;
    }

    public static void startTimer() {
        for (Player player : players) {
            Main.getPlugin(Main.class).setConfig(player.getName() + ".isFinished", "false");
        }
        timer = Instant.now();
    }

    public static void stopTimer(Player player) {
        Main.getPlugin(Main.class).setConfig(player.getName() + ".isFinished", "true");
        Main.getPlugin(Main.class).setConfig(player.getName() + ".time", Duration.between(timer, Instant.now()).toMillis());
    }

    public static long getTime() {
        return Duration.between(timer, Instant.now()).toMillis();
    }

    public static String formatTime(long time) {
        long MM = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        long MS = TimeUnit.MILLISECONDS.toMillis(time) % 600;

        String min = String.valueOf(MM);
        String sec = String.valueOf(SS);
        String msec = String.valueOf(MS);

        if(min.length() == 1) min = "0" + min;
        if(sec.length() == 1) sec = "0" + sec;
        if(msec.length() == 1) msec = "0" + msec;

        return min + ":" + sec + ":" + msec;
    }

    public void setConfig(String key, Object val) {
        getConfig().set(key, val);
    }
}
