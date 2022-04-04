package me.scareddev.authenticator.listener;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import me.scareddev.authenticator.Authenticator;
import me.scareddev.authenticator.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public final class AuthListener implements Listener {

    @EventHandler
    private void onJoin(PlayerJoinEvent playerJoinEvent) {
        final Player player = playerJoinEvent.getPlayer();
        if (!player.isOp())
            return;

        final PlayerData playerData = Authenticator.getAuthenticator().getPlayerDataManager().getPlayerData(player);

        if (playerData.getGoogleKey().equals("NULL_CODE")) {
            final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
            final GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();

            player.sendMessage(text("&8[&bGoogleAuth&8] &7Your Key is&8: &3" + googleAuthenticatorKey.getKey()));

            Authenticator.getAuthenticator().getConfig().set("GoogleCodes." + player.getUniqueId(), googleAuthenticatorKey.getKey());
            Authenticator.getAuthenticator().saveConfig();
            playerData.setGoogleKey(googleAuthenticatorKey.getKey());

        } else {
            player.sendMessage(text("&8[&bGoogleAuth&8] &7Your Key is&8: &3" + playerData.getGoogleKey()));
        }

        playerData.setInSecurity(true);
    }

    @EventHandler
    private void onAsyncChat(AsyncPlayerChatEvent asyncPlayerChatEvent) {
        final Player player = asyncPlayerChatEvent.getPlayer();
        final String message = asyncPlayerChatEvent.getMessage();
        final PlayerData playerData = Authenticator.getAuthenticator().getPlayerDataManager().getPlayerData(player);
        if (!playerData.isInSecurity())
            return;

        try {
            final int code = Integer.parseInt(message);
            final boolean invalid = !validCode(playerData, code);

            if (invalid) {
                player.sendMessage(text("&8[&bGoogleAuth&8] &cIncorrect or expired Google authentication code"));
                return;
            }

            player.sendMessage(text("&8[&bGoogleAuth&8] &aYour verification with the Google Auth code has been confirmed"));
            playerData.setInSecurity(false);

        } catch (Exception exception) {
            player.sendMessage(text("&8[&bGoogleAuth&8] &cIncorrect or expired Google authentication code"));
            asyncPlayerChatEvent.setCancelled(true);
        }

        asyncPlayerChatEvent.setCancelled(true);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent playerMoveEvent) {
        final Player player = playerMoveEvent.getPlayer();
        final PlayerData playerData = Authenticator.getAuthenticator().getPlayerDataManager().getPlayerData(player);

        if (playerData.isInSecurity())
            playerMoveEvent.setCancelled(true);
    }

    private boolean validCode(PlayerData playerData, Integer code) {
        final String googleKey = playerData.getGoogleKey();
        final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(googleKey, code);
    }

    private String text(String textToTranslate) {
        return ChatColor.translateAlternateColorCodes('&', textToTranslate);
    }
}
