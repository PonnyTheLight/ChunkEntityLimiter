package es.ponnythelight.listeners;

import es.ponnythelight.chunkentitylimiter.ChunkEntityLimiter;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

        List<Entity> en = List.of(chunk.getEntities());

        AtomicInteger count = new AtomicInteger();

        en.stream().forEach(k -> {
            if (k.getType().equals(EntityType.DROPPED_ITEM)) return;
            if (k.getType().equals(EntityType.PLAYER)) return;
            if (k.getType().equals(EntityType.ITEM_FRAME)) return;
            if (k.getType().equals(EntityType.UNKNOWN)) return;
            if (k.getType().equals(EntityType.EXPERIENCE_ORB)) return;

            count.getAndIncrement();
        });

        if (count.get() >= config.getInt("Config.entities-per-chunk")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void checkChunkLoaded(ChunkLoadEvent e) {
        Configuration config = plugin.getConfig();
        Chunk chunk = e.getChunk();

        List<Entity> en = List.of(chunk.getEntities());

        AtomicInteger count = new AtomicInteger();

        en.stream().forEach(k -> {
            if (k.getType().equals(EntityType.DROPPED_ITEM)) return;
            if (k.getType().equals(EntityType.PLAYER)) return;
            if (k.getType().equals(EntityType.ITEM_FRAME)) return;
            if (k.getType().equals(EntityType.UNKNOWN)) return;
            if (k.getType().equals(EntityType.EXPERIENCE_ORB)) return;

            count.getAndIncrement();

            if (count.get() > config.getInt("Config.entities-per-chunk")) {
                k.remove();
            }
        });

        if (count.get() > config.getInt("Config.entities-per-chunk")) {
            String cords = "World: " + chunk.getWorld().getName() + " X: " + chunk.getX() + " Z: " + chunk.getZ();

            plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("Messages.prefix") + config.getString("Messages.automatic-clear-console-log")
                            .replaceAll("%entities%", Integer.toString(count.get()))
                            .replaceAll("%coordinates%", cords)
            ));
        }
    }


}
