package me.leminhbao.mobbounty;

import lombok.Getter;
import me.leminhbao.mobbounty.database.Database;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class PlayerKillTrack {

    private final Database database;


    public PlayerKillTrack(MobBounty plugin) {
        this.database = new Database(plugin);
        this.database.createConnection();
    }

    public void disconnect() {
        this.database.closeConnection();
    }

    /**
     * UUID -> MOB_NAME -> KILL_COUNT
     */
    private HashMap<UUID, HashMap<String, Integer>> playerKillData = new HashMap<>();

    private void setPlayerKillData(HashMap<UUID, HashMap<String, Integer>> playerKillData) {
        this.playerKillData = playerKillData;
    }

    public void onConfigReload(List<String> newMobList) {
        // Save the current player kill data
        database.batchUpdatePlayerKillData(playerKillData);

        // Get current uuid list
        List<UUID> uuidList = playerKillData.keySet().stream().toList();

        // Clear the current player kill data
        playerKillData.clear();

        // Load the new mob list
        setPlayerKillData(database.registerPlayers(uuidList, newMobList));

        database.getPlugin().getLogger().info("Player kill data has been reloaded");
    }


    public HashMap<String, Integer> getPlayerKill(UUID uuid) {
        return playerKillData.get(uuid);
    }

    public void savePlayerKills(UUID uuid, boolean isPlayerLeave) {
        database.batchUpdatePlayerKillData(uuid, playerKillData.get(uuid));

        if (isPlayerLeave) {
            playerKillData.remove(uuid);
        }
    }

    public void saveAllPlayersKills() {
        database.batchUpdatePlayerKillData(playerKillData);
    }

    public void registerPlayer(UUID uuid) {
        playerKillData.put(uuid, database.registerPlayer(uuid, database.getPlugin().getMobList()));
    }

    public void increasePlayerKill(UUID uuid, String mobName) {
        int currentKills = playerKillData.get(uuid).get(mobName);
        playerKillData.get(uuid).put(mobName, currentKills + 1);
    }

    public void resetPlayerKill(UUID uuid, String mobName) {
        playerKillData.get(uuid).put(mobName, 0);
    }

    public void resetAllPlayersKill(boolean saveToDatabase) {
        playerKillData.forEach((uuid, mobData) -> {
            mobData.forEach((mob, kills) -> {
                mobData.put(mob, 0);
            });
        });

        if (saveToDatabase) {
            saveAllPlayersKills();
        }
    }

    public void resetAllPlayerKill(String mobName, boolean saveToDatabase) {
        playerKillData.forEach((uuid, mobData) -> {
            mobData.put(mobName, 0);
        });

        if (saveToDatabase) {
            saveAllPlayersKills();
        }
    }
}
