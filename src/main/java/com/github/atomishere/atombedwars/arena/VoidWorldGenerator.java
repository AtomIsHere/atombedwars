package com.github.atomishere.atombedwars.arena;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VoidWorldGenerator extends ChunkGenerator {
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public byte[] generate(World world, Random rand, int chunkx, int chunky) {
        return new byte[32768];
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random rand) {
        return new Location(world, 0, 128, 0);
    }
}
