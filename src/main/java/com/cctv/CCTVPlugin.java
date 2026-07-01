package com.cctv;

import com.cctv.commands.CCTVCommand;
import com.cctv.listeners.CameraListener;
import com.cctv.managers.CameraManager;
import com.cctv.managers.RecordingManager;
import com.cctv.managers.WatchManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CCTVPlugin extends JavaPlugin {

    private static CCTVPlugin instance;

    private CameraManager cameraManager;
    private RecordingManager recordingManager;
    private WatchManager watchManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.cameraManager = new CameraManager(this);
        this.recordingManager = new RecordingManager(this);
        this.watchManager = new WatchManager(this);

        cameraManager.loadCameras();

        getCommand("cctv").setExecutor(new CCTVCommand(this));
        getServer().getPluginManager().registerEvents(new CameraListener(this), this);

        getLogger().info("CCTV Plugin enabled with " + cameraManager.getCameras().size() + " camera(s) loaded.");
    }

    @Override
    public void onDisable() {
        if (recordingManager != null) recordingManager.stopAllRecordings();
        if (cameraManager != null) cameraManager.saveCameras();
        getLogger().info("CCTV Plugin disabled.");
    }

    public static CCTVPlugin getInstance() {
        return instance;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public RecordingManager getRecordingManager() {
        return recordingManager;
    }

    public WatchManager getWatchManager() {
        return watchManager;
    }
}
