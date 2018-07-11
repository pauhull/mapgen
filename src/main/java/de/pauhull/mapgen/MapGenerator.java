package de.pauhull.mapgen;

import de.pauhull.mapgen.command.CommandGetMap;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class MapGenerator {

    @Getter
    private static MapGenerator instance;

    @Getter
    private JavaPlugin plugin;

    @Getter
    private ScheduledExecutorService scheduledExecutorService;

    @Getter
    private ExecutorService executorService;

    public MapGenerator(JavaPlugin plugin, ScheduledExecutorService scheduledExecutorService, ExecutorService executorService) {
        instance = this;

        this.plugin = plugin;
        this.scheduledExecutorService = scheduledExecutorService;
        this.executorService = executorService;
    }

    public void start() {
        plugin.getCommand("getmap").setExecutor(new CommandGetMap());
    }

    public void stop() {

    }

}
