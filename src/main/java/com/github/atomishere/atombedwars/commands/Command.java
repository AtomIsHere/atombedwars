package com.github.atomishere.atombedwars.commands;

import com.github.atomishere.atombedwars.AtomBedwars;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Command extends BukkitCommand {
    public static final String NO_PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission";

    protected final AtomBedwars plugin;

    protected String permission;
    protected CommandSource source;

    protected Command(String name, AtomBedwars plugin) {
        super(name);
        this.plugin = plugin;
    }

    public void setup(CommandParameters params, CommandPermissions perms) {
        this.description = params.description();
        this.usageMessage = "/" + params.usage().replaceFirst("<command>", getName());

        String[] primAl = params.aliases().split(",");

        List<String> aliases = Arrays.stream(primAl).collect(Collectors.toList());
        setAliases(aliases);

        this.permission = "atombedwars." + perms.permission();
        this.source = perms.source();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(NO_PERMISSION_MESSAGE);
            return true;
        }

        Player player = null;
        if(sender instanceof Player) {
            player = (Player) sender;
        }

        onCommand(sender, player, alias, args);

        return true;
    }

    public abstract void onCommand(CommandSender sender, Player playerSender, String alias, String[] args);

    private boolean hasPermission(CommandSender sender) {
        switch(source) {
            case PLAYER:
                return (sender instanceof Player) && sender.hasPermission(permission);
            case CONSOLE:
                return !(sender instanceof Player) && sender.hasPermission(permission);
            case BOTH:
                return sender.hasPermission(permission);
        }

        return false;
    }
}
