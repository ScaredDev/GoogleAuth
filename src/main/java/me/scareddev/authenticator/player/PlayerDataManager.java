package me.scareddev.authenticator.player;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {

    private final Map<UUID, PlayerData> playerDataMap = Maps.newConcurrentMap();

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), PlayerData::new);
    }
}
