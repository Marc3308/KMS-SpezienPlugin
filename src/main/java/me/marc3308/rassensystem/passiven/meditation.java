package me.marc3308.rassensystem.passiven;

import me.marc3308.rassensystem.ItemCreater;
import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.marc3308.rassensystem.ItemCreater.getcon;
import static me.marc3308.rassensystem.ItemCreater.isapassive;

public class meditation implements Listener {

    @EventHandler
    public void onmeditation(PlayerInteractEvent e){

        Player p=e.getPlayer();
        if(!isapassive(p,"meditatin"))return;
        if(!e.getAction().isRightClick())return;
        if(p.isSneaking())return;
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.AIR))return;

        ArmorStand ar=p.getWorld().spawn(p.getLocation().add(0,0,0),ArmorStand.class);
        p.sendTitle(ChatColor.GREEN+getcon(9).getString("meditatin"+".AnzeigeName"),"");

        //to make sure its not spameble
        p.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE,40,20,false,false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,40,20,false,false));
        ar.setMaxHealth(1000.0);
        ar.setSmall(true);
        ar.setCustomNameVisible(false);
        ar.setInvulnerable(true);
        ar.setVisible(false);
        ar.setPassenger(p);

        new BukkitRunnable(){
            @Override
            public void run() {
                if(!p.isOnline()
                        || p.isDead()
                        || ar.getPassenger()==null
                        || !ar.getPassenger().equals(p)
                        || p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)) {
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "passivmediataion"), PersistentDataType.INTEGER,0);
                    p.sendTitle("","");
                    ar.remove();
                    cancel();
                    return;
                }
                //positioning
                ar.setPassenger(p);
                //werte
                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "passivmediataion"), PersistentDataType.INTEGER,getcon(9).getInt("meditatin"+".Stärke"));

            }
        }.runTaskTimer(Rassensystem.getPlugin(),40,20);


    }

}
