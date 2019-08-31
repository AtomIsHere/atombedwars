package com.github.atomishere.atombedwars.commands;

import com.github.atomishere.atombedwars.AtomBedwars;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(permission = "testgeneration", source = CommandSource.PLAYER)
@CommandParameters(description = "Test schematic generation.", usage = "<command> <schematic name>")
public class Command_testgeneration extends Command {
    public Command_testgeneration(String name, AtomBedwars plugin) {
        super(name, plugin);
    }

    @Override
    public void onCommand(CommandSender sender, Player playerSender, String alias, String[] args) {
        if(args.length != 1) {
            sender.sendMessage(ChatColor.RED + getUsage());
            return;
        }

        String schematicName = args[0];

        World world = null;
        if(plugin.getTestWorldManager().hasWorld(schematicName)) {
            world = plugin.getTestWorldManager().getWorld(schematicName);
        } else {
            world = plugin.getTestWorldManager().loadWorld(schematicName, schematicName);
        }

        if(world == null) {
            playerSender.sendMessage(ChatColor.RED + "Unable to load test world.");
            return;
        }

        playerSender.teleport(world.getSpawnLocation());
    }
}
