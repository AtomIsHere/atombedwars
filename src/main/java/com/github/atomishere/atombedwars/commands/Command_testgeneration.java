package com.github.atomishere.atombedwars.commands;

import com.github.atomishere.atombedwars.AtomBedwars;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(permission = "testgeneration", source = CommandSource.PLAYER)
@CommandParameters(description = "Test schematic generation.", usage = "<command> <schematic name>")
public class Command_testgeneration extends Command {
    protected Command_testgeneration(String name, AtomBedwars plugin) {
        super(name, plugin);
    }

    @Override
    public void onCommand(CommandSender sender, Player playerSender, String alias, String[] args) {

    }
}
