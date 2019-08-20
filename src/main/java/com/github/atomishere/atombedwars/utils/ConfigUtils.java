package com.github.atomishere.atombedwars.utils;

import com.github.atomishere.atombedwars.AtomBedwars;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigUtils {
    private static AtomBedwars plugin = null;

    private ConfigUtils() {
    }

    public static void setPlugin(AtomBedwars plugin) {
        if(ConfigUtils.plugin == null) ConfigUtils.plugin = plugin;
    }

    public static FileConfiguration getConifg(String configName) {
        File customConfigFile = new File(plugin.getDataFolder(), configName);
        if(!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource(configName, false);
        }

        YamlConfiguration customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return customConfig;
    }

    public static void saveConfig(FileConfiguration config, String file) throws IOException {
        File configFile = new File(plugin.getDataFolder(), file);
        if(!configFile.exists()) {
            if(!configFile.createNewFile()) return;
        }

        config.save(configFile);
    }

}
