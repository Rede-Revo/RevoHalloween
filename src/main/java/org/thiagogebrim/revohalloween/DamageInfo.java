package org.thiagogebrim.revohalloween;

import org.bukkit.entity.Player;

public class DamageInfo {
    private final Player player;
    private int damage;

    public DamageInfo(Player player, int damage) {
        this.player = player;
        this.damage = damage;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDamage() {
        return damage;
    }

    public void addDamage(int damage) {
        this.damage += damage;
    }
}
