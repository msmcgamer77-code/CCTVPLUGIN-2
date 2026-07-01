package com.cctv.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class ItemUtil {

    public static final String CAMERA_TAG = "cctv_camera_item";
    public static final String MONITOR_TAG = "cctv_monitor_item";

    public static ItemStack createCameraItem(Plugin plugin) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§b§lCCTV Camera");
        meta.setLore(java.util.List.of(
                "§7Right-click a block to place a camera",
                "§7here."
        ));
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, CAMERA_TAG), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createMonitorItem(Plugin plugin) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§lCCTV Monitor");
        meta.setLore(java.util.List.of(
                "§7Right-click (in air) to open",
                "§7the camera list and watch live."
        ));
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, MONITOR_TAG), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isCameraItem(Plugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, CAMERA_TAG), PersistentDataType.BYTE);
    }

    public static boolean isMonitorItem(Plugin plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, MONITOR_TAG), PersistentDataType.BYTE);
    }
}
