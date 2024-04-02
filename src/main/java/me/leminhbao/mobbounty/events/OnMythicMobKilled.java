package me.leminhbao.mobbounty.events;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import me.leminhbao.mobbounty.MobBounty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnMythicMobKilled implements Listener {

    private final MobBounty plugin;

    public OnMythicMobKilled(MobBounty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMythicMobKilled(MythicMobDeathEvent event) {
        // Check if player killed the mob
        if (event.getKiller() != null) {
            // Check mob is in the list
            if (plugin.getMobList().contains(event.getMobType().getInternalName())) {
                // Increase player kill
                plugin.getPlayerKillTrack().increasePlayerKill(event.getKiller().getUniqueId(), event.getMobType().getInternalName());
            }
        }
    }
}
