package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class wasser implements Listener {

    @EventHandler
    public void waterwalk(PlayerMoveEvent e){

        Player p = e.getPlayer();
        if(!p.isSneaking() && utilitys.kannpassive(p, "wasserlauf")
                && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.WATER)
                && p.getLocation().getBlock().getType().equals(Material.AIR)
                && p.getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.AIR)){
            Location loc = p.getLocation().subtract(0,1,0);
            p.sendBlockChange(loc,Material.BARRIER.createBlockData());
            Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () ->p.sendBlockChange(loc,loc.getBlock().getBlockData()), 1L);
        }
    }
}
