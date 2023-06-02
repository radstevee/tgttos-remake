package net.radsteve.tgttos;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;


public class Config {
    public static String[] availableMaps = {"skydive", "walls"};

    public static void setDefaultConfig(JavaPlugin plugin) {
        Configuration config = plugin.getConfig();
        if (config.getString("current_map") == "") config.set("current_map", "skydive");
        if (config.getString("maps.skydive.chicken_loc") == "") config.set("maps.skydive.chicken_loc", "49, -19, 70");
        if (config.getString("maps.skydive.barrier_area.corner_1") == "")
            config.set("maps.skydive.barrier_area.corner_1", "62, 58, -11");
        if (config.getString("maps.skydive.barrier_area.corner_2") == "")
            config.set("maps.skydive.barrier_area.corner_2", "41, 55, -11");
        if (config.getString("maps.skydive.spawn") == "") config.set("maps.skydive.spawn", "52, 55, -12");
        if (config.getString("maps.walls.chicken_loc") == "") config.set("maps.walls.chicken_loc", "318, 18, -236");
        if (config.getString("maps.walls.barrier_area.corner_1") == "")
            config.set("maps.walls.barrier_area.corner_1", "244, 9, -247");
        if (config.getString("maps.walls.barrier_area.corner_2") == "")
            config.set("maps.walls.barrier_area.corner_2", "244, 6, -226");
        if (config.getString("maps.walls.spawn") == "") config.set("maps.walls.spawn", "243, 6, -236");
        plugin.saveConfig();
    }

    public static String getCurrentMap(JavaPlugin plugin) {
        return plugin.getConfig().getString("current_map");
    }

    public static void setCurrentMap(JavaPlugin plugin, String map) {
        if (!Arrays.stream(availableMaps).anyMatch(map::equals)) return;
        plugin.getConfig().set("current_map", map);
        plugin.saveConfig();
    }

    public static String[] getBarrierArea(JavaPlugin plugin, World world) {
        Configuration config = plugin.getConfig();
        String corner1 = config.getString("maps." + getCurrentMap(plugin) + ".barrier_area.corner_1").replace(",", "");
        String corner2 = config.getString("maps." + getCurrentMap(plugin) + ".barrier_area.corner_2").replace(",", "");
        return new String[]{corner1, corner2};
    }

    public static Location getChickenLocation(JavaPlugin plugin, World world) {
        String[] coords = plugin.getConfig().getString("maps." + getCurrentMap(plugin) + ".chicken_loc").replace(",", "").split(" ");
        return new Location(world, Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2]));
    }

    public static Location getSpawnLocation(JavaPlugin plugin, World world) {
        String[] coords = plugin.getConfig().getString("maps." + getCurrentMap(plugin) + ".spawn").replace(",", "").split(" ");
        return new Location(world, Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2]));
    }
}
