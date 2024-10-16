package me.marc3308.rassensystem.command;

import me.marc3308.rassensystem.ItemCreater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class rassencommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {

        //check if sender is mensch
        if(!(commandSender instanceof Player))return false;
        Player p=(Player) commandSender;

        Inventory rassen= Bukkit.createInventory(p,27, ChatColor.WHITE+"          Spezienauswahl:");

        rassen.setItem(11, ItemCreater.itcreater("Mensch",null));
        rassen.setItem(13,ItemCreater.itcreater("Elfen",null));
        rassen.setItem(15, ItemCreater.itcreater("Chimären",null));

        ItemStack pfeil=new ItemStack(Material.ARROW);
        ItemMeta pfeil_met= pfeil.getItemMeta();
        pfeil_met.setDisplayName(ChatColor.BOLD+"BACK");
        pfeil.setItemMeta(pfeil_met);
        rassen.setItem(26,pfeil);
        p.openInventory(rassen);


        return false;
    }
}
