package me.zacherycoleman.paintball.Util;

import org.bukkit.entity.Player;

public class User
{
    private final Player PL;
    private boolean PCooldown = false;

    public User(Player PL) {this.PL = PL;} 
    public Player getPlayer() {return this.PL;}

    public boolean HasCooldown()
    {
        return this.PCooldown;
    }

    public void SetPCooldown(boolean PCooldown)
    {
        this.PCooldown = PCooldown;
    }
}