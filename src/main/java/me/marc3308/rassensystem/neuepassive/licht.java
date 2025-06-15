package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.utilitys;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class licht implements Listener {


    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(utilitys.kannpassive(p,"mediation") && e.getAction().equals(Action.LEFT_CLICK_BLOCK) && p.isSneaking()
                && e.getClickedBlock().getLocation().equals(p.getLocation().subtract(0, 1, 0).getBlock().getLocation())){
            p.setSneaking(false);
            // Spawn ArmorStand slightly lower for sitting look
            ArmorStand chair = (ArmorStand) p.getWorld().spawn(p.getLocation().clone().add(0, 1, 0), ArmorStand.class);
            chair.setVisible(false);
            chair.setGravity(true);
            chair.setMarker(true); // hitbox = 0
            chair.setInvulnerable(true);
            chair.setSmall(true);
            chair.setCustomName("meditation");
            chair.setCustomNameVisible(false);

            // Set player as passenger
            chair.addPassenger(p);

        }
    }
    @EventHandler
    public void dismoint(EntityDismountEvent e){
        if(e.getEntity() instanceof Player p && utilitys.kannpassive(p,"mediation") &&  e.getDismounted() instanceof ArmorStand as
                && as.getCustomName().equals("meditation"))as.remove();
    }
}
