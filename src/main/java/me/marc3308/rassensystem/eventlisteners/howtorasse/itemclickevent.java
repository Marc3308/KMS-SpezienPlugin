package me.marc3308.rassensystem.eventlisteners.howtorasse;

import me.marc3308.rassensystem.ItemCreater;
import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

import static me.marc3308.rassensystem.ItemCreater.getcon;

public class itemclickevent implements Listener {


    @EventHandler
    public void onClick(PlayerInteractEvent e){

        //check if palyer is holding a paper that is a rasse
        Player p=e.getPlayer();
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.PAPER))return;
        if(!p.getInventory().getItemInMainHand().hasItemMeta())return;
        if(!p.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData())return;
        if(!p.getInventory().getItemInMainHand().getItemMeta().hasLore())return;

        //check if rasse bind to player
        List new_list=p.getInventory().getItemInMainHand().getItemMeta().getLore();
        try {
            UUID person=UUID.fromString(new_list.get(0).toString());
            if(!person.toString().equalsIgnoreCase(p.getUniqueId().toString()))return;

            //check if Rasse is a rasse

            String rasse=new_list.get(1).toString();
            if(getcon(2).get(rasse)==null)return;
            if(!getcon(2).get(rasse+".custemmoddeldata").equals(p.getInventory().getItemInMainHand().getItemMeta().getCustomModelData()))return;

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,rasse);
            p.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(ItemCreater.getcon(2).get(rasse+".SpielerGröße")!=null ? ItemCreater.getcon(2).getDouble(rasse+".SpielerGröße") : 1);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
            p.sendMessage(ChatColor.DARK_GREEN+"Du bist nun ein: "+ChatColor.GREEN+getcon(2).getString(rasse+".name"));
            p.getInventory().setItemInMainHand(null);
        } catch (IllegalArgumentException  | NullPointerException ex){
            return;
        }

    }
}
