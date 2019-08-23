package com.github.atomishere.atombedwars.utils;

import com.boydti.fawe.object.schematic.Schematic;
import com.github.atomishere.atombedwars.AtomBedwars;
import com.github.atomishere.atombedwars.arena.VoidWorldGenerator;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.IntStream;

public final class AtomUtils {
    private AtomUtils() {
        throw new AssertionError();
    }

    private static AtomBedwars plugin = null;

    public static void setPlugin(AtomBedwars plugin) {
        if(AtomUtils.plugin == null) {
            AtomUtils.plugin = plugin;
        }
    }

    public static String hideText(String text) {
        Objects.requireNonNull(text, "text can not be null!");

        StringBuilder output = new StringBuilder();

        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        String hex = Hex.encodeHexString(bytes);

        for (char c : hex.toCharArray()) {
            output.append(ChatColor.COLOR_CHAR).append(c);
        }

        return output.toString();
    }

    public static String revealText(String text) {
        Objects.requireNonNull(text, "text can not be null!");

        if (text.isEmpty()) {
            return text;
        }

        char[] chars = text.toCharArray();

        char[] hexChars = new char[chars.length / 2];

        IntStream.range(0, chars.length)
                .filter(value -> value % 2 != 0)
                .forEach(value -> hexChars[value / 2] = chars[value]);

        try {
            return new String(Hex.decodeHex(hexChars), StandardCharsets.UTF_8);
        } catch (DecoderException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Couldn't decode text", e);
        }
    }

    public static World generateWorld(String schematicName, String arenaName) {
        File schematicFile = new File(new File(plugin.getDataFolder(), "schematics"), schematicName);
        if (!schematicFile.exists()) {
            plugin.getLogger().severe("Could not find schematic file for arena: " + arenaName + "!");
            return null;
        }

        Schematic arenaSchematic;
        try {
            arenaSchematic = ClipboardFormats.findByFile(schematicFile).load(schematicFile);
        } catch(IOException ex) {
            plugin.getLogger().severe("Invalid schematic in arena: " + arenaName + ".");
            return null;
        }

        WorldCreator creator = new WorldCreator(arenaName)
                .generateStructures(false)
                .type(WorldType.FLAT)
                .generator(new VoidWorldGenerator());

        World arenaWorld = creator.createWorld();
        arenaSchematic.paste(BukkitAdapter.adapt(arenaWorld), new Vector(0, 50, 0));

        return arenaWorld;
    }
}
