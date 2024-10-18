package me.marc3308.rassensystem.passiven;

import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

import static me.marc3308.rassensystem.ItemCreater.*;

public class Luckeffeckt implements Listener {


    private static ArrayList<Material> erzlist=new ArrayList<>();
    public Luckeffeckt(){

        erzlist.add(Material.COAL_ORE);
        erzlist.add(Material.COPPER_ORE);
        erzlist.add(Material.LAPIS_ORE);
        erzlist.add(Material.IRON_ORE);
        erzlist.add(Material.REDSTONE_ORE);
        erzlist.add(Material.DIAMOND_ORE);
        erzlist.add(Material.GOLD_ORE);
        erzlist.add(Material.EMERALD_ORE);
        erzlist.add(Material.DEEPSLATE_COAL_ORE);
        erzlist.add(Material.DEEPSLATE_COPPER_ORE);
        erzlist.add(Material.DEEPSLATE_LAPIS_ORE);
        erzlist.add(Material.DEEPSLATE_IRON_ORE);
        erzlist.add(Material.DEEPSLATE_REDSTONE_ORE);
        erzlist.add(Material.DEEPSLATE_DIAMOND_ORE);
        erzlist.add(Material.DEEPSLATE_GOLD_ORE);
        erzlist.add(Material.DEEPSLATE_EMERALD_ORE);
    }

    @EventHandler
    public void onaktiv(PlayerInteractEvent e){
        Player p=e.getPlayer();

        //is passive?
        if(!isapassive(p,"luckpassive"))return;
        if(!p.isSneaking() || e.getAction().isLeftClick())return;

        //should stop the player from doing stuff
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE)
                && p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE)>1 ){
            barremove(p);
            p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"));
            p.setSneaking(false);
            return;
        }

        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE,0.0);
        p.setSneaking(false);
        bar(p,"leben",getcon(9).getString("luckpassive"+".AnzeigeName"));

        new BukkitRunnable(){
            @Override
            public void run() {

                if(!p.isOnline() || !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE)){
                    barremove(p);
                    p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"));
                    cancel();
                    return;
                }

                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE
                        ,p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE)+1);

                p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK,20*3,10,false,false));


                double kosten=getcon(9).getInt("luckpassive"+".Kosten");
                double kostenwertnow=p.getHealth();
                if(kostenwertnow - kosten > (p.getMaxHealth()/100.0)*10) {
                    p.setHealth(kostenwertnow-kosten);
                } else {
                    barremove(p);
                    p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"));
                    cancel();
                }

            }
        }.runTaskTimer(Rassensystem.getPlugin(),0,20);
    }

    @EventHandler
    public void ondmg(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player))return;
        Player p= (Player) e.getEntity();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE))return;
        if(new Random().nextInt(0,101)>getcon(9).getInt("luckpassive"+".Ausweichchancheinprozent"))e.setCancelled(true);
    }

    @EventHandler
    public void onkill (EntityDeathEvent e){
        if(!(e.getEntity().getKiller() instanceof Player))return;
        Player p= (Player) e.getEntity().getKiller();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE))return;
        ArrayList<ItemStack> dops=new ArrayList<>();
        for (ItemStack drops : e.getDrops())dops.add(drops);
        for (int i=0;i<getcon(9).getInt("luckpassive"+".lootingstufe");i++){
            int ran=new Random().nextInt(0,dops.size());
            dops.get(ran).setAmount(dops.get(ran).getAmount()+1);
        }
        e.getDrops().clear();
        for (ItemStack drops : dops)p.getWorld().dropItemNaturally(e.getEntity().getLocation(),drops);

    }
    @EventHandler
    public void onbreak (BlockBreakEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "luckpassive"), PersistentDataType.DOUBLE))return;
        if(!erzlist.contains(e.getBlock().getType()))return;
        ArrayList<ItemStack> dops=new ArrayList<>();
        for (ItemStack drops : e.getBlock().getDrops())dops.add(drops);
        dops.get(0).setAmount(dops.get(0).getAmount()+new Random().nextInt(0,getcon(9).getInt("luckpassive"+".lootingstufe")));
        e.getBlock().getDrops().clear();
        for (ItemStack drops : dops)p.getWorld().dropItemNaturally(e.getBlock().getLocation(),drops);
    }
}
