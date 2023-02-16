package es.ponnythelight.gui;

import es.ponnythelight.chunkentitylimiter.ChunkEntityLimiter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class configGUI implements Listener {

    public ChunkEntityLimiter plugin;
    private String chatvalue;

    private final Inventory inv;

    public configGUI(ChunkEntityLimiter plugin) {
        inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&8[&eChunkEntityLimiter&8]"));

        this.plugin = plugin;
        initializeItems(true, 0);
    }

    public void initializeItems(Boolean all, int slot) {
        Configuration config = plugin.getConfig();

        if (slot == 10 || all) {
            final ItemStack item1 = new ItemStack(Material.WOLF_SPAWN_EGG, 1);
            final ItemMeta meta1 = item1.getItemMeta();

            meta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eEntities Per Chunk"));

            ArrayList<String> lore1 = new ArrayList<>();
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&7Change max entities per chunk."));
            lore1.add(ChatColor.translateAlternateColorCodes('&', " "));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&eValue: &f%value%")
                    .replaceAll("%value%", String.valueOf(config.getInt("Config.entities-per-chunk"))));

            meta1.setLore(lore1);

            item1.setItemMeta(meta1);

            inv.setItem(10, item1);
        }

        // ANOTHER ITEM
        if (slot == 12 || all) {
            final ItemStack item2 = new ItemStack(Material.POTATO, 1);
            final ItemMeta meta2 = item2.getItemMeta();

            meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eDropped Items Per Chunk"));

            ArrayList<String> lore2 = new ArrayList<>();
            lore2.add(ChatColor.translateAlternateColorCodes('&', "&7Change max dropped items per chunk."));
            lore2.add(ChatColor.translateAlternateColorCodes('&', " "));
            lore2.add(ChatColor.translateAlternateColorCodes('&', "&eValue: &f%value%")
                    .replaceAll("%value%", String.valueOf(config.getInt("Config.dropped-items-per-chunk"))));

            meta2.setLore(lore2);

            item2.setItemMeta(meta2);

            inv.setItem(12, item2);
        }

        // ANOTHER ITEM
        if (slot == 14 || all) {

            final ItemStack item3 = new ItemStack(Material.GRASS_BLOCK, 1);
            final ItemMeta meta3 = item3.getItemMeta();

            meta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eCheck Chunks Loaded"));

            ArrayList<String> lore3 = new ArrayList<>();
            lore3.add(ChatColor.translateAlternateColorCodes('&', "&7Change loaded chunks check."));
            lore3.add(ChatColor.translateAlternateColorCodes('&', "&cThis can make lag if the sever have a lot"));
            lore3.add(ChatColor.translateAlternateColorCodes('&', "&cof players."));
            lore3.add(ChatColor.translateAlternateColorCodes('&', " "));
            lore3.add(ChatColor.translateAlternateColorCodes('&', "&eValue: &f%value%")
                    .replaceAll("%value%", String.valueOf(config.getBoolean("Config.check-chunks-loaded"))));

            meta3.setLore(lore3);

            item3.setItemMeta(meta3);

            inv.setItem(14, item3);
        }
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Configuration config = plugin.getConfig();

        if (!e.getInventory().getName().equals(inv.getName())) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        final Player p = (Player) e.getWhoClicked();


        if (e.getRawSlot() == 10) {
            p.closeInventory();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aPlease write in chat a &fnumber to change the per chunk entities limit&a. Write &fcancel &ato exit."));
            chatvalue = "PerChunkEntities";
        } else if (e.getRawSlot() == 12) {
            p.closeInventory();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aPlease write in chat a &fnumber &ato change the per chunk &fdropped items limit&a. Write &fcancel &ato exit."));
            chatvalue = "PerChunkDropped";
        } else if (e.getRawSlot() == 14) {
            Boolean  bol = config.getBoolean("Config.check-chunks-loaded");
            if (bol) {
                config.set("Config.check-chunks-loaded", false);
                plugin.saveConfig();
                plugin.reloadConfig();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aNow check loaded chunks value is: &f%newvalue%."
                           .replaceAll("%newvalue%", "false")));

                // ITEM UPDATE
                final ItemStack item3 = e.getInventory().getItem(14);
                final ItemMeta meta3 = item3.getItemMeta();

                meta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eCheck Chunks Loaded"));

                ArrayList<String> lore3 = new ArrayList<>();
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&7Change loaded chunks check."));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&cThis can make lag if the sever have a lot"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&cof players."));
                lore3.add(ChatColor.translateAlternateColorCodes('&', " "));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&eValue: &f%value%")
                        .replaceAll("%value%", String.valueOf(config.getBoolean("Config.check-chunks-loaded"))));

                meta3.setLore(lore3);

                item3.setItemMeta(meta3);

                e.getInventory().setItem(14, item3);
            } else {
                config.set("Config.check-chunks-loaded", true);
                plugin.saveConfig();
                plugin.reloadConfig();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aNow check loaded chunks value is: &f%newvalue%."
                        .replaceAll("%newvalue%", "true")));

                // ITEM UPDATE
                final ItemStack item3 = e.getInventory().getItem(14);
                final ItemMeta meta3 = item3.getItemMeta();

                meta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eCheck Chunks Loaded"));

                ArrayList<String> lore3 = new ArrayList<>();
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&7Change loaded chunks check."));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&cThis can make lag if the sever have a lot"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&cof players."));
                lore3.add(ChatColor.translateAlternateColorCodes('&', " "));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&eValue: &f%value%")
                        .replaceAll("%value%", String.valueOf(config.getBoolean("Config.check-chunks-loaded"))));

                meta3.setLore(lore3);

                item3.setItemMeta(meta3);

                e.getInventory().setItem(14, item3);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        System.out.println("Debug: 1");

        if (!e.getInventory().getName().equals(inv.getName())) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent e) {
        Configuration config = plugin.getConfig();
        Player p = e.getPlayer();

        if (chatvalue == "PerChunkEntities") {
            e.setCancelled(true);
            if (e.getMessage() == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aPlease write in chat a &fnumber &ato change the per &fchunk entities limit&a. Write &fcancel &ato exit."));
            } else if (e.getMessage().equalsIgnoreCase("cancel")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aProcess canceled successful."));
                chatvalue = null;
            } else {
                try {
                    int num = Integer.parseInt(e.getMessage());

                    config.set("Config.entities-per-chunk", num);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aNow the per chunk entities limit is: &f%newlimit%."
                            .replaceAll("%newlimit%", Integer.toString(num))));
                    chatvalue = null;
                } catch (NumberFormatException err) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aPlease write in chat a &fnumber &ato change the per &fchunk entities limit&a. Write &fcancel &ato exit."));
                }
            }
        } else if (chatvalue == "PerChunkDropped") {
            e.setCancelled(true);

            if (e.getMessage() == null) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aPlease write in chat a &fnumber &ato change the per chunk &fdropped items limit&a. Write &fcancel &ato exit."));
            } else if (e.getMessage().equalsIgnoreCase("cancel")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aProcess canceled successful."));
                chatvalue = null;
            } else {
                try {
                    int num = Integer.parseInt(e.getMessage());

                    config.set("Config.dropped-items-per-chunk", num);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aNow the per chunk dropped items limit is: &f%newlimit%."
                            .replaceAll("%newlimit%", Integer.toString(num))));
                    chatvalue = null;
                } catch (NumberFormatException err) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + "&aPlease write in chat a &fnumber &ato change the per chunk &fdropped items limit&a. Write &fcancel &ato exit."));
                }
            }
        }
    }
}
