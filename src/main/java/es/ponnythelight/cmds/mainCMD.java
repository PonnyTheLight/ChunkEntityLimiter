package es.ponnythelight.cmds;

import es.ponnythelight.chunkentitylimiter.ChunkEntityLimiter;
import es.ponnythelight.gui.configGUI;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class mainCMD implements CommandExecutor {

    public ChunkEntityLimiter plugin;

    public mainCMD(ChunkEntityLimiter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Configuration config = plugin.getConfig();


            if (args.length == 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aUse &f/cel help &ato see the plugin commands."));

            } else if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m------&a[ &fChunkEntityLimiter &a]&a&m------"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cel help &f- See this message."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cel check &f- Check chunk entities."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/cel clear &f- Clear chunk entities."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', " "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m------&a[ &fChunkEntityLimiter &a]&a&m------"));
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("chunkentitylimit.reload")) {
                    plugin.reloadConfig();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.reload")));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.perms")));
                }
            } else if (args[0].equalsIgnoreCase("check")) {
                if (player.hasPermission("chunkentitylimit.check")) {
                    Chunk chunk = player.getLocation().getChunk();

                    List<Entity> en = Arrays.asList(chunk.getEntities());

                    AtomicInteger count = new AtomicInteger();

                    en.stream().forEach(k -> {
                        if (k.getType().equals(EntityType.DROPPED_ITEM)) return;
                        if (k.getType().equals(EntityType.PLAYER)) return;
                        if (k.getType().equals(EntityType.ITEM_FRAME)) return;
                        if (k.getType().equals(EntityType.UNKNOWN)) return;
                        if (k.getType().equals(EntityType.EXPERIENCE_ORB)) return;

                        count.getAndIncrement();
                    });

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.check-chunk")
                            .replaceAll("%entities%", Integer.toString(count.get()))));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.perms")));
                }
            }
                else if (args[0].equalsIgnoreCase("checkdropped")) {
                    if (player.hasPermission("chunkentitylimit.checkdropped")) {
                        Chunk chunk = player.getLocation().getChunk();

                        List<Entity> en = Arrays.asList(chunk.getEntities());

                        AtomicInteger counti = new AtomicInteger();

                        en.stream().forEach(k -> {
                            if (k.getType().equals(EntityType.DROPPED_ITEM)) {
                                counti.getAndIncrement();
                            }
                        });

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "%entities% dropped items in this chunk.%")
                                .replaceAll("%entities%", Integer.toString(counti.get())));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.perms")));
                    }
            } else if (args[0].equalsIgnoreCase("clear")) {
                    if (player.hasPermission("chunkentitylimit.clear")) {
                        Chunk chunk = player.getLocation().getChunk();

                        List<Entity> en = Arrays.asList(chunk.getEntities());

                        AtomicInteger count = new AtomicInteger();

                        en.stream().forEach(k -> {
                            if (k.getType().equals(EntityType.PLAYER)) return;
                            if (k.getType().equals(EntityType.ITEM_FRAME)) return;
                            if (k.getType().equals(EntityType.UNKNOWN)) return;
                            if (k.getType().equals(EntityType.EXPERIENCE_ORB)) return;

                            count.getAndIncrement();
                            k.remove();
                        });
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.clear-chunk")
                                .replaceAll("%entities%", Integer.toString(count.get()))));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.perms")));
                    }
            } else if (args[0].equalsIgnoreCase("config")) {
                    if (player.hasPermission("chunkentitylimit.config")) {
                        configGUI gui = new configGUI(plugin);

                        gui.openInventory(player);
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.perms")));
                    }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + config.getString("Messages.ncmd")));
            }
        }
        return false;
    }
}
