package es.ponnythelight.listeners;

import es.ponnythelight.chunkentitylimiter.ChunkEntityLimiter;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Chunk;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class checkListener implements Listener {

    public ChunkEntityLimiter plugin;

    public checkListener(ChunkEntityLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void entitySpawnEvent(CreatureSpawnEvent e) {
        Configuration config = plugin.getConfig();

        Entity entity = e.getEntity();

        Chunk chunk = e.getLocation().getChunk();
        Entity[] entities = chunk.getEntities();

        if (entities.length > config.getInt("Config.entities-per-chunk")) {
            e.setCancelled(true);
            for (Entity d : entities) {
                if (d instanceof Player) return;
                if (entities.length > config.getInt("Config.entities-per-chunk")) {
                    d.remove();
                }
            }
        }
    }

    @EventHandler
    public void checkChunkLoaded(ChunkLoadEvent e) {
        Configuration config = plugin.getConfig();
        Chunk chunk = e.getChunk();

        Entity[] entities = chunk.getEntities();
        System.out.println("Load Lenght: " + entities.length);

        if (entities.length > config.getInt("Config.entities-per-chunk")) {
            for (Entity d : entities) {
                if (entities.length > config.getInt("Config.entities-per-chunk")) {
                    d.remove();
                }
            }
        }
    }

}
