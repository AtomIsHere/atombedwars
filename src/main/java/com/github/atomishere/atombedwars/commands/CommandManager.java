package com.github.atomishere.atombedwars.commands;

import com.github.atomishere.atombedwars.AtomBedwars;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CommandManager {
    public static final String COMMAND_PREFIX = "Command_";

    private final AtomBedwars plugin;

    public void loadCommands() {
        List<ClassLoader> classLoaderList = new LinkedList<>();
        classLoaderList.add(ClasspathHelper.contextClassLoader());
        classLoaderList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoaderList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.github.atomishere.atombedwars.commands"))));

        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        for(Class<? extends Command> clazz : classes) {
            if(clazz.getSimpleName().startsWith(COMMAND_PREFIX)) loadCommandClass(clazz);
        }
    }

    private void loadCommandClass(Class<? extends Command> clazz) {
        String commandName = clazz.getSimpleName().replaceFirst(COMMAND_PREFIX, "");

        Constructor<? extends Command> con;
        try {
            con = clazz.getConstructor(String.class, AtomBedwars.class);
        } catch(NoSuchMethodException ex) {
            plugin.getLogger().warning("Not loading command " + commandName + ". Invalid Constructor!");
            return;
        }

        Command inst;
        try {
            inst = con.newInstance(commandName, plugin);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return;
        }

        CommandParameters params = inst.getClass().getAnnotation(CommandParameters.class);
        if(params == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Command class does not have Parameters");
            return;
        }
        CommandPermissions perms = inst.getClass().getAnnotation(CommandPermissions.class);
        if(perms == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Command class does not have Permissions");
            return;
        }

        inst.setup(params, perms);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(inst.getName(), inst);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
