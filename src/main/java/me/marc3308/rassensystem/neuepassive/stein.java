package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataType;

public class stein implements Listener {

    @EventHandler
    public void onblockbrake(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(isstein(e.getBlock().getType()) && utilitys.kannpassive(p,"steinschlager"))p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"steinschlagerwert"), PersistentDataType.INTEGER,
                Math.max(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"steinschlagerwert"), PersistentDataType.INTEGER,0)+1,utilitys.passiveliste.get("steinschlager").getWerte().get("Max")));
    }

    @EventHandler
    public void onblockbreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(utilitys.kannpassive(p,"erzflusterer") && e.getBlock().getType().name().contains("_ORE")){
            e.setCancelled(true);
            crawler(e.getBlock(),p);
        }
    }

    private void crawler(Block b,Player p) {
        if(utilitys.kannpassive(p,"erzflusterer") && p.isSneaking()) {
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-utilitys.passiveliste.get("erzflusterer").getWerte().get("Kosten"));
            for (BlockFace bf : BlockFace.values())if( b.getRelative(bf).getType().equals(b.getType()))crawler(b.getRelative(bf),p);
            b.breakNaturally();
        }
    }

    public static boolean isstein(Material mat){
        return mat == Material.STONE ||
               mat == Material.GRANITE ||
               mat == Material.DIORITE ||
               mat == Material.ANDESITE ||
               mat == Material.COBBLESTONE ||
               mat == Material.STONE_BRICKS ||
               mat == Material.MOSSY_COBBLESTONE ||
               mat == Material.SANDSTONE ||
               mat == Material.RED_SANDSTONE ||
               mat == Material.QUARTZ_BLOCK ||
               mat == Material.END_STONE ||
               mat == Material.BLACKSTONE;

    }
}
