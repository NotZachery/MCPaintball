package me.zacherycoleman.paintball.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.util.Vector;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;

import me.zacherycoleman.paintball.Main;

public class ProjectileHitListener implements Listener
{
    // Main class reference
    Main This = Main.getPlugin(Main.class);
    private static final List<Block> PaintedBlocks = new ArrayList<Block>();

    // Listening for ProjectileHitEvents
    @EventHandler
    public void ProjectileHitEvent(ProjectileHitEvent Event)
    {
        // Making sure the entity is a snowball
        if (Event.getEntity().hasMetadata("zacherycoleman_snowball"))
        {
            Block blck = Event.getHitBlock();
            // We've hit an entity.
            if (blck == null)
                return;

            if (!(blck.getType() == Material.AIR))
            {
                // But make sure we're not already a painted block.
                if (blck.hasMetadata("zacherycoleman_orig"))
                    return;

                blck.setMetadata("zacherycoleman_orig", new FixedMetadataValue(This, blck.getType()));
                blck.setMetadata("zacherycoleman_expire", new FixedMetadataValue(This, (System.currentTimeMillis() / 1000L) + 8L));

                // Setting the block to red wool
                blck.setType(Material.RED_WOOL);

                // Add to a list to track
                PaintedBlocks.add(blck);
                // A timer to set it back to the original block after a few seconds
                Bukkit.getScheduler().scheduleSyncRepeatingTask(This, () ->
                {
                    // setting the block back....
                    long Now = System.currentTimeMillis() / 1000L;
                    List<Block> toremove = new ArrayList<Block>();
                    synchronized(this)
                    {
                        for (Block painted : PaintedBlocks)
                        {
                            // Make sure the time is actually expired.
                            if (painted.getMetadata("zacherycoleman_expire").get(0).asLong() <= Now)
                            {
                                painted.setType((Material)painted.getMetadata("zacherycoleman_orig").get(0).value());
                                painted.removeMetadata("zacherycoleman_expire", This);
                                painted.removeMetadata("zacherycoleman_orig", This);
                                toremove.add(painted);
                            }
                        }
                        // actually remove the blocks.
                        PaintedBlocks.removeAll(toremove);
                    }
                }, 40L, 40L); 
            }
        }
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent Event)
    {
        if (Event.getPlayer().getGameMode() != GameMode.SURVIVAL)
        {
            // Just exit the event.
            return;
        }

        Block b = Event.getBlock();
        //World w = Event.getBlock().getWorld();

        // We found a block that is ours, lets not drop other types of blocks.
        if (b.hasMetadata("zacherycoleman_orig"))
        {
            // Set the block back to it's original value.
            b.setType((Material)b.getMetadata("zacherycoleman_orig").get(0).value());
            b.removeMetadata("zacherycoleman_expire", This);
            b.removeMetadata("zacherycoleman_orig", This);
            // Sync the threads and remove the block from the PaintedBlocks list.
            synchronized(this)
            {
                PaintedBlocks.remove(b);
            }
            // Don't destroy the block in the world.
            Event.setCancelled(true);
        }
    }

    // Debug, weather is FUCKING ANNOYING!
    @EventHandler
    public void WeatherChangeEvent(WeatherChangeEvent Event)
    {
        if (Event.toWeatherState())
            Event.setCancelled(true);
    }
}