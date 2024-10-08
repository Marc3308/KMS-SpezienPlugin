package me.marc3308.rassensystem.eventlisteners;

import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.persistence.PersistentDataType;

public class infight implements Listener {

    @EventHandler
    public void onopen(InventoryOpenEvent e){
        Player p= (Player) e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onp2(InventoryClickEvent e){
        Player p= (Player) e.getWhoClicked();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        e.setCancelled(true);
    }
}
