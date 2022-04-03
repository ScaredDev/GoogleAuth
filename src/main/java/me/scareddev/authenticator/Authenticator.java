package me.scareddev.authenticator;

import me.scareddev.authenticator.listener.AuthListener;
import me.scareddev.authenticator.player.PlayerDataBus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Authenticator extends JavaPlugin {

    private static Authenticator authenticator;
    private final PlayerDataBus playerDataBus = new PlayerDataBus();

    @Override
    public void onEnable() {
        authenticator = this;
        getConfig().options().copyDefaults(true);
        saveConfig();

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AuthListener(), this);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static Authenticator getAuthenticator() {
        return authenticator;
    }

    public PlayerDataBus getPlayerDataBus() {
        return playerDataBus;
    }
}
