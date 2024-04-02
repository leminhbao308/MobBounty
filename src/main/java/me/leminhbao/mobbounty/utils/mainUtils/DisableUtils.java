package me.leminhbao.mobbounty.utils.mainUtils;

import me.leminhbao.mobbounty.MobBounty;

public class DisableUtils {

    public static void unregisterTasks(MobBounty plugin) {
        // Unregister tasks
    }

    public static void closeDatabase(MobBounty plugin) {
        // Close database
        plugin.getPlayerKillTrack().saveAllPlayersKills();

        plugin.getPlayerKillTrack().disconnect();
    }

}
