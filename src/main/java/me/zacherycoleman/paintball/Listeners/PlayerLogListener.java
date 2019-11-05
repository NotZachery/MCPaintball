package me.zacherycoleman.paintball.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import me.zacherycoleman.paintball.Util.User;
import me.zacherycoleman.paintball.Main;

public class PlayerLogListener implements Listener
{
    public static Plugin plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent Event)
    {
        Player p = Event.getPlayer();
        Main.USERS.put(p.getUniqueId(), new User(p));
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent Event)
    {
        User u = Main.USERS.get(Event.getPlayer().getUniqueId());
        u.SetPCooldown(false);
        Main.USERS.remove(Event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent Event)
    {
        User u = Main.USERS.get(Event.getPlayer().getUniqueId());
        u.SetPCooldown(false);
        Main.USERS.remove(Event.getPlayer().getUniqueId());
    }
}
