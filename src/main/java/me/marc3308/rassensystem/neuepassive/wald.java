package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class wald implements Listener {

    @EventHandler
    public void onscan(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(utilitys.kannpassive(p,"waldscann") && e.getAction().equals(Action.LEFT_CLICK_AIR) && p.isSneaking()){
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-utilitys.passiveliste.get("waldscann").getWerte().get("Kosten"));
            p.getNearbyEntities(utilitys.passiveliste.get("waldscann").getWerte().get("größe"), utilitys.passiveliste.get("waldscann").getWerte().get("größe"), utilitys.passiveliste.get("waldscann").getWerte().get("größe")).stream()
                    .filter(et -> et instanceof Animals || et instanceof Monster).forEach(et -> ((Creature) et).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20*5,3, false, false)));
        }
    }
}
