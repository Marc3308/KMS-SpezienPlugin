package me.marc3308.rassensystem.command;

import me.marc3308.rassensystem.ItemCreater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getrasse implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {

        if(!(commandSender instanceof Player))return false;
        Player p=(Player) commandSender;
        if(!p.hasPermission("parteleitung"))return false;

        if(strings.length==2){
            Player pp= Bukkit.getPlayer(strings[1]);
            if(pp==null){
                p.sendMessage(ChatColor.RED+"Dieser Spieler existiert nicht");
                return false;
            }
            p.getWorld().dropItemNaturally(p.getLocation(), ItemCreater.itcreater(strings[0],pp));
        } else {
            p.sendMessage(ChatColor.GREEN+"----------------------------------");
            p.sendMessage(ChatColor.GREEN+"Bitte versuche /giverasse %rasse% %spielerbind%");
            p.sendMessage(ChatColor.GREEN+"----------------------------------");
        }





        return false;
    }
}
