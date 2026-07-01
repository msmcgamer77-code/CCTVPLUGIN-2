package com.cctv.managers;

import com.cctv.models.Camera;
import com.cctv.models.RecordEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecordingManager {

    private final JavaPlugin plugin;
    private final File footageFolder;

    private final Map<String, Session> active = new HashMap<>();

    public RecordingManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.footageFolder = new File(plugin.getDataFolder(), "footage");
        if (!footageFolder.exists()) footageFolder.mkdirs();
    }

    private static class Session {
        final long startTime = System.currentTimeMillis();
        final List<RecordEntry> entries = new ArrayList<>();
        BukkitTask task;
    }

    public boolean isRecording(String cameraName) {
        return active.containsKey(cameraName.toLowerCase());
    }

    public void startRecording(Camera camera) {
        String key = camera.getName().toLowerCase();
        if (active.containsKey(key)) return;

        Session session = new Session();
        int radius = plugin.getConfig().getInt("record-radius", 20);
        long interval = plugin.getConfig().getLong("record-interval-ticks", 20);

        session.task = new BukkitRunnable() {
            @Override
            public void run() {
                Location camLoc = camera.getLocation();
                for (Player p : camLoc.getWorld().getPlayers()) {
                    if (p.getLocation().distance(camLoc) <= radius) {
                        long offset = System.currentTimeMillis() - session.startTime;
                        session.entries.add(new RecordEntry(
                                offset, p.getName(), p.getWorld().getName(),
                                p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(),
                                p.getLocation().getYaw(), p.getLocation().getPitch()
                        ));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval);

        active.put(key, session);
    }

    public String stopRecording(String cameraName) {
        String key = cameraName.toLowerCase();
        Session session = active.remove(key);
        if (session == null) return null;
        session.task.cancel();

        String fileName = key + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".yml";
        File file = new File(footageFolder, fileName);
        FileConfiguration config = new YamlConfiguration();

        int i = 0;
        for (RecordEntry entry : session.entries) {
            String path = "entries." + i;
            config.set(path + ".time", entry.getOffsetMillis());
            config.set(path + ".player", entry.getPlayerName());
            config.set(path + ".world", entry.getWorld());
            config.set(path + ".x", entry.getX());
            config.set(path + ".y", entry.getY());
            config.set(path + ".z", entry.getZ());
            config.set(path + ".yaw", entry.getYaw());
            config.set(path + ".pitch", entry.getPitch());
            i++;
        }
        config.set("camera", cameraName);
        config.set("recorded-entries", session.entries.size());

        try {
            config.save(file);
        } catch (IOException e) {
