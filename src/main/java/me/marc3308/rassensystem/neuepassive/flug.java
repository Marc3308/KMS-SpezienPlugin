package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.persistence.PersistentDataType;

public class flug implements Listener {

    @EventHandler
    public void flugbienen(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(p.getAllowFlight() && p.isOnGround() && p.getFallDistance()>3)p.damage(p.getFallDistance()-3);
    }

    @EventHandler
    public void trytopassivfly(PlayerToggleFlightEvent e){
        Player p=e.getPlayer();
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        e.setCancelled(true);
        if(p.isGliding())return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        if(!utilitys.spezienliste.containsKey(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non")))return;
        if(!utilitys.spezienliste.get(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non")).getPassiven().contains("flug"))return;
        if(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)>=utilitys.passiveliste.get("flug").getWerte().get("Kosten"))p.setGliding(true);
    }

    @EventHandler
    public void onglide(EntityToggleGlideEvent e){
        if(e.getEntity() instanceof Player p &&  p.getGameMode().equals(GameMode.SURVIVAL) && !p.isOnGround())e.setCancelled(true);
    }
}
