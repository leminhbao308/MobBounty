package me.leminhbao.mobbounty.events;

import me.leminhbao.mobbounty.MobBounty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

    private final MobBounty plugin;

    public OnPlayerLeave(MobBounty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        plugin.getPlayerKillTrack().savePlayerKills(event.getPlayer().getUniqueId(), true);
    }
}
