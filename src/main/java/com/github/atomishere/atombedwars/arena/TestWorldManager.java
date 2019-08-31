package com.github.atomishere.atombedwars.arena;

import com.github.atomishere.atombedwars.utils.AtomUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestWorldManager {
    private final Map<String, World> worlds = new HashMap<>();

    public World loadWorld(String schematicName, String arenaName) {
        World world = AtomUtils.generateWorld(schematicName, arenaName);
        if(world != null) {
            worlds.put(schematicName, world);
        }

        return world;
    }

    public void removeWorld(String schematicName) {
        World world = worlds.get(schematicName);
        if(world == null) {
            return;
        }

        File worldFile = world.getWorldFolder();
        Bukkit.getServer().unloadWorld(world, false);

        try {
            FileUtils.deleteDirectory(worldFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public World getWorld(String world) {
        return worlds.get(world);
    }

    public boolean hasWorld(String world) {
        return worlds.containsKey(world);
    }
}
