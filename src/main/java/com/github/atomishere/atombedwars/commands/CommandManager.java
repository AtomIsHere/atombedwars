package com.github.atomishere.atombedwars.commands;

import com.github.atomishere.atombedwars.AtomBedwars;
import com.google.common.reflect.ClassPath;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
public class CommandManager {
    public static final String COMMAND_PREFIX = "Command_";

    private final AtomBedwars plugin;

    public void loadCommands() {
        loadCommandClass(Command_testgeneration.class);
    }

    /*
    public void loadCommands() {
        List<Class<?>> classes;
        try {
            classes = getClasses("com.github.atomishere.atombedwars.commands");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for(Class<?> clazz : classes) {
            loadCommandClass(clazz);
        }
    }
    */

    private void loadCommandClass(Class<?> clazz) {
        String commandName = clazz.getSimpleName().replaceFirst(COMMAND_PREFIX, "");

        Constructor con;
        try {
            con = clazz.getConstructor(String.class, AtomBedwars.class);
        } catch(NoSuchMethodException ex) {
            plugin.getLogger().warning("Not loading command " + commandName + ". Invalid Constructor!");
            return;
        }

        Object inst;
        try {
            inst = con.newInstance(commandName, plugin);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return;
        }

        if(!(inst instanceof Command)) {
            plugin.getLogger().severe("Could not load command " + commandName + ". Does not extend Command");
            return;
        }

        Command command = (Command) inst;

        CommandParameters params = command.getClass().getAnnotation(CommandParameters.class);
        if(params == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Command class does not have Parameters");
            return;
        }
        CommandPermissions perms = command.getClass().getAnnotation(CommandPermissions.class);
        if(perms == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Command class does not have Permissions");
            return;
        }

        command.setup(params, perms);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command.getName(), command);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /*
    private static List<Class<?>> getClasses(String packageName) throws IOException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        List<Class<?>> classes = new ArrayList<>();

        Set<ClassPath.ClassInfo> classInfoSet = ClassPath.from(loader).getTopLevelClasses();
        for(final ClassPath.ClassInfo info : classInfoSet) {
            if(info.getName().startsWith(packageName)) {
                final Class<?> clazz = info.load();
                classes.add(clazz);
            }
        }

        return classes;
    }
    */
}
