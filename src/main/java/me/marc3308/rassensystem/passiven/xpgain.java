package me.marc3308.rassensystem.passiven;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.marc3308.rassensystem.ItemCreater.getcon;
import static me.marc3308.rassensystem.ItemCreater.isapassive;

public class xpgain implements Listener {


    @EventHandler
    public void onxp(PlayerPickupExperienceEvent e){
        Player p=e.getPlayer();
        if(!isapassive(p,"xporbgain"))return;
        e.getExperienceOrb().setExperience((int) (e.getExperienceOrb().getExperience()*(getcon(9).getInt("xporbgain"+".Kosten")/100.0)));
    }
}
