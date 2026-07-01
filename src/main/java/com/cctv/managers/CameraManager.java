package com.cctv.managers;

import com.cctv.models.Camera;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CameraManager {

    private final JavaPlugin plugin;
    private final File file;
    private final Map<String, Camera> cameras = new LinkedHashMap<>();
    private int counter = 0;

    public CameraManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "cameras.yml");
    }

    public Map<String, Camera> getCameras() {
        return cameras;
    }

    public Camera getCamera(String name) {
        return cameras.get(name.toLowerCase());
    }

    public Camera placeCamera(Location location) {
        counter++;
        String name = "cam" + counter;
        while (cameras.containsKey(name)) {
            counter++;
            name = "cam" + counter;
        }

        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setSmall(true);
        stand.setInvulnerable(true);
        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setArms(false);
        stand.setCustomNameVisible(true);
        stand.setCustomName("§b[CCTV] " + name);
        stand.getEquipment().setHelmet(new ItemStack(Material.PLAYER_HEAD));

        Camera camera = new Camera(name, location);
        camera.setStandEntity(stand);
        cameras.put(name, camera);
        saveCameras();
        return camera;
    }

    public boolean removeCamera(String name) {
        Camera camera = cameras.remove(name.toLowerCase());
        if (camera == null) return false;
        if (camera.getStandEntity() != null && !camera.getStandEntity().isDead()) {
            camera.getStandEntity().remove();
        }
        saveCameras();
        return true;
    }

    public void saveCameras() {
        FileConfiguration config = new YamlConfiguration
