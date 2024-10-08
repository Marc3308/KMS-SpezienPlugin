package me.marc3308.rassensystem.eventlisteners;

import me.marc3308.rassensystem.ItemCreater;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class gui implements Listener {

    @EventHandler
    public void oninv(InventoryClickEvent e){


        //rassengui
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.WHITE+"          Spezienauswahl:")){

            Player p=(Player) e.getWhoClicked();
            e.setCancelled(true);
            if(e.getCurrentItem()==null)return;

            ItemStack pfeil=new ItemStack(Material.ARROW);
            ItemMeta pfeil_met= pfeil.getItemMeta();
            pfeil_met.setDisplayName(ChatColor.BOLD+"BACK");
            pfeil.setItemMeta(pfeil_met);

            if(e.getCurrentItem().getType().equals(Material.ARROW)){
                if(e.getInventory().getItem(11)!=null && e.getInventory().getItem(11).equals(ItemCreater.itcreater("Mensch",null))){
                    Bukkit.getServer().dispatchCommand(p,"profil");
                } else if((e.getInventory().getItem(11)!=null && e.getInventory().getItem(11).equals(ItemCreater.itcreater("Hochelf",null)))
                        || (e.getInventory().getItem(12)!=null && e.getInventory().getItem(12).equals(ItemCreater.itcreater("Raubtierchimären",null)))){
                    Bukkit.getServer().dispatchCommand(p,"spezienwahl");
                } else {
                    Inventory buckel= Bukkit.createInventory(p,36,ChatColor.WHITE+"          Spezienauswahl:");
                    buckel.setItem(12,ItemCreater.itcreater("Raubtierchimären",null));
                    buckel.setItem(14,ItemCreater.itcreater("Weidetierchimären",null));
                    buckel.setItem(20,ItemCreater.itcreater("Lasttierchimären",null));
                    buckel.setItem(22,ItemCreater.itcreater("Flugtierchimären",null));
                    buckel.setItem(24,ItemCreater.itcreater("SonstigeChimären",null));
                    buckel.setItem(35,pfeil);
                    p.openInventory(buckel);
                }
                return;
            }
            if(!e.getCurrentItem().hasItemMeta())return;
            if(!e.getCurrentItem().getItemMeta().hasCustomModelData())return;

            String Name=e.getCurrentItem().getItemMeta().getDisplayName();

            switch (Name){
                    //elfen
                case "Elfen":
                    Inventory elfen= Bukkit.createInventory(p,27,ChatColor.WHITE+"          Spezienauswahl:");
                    elfen.setItem(11,ItemCreater.itcreater("Hochelf",null));
                    elfen.setItem(13,ItemCreater.itcreater("Dunkelelf",null));
                    elfen.setItem(15,ItemCreater.itcreater("Waldelf",null));
                    elfen.setItem(26,pfeil);
                    p.openInventory(elfen);
                    break;
                    //tiere
                case "Chimären":
                    Inventory tiere= Bukkit.createInventory(p,36,ChatColor.WHITE+"          Spezienauswahl:");

                    tiere.setItem(12,ItemCreater.itcreater("Raubtierchimären",null));
                    tiere.setItem(14,ItemCreater.itcreater("Weidetierchimären",null));
                    tiere.setItem(20,ItemCreater.itcreater("Lasttierchimären",null));
                    tiere.setItem(22,ItemCreater.itcreater("Flugtierchimären",null));
                    tiere.setItem(24,ItemCreater.itcreater("SonstigeChimären",null));
                    tiere.setItem(35,pfeil);
                    p.openInventory(tiere);
                    break;
                case "Raubtierchimären":
                    Inventory raubtiere= Bukkit.createInventory(p,27,ChatColor.WHITE+"          Spezienauswahl:");
                    raubtiere.setItem(10,ItemCreater.itcreater("Wolfchimäre",null));
                    raubtiere.setItem(12,ItemCreater.itcreater("Katzenchimäre",null));
                    raubtiere.setItem(14,ItemCreater.itcreater("Fuchschimäre",null));
                    raubtiere.setItem(16,ItemCreater.itcreater("Eisbärchimäre",null));
                    raubtiere.setItem(26,pfeil);
                    p.openInventory(raubtiere);
                    break;
                case "Weidetierchimären":
                    Inventory Weidetierchimären= Bukkit.createInventory(p,27,ChatColor.WHITE+"          Spezienauswahl:");

                    Weidetierchimären.setItem(10,ItemCreater.itcreater("Huhnchimäre",null));
                    Weidetierchimären.setItem(12,ItemCreater.itcreater("Schweinchimäre",null));
                    Weidetierchimären.setItem(14,ItemCreater.itcreater("Kuhchimäre",null));
                    Weidetierchimären.setItem(16,ItemCreater.itcreater("Schafchimäre",null));
                    Weidetierchimären.setItem(26,pfeil);
                    p.openInventory(Weidetierchimären);
                    break;
                case "Lasttierchimären":
                    Inventory Lasttierchimären= Bukkit.createInventory(p,27,ChatColor.WHITE+"          Spezienauswahl:");
                    Lasttierchimären.setItem(10,ItemCreater.itcreater("Alpakachimäre",null));
                    Lasttierchimären.setItem(12,ItemCreater.itcreater("Eselchimäre",null));
                    Lasttierchimären.setItem(14,ItemCreater.itcreater("Pferdechimäre",null));
                    Lasttierchimären.setItem(16,ItemCreater.itcreater("Kamelchimäre",null));
                    Lasttierchimären.setItem(26,pfeil);
                    p.openInventory(Lasttierchimären);
                    break;
                case "Flugtierchimären":
                    Inventory Flugtierchimären= Bukkit.createInventory(p,27,ChatColor.WHITE+"          Spezienauswahl:");
                    Flugtierchimären.setItem(11,ItemCreater.itcreater("Fledermauschimäre",null));
                    Flugtierchimären.setItem(13,ItemCreater.itcreater("Papageichimäre",null));
                    Flugtierchimären.setItem(15,ItemCreater.itcreater("Bienenchimäre",null));
                    Flugtierchimären.setItem(26,pfeil);
                    p.openInventory(Flugtierchimären);
                    break;
                case "Sonstige Chimären":
                    Inventory Sonstige= Bukkit.createInventory(p,27,ChatColor.WHITE+"          Spezienauswahl:");
                    Sonstige.setItem(11,ItemCreater.itcreater("Hasenchimäre",null));
                    Sonstige.setItem(13,ItemCreater.itcreater("Ziegenchimäre",null));
                    Sonstige.setItem(15,ItemCreater.itcreater("Pandachimäre",null));
                    Sonstige.setItem(26,pfeil);
                    p.openInventory(Sonstige);
                    break;
                default:
                    if(p.hasPermission("parte") && !p.getPersistentDataContainer().has(new NamespacedKey("patenplugin","partenmodus"), PersistentDataType.STRING))return;


                    if(!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)){

                        p.closeInventory();
                        p.sendMessage(ChatColor.RED+"Bitte habe nichts in deiner hand");
                        p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT,1,1);
                        return;
                    }

                    p.closeInventory();
                    if(Name==null)return;
                    p.getWorld().dropItemNaturally(p.getLocation(),
                            ItemCreater.itcreater(
                                    e.getCurrentItem().getLore().get(0),
                                    Bukkit.getPlayer(UUID.fromString(p.getPersistentDataContainer().get(new NamespacedKey("patenplugin","partenmodus"), PersistentDataType.STRING)))));
                    break;

            }
        }
    }
}
