package me.marc3308.rassensystem.Gui;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class openeditorcommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,Command command,String label, String[] args) {
        Player p = (Player) sender;
        p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl"));
        return false;
    }
}
