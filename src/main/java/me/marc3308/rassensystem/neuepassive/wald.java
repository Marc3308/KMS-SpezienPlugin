package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class wald implements Listener {

    @EventHandler
    public void onscan(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(utilitys.kannpassive(p,"waldscann") && e.getAction().equals(Action.LEFT_CLICK_AIR) && p.isSneaking()){
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-utilitys.passiveliste.get("waldscann").getWerte().get("Kosten"));
            p.getNearbyEntities(utilitys.passiveliste.get("waldscann").getWerte().get("größe"), utilitys.passiveliste.get("waldscann").getWerte().get("größe"), utilitys.passiveliste.get("waldscann").getWerte().get("größe")).stream()
                    .filter(et -> et instanceof Animals || et instanceof Monster).forEach(et -> ((Creature) et).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,20*5,3, false, false)));
        }
    }

    @EventHandler
    public void onblockbrake(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(iswood(e.getBlock().getType()) && utilitys.kannpassive(p,"woodschläger"))p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"woodschläger"), PersistentDataType.INTEGER,
                Math.max(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"woodschlägererwert"), PersistentDataType.INTEGER,0)+1,utilitys.passiveliste.get("woodschläger").getWerte().get("Max")));
    }

    private boolean iswood(Material mat){
        return mat.name().contains("OAK") || mat.name().contains("SPRUCE") ||
                mat.name().contains("BIRCH") || mat.name().contains("JUNGLE") ||
                mat.name().contains("ACACIA") || mat.name().contains("DARK_OAK") ||
                mat.name().contains("MANGROVE") || mat.name().contains("CHERRY") ||
                mat.name().contains("BAMBOO");
    }
}
