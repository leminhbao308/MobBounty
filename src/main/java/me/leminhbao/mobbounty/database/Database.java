package me.leminhbao.mobbounty.database;

import lombok.Getter;
import me.leminhbao.mobbounty.MobBounty;
import me.leminhbao.mobbounty.utils.ExceptionHandling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Database {

    @Getter
    private final MobBounty plugin;
    private Connection databaseConnection;

    public Database(MobBounty plugin) {
        this.plugin = plugin;
    }

    public void createConnection() {
        this.databaseConnection = Connect.setupConnection(this.plugin);
    }

    public void closeConnection() {
        Connect.closeConnection(this.databaseConnection, this.plugin);
    }

    public HashMap<String, Integer> registerPlayer(UUID uuid, List<String> mobList) {
        // inset, skip if exists
        String query = "INSERT INTO mythicmob_kills (uuid, mob_name, kills) VALUES (?, ?, 0) ON CONFLICT(uuid, mob_name) DO NOTHING";

        try {
            PreparedStatement statement = this.databaseConnection.prepareStatement(query);
            for (String mob : mobList) {
                statement.setString(1, uuid.toString());
                statement.setString(2, mob);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            ExceptionHandling.log(e, this.plugin);
        }

        // get data
        return this.getPlayerKillData(uuid, mobList);
    }

    public HashMap<UUID, HashMap<String, Integer>> registerPlayers(List<UUID> uuidList, List<String> mobList) {
        HashMap<UUID, HashMap<String, Integer>> playerKillData = new HashMap<>();

        for (UUID uuid : uuidList) {
            playerKillData.put(uuid, this.registerPlayer(uuid, mobList));
        }

        return playerKillData;
    }

    public boolean unregisterPlayer(UUID uuid, HashMap<String, Integer> mobData) {
        // Save the player's kill data to the database
        String query = "UPDATE mythicmob_kills SET kills = ? WHERE uuid = ? AND mob_name = ?";
        try {
            PreparedStatement statement = this.databaseConnection.prepareStatement(query);
            for (Map.Entry<String, Integer> mob : mobData.entrySet()) {
                statement.setInt(1, mob.getValue());
                statement.setString(2, uuid.toString());
                statement.setString(3, mob.getKey());
                statement.addBatch();
            }

            statement.executeBatch();

            // check if all data is saved
            return statement.getUpdateCount() == mobData.size();
        } catch (SQLException e) {
            ExceptionHandling.log(e, this.plugin);
        }

        return false;
    }

    public HashMap<String, Integer> getPlayerKillData(UUID uuid, List<String> mobList) {

        HashMap<String, Integer> playerKillData = new HashMap<>();

        String query = "SELECT * FROM mythicmob_kills WHERE uuid = ? AND mob_name IN ("
                + String.join(",", Collections.nCopies(mobList.size(), "?")) + ")";

        try {
            PreparedStatement statement = this.databaseConnection.prepareStatement(query);

            statement.setString(1, uuid.toString());
            for (int i = 0; i < mobList.size(); i++) {
                statement.setString(i + 2, mobList.get(i));
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String mobName = resultSet.getString("mob_name");
                int kills = resultSet.getInt("kills");
                if (!playerKillData.containsKey(mobName)) {
                    playerKillData.put(mobName, kills);
                }
                playerKillData.put(mobName, kills);
            }

            for (String mob : mobList) {
                if (!playerKillData.containsKey(mob)) {
                    playerKillData.put(mob, 0);
                }
            }
        } catch (SQLException e) {
            ExceptionHandling.log(e, this.plugin);
        }

        return playerKillData;
    }

    public void updatePlayerKillData(UUID uuid, String mobName, int kills) {
        String query = "INSERT INTO mythicmob_kills (uuid, mob_name, kills) VALUES (?, ?, ?) ON CONFLICT(uuid, mob_name) DO UPDATE SET kills = ?";

        try {
            PreparedStatement statement = this.databaseConnection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            statement.setString(2, mobName);
            statement.setInt(3, kills);
            statement.setInt(4, kills);
            statement.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandling.log(e, this.plugin);
        }
    }

    public void batchUpdatePlayerKillData(HashMap<UUID, HashMap<String, Integer>> playerKillData) {
        String query = "INSERT INTO mythicmob_kills (uuid, mob_name, kills) VALUES (?, ?, ?) ON CONFLICT(uuid, mob_name) DO UPDATE SET kills = ?";

        try {
            PreparedStatement statement = this.databaseConnection.prepareStatement(query);
            for (Map.Entry<UUID, HashMap<String, Integer>> playerData : playerKillData.entrySet()) {
                UUID uuid = playerData.getKey();
                for (Map.Entry<String, Integer> mobData : playerData.getValue().entrySet()) {
                    String mobName = mobData.getKey();
                    int kills = mobData.getValue();
                    statement.setString(1, uuid.toString());
                    statement.setString(2, mobName);
                    statement.setInt(3, kills);
                    statement.setInt(4, kills);
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            ExceptionHandling.log(e, this.plugin);
        }
    }

    public void batchUpdatePlayerKillData(UUID uuid, HashMap<String, Integer> mobData) {
        String query = "INSERT INTO mythicmob_kills (uuid, mob_name, kills) VALUES (?, ?, ?) ON CONFLICT(uuid, mob_name) DO UPDATE SET kills = ?";

        try {
            PreparedStatement statement = this.databaseConnection.prepareStatement(query);
            for (Map.Entry<String, Integer> mob : mobData.entrySet()) {
                String mobName = mob.getKey();
                int kills = mob.getValue();
                statement.setString(1, uuid.toString());
                statement.setString(2, mobName);
                statement.setInt(3, kills);
                statement.setInt(4, kills);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            ExceptionHandling.log(e, this.plugin);
        }
    }

    public void saveLeaderboard(HashMap<UUID, HashMap<String, Integer>> playerKillData) {
        // Save the leaderboard data to the database with string format, and sort the data by kills in descending order
        // uuid(kills),uuid(kills),uuid(kills),...
        String query = "INSERT INTO leaderboard (mob_name, data) VALUES (?, ?) ON CONFLICT(mob_name) DO UPDATE SET data = ?";

        // Sort the data by kills in descending order
        String sortedData = "";
        for (Map.Entry<String, Integer> mobData : playerKillData.entrySet().iterator().next().getValue().entrySet()) {
            String mobName = mobData.getKey();
//            List<Map.Entry<UUID, Integer>> sortedList = new ArrayList<>(playerKillData.keySet());
//            sortedList.sort((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()));
//            for (Map.Entry<UUID, Integer> playerData : sortedList) {
//                sortedData += playerData.getKey().toString() + "(" + playerData.getValue() + "),";
//            }
//            sortedData = sortedData.substring(0, sortedData.length() - 1);
//            this.saveLeaderboard(mobName, sortedData);
        }
    }
}
