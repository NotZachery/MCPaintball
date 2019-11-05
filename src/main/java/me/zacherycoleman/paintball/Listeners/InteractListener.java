package me.zacherycoleman.paintball.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import me.zacherycoleman.paintball.Main;
import me.zacherycoleman.paintball.Util.User;

import java.util.Objects;

public class InteractListener implements Listener
{
    // Main class reference
    Main This = Main.getPlugin(Main.class);

    // Listening for PlayerInteractEvents
    @EventHandler
    public void OnInteract(PlayerInteractEvent Event)
    {
        // var for the player
        Player PL = (Player) Event.getPlayer();
        User u = Main.USERS.get(PL.getUniqueId());
        
        // Make sure the item in hand in an Iron Horse Armor
        if (PL.getItemInHand().getType() == Material.IRON_HORSE_ARMOR)
        {
            if (!(u.HasCooldown()))
            {
                // Location...
                Location Loc = Event.getPlayer().getLocation();

                // Setting the Yaw/Pitch from the player
                Loc.setYaw(Loc.getYaw());
                Loc.setPitch(Loc.getPitch());

                // Getting world
                World Wrld = Loc.getWorld();

                // Setting the velocity for the "paintball" to shoot at
                Vector Vel = Loc.getDirection().multiply(2.1f);

                // Spawn the snowball
                Entity snowball = Wrld.spawnEntity(Loc.add(0, 1.5, 0), Objects.requireNonNull(EntityType.SNOWBALL));
                snowball.setVelocity(Vel);
                snowball.setMetadata("zacherycoleman_snowball", new FixedMetadataValue(This, true));
                Wrld.playSound(Loc, Sound.BLOCK_LAVA_POP, 3.0F, 0.5F);

                // Set the cooldown, so we don't have sticky blocks (Block that stay when they are supposed to be removed)
                u.SetPCooldown(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(This, () ->
                {
                    // Reset the cooldown
                    u.SetPCooldown(false);
                }, 1L); 
            }
        }
    }
}
