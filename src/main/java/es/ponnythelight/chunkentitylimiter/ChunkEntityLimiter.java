package es.ponnythelight.chunkentitylimiter;

import es.ponnythelight.cmds.mainCMD;
import es.ponnythelight.listeners.checkListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkEntityLimiter extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eChunkEntityLimiter&8] &aPlugin enabled successful."));

        saveDefaultConfig();
        loadEvents();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&eChunkEntityLimiter&8] &cPlugin disabled successful."));
    }


    public void loadEvents(){
        getServer().getPluginManager().registerEvents(new checkListener(this), this);
    }

    public void loadCommands() {
        getCommand("chunkentitylimiter").setExecutor(new mainCMD());
    }
}
