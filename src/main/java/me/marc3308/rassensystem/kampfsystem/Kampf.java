package me.marc3308.rassensystem.kampfsystem;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class Kampf implements Listener {


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        double alldmg = e.getDamage();
        //set fight damager
        if(e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile pr && pr.getShooter() instanceof Player)) {
            Player p = (Player) (e.getDamager() instanceof Player ? e.getDamager() : ((Projectile) e.getDamager()).getShooter());
            //get all werte von grund und klasse
            alldmg = e.getDamage()*((utilitys.einstellungen.getWaffenschaden()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "waffenschaden"), PersistentDataType.DOUBLE,0.0))/100.0);
            double allcrit = (utilitys.einstellungen.getWaffencritdmg()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "waffencritdmg"), PersistentDataType.DOUBLE,0.0))/100.0;
            double allcritchange = utilitys.einstellungen.getWaffencritdmg()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "waffencritchance"), PersistentDataType.DOUBLE,0.0);

            double ran = ThreadLocalRandom.current().nextDouble(1,101);

            if(ran<=allcritchange)alldmg*=allcrit;

            //ausdauerkosten
            if(utilitys.einstellungen.getWaffenschlagkosnte().containsKey(p.getInventory().getItemInMainHand().getType())) {
                double kosten = utilitys.einstellungen.getWaffenschlagkosnte().get(p.getInventory().getItemInMainHand().getType());
                if(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE)<kosten){
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE,0.0);
                    alldmg*=(utilitys.einstellungen.getSchadenohneausdauer()/100.0);
                } else {
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE
                            ,p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE,0.0)-kosten);
                }
            }

            e.setDamage(alldmg);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE, 0.0) + (alldmg*2));
        }

        //set fight damagee
        if(e.getEntity() instanceof Player p) {
            //get all werte von grund und klasse
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE, 0.0) + (alldmg*2));
        }
    }

    @EventHandler
    public void onopen(InventoryOpenEvent e){
        Player p= (Player) e.getPlayer();
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onp2(InventoryClickEvent e){
        Player p= (Player) e.getWhoClicked();
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        e.setCancelled(true);
    }
}
