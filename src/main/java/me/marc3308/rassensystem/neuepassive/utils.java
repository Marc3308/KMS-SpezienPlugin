package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static me.marc3308.rassensystem.neuepassive.schatten.shadowaperat;

public class utils implements Listener {

    private ArrayList<String> pasliste = new ArrayList<>(){{
        add("lufatem");
        add("feueratem");
        add("erdatem");
        add("eisatem");

        add("schattenklon");
        add("jugment");
    }};

    @EventHandler
    public void onattack(EntityDamageByEntityEvent e){
        pasliste.forEach(pa -> {
            if(e.getDamager() instanceof Player p && utilitys.kannpassive(p,pa)
                    && utilitys.passiveliste.get(pa).getWerte().get("Dauer")
                    ==p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), pa+"d"), PersistentDataType.INTEGER,0)
                    && !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), pa+"mak"), PersistentDataType.BOOLEAN)){
                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), pa+"mak"), PersistentDataType.BOOLEAN,true);
                doshit(pa, p,e.getEntity());
            }
        });
    }

    @EventHandler
    public void oncklic(PlayerInteractEvent e){
        Player p = e.getPlayer();
        pasliste.forEach(pa -> {
            if(e.getAction().equals(Action.LEFT_CLICK_AIR) && utilitys.kannpassive(p,pa)
                    && utilitys.passiveliste.get(pa).getWerte().get("Dauer")
                    ==p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), pa+"d"), PersistentDataType.INTEGER,0)
                    && !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), pa+"mak"), PersistentDataType.BOOLEAN)){
                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), pa+"mak"), PersistentDataType.BOOLEAN,true);
                doshit(pa, p,null);
            }
        });
    }

    private void doshit(String passive, Player p, Entity secent) {
        switch (passive){
            case "jugment":
                if(!p.getInventory().getItemInMainHand().getType().isAir()){
                    p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), passive+"mak"));
                    return;
                }
                if(secent!=null && secent instanceof Player se){
                    se.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), passive+"mak"), PersistentDataType.BOOLEAN,true);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if(!p.isOnline() || !se.isOnline() || !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), passive+"mak"), PersistentDataType.BOOLEAN)
                                    || !se.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), passive+"mak"), PersistentDataType.BOOLEAN)){
                                se.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), passive+"mak"));
                                cancel();
                                return;
                            }
                            se.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20*2,0,false,false));
                        }
                    }.runTaskTimer(Rassensystem.getPlugin(), 0, 20);
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(Rassensystem.getPlugin(), () -> p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), passive+"mak"), PersistentDataType.BOOLEAN,true), 10L);
                break;
            case "schattenklon":
                shadowaperat(p.getHealth()/2.0, p.getLocation(), p);
                p.setVelocity(new Vector(
                        -p.getLocation().getDirection().normalize().getX() * 0.5,
                        -1,
                        -p.getLocation().getDirection().normalize().getZ() * 0.5
                ));
                break;
        }
    }


}
