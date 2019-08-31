package com.github.atomishere.atombedwars;

import com.github.atomishere.atombedwars.arena.TestWorldManager;
import com.github.atomishere.atombedwars.commands.CommandManager;
import com.github.atomishere.atombedwars.utils.AtomUtils;
import com.github.atomishere.atombedwars.utils.ConfigUtils;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class AtomBedwars extends JavaPlugin {
    @Getter
    private TestWorldManager testWorldManager;
    @Getter
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        PluginDescriptionFile pdf = getDescription();
        getLogger().info("Enabling " + pdf.getName() + " v" + pdf.getVersion());

        if(!getDataFolder().exists()) {
            if(!getDataFolder().mkdirs()) {
                getLogger().info("Unable to create a data folder. Please create it yourself.");
            }
        }

        //Initialize Utils
        ConfigUtils.setPlugin(this);
        AtomUtils.setPlugin(this);

        //Initialize Services
        testWorldManager = new TestWorldManager();
        commandManager = new CommandManager(this);

        //Start Services
        commandManager.loadCommands();
        getLogger().info("Enabled " + pdf.getName());
    }

    @Override
    public void onDisable() {

    }
}
