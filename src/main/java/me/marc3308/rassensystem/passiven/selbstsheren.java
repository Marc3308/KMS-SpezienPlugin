package me.marc3308.rassensystem.passiven;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static me.marc3308.rassensystem.ItemCreater.getcon;
import static me.marc3308.rassensystem.ItemCreater.isapassive;
import static me.marc3308.rassensystem.Rassensystem.timermap;

public class selbstsheren implements Listener {

    public static Random random=new Random();

    @EventHandler
    public void onschere(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(!isapassive(p,"selbstscheren"))return;
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.SHEARS))return;
        if(timermap.containsKey(p) && System.currentTimeMillis()-timermap.get(p)<=(getcon(9).getInt("selbstscheren"+".Cooldown")*1000))return;
        timermap.put(p,System.currentTimeMillis());
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHEEP_SHEAR,1,1);
        ItemStack it=new ItemStack(Material.WHITE_WOOL);
        it.setAmount(random.nextInt(0,getcon(9).getInt("selbstscheren"+".Stärke")+1));
        p.getWorld().dropItemNaturally(p.getLocation(),it);
    }
}
