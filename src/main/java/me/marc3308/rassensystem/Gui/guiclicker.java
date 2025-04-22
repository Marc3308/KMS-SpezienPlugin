package me.marc3308.rassensystem.Gui;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class guiclicker implements Listener {

    @EventHandler
    public void onPlayerInteract(InventoryClickEvent e) {
        Player p= (Player) e.getWhoClicked();
        if(e.getCurrentItem()==null)return;

        if(e.getView().getTitle().equals("Grund Auswahl")){
            e.setCancelled(true);
            switch(e.getSlot()){
                case 11:
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Spezies"));
                    break;
                case 13:
                    p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Einstellungen"));
                    break;
                case 15:
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Passive"));
                    break;
                case 18:
                    p.openInventory(Bukkit.createInventory(p,54, "Admin Menu"));
                    break;
            }
        }

        if(e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Spezies")){

        }

        if(e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Einstellungen")){
            e.setCancelled(true);
            switch (e.getSlot()){
                case 12:
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf"));
                    break;
                case 14:
                    p.openInventory(Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"));
                    break;
                case 18:
                    p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl"));
                    break;
            }
        }

        if(e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Passive")){

        }

        if(e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Einstellungen > Kampf")){
            e.setCancelled(true);
            int wert = e.getAction().equals(InventoryAction.PICKUP_HALF) ? 10 : 1;
            switch (e.getSlot()){
                case 11:
                    utilitys.einstellungen.setSchadenimkampf(utilitys.einstellungen.getSchadenimkampf()-wert);
                    break;
                case 12:
                    utilitys.einstellungen.setSchadenimkampf(utilitys.einstellungen.getSchadenimkampf()+wert);
                    break;
                case 20:
                    utilitys.einstellungen.setSchadenohneausdauer(utilitys.einstellungen.getSchadenohneausdauer()-wert);
                    break;
                case 21:
                    utilitys.einstellungen.setSchadenohneausdauer(utilitys.einstellungen.getSchadenohneausdauer()+wert);
                    break;
                case 29:
                    utilitys.einstellungen.setRegenerationimkampf(utilitys.einstellungen.getRegenerationimkampf()-wert);
                    break;
                case 30:
                    utilitys.einstellungen.setRegenerationimkampf(utilitys.einstellungen.getRegenerationimkampf()+wert);
                    break;
                case 15:
                    utilitys.einstellungen.setKampfdauer(utilitys.einstellungen.getKampfdauer()-wert);
                    break;
                case 16:
                    utilitys.einstellungen.setKampfdauer(utilitys.einstellungen.getKampfdauer()+wert);
                    break;
                case 24:
                    utilitys.einstellungen.setSchadenfurkampfstart(utilitys.einstellungen.getSchadenfurkampfstart()-wert);
                    break;
                case 25:
                    utilitys.einstellungen.setSchadenfurkampfstart(utilitys.einstellungen.getSchadenfurkampfstart()+wert);
                    break;
                case 33:
                    utilitys.einstellungen.setStandartwaffenkosten(utilitys.einstellungen.getStandartwaffenkosten()-wert);
                    break;
                case 34:
                    utilitys.einstellungen.setStandartwaffenkosten(utilitys.einstellungen.getStandartwaffenkosten()+wert);
                    break;
                case 40:
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf > Waffen"));
                    return;
                case 45:
                    p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Einstellungen"));
                    return;
            }
            p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf"));
        }

        if(e.getView().getTitle().split(" : ")[0].equalsIgnoreCase("Grund Auswahl > Einstellungen > Kampf > Waffen")){
            e.setCancelled(true);

            double wert = e.getAction().equals(InventoryAction.PICKUP_HALF) ? 0.1 : 1.0;

            switch (e.getCurrentItem().getType()){
                case RED_CONCRETE:
                    utilitys.einstellungen.getWaffenschlagkosnte().put(e.getInventory().getItem(e.getSlot()-1).getType(),
                            utilitys.einstellungen.getWaffenschlagkosnte().get(e.getInventory().getItem(e.getSlot()-1).getType())-wert);
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf > Waffen : "+(Integer.valueOf(e.getInventory().getItem(51).getItemMeta().getDisplayName())-1)));
                    return;
                case GREEN_CONCRETE:
                    utilitys.einstellungen.getWaffenschlagkosnte().put(e.getInventory().getItem(e.getSlot()-2).getType(),
                            utilitys.einstellungen.getWaffenschlagkosnte().get(e.getInventory().getItem(e.getSlot()-2).getType())+wert);
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf > Waffen : "+(Integer.valueOf(e.getInventory().getItem(51).getItemMeta().getDisplayName())-1)));
                    return;
                case BARRIER:
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf"));
                    return;
                case ANVIL:
                    extras.openMaterialAnvilgui(p,
                            Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf > Waffen : "+(Integer.valueOf(e.getInventory().getItem(51).getItemMeta().getDisplayName())-1)),
                            "Grund Auswahl > Einstellungen > Kampf > Waffen > Hinzufügen","§oSUCHEN", mat -> utilitys.einstellungen.getWaffenschlagkosnte().put(mat,1.0));
                    break;
                case BOOK:
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf > Waffen"));
                    break;
                case ARROW:
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf > Waffen : "+e.getCurrentItem().getItemMeta().getDisplayName()));
                    break;
                default:
                    if(wert == 1)utilitys.einstellungen.getWaffenschlagkosnte().put(e.getCurrentItem().getType(),1.0);
                    else utilitys.einstellungen.getWaffenschlagkosnte().remove(e.getCurrentItem().getType());
                    p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Einstellungen > Kampf > Waffen : "+(Integer.valueOf(e.getInventory().getItem(51).getItemMeta().getDisplayName())-1)));
                    return;
            }
        }

        if(e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Einstellungen > Spieler")){
            e.setCancelled(true);
            switch (e.getSlot()){
                case 10:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Leben","Zahl",
                            sti -> utilitys.einstellungen.setLeben(Double.valueOf(sti)));
                    break;
                case 11:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Lebenreg","Zahl",
                            sti -> utilitys.einstellungen.setLebenreg(Double.valueOf(sti)));
                    break;
                case 19:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Ausdauer","Zahl",
                            sti -> utilitys.einstellungen.setAusdauer(Double.valueOf(sti)));
                    break;
                case 20:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Ausdauerreg","Zahl",
                            sti -> utilitys.einstellungen.setAusreg(Double.valueOf(sti)));
                    break;
                case 28:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Mana","Zahl",
                            sti -> utilitys.einstellungen.setMana(Double.valueOf(sti)));
                    break;
                case 29:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Manareg","Zahl",
                            sti -> utilitys.einstellungen.setManareg(Double.valueOf(sti)));
                    break;
                case 13:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffenschaden","Zahl",
                            sti -> utilitys.einstellungen.setWaffenschaden(Double.valueOf(sti)));
                    break;
                case 14:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffen-Geschwindigkeit","Zahl",
                            sti -> utilitys.einstellungen.setWaffengeschwindigkeit(Double.valueOf(sti)));
                    break;
                case 22:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffen-crit-dmg","Zahl",
                            sti -> utilitys.einstellungen.setWaffencritdmg(Double.valueOf(sti)));
                    break;
                case 23:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffen-crit-chance","Zahl",
                            sti -> utilitys.einstellungen.setWaffencritchance(Double.valueOf(sti)));
                    break;
                case 15:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeitsschaden","Zahl",
                            sti -> utilitys.einstellungen.setFahigkeitsdmg(Double.valueOf(sti)));
                    break;
                case 16:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeit-Geschwindigkeit","Zahl",
                            sti -> utilitys.einstellungen.setFahigkeitsgeschwindigkeit(Double.valueOf(sti)));
                    break;
                case 24:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeit-crit-dmg","Zahl",
                            sti -> utilitys.einstellungen.setFahigkeitscritdmg(Double.valueOf(sti)));
                    break;
                case 25:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeit-crit-chance","Zahl",
                            sti -> utilitys.einstellungen.setFahigkeitscritchance(Double.valueOf(sti)));
                    break;
                case 32:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Bewegungsgeschwindigkeit","Zahl",
                            sti -> utilitys.einstellungen.setBewegungsgeschwindigkeit(Double.valueOf(sti)));
                    break;
                case 33:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Skill-Slots","Zahl",
                            sti -> utilitys.einstellungen.setSkillslots(Integer.valueOf(sti)));
                    break;
                case 36:
                    p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Einstellungen"));
                    break;
            }

        }
    }
}
