package me.scareddev.authenticator.player;

import me.scareddev.authenticator.Authenticator;

import java.util.UUID;

public final class PlayerDataFactory {

    private final UUID uuid;
    private String googleKey;
    private boolean inSecurity;

    public PlayerDataFactory(UUID uuid) {
        this.uuid = uuid;
        this.googleKey =
                Authenticator.getAuthenticator().getConfig().getString("GoogleCodes." + uuid) == null
                        ? "NULL_CODE"
                        : Authenticator.getAuthenticator().getConfig().getString("GoogleCodes." + uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public boolean isInSecurity() {
        return inSecurity;
    }

    public void setInSecurity(boolean inSecurity) {
        this.inSecurity = inSecurity;
    }
}
