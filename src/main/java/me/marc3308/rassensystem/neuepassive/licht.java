package me.marc3308.rassensystem.neuepassive;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.Random;

public class licht implements Listener {


    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak"))){
            p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak"));
        }
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

    @EventHandler
    public void schalg(EntityDamageByEntityEvent e){
        if(isUndead(e.getEntity()) && ((e.getDamager() instanceof Player p && utilitys.kannpassive(p,"holyschaden"))
                || (e.getDamager() instanceof Projectile pr && pr.getShooter() instanceof Player pl && utilitys.kannpassive(pl,"holyschaden"))))e.setDamage(utilitys.passiveliste.get("holyschaden").getWerte().get("Stärke"));
    }

    @EventHandler
    public void onxp(PlayerPickupExperienceEvent e){
        if(utilitys.kannpassive(e.getPlayer(),"xpsammler")) {
            Player p = e.getPlayer();
            p.giveExp((int) (e.getExperienceOrb().getExperience()*((utilitys.passiveliste.get("xpsammler").getWerte().get("Stärke")/100.0)+1)));
            e.getExperienceOrb().remove();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void ondmg(PlayerItemDamageEvent e){
        if(utilitys.kannpassive(e.getPlayer(),"haltbar") && new Random().nextInt(101)<=utilitys.passiveliste.get("haltbar").getWerte().get("Chanche"))e.setCancelled(true);
    }



    @EventHandler
    public void ondamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak")))e.setCancelled(true);
    }
    @EventHandler
    public void onblock(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak")))e.setCancelled(true);
    }
    @EventHandler
    public void ondamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak")))e.setCancelled(true);
    }
    @EventHandler
    public void onpicup(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak")))e.setCancelled(true);
    }
    @EventHandler
    public void ondrop(EntityDropItemEvent e){
        if(e.getEntity() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak")))e.setCancelled(true);
    }
    @EventHandler
    public void oninv(InventoryClickEvent e){
        if(e.getWhoClicked() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "jugmentmak")))e.setCancelled(true);
    }



    public static boolean isUndead(Entity entity) {
        return switch (entity.getType()) {
            case ZOMBIE, SKELETON, STRAY, WITHER_SKELETON, ZOMBIFIED_PIGLIN,
                 DROWNED, HUSK, ZOGLIN, WITHER, ZOMBIE_HORSE, ZOMBIE_VILLAGER, SKELETON_HORSE -> true;
            default -> false;
        };
    }
}
