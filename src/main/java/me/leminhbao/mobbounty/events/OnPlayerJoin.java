package me.leminhbao.mobbounty.events;

import me.leminhbao.mobbounty.MobBounty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoin implements Listener {

    private final MobBounty plugin;

    public OnPlayerJoin(MobBounty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        // Start monitoring player's playtime
        plugin.getPlayerKillTrack().registerPlayer(uuid);
        plugin.getLeaderboardHandler().updateLeaderboard(plugin.getPlayerKillTrack());
    }
}
