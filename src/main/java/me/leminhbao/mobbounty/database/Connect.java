package me.leminhbao.mobbounty.database;

import me.leminhbao.mobbounty.MobBounty;
import me.leminhbao.mobbounty.utils.ColorCode;
import me.leminhbao.mobbounty.utils.ExceptionHandling;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Connect {

    private static final String mythicmob_kills =
            """
                        CREATE TABLE IF NOT EXISTS mythicmob_kills (
                            uuid TEXT NOT NULL,
                            mob_name TEXT NOT NULL,
                            kills INTEGER DEFAULT 0,
                            PRIMARY KEY (uuid, mob_name)
                        );
                    """;

    private static final String kills_leaderboard =
            """
                        CREATE TABLE IF NOT EXISTS leaderboard (
                            mob_name TEXT NOT NULL,
                            data TEXT NOT NULL,
                            PRIMARY KEY (mob_name)
                        );
                    """;

    public static Connection setupConnection(MobBounty plugin) {
        Logger logger = plugin.getLogger();
        long startTime = System.currentTimeMillis();
        Connection conn = null;
        try {
            String dbFolderPath = plugin.getDataFolder().getAbsolutePath();
            boolean isExist = new File(dbFolderPath, "users.db").exists();

            // db parameters
            String url = "jdbc:sqlite:"+ dbFolderPath +"/users.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            if (!isExist && conn != null) {
                logger.info(ColorCode.colorize("Found newly created database! Creating default table...", ColorCode.BLUE));
                conn.createStatement().execute(mythicmob_kills);
                conn.createStatement().execute(kills_leaderboard);
            }

            long endTime = System.currentTimeMillis();
            String time = ColorCode.timeColor(endTime - startTime);

            logger.info(ColorCode.colorize("Connected to the database! (Took ", ColorCode.GREEN) + time + ColorCode.colorize(")", ColorCode.GREEN));
        } catch (SQLException e) {
            ExceptionHandling.log(e, plugin);
        }

        return conn;
    }

    public static void closeConnection(Connection conn, MobBounty plugin) {
        long startTime = System.currentTimeMillis();
        try {
            if (conn != null) {
                conn.close();
            }

            long endTime = System.currentTimeMillis();
            String time = ColorCode.timeColor(endTime - startTime);

            plugin.getLogger().info(ColorCode.colorize("Disconnected from the database! (Took ", ColorCode.PURPLE) + time + ColorCode.colorize(")", ColorCode.PURPLE));
        } catch (SQLException ex) {
            ExceptionHandling.log(ex, plugin);
        }
    }
}
