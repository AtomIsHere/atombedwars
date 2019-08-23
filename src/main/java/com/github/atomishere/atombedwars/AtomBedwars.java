package com.github.atomishere.atombedwars;

import com.github.atomishere.atombedwars.utils.AtomUtils;
import com.github.atomishere.atombedwars.utils.ConfigUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class AtomBedwars extends JavaPlugin {
    @Override
    public void onEnable() {
        ConfigUtils.setPlugin(this);
        AtomUtils.setPlugin(this);
    }

    @Override
    public void onDisable() {

    }
}
