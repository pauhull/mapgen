package de.pauhull.mapgen.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Configuration {

    @Getter
    private File file;

    @Getter
    private FileConfiguration config;

    public Configuration(File file) {
        this.load(file);
    }

    private void load(File file) {
        this.file = file;
        this.file.getParentFile().mkdirs();

        if (!this.file.exists()) {
            try {
                Files.copy(Configuration.class.getClassLoader().getResourceAsStream(this.file.getName()), this.file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.load(this.file);
    }
}
