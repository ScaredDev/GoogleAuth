package me.scareddev.authenticator.player;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class PlayerDataFactoryManager {

    private final Map<UUID, PlayerDataFactory> playerDataMap = Maps.newConcurrentMap();

    public PlayerDataFactory getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), PlayerDataFactory::new);
    }
}
