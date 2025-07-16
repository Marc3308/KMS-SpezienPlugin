package me.marc3308.rassensystem.command;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class setrasse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {
        if(commandSender instanceof Player)return false;
        if(strings.length<2 || Bukkit.getPlayer(strings[0])==null){
            return false;
        }
        Player p=Bukkit.getPlayer(strings[0]);

        String bigon= strings[1];
        for (int i =2; i < strings.length; i++)bigon=bigon+" "+strings[i];
        String finalBigon = bigon;
        utilitys.spezienliste.values().stream().filter(sp -> sp.getTicker().equals(finalBigon)).findFirst().ifPresent(sp -> {
            //give player rasse
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,sp.getErkennung());
            p.sendMessage(ChatColor.DARK_GREEN+"Du bist nun ein: "+ extras.getCustemModel(sp.getTicker()).getModelName());
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
            p.getAttribute(Attribute.SCALE).setBaseValue(sp.getGrose());
        });
        return false;
    }

}
