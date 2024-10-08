package me.marc3308.rassensystem.passiven;

import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import static me.marc3308.rassensystem.ItemCreater.getcon;

public class mobdinge implements Listener {

    @EventHandler
    public void oncrep(EntityTargetLivingEntityEvent e){
        if(!(e.getTarget() instanceof Player))return;
        if(!(e.getEntity() instanceof Creeper))return;
        Player p= (Player) e.getTarget();

        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "creepergoawai"), PersistentDataType.BOOLEAN))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;

        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return;
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("creepergoawai"))break;
        }
        e.setCancelled(true);
        Creeper creeper = (Creeper) e.getEntity();
        Player player = (Player) e.getTarget();
        // Get direction from player to creeper and reverse it to push creeper away
        Vector playerToCreeper = creeper.getLocation().toVector().subtract(player.getLocation().toVector());
        Vector knockbackDirection = playerToCreeper.normalize().multiply(1.5); // Multiply to control knockback strength
        // Apply knockback to the creeper
        creeper.setVelocity(knockbackDirection);


    }
}