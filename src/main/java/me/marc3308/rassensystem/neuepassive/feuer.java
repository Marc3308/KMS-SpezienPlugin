package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.utilitys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class feuer implements Listener {

    @EventHandler
    public void onfire(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player p && utilitys.kannpassive(p,"feuerhand"))e.getEntity().setFireTicks(utilitys.passiveliste.get("feuerhand").getWerte().get("dauer")*20);
    }

    @EventHandler
    public void firedmg(EntityDamageEvent e){
        if(e.getEntity() instanceof Player p && utilitys.kannpassive(p,"feuerfest") && (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)))e.setCancelled(true);
    }
}
