package me.leminhbao.mobbounty;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class MobBountyPlaceholder extends PlaceholderExpansion {

    private final MobBounty plugin;

    public MobBountyPlaceholder(MobBounty plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mobbounty";
    }

    @Override
    public @NotNull String getAuthor() {
        return "LeMinhBao";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        return null;
    }
}
