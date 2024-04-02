package me.leminhbao.mobbounty;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

@Getter
public class LeaderboardHandler {

    /**
     * MOB_NAME -> (UUID -> KILL_COUNT)
     */
    private final HashMap<String, LinkedList<HashMap<UUID, Integer>>> leaderboardData = new HashMap<>();

    public LeaderboardHandler(MobBounty plugin) {
        // Load leaderboard data
        updateLeaderboard(plugin.getPlayerKillTrack());
    }

    public void onPluginReload(PlayerKillTrack playerKillTrack) {
        // Load new leaderboard data
        updateLeaderboard(playerKillTrack);
    }

    public void updateLeaderboard(PlayerKillTrack playerKillTrack) {
        leaderboardData.clear();

        HashMap<UUID, HashMap<String, Integer>> playerKillData = playerKillTrack.getPlayerKillData();

        for (UUID uuid : playerKillData.keySet()) {
            HashMap<String, Integer> playerData = playerKillData.get(uuid);
            for (String mob : playerData.keySet()) {
                if (!leaderboardData.containsKey(mob)) {
                    leaderboardData.put(mob, new LinkedList<>());
                }
                LinkedList<HashMap<UUID, Integer>> leaderboard = leaderboardData.get(mob);
                HashMap<UUID, Integer> player = new HashMap<>();
                player.put(uuid, playerData.get(mob));
                leaderboard.add(player);
            }
        }

        for (String mob : leaderboardData.keySet()) {
            LinkedList<HashMap<UUID, Integer>> leaderboard = leaderboardData.get(mob);
            leaderboard.sort((o1, o2) -> {
                int o1Kills = o1.values().iterator().next();
                int o2Kills = o2.values().iterator().next();
                return Integer.compare(o2Kills, o1Kills);
            });
        }
    }

    public LinkedList<HashMap<UUID, Integer>> getLeaderboard(String mob) {
        return leaderboardData.get(mob);
    }

    public LinkedList<HashMap<UUID, Integer>> getTopTen(String mob) {
        LinkedList<HashMap<UUID, Integer>> leaderboard = getLeaderboard(mob);
        LinkedList<HashMap<UUID, Integer>> topTen = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            if (i < leaderboard.size()) {
                topTen.add(leaderboard.get(i));
            }
        }
        return topTen;
    }

    public int getPlayerRank(String mob, UUID uuid) {
        LinkedList<HashMap<UUID, Integer>> leaderboard = leaderboardData.get(mob);
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).containsKey(uuid)) {
                return i + 1;
            }
        }
        return -1;
    }
}
