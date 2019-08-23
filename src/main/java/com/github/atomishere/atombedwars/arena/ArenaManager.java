package com.github.atomishere.atombedwars.arena;

import com.boydti.fawe.object.schematic.Schematic;
import com.github.atomishere.atombedwars.AtomBedwars;
import com.github.atomishere.atombedwars.utils.AtomUtils;
import com.github.atomishere.atombedwars.utils.ConfigUtils;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ArenaManager {
    private final AtomBedwars plugin;

    private final Map<String, ConfigurationSection> unloadedArenas = new HashMap<>();
    private final Map<UUID, BedwarsArena> arenas = new HashMap<>();

    public void registerArenas() {
        File dataFolder = plugin.getDataFolder();
        File schematicFolder = new File(dataFolder, "schematics");

        if(!dataFolder.exists()) {
            if(!dataFolder.mkdirs()) {
                plugin.getLogger().severe("Unable to create arenas directory.");
                return;
            }
        }

        if(!schematicFolder.exists()) {
            if(!schematicFolder.mkdirs()) {
                plugin.getLogger().severe("Unable to create schematic directory, please do it yourself");
            }
        }

        try(Stream<Path> walk = Files.walk(Paths.get(dataFolder.toURI()))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(Path::toString).filter(f -> f.endsWith(".yml") || f.endsWith("yaml")).collect(Collectors.toList());

            for(String path : result) {
                File configFile = new File(path);

                FileConfiguration arenaData = ConfigUtils.getConifg(configFile.getName());
                if(arenaData == null) {
                    continue;
                }

                if(!verifyArenaData(arenaData)) {
                    continue;
                }

                unloadedArenas.put(arenaData.getString("name"), arenaData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BedwarsArena loadArena(String arenaName, Player playerOne, Player playerTwo) {
        ConfigurationSection arenaData = unloadedArenas.get(arenaName);
        if (arenaData == null) {
            return null;
        }

        World arenaWorld = AtomUtils.generateWorld(arenaData.getString("schematic_name"), arenaName);
        if(arenaWorld == null) {
            return null;
        }

        BedwarsArena.PlayerContainer playerOneContainer = loadPlayerContainer(arenaData.getConfigurationSection("island_one"), arenaWorld, playerOne);
        BedwarsArena.PlayerContainer playerTwoContainer = loadPlayerContainer(arenaData.getConfigurationSection("island_two"), arenaWorld, playerTwo);

        BedwarsArena arena = BedwarsArena.Builder.newBuilder()
                .setArenaWorld(arenaWorld)
                .setPlayerOne(playerOneContainer)
                .setPlayerTwo(playerTwoContainer)
                .setDiamondGenerators(getLocationList(arenaData.getConfigurationSection("diamond_generators"), arenaWorld))
                .setEmeraldGenerators(getLocationList(arenaData.getConfigurationSection("emerald_generators"), arenaWorld))
                .build();

        arenas.put(arenaWorld.getUID(), arena);
        return arena;
    }

    private BedwarsArena.PlayerContainer loadPlayerContainer(ConfigurationSection islandData, World arenaWorld, Player player) {
        Location bedLocation = getLocation(islandData.getConfigurationSection("bed_location"), arenaWorld);
        arenaWorld.getBlockAt(bedLocation);

        return BedwarsArena.PlayerContainer.Builder.newBuilder()
                .setPlayer(player)
                .setIslandColor(ChatColor.valueOf(islandData.getString("island_color")))
                .setSpawnLocation(getLocation(islandData.getConfigurationSection("spawn_location"), arenaWorld))
                .setBedLocation(bedLocation)
                .setGenerator(getLocation(islandData.getConfigurationSection("generator_location"), arenaWorld))
                .build();
    }

    private List<Location> getLocationList(ConfigurationSection section, World world) {
        List<Location> locationList = new ArrayList<>();

        for(String key : section.getKeys(false)) {
            if(!section.isConfigurationSection(key)) {
                continue;
            }

            ConfigurationSection locationSection = section.getConfigurationSection(key);
            if(!verifyLocation(locationSection)) {
                return null;
            }

            locationList.add(getLocation(locationSection, world));
        }

        return locationList;
    }

    private Location getLocation(ConfigurationSection section, World world) {
        if(!verifyLocation(section)) {
            return null;
        }

        return new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"));
    }

    private boolean verifyArenaData(ConfigurationSection section) {
        if(!section.isString("name")
                || !section.isString("schematic_name")
                || !section.isConfigurationSection("emerald_generators")
                || !section.isConfigurationSection("diamond_generators")
                || !section.isConfigurationSection("island_one")
                || !section.isConfigurationSection("island_two")) {
            return false;
        }

        if(!verifyIslandData(section.getConfigurationSection("island_one")) && !verifyIslandData(section.getConfigurationSection("island_two"))) {
            return false;
        }

        return true;
    }

    private boolean verifyIslandData(ConfigurationSection section) {
        if(!section.isString("island_color")
                || !section.isConfigurationSection("spawn_location")
                || !section.isConfigurationSection("bed_location")
                || !section.isConfigurationSection("generator_location")) {
            return false;
        }

        if(!verifyColor(section.getString("island_color"))
                || !verifyLocation(section.getConfigurationSection("spawn_location"))
                || !verifyLocation(section.getConfigurationSection("bed_location"))
                || !verifyLocation(section.getConfigurationSection("generator_location"))){
            return false;
        }

        return true;
    }

    private boolean verifyColor(String chatColor) {
        for(ChatColor color : ChatColor.values()) {
            if(color.name().equalsIgnoreCase(chatColor)) {
                return true;
            }
        }

        return false;
    }

    private boolean verifyLocation(ConfigurationSection section) {
        if(!section.isDouble("x")
                || !section.isDouble("y")
                || !section.isDouble("z")) {
            return false;
        }

        return true;
    }
}
