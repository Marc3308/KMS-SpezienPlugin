package me.marc3308.rassensystem.command;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.ItemCreater;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class getrasse implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender,Command command,String s,String[] strings) {

        if(!(commandSender instanceof Player))return false;
        Player p=(Player) commandSender;

        if(strings.length<2 || Bukkit.getPlayer(strings[0])==null){
            senderror(p);
            return false;
        }
        String bigon= strings[1];
        for (int i =2; i < strings.length; i++)bigon=bigon+" "+strings[i];
        String finalBigon = bigon;
        utilitys.spezienliste.values().stream().filter(sp -> extras.getCustemModel(sp.getTicker()).getModelName().replaceAll("§[0-9a-fk-orA-FK-OR]", "").equals(finalBigon)).findFirst().ifPresent(sp -> {
            p.getWorld().dropItemNaturally(p.getLocation(), utilitys.getSpeziesitem(sp,Bukkit.getPlayer(strings[0])));
        });
        return false;
    }

    private void senderror(Player p){
        p.sendMessage(ChatColor.RED+"----------------------------------");
        p.sendMessage(ChatColor.RED+"Bitte versuche /givespezies <spielerbind> <spezies>");
        p.sendMessage(ChatColor.RED+"----------------------------------");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p= (Player) sender;
        ArrayList<String> list =new ArrayList<>();

        try {
            if(args.length == 0)return list;
            if(args.length == 1)for (Player pl : Bukkit.getOnlinePlayers())list.add(pl.getName());
            if(args.length == 2)for (Spezies sp : utilitys.spezienliste.values())list.add(extras.getCustemModel(sp.getTicker()).getModelName().replaceAll("§[0-9a-fk-orA-FK-OR]", ""));
            if(args.length>2)return list;
            if(list.isEmpty())return list;

            //autocompetion
            ArrayList<String> commpleteList = new ArrayList<>();
            String currentarg = args[args.length-1].toLowerCase();
            for (String s : list){
                if(s==null)return list;
                String s1 =s.toLowerCase();
                if(s1.startsWith(currentarg)){
                    commpleteList.add(s);
                }
            }

            return commpleteList;
        } catch (CommandException e){
            return list;
        }
    }
}
