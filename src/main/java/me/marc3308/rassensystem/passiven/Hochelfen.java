package me.marc3308.rassensystem.passiven;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class Hochelfen implements Listener {

    @EventHandler
    public void onschiftright(PlayerInteractEvent e){
        Player p=e.getPlayer();
    }
}
