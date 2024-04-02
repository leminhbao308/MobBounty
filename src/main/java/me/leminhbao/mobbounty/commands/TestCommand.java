package me.leminhbao.mobbounty.commands;

import me.leminhbao.mobbounty.MobBounty;
import me.leminhbao.mobbounty.inventoryframework.MobViewMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand {

    private final MobBounty plugin;

    public TestCommand(MobBounty plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            new MobViewMenu(plugin).open(player);
        }

        return true;
    }
}
