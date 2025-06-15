package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class erde implements Listener {

    @EventHandler
    public void ondown(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(utilitys.kannpassive(p,"steinversteck") && p.isSneaking()
                && e.getAction().equals(Action.LEFT_CLICK_BLOCK) && p.getLocation().distance(e.getClickedBlock().getLocation()) <= utilitys.passiveliste.get("steinversteck").getWerte().get("Reichweite")
                && p.getLocation().getBlockY()>=e.getClickedBlock().getLocation().getBlockY()
                && !p.getLocation().getBlock().getType().isSolid()){
            Location blockloc = e.getClickedBlock().getLocation().add(0.5,0,0.5);
            Location loc = e.getClickedBlock().getLocation().clone().add(0.5, -((int) Math.ceil(p.getEyeLocation().getY()-p.getLocation().getY()))+1, 0.5); // Center of clicked block
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(!utilitys.kannpassive(p,"steinversteck") || !p.isOnline() || (!p.getLocation().getBlock().getType().isSolid() && !p.getEyeLocation().getBlock().isSolid() && !p.isSneaking())){
                        cancel();
                        return;
                    } else if(!p.isSneaking()){
                        p.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, blockloc.add(0.5,1,0.5), 20, 0.3, 0.3, 0.3, blockloc.getBlock().getBlockData());
                        p.teleport(p.getLocation().add(0,1,0));
                    } else if(p.getLocation().distance(loc)>=0.8){
                        p.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, p.getLocation(), 20, 0.3, 0.3, 0.3, blockloc.getBlock().getBlockData());
                        p.teleport(p.getLocation().add(loc.toVector().subtract(p.getLocation().toVector()).multiply(0.3)));
                    } else if(p.getLocation().distance(loc)<0.8 && p.getLocation().distance(loc)>0.1){
                        p.teleport(loc);
                    }
                    if(p.getLocation().distance(loc)<=0.1){
                        p.sendBlockChange(blockloc, Material.BARRIER.createBlockData());
                        Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () ->{
                            if(p.getLocation().distance(loc)>0.1)p.sendBlockChange(blockloc,blockloc.getBlock().getBlockData());
                        }, 3L);
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                            p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-(utilitys.passiveliste.get("steinversteck").getWerte().get("Kosten")/4.0));
                    utilitys.showbar(p,5);
                }
            }.runTaskTimer(Rassensystem.getPlugin(), 0, 5);
        }
    }

    @EventHandler
    public void onSuffocate(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p && utilitys.kannpassive(p,"erdatmung") && event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION))event.setCancelled(true);
    }
}
