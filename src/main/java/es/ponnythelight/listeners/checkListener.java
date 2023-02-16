package es.ponnythelight.listeners;

import es.ponnythelight.chunkentitylimiter.ChunkEntityLimiter;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Arrays;
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

        List<Entity> en = Arrays.asList(chunk.getEntities());

        AtomicInteger count = new AtomicInteger();
        AtomicInteger counti = new AtomicInteger();
        AtomicInteger countc = new AtomicInteger();

        en.stream().forEach(k -> {
            if (k.getType().equals(EntityType.DROPPED_ITEM)) {
                counti.getAndIncrement();

                if (counti.get() >= config.getInt("Config.dropped-items-per-chunk")) {
                    e.setCancelled(true);
                }

            } else
            if (k.getType().equals(EntityType.PLAYER)) {

            } else
            if (k.getType().equals(EntityType.ITEM_FRAME)) {

            } else
            if (k.getType().equals(EntityType.UNKNOWN)) {

            } else
            if (k.getType().equals(EntityType.EXPERIENCE_ORB)) {

            } else {
                count.getAndIncrement();

                if (count.get() >= config.getInt("Config.entities-per-chunk")) {
                    e.setCancelled(true);
                }
            }
        });
    }

    @EventHandler
    public void checkChunkLoaded(ChunkLoadEvent e) {
        Configuration config = plugin.getConfig();
        Chunk chunk = e.getChunk();

        List<Entity> en = Arrays.asList(chunk.getEntities());

        AtomicInteger count = new AtomicInteger();
        AtomicInteger counti = new AtomicInteger();
        AtomicInteger countc = new AtomicInteger();

        en.stream().forEach(k -> {
            if (k.getType().equals(EntityType.DROPPED_ITEM)) {
                counti.getAndIncrement();

                if (counti.get() >= config.getInt("Config.dropped-items-per-chunk")) {
                    k.remove();
                }

            } else
            if (k.getType().equals(EntityType.PLAYER)) {

            } else
            if (k.getType().equals(EntityType.ITEM_FRAME)) {
                countc.getAndIncrement();

                if (countc.get() >= config.getInt("Config.item-frames-per-chunk")) {
                    k.remove();
                }
            } else
            if (k.getType().equals(EntityType.UNKNOWN)) {

            } else
            if (k.getType().equals(EntityType.EXPERIENCE_ORB)) {

            } else {
                count.getAndIncrement();

                if (count.get() > config.getInt("Config.entities-per-chunk")) {
                    k.remove();
                }
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

    @EventHandler
    public void playerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Configuration config = plugin.getConfig();

        Chunk chunk = player.getLocation().getChunk();

        List<Entity> en = Arrays.asList(chunk.getEntities());

        AtomicInteger counti = new AtomicInteger();

//        en.stream().forEach(k -> {
//
//            if (k.getType().equals(EntityType.DROPPED_ITEM)) {
//                counti.getAndIncrement();
//
//                if (counti.get() >= config.getInt("Config.dropped-items-per-chunk")) {
//                    System.out.println("Spawn");
//                    e.setCancelled(true);
//                    return player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.max-dropped-items")));
//                }
//
//            }
//        });

        for (Entity i : en) {
            if (i.getType().equals(EntityType.DROPPED_ITEM)) {
                counti.getAndIncrement();

                if (counti.get() >= config.getInt("Config.dropped-items-per-chunk")) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.max-dropped-items")));
                    return;
                }

            }
        }
    }


}
