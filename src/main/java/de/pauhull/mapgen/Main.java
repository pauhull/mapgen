package de.pauhull.mapgen;

import de.pauhull.mapgen.config.Configuration;
import de.pauhull.mapgen.util.ColorMap;
import de.pauhull.mapgen.util.ColorSpace;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main extends JavaPlugin {

    public static final String PLUGIN_NAME = "MapGenerator";

    private int threadID;
    private MapGenerator mapGenerator;
    private Configuration configuration;

    @Override
    public void onEnable() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(this::createThread);
        ExecutorService executorService = Executors.newCachedThreadPool(this::createThread);

        configuration = new Configuration(new File("plugins/Map Generator/config.yml"));
        ColorMap.init(this.getColorSpace());

        mapGenerator = new MapGenerator(this, scheduledExecutorService, executorService);
        mapGenerator.start();
    }

    @Override
    public void onDisable() {
        mapGenerator.stop();
    }

    private Thread createThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(PLUGIN_NAME + " Task # " + (++threadID));
        return thread;
    }

    private ColorSpace getColorSpace() {
        ColorSpace colorSpace = ColorSpace.ORIGINAL;
        String version = configuration.getConfig().getString("ColorSpace");

        if (version.equalsIgnoreCase("AUTO")) {
            String bukkitVersion = Bukkit.getBukkitVersion();

            if (bukkitVersion.contains("1.15")) {
                colorSpace = ColorSpace.MC1_12;
            } else if (bukkitVersion.contains("1.14")) {
                colorSpace = ColorSpace.MC1_12;
            } else if (bukkitVersion.contains("1.13")) {
                colorSpace = ColorSpace.MC1_12;
            } else if (bukkitVersion.contains("1.12")) {
                colorSpace = ColorSpace.MC1_12;
            } else if (bukkitVersion.contains("1.11")) {
                colorSpace = ColorSpace.MC1_8_1;
            } else if (bukkitVersion.contains("1.10")) {
                colorSpace = ColorSpace.MC1_8_1;
            } else if (bukkitVersion.contains("1.9")) {
                colorSpace = ColorSpace.MC1_8_1;
            } else if (bukkitVersion.contains("1.8")) {
                if (!bukkitVersion.contains("1.8-")) {
                    colorSpace = ColorSpace.MC1_8_1;
                } else {
                    colorSpace = ColorSpace.MC1_7_2;
                }
            } else if (bukkitVersion.contains("1.7")) {
                colorSpace = ColorSpace.MC1_7_2;
            } else if (bukkitVersion.contains("1.6")) {
                colorSpace = ColorSpace.ORIGINAL;
            } else if (bukkitVersion.contains("1.5")) {
                colorSpace = ColorSpace.ORIGINAL;
            } else if (bukkitVersion.contains("1.4")) {
                colorSpace = ColorSpace.ORIGINAL;
            } else if (bukkitVersion.contains("1.3")) {
                colorSpace = ColorSpace.ORIGINAL;
            } else if (bukkitVersion.contains("1.2")) {
                colorSpace = ColorSpace.ORIGINAL;
            } else if (bukkitVersion.contains("1.1")) {
                colorSpace = ColorSpace.ORIGINAL;
            } else if (bukkitVersion.contains("1.0")) {
                colorSpace = ColorSpace.ORIGINAL;
            }

        } else if (version.equalsIgnoreCase("MC1_12")) {
            colorSpace = ColorSpace.MC1_12;
        } else if (version.equalsIgnoreCase("MC1_8_1")) {
            colorSpace = ColorSpace.MC1_8_1;
        } else if (version.equalsIgnoreCase("MC1_7_2")) {
            colorSpace = ColorSpace.MC1_7_2;
        } else if (version.equalsIgnoreCase("ORIGINAL")) {
            colorSpace = ColorSpace.ORIGINAL;
        }

        Bukkit.getConsoleSender().sendMessage("[" + Main.PLUGIN_NAME + "] Using Color Space: " + colorSpace.name());

        return colorSpace;
    }

}
