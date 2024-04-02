package me.leminhbao.mobbounty.utils;

import me.leminhbao.mobbounty.MobBounty;

public class ExceptionHandling {
    public static void log(Exception exception, MobBounty plugin) {
        // Handle the exception (e.g., log it)
        plugin.getLogger().severe(ColorCode.colorize("An error occurred! Send this to the developer:\n", ColorCode.RED));

        plugin.getLogger().severe(ColorCode.colorize(exception.getMessage(), ColorCode.WHITE, ColorCode.RED_BACKGROUND));
        for (StackTraceElement element : exception.getStackTrace()) {
            plugin.getLogger().severe(ColorCode.colorize(element.toString(), ColorCode.WHITE, ColorCode.RED_BACKGROUND));
        }
    }
}
