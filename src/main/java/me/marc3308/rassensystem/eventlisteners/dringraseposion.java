package me.marc3308.rassensystem.eventlisteners;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.persistence.PersistentDataType;

public class dringraseposion implements Listener {


    @EventHandler
    public void ondrink(PlayerItemConsumeEvent e){

        //check if palyer is holding a paper that is a rasse
        Player p=e.getPlayer();

        if(!p.getInventory().getItemInMainHand().getType().equals(Material.HONEY_BOTTLE)
                || !p.getInventory().getItemInMainHand().hasItemMeta())return;


        if(utilitys.spezienliste.containsKey(p.getItemInHand().getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING))) {
            Spezies sp = utilitys.spezienliste.get(p.getItemInHand().getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING));
            //check if its the players
            if(!p.getInventory().getItemInMainHand().getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"bind"), PersistentDataType.STRING).equals(p.getUniqueId().toString()))return;

            //give player rasse
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,sp.getErkennung());
            p.getInventory().setItemInMainHand(null);
            p.sendMessage(ChatColor.DARK_GREEN+"Du bist nun ein: "+ extras.getCustemModel(sp.getTicker()).getModelName());
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
            p.getAttribute(Attribute.SCALE).setBaseValue(sp.getGrose());
        }
    }
}
