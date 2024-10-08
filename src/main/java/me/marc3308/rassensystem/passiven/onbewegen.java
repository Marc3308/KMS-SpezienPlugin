package me.marc3308.rassensystem.passiven;

import me.marc3308.rassensystem.Rassensystem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.marc3308.rassensystem.ItemCreater.getcon;
import static me.marc3308.rassensystem.ItemCreater.isapassive;

public class onbewegen implements Listener {

    @EventHandler
    public void onsichbewegen(PlayerMoveEvent e){
        Player p=e.getPlayer();
        //chicken slowfall nach 3 blocken
        if(isapassive(p,"slowfall"))if(p.getFallDistance()>=3)p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,20,0,false,false));
        //man tut auf ice nicht rutschen
        if(isapassive(p,"nosliponice")){
            Material iceblock=p.getLocation().getBlock().getRelative(0,-1,0).getType();
            if(iceblock.equals(Material.ICE) || iceblock.equals(Material.PACKED_ICE) || iceblock.equals(Material.BLUE_ICE)){
                Location loc=p.getLocation().subtract(0,1,0);
                p.sendBlockChange(loc,Material.BLUE_STAINED_GLASS.createBlockData());
                Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () -> p.sendBlockChange(loc,loc.getBlock().getBlockData()), 10L);
            }
        }
        //man versinkt nicht in pulverschnee
        if(isapassive(p,"nopulverschneesinken"))if(p.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.POWDER_SNOW))p.sendBlockChange(p.getLocation().getBlock().getRelative(0,-1,0).getLocation(), Material.SNOW_BLOCK.createBlockData());
        //man springt höher
        if(isapassive(p,"highjump")
                && p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"onhighjump"), PersistentDataType.INTEGER,-1)<0){
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(!p.isSneaking() || p.isDead() || !p.isOnline()){
                        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"onhighjump"), PersistentDataType.INTEGER,-1);
                        cancel();
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"onhighjump"),
                            PersistentDataType.INTEGER,p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"onhighjump"), PersistentDataType.INTEGER,0)+1);

                    //custem anzeige
                    String werteanzeige = ChatColor.GREEN+String.valueOf(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"onhighjump"),PersistentDataType.INTEGER));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(werteanzeige));


                    if(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"onhighjump"), PersistentDataType.INTEGER,-1)>=3){
                        p.setSneaking(false);
                        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"onhighjump"), PersistentDataType.INTEGER,-1);
                        p.playSound(p,Sound.ENTITY_PLAYER_LEVELUP,1,1);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST,getcon(9).getInt("highjump"+".EffectDauer")*20,getcon(9).getInt("highjump"+".EffectStärke"),false,false));
                        cancel();
                    }

                }
            }.runTaskTimer(Rassensystem.getPlugin(),0,20);
        }
    }
}
