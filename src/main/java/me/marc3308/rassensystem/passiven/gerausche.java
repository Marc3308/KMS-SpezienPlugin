package me.marc3308.rassensystem.passiven;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static me.marc3308.rassensystem.ItemCreater.isapassive;

public class gerausche implements Listener {

    @EventHandler
    public void onblockbreak(BlockBreakEvent e){
        Player p=e.getPlayer();
        if(!isapassive(p,"noabbausound"))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey("siedlungundberufe", "insone"), PersistentDataType.INTEGER))return;
        e.setCancelled(true);
        for(ItemStack it : e.getBlock().getDrops())e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),it);
        e.getBlock().getWorld().setBlockData(e.getBlock().getLocation(), Material.AIR.createBlockData());
    }

    @EventHandler
    public void onblockbreak(PlayerPickupItemEvent e){
        Player p=e.getPlayer();
        if(!isapassive(p,"noaufsammelsound"))return;
        if(p.getInventory().firstEmpty()==-1)return;
        e.setCancelled(true);
        e.getItem().remove();
        p.getInventory().setItem(p.getInventory().firstEmpty(),e.getItem().getItemStack());
    }
}
