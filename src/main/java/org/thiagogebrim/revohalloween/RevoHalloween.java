package org.thiagogebrim.revohalloween;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Ravager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class RevoHalloween extends JavaPlugin implements Listener {

    private final List<DamageInfo> damageInfos = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Ravager && event.getDamager() instanceof Player player) {
            int damage = (int) Math.round(event.getDamage());

            DamageInfo damageInfo = findDamageInfo(player);
            if (damageInfo == null) {
                damageInfos.add(new DamageInfo(player, damage));
            } else {
                damageInfo.addDamage(damage);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Ravager) {
            executeTopDamageCommands();
            damageInfos.clear();
        }
    }

    private DamageInfo findDamageInfo(Player player) {
        for (DamageInfo info : damageInfos) {
            if (info.getPlayer().equals(player)) {
                return info;
            }
        }
        return null;
    }

    private void executeTopDamageCommands() {
        FileConfiguration config = getConfig();
        damageInfos.sort((info1, info2) -> Integer.compare(info2.getDamage(), info1.getDamage()));

        for (int i = 0; i < 3 && i < damageInfos.size(); i++) {
            DamageInfo info = damageInfos.get(i);
            List<String> commands = config.getStringList("TopDamage." + (i+1));
            for (String command : commands) {
                command = command.replace("{player}", info.getPlayer().getName());
                command = command.replace("{damage}", String.valueOf(info.getDamage()));
                getServer().dispatchCommand(getServer().getConsoleSender(), command);
            }
        }
    }
}
