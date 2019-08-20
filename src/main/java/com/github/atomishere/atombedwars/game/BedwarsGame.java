package com.github.atomishere.atombedwars.game;

import com.github.atomishere.atombedwars.AtomBedwars;
import com.github.atomishere.atombedwars.arena.BedwarsArena;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class BedwarsGame implements Listener {
    private final AtomBedwars plugin;

    @Getter
    private final BedwarsArena arena;

    private BukkitTask diamondGeneratorTask = null;
    private BukkitTask emeraldGeneratorTask = null;
    private BukkitTask islandGeneratorTask = null;

    public void start() {
        diamondGeneratorTask = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new DiamondRunnable(arena), 20 * 30, 20 * 30);
        emeraldGeneratorTask = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new EmeraldRunnable(arena), 20 * 60, 20 * 60);
        islandGeneratorTask = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new IslandRunnable(arena), 20 * 5, 20 * 5);
    }

    public void stop() {
        diamondGeneratorTask.cancel();
        emeraldGeneratorTask.cancel();
        islandGeneratorTask.cancel();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

    }

    private BedwarsArena.Island getIsland(Bed bed) {
        if(arena.getPlayerOne().getIsland().getBed().equals(bed)) {
            return arena.getPlayerOne().getIsland();
        } else if(arena.getPlayerTwo().getIsland().getBed().equals(bed)) {
            return arena.getPlayerTwo().getIsland();
        }

        return null;
    }

    @RequiredArgsConstructor
    public static class DiamondRunnable implements Runnable {
        private final BedwarsArena arena;

        @Override
        public void run() {
            for(Location generator : arena.getDiamondGenerators()) {
                arena.getArenaWorld().dropItem(generator, new ItemStack(Material.DIAMOND));
            }
        }
    }

    @RequiredArgsConstructor
    public static class EmeraldRunnable implements Runnable {
        private final BedwarsArena arena;

        @Override
        public void run() {
            for(Location generator : arena.getEmeraldGenerators()) {
                arena.getArenaWorld().dropItem(generator, new ItemStack(Material.EMERALD));
            }
        }
    }

    @RequiredArgsConstructor
    public static class IslandRunnable implements Runnable {
        private final BedwarsArena arena;

        @Override
        public void run() {
            if(Math.random() < 0.625) {
                arena.getArenaWorld().dropItem(arena.getPlayerOne().getIsland().getGenerator(), new ItemStack(Material.IRON_INGOT));
                arena.getArenaWorld().dropItem(arena.getPlayerTwo().getIsland().getGenerator(), new ItemStack(Material.IRON_INGOT));
            } else {
                arena.getArenaWorld().dropItem(arena.getPlayerOne().getIsland().getGenerator(), new ItemStack(Material.GOLD_INGOT));
                arena.getArenaWorld().dropItem(arena.getPlayerTwo().getIsland().getGenerator(), new ItemStack(Material.GOLD_INGOT));
            }
        }
    }
}
