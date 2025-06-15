package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.utilitys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class schatten implements Listener {

    @EventHandler
    public void onschadow(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if((e.getFrom().getY()!=e.getTo().getY() || e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ()!=e.getTo().getZ())
                && utilitys.kannpassive(p,"schattenstand"))utilitys.timemap.put(p.getUniqueId().toString(),System.currentTimeMillis());

    }
}
