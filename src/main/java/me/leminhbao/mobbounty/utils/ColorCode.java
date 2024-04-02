package me.leminhbao.mobbounty.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class ColorCode {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static String colorize(String message, String color) {
        return color + message + RESET;
    }

    public static String colorize(String message, String color, String background) {
        return color + background + message + RESET;
    }

    public static String timeColor(long time) {
        String timeString = "";
        if (time > 1000) {
            timeString = ColorCode.colorize((time) + "ms", ColorCode.RED);
        } else if (time > 500) {
            timeString = ColorCode.colorize((time) + "ms", ColorCode.YELLOW);
        } else {
            timeString = ColorCode.colorize((time) + "ms", ColorCode.CYAN);
        }
        return timeString;
    }

    public static String getStringConfig(String path, String def, FileConfiguration config, @Nullable Player player) {
        String value = config.getString(path, def);
        String colorizedValue = ChatColor.translateAlternateColorCodes('&', value);

        return PlaceholderAPI.setPlaceholders(player, colorizedValue);
    }
}
