package me.marc3308.rassensystem.passiven;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import static me.marc3308.rassensystem.ItemCreater.getcon;
import static me.marc3308.rassensystem.ItemCreater.isapassive;

public class töten implements Listener {

    @EventHandler
    public void ontot(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player))return;
        if(!(e.getEntity() instanceof LivingEntity))return;
        Player p= (Player) e.getDamager();
        if(!isapassive(p,"heilungdurchtot"))return;

        if(((LivingEntity) e.getEntity()).getHealth()-e.getFinalDamage()<1){
            p.setHealth(p.getHealth()+(p.getMaxHealth()*(getcon(9).getInt("heilungdurchtot"+".Stärke")/100.0))>p.getMaxHealth()
                    ? p.getMaxHealth() : p.getHealth()+(p.getMaxHealth()*(getcon(9).getInt("heilungdurchtot"+".Stärke")/100.0)));
        }
    }

    @EventHandler
    public void onhit(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player))return;
        Player p= (Player) e.getEntity();
        if(!isapassive(p,"wenigruckstos"));

        Player player = (Player) e.getEntity();
        // Get the current velocity (knockback effect)
        Vector knockback = player.getVelocity();
        // Reduce the knockback by multiplying the velocity (reduce by 50% in this case)
        Vector reducedKnockback = knockback.multiply(getcon(9).getInt("wenigruckstos"+".Stärke")/100.0);  // Adjust this factor to reduce more or less
        // Set the player's new velocity
        player.setVelocity(reducedKnockback);


    }
}
