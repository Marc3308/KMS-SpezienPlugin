package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class flug implements Listener {

    @EventHandler
    public void flugbienen(PlayerMoveEvent e){

        Player p = e.getPlayer();
        if(p.getAllowFlight() && p.isOnGround() && p.getFallDistance()>3 && !utilitys.kannpassive(p,"keinfallschaden"))p.damage(p.getFallDistance()-3);
        if(!p.isSneaking() && utilitys.kannpassive(p,"bounce") && p.isOnGround() && p.getFallDistance()>3){
            p.setVelocity(new Vector(p.getVelocity().getX(), (p.getFallDistance()*0.6*(utilitys.passiveliste.get("bounce").getWerte().get("Stärke")/100.0))/5.0, p.getVelocity().getZ()));
        }
    }

    @EventHandler
    public void trytopassivfly(PlayerToggleFlightEvent e){
        Player p=e.getPlayer();
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        e.setCancelled(true);
        if(p.isGliding())return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        if(utilitys.kannpassive(p,"flug"))p.setGliding(true);
        if(utilitys.kannpassive(p,"doppelsprung")){
            p.setVelocity(p.getVelocity().add(p.getLocation().getDirection().setY(0.5).multiply(utilitys.passiveliste.get("doppelsprung").getWerte().get("Stärke")).normalize()));
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-utilitys.passiveliste.get("doppelsprung").getWerte().get("Kosten"));
        }
    }

    @EventHandler
    public void onglide(EntityToggleGlideEvent e){
        if(e.getEntity() instanceof Player p &&  p.getGameMode().equals(GameMode.SURVIVAL) && !p.isOnGround())e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerInteractEvent e) {
        Player p=e.getPlayer();
        if(e.getAction().isRightClick())return;
        if(!p.isGliding() && (!p.getLocation().subtract(0,1,0).getBlock().getType().equals(Material.AIR) || !p.getLocation().subtract(0,2,0).getBlock().getType().equals(Material.AIR)))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        if(!utilitys.spezienliste.containsKey(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non")))return;
        if(!utilitys.spezienliste.get(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non")).getPassiven().contains("flugboost"))return;
        if(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)>=utilitys.passiveliste.get("flugboost").getWerte().get("Kosten")) {
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE) - utilitys.passiveliste.get("flugboost").getWerte().get("Kosten"));
            // Get normalized direction (unit vector)
            Vector direction = p.getLocation().getDirection().normalize();
            // Multiply by the strength of the boost (number of blocks)
            Vector velocity = direction.multiply(utilitys.passiveliste.get("flugboost").getWerte().get("Stärke"));
            // Apply the velocity to the player
            p.setVelocity(velocity);
        }
    }
    @EventHandler
    public void interact(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(!p.isSneaking())return;
        if(!p.isOnGround())return;
        if(!e.getAction().equals(Action.LEFT_CLICK_AIR))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        if(!utilitys.kannpassive(p,"hochflug"))return;
        if(p.getLocation().getPitch()>utilitys.passiveliste.get("hochflug").getWerte().get("winkel"))return;
        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE) - utilitys.passiveliste.get("hochflug").getWerte().get("Kosten"));
        // Get normalized direction (unit vector)
        Vector direction = p.getLocation().getDirection().normalize();
        // Multiply by the strength of the boost (number of blocks)
        Vector velocity = direction.multiply(utilitys.passiveliste.get("hochflug").getWerte().get("Stärke"));
        // Apply the velocity to the player
        p.setVelocity(velocity);
        if(utilitys.kannpassive(p,"flug")) Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () -> {
            p.setSneaking(false);
            p.setGliding(true);
        }, 10L);
    }

    @EventHandler
    public void onfall(EntityDamageEvent e){
        if(e.getEntity() instanceof Player p && utilitys.kannpassive(p,"keinfallschaden") && e.getCause().equals(EntityDamageEvent.DamageCause.FALL))e.setCancelled(true);
    }
}
