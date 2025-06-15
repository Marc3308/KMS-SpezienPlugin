package me.marc3308.rassensystem.kampfsystem;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class KO implements Listener {

    @EventHandler
    public void playerdeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)) {
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "seelenenergie"), PersistentDataType.INTEGER,
                    p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "seelenenergie"), PersistentDataType.INTEGER,100)-1);
            p.getLocation().getNearbyEntities(1,1,1).stream().filter(et -> et instanceof ArmorStand && ((ArmorStand) et).isSmall()).forEach(entity -> ((ArmorStand) entity).remove());
            p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "istko"));
        } else if(e.getEntity() instanceof Player p){

            for(ItemStack it : p.getInventory().getArmorContents())if(it!=null)p.getWorld().dropItemNaturally(p.getLocation(), it);
            p.getInventory().setArmorContents(null);

            e.setCancelled(true);
            if(p.getInventory().getItemInOffHand()!=null){
                p.getWorld().dropItemNaturally(p.getLocation(),p.getInventory().getItemInOffHand());
                p.getInventory().setItemInOffHand(null);
            }
            p.setHealth(1);
            Location loc = p.getLocation();
            loc.setPitch(90);
            p.teleport(loc);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER, utilitys.einstellungen.getKozeit());

            // Spawn ArmorStand slightly lower for sitting look
            ArmorStand chair = (ArmorStand) p.getWorld().spawn(p.getLocation().clone().add(0, 0, 0), ArmorStand.class);
            chair.setVisible(false);
            chair.setGravity(true);
            chair.setMarker(true); // hitbox = 0
            chair.setInvulnerable(true);
            chair.setSmall(true);
            chair.setCustomNameVisible(false);

            // Set player as passenger
            chair.addPassenger(p);
        }
    }

    @EventHandler
    public void onschlagen(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player))return;
        Player p= (Player) e.getDamager();
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER))e.setCancelled(true);
    }

    @EventHandler
    public void onmove(PlayerMoveEvent e){
        Player p= (Player) e.getPlayer();
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER) && e.getFrom().getY()<=e.getTo().getY())e.setCancelled(true);
    }

    @EventHandler
    public void inclick(InventoryClickEvent e){
        Player p= (Player) e.getWhoClicked();
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER))e.setCancelled(true);
    }

    @EventHandler
    public void armor(PlayerAttemptPickupItemEvent e){
        Player p = e.getPlayer();
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER))e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER) && e.getDismounted() instanceof ArmorStand)e.setCancelled(true);
    }

    @EventHandler
    public void oninv(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof Player p && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)){
            e.getPlayer().openInventory(p.getInventory());
        }
    }

    @EventHandler
    public void onsneak(PlayerToggleSneakEvent e){

        Player p=e.getPlayer();
        if(!p.isSneaking() && !p.getPassengers().isEmpty()){
            p.removePassenger(p.getPassengers().get(0));
        } else if(!p.isSneaking() && p.getPassengers().isEmpty()){
            p.getNearbyEntities(1,1,1).stream().filter(et -> et instanceof Player && ((Player) et).getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)).findFirst().ifPresent(
                pl -> {
                    final int[] i = {0};
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if(!p.isSneaking()
                                    || !pl.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)){
                                cancel();
                                return;
                            }

                            //party
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM,i[0],i[0]);
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0f); // Color and size
                            p.getWorld().spawnParticle(Particle.DUST, pl.getLocation(), 50, 0.5, 0.5, 0.5, dustOptions);

                            i[0]++;
                            if(i[0] >= 5) {
                                pl.getLocation().getNearbyEntities(2,2,2).stream().filter(et -> et instanceof ArmorStand && ((ArmorStand) et).isSmall()).forEach(entity -> ((ArmorStand) entity).remove());
                                p.addPassenger(pl);
                                cancel();
                                return;
                            }
                        }
                    }.runTaskTimer(Rassensystem.getPlugin(),0, 20);
                }
            );
        }
    }
}
