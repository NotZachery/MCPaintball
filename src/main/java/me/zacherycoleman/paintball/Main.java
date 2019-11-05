package me.zacherycoleman.paintball;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import me.zacherycoleman.paintball.Listeners.InteractListener;
import me.zacherycoleman.paintball.Listeners.PlayerLogListener;
import me.zacherycoleman.paintball.Listeners.ProjectileHitListener;
import me.zacherycoleman.paintball.Util.User;

public final class Main extends JavaPlugin
{
    //List of UUIDs
    public static HashMap<UUID, User> USERS = new HashMap<UUID, User>(); 

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        // Registering the listeners
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLogListener(), this);

        // The plugin started ;D
        getLogger().info("MC Paintballs enabled successfully! :D");
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        getLogger().info("MC Paintballs disabled successfully! :D");
    }
}
