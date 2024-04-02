package me.leminhbao.mobbounty.commands;

import me.leminhbao.mobbounty.MobBounty;
import me.leminhbao.mobbounty.commands.subCommands.ReloadCommand;
import me.leminhbao.mobbounty.commands.subCommands.ResetCommand;
import me.leminhbao.mobbounty.commands.subCommands.StatCommand;
import me.leminhbao.mobbounty.commands.subCommands.TopCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandRouter implements TabExecutor {

    private final MobBounty plugin;

    public CommandRouter(MobBounty plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }

        return switch (args[0].toLowerCase()) {
            case "reload" -> new ReloadCommand(plugin).onCommand(sender, command, label, args);
            case "stat" -> new StatCommand(plugin).onCommand(sender, command, label, args);
            case "reset" -> new ResetCommand(plugin).onCommand(sender, command, label, args);
            case "top" -> new TopCommand(plugin).onCommand(sender, command, label, args);
            default -> false;
        };
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> List.of("reload", "stat", "reset", "top");
            case 2 -> switch (args[0].toLowerCase()) {
                case "stat" -> {
                    List<String> result = new ArrayList<>();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        result.add(player.getName());
                    }

                    yield result;
                }
                case "reset" -> {
                    List<String> result = new ArrayList<>();

                    result.add("all");
                    result.addAll(plugin.getMobList());

                    yield result;
                }
                case "top" -> {
                    yield new ArrayList<>(plugin.getMobList());
                }
                default -> null;
            };
            default -> null;
        };
    }
}
