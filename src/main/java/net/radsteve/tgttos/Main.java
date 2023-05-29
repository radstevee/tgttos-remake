package net.radsteve.tgttos;

import net.radsteve.tgttos.commands.Colour;
import net.radsteve.tgttos.commands.StartGame;
import net.radsteve.tgttos.commands.StopGame;
import net.radsteve.tgttos.handlers.ChickenHandler;
import net.radsteve.tgttos.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class Main extends JavaPlugin {
    public static boolean isRunning = false;
    public static boolean isTimerRunning = false;
    public static Instant timer;
    public static long timeElapsed = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("[tgttos] Initializing To get to the other side (and whack a fan!)");
        getCommand("stopgame").setExecutor(new StopGame());
        getCommand("startgame").setExecutor(new StartGame());
        getCommand("colour").setExecutor(new Colour());
        new ChickenHandler(this);
        new PlayerHandler(this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(PlayerHandler.placedBlocks != null) {
            for(Location loc : PlayerHandler.placedBlocks) {
                loc.getBlock().setType(Material.AIR);
            }
        }
        Bukkit.getLogger().warning("[tgttos] Shutting down To get to the other side (and whack a fan!)");
    }

    public static boolean getRunning() {
        return isRunning;
    }

    public static void setRunning(boolean bool) {
        isRunning = bool;
    }

    public static void startTimer() {
        timer = Instant.now();
        isTimerRunning = true;
    }

    public static void stopTimer() {
        timeElapsed = Duration.between(timer, Instant.now()).toMillis();
        isTimerRunning = false;
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
