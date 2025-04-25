package me.marc3308.rassensystem.Gui;

import me.marc3308.kMSCustemModels.KMSCustemModels;
import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

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

        if(e.getView().getTitle().split(" : ")[0].equalsIgnoreCase("Grund Auswahl > Spezies")){
            e.setCancelled(true);
            standartinv(p,e,Bukkit.createInventory(p,27, "Grund Auswahl"),
                    () -> {
                extras.openMaterialAnvilgui(p,
                        Bukkit.createInventory(p,54, e.getView().getTitle()),
                        "Grund Auswahl > Spezies > Hinzufügen","§oMaterial", mat -> {
                            while (true){
                                String tik= extras.randomLetters(6);
                                if(!utilitys.spezienliste.stream().filter(sp -> sp.getErkennung().equals(tik)).findAny().isPresent()){
                                    utilitys.spezienliste.add(new Spezies(tik,tik,0.0,0.0,0.0,0.0,0.0,0.0,new ArrayList<String>()));
                                    break;
                                }
                            }
                        });
            },() -> {
                if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                    p.openInventory(Bukkit.createInventory(p,45, "Grund Auswahl > Spezies > "
                            +e.getCurrentItem().getPersistentDataContainer().get(new NamespacedKey(KMSCustemModels.getPlugin(),"kurzel"), PersistentDataType.STRING)));
                } else if(e.getAction().equals(InventoryAction.PICKUP_HALF)){
                    utilitys.spezienliste.stream().filter(sp ->
                            sp.getErkennung().equals(e.getCurrentItem().getPersistentDataContainer()
                                    .get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING)))
                            .findFirst().ifPresent(sp ->{
                                utilitys.spezienliste.remove(sp);
                                p.openInventory(Bukkit.createInventory(p,54, e.getView().getTitle()));
                            });
                }
            });
        }

        if(e.getView().getTitle().split(" > ").length==3 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Spezies")
                && utilitys.spezienliste.stream().filter(sp -> sp.getErkennung().equalsIgnoreCase(e.getView().getTitle().split(" > ")[2])).findFirst().isPresent()){
            e.setCancelled(true);
            utilitys.spezienliste.stream().filter(sp -> sp.getErkennung().equalsIgnoreCase(e.getView().getTitle().split(" > ")[2])).findFirst().ifPresent(sp -> {
                switch (e.getSlot()) {
                    case 36:
                        p.openInventory(Bukkit.createInventory(p, 54, "Grund Auswahl > Spezies"));
                        return;
                    case 32:
                        p.openInventory(Bukkit.createInventory(p, 54, e.getView().getTitle() + " > " + "Passiven"));
                        return;
                    default:
                        int i = e.getSlot();
                        String tiker = i == 11 ? "l" : i == 12 ? "lr" : i == 20 ? "a" : i == 21 ? "ar" : i == 29 ? "m" : i==30 ? "mr" : "tk";
                        double wert = i == 11 ? sp.getLeben() : i == 12 ? sp.getLebenreg() : i == 20 ? sp.getAusdauer() : i == 21 ? sp.getAusreg() : i == 29 ? sp.getMana() : sp.getManareg();

                        if(tiker.equals("tk") && e.getAction().equals(InventoryAction.PICKUP_HALF)){
                            p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Passive > "+sp.getTicker()));
                            return;
                        }

                        extras.openStringAnvilgui(p, Bukkit.createInventory(p, 45, e.getView().getTitle()),
                                e.getView().getTitle() + " > " + (tiker.equals("tk") ? "Ticker": extras.getCustemModel(tiker).getModelName()), (tiker.equals("tk") ? sp.getTicker(): String.valueOf(wert)), sti -> {
                                    switch (tiker) {
                                        case "l":
                                            sp.setLeben(Double.valueOf(sti));
                                            break;
                                        case "lr":
                                            sp.setLebenreg(Double.valueOf(sti));
                                            break;
                                        case "a":
                                            sp.setAusdauer(Double.valueOf(sti));
                                            break;
                                        case "ar":
                                            sp.setAusreg(Double.valueOf(sti));
                                            break;
                                        case "m":
                                            sp.setMana(Double.valueOf(sti));
                                            break;
                                        case "mr":
                                            sp.setManareg(Double.valueOf(sti));
                                            break;
                                        case "tk":
                                            sp.setTicker(sti);
                                            break;
                                    }
                                });
                        return;
                }
            });
        }

        if(e.getView().getTitle().split(" > ").length==4 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Spezies")
                && utilitys.spezienliste.stream().filter(sp -> sp.getErkennung().equalsIgnoreCase(e.getView().getTitle().split(" > ")[2])).findFirst().isPresent()){
            e.setCancelled(true);
            utilitys.spezienliste.stream().filter(sp -> sp.getErkennung().equalsIgnoreCase(e.getView().getTitle().split(" > ")[2])).findFirst().ifPresent(sp -> {
               standartinv(p,e,Bukkit.createInventory(p,54, "Grund Auswahl > Spezies"),() -> {},() -> {
                    switch (e.getCurrentItem().getType()){
                        case RED_CONCRETE:
                            sp.getPassiven().add(e.getInventory().getItem(e.getSlot()-1).getPersistentDataContainer()
                                    .get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING));
                            p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(),e.getView().getTitle()));
                            break;
                        case GREEN_CONCRETE:
                            sp.getPassiven().remove(e.getInventory().getItem(e.getSlot()-1).getPersistentDataContainer()
                                    .get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING));
                            p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(),e.getView().getTitle()));
                            break;
                        default:
                            if(e.getCurrentItem().getPersistentDataContainer()
                                    .get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING)!=null);
                            p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Passive > "
                                    +e.getCurrentItem().getPersistentDataContainer()
                                    .get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING)));
                            break;
                    }
               });
            });
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

        if(e.getView().getTitle().split(" : ")[0].equalsIgnoreCase("Grund Auswahl > Passive")){
            e.setCancelled(true);
            standartinv(p,e,Bukkit.createInventory(p,27, "Grund Auswahl"),
                    () -> {},() -> {
                        if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                            p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Passive > "
                                    +e.getCurrentItem().getPersistentDataContainer().get(new NamespacedKey(KMSCustemModels.getPlugin(),"kurzel"), PersistentDataType.STRING)));
                        } else if(e.getAction().equals(InventoryAction.PICKUP_HALF)){
                            utilitys.passiveliste.stream().filter(ps -> ps.getErkennung().equals(e.getCurrentItem().getPersistentDataContainer()
                                    .get(new NamespacedKey(KMSCustemModels.getPlugin(),"kurzel"), PersistentDataType.STRING))).findFirst().ifPresent(sp -> {
                                utilitys.passiveliste.remove(sp);
                                utilitys.savepassive();
                                Bukkit.getScheduler().scheduleSyncDelayedTask(KMSCustemModels.getPlugin(),()->{
                                    utilitys.loadpassive();
                                    p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(), e.getView().getTitle()));
                                },5L);
                            });
                        }
                    });
        }

        //einzelne passive
        if(e.getView().getTitle().split(" > ").length==3 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Passive")
                && utilitys.passiveliste.stream().filter(sp -> sp.getErkennung().equalsIgnoreCase(e.getView().getTitle().split(" > ")[2])).findFirst().isPresent()){
            utilitys.passiveliste.stream().filter(pa -> pa.getErkennung().equals(e.getView().getTitle().split(" > ")[2])).findFirst().ifPresent(pa -> {
                e.setCancelled(true);
                switch (e.getSlot()){
                    case 18:
                        p.openInventory(Bukkit.createInventory(p,54, "Grund Auswahl > Passive"));
                        return;
                    case 20:
                        pa.setToggle(!pa.getToggle());
                        p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(), e.getView().getTitle()));
                        return;
                    case 22:
                        if(e.getAction().equals(InventoryAction.PICKUP_HALF)){
                            p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Passive > "+pa.getTicker()));
                            return;
                        } else {
                            extras.openStringAnvilgui(p, Bukkit.createInventory(p, e.getInventory().getSize(), e.getView().getTitle()),
                                    e.getView().getTitle() + " > " + "Ticker", pa.getTicker(), sti -> {
                                        pa.setTicker(sti);
                                    });
                        }
                        break;
                    default:
                        extras.openStringAnvilgui(p, Bukkit.createInventory(p, e.getInventory().getSize(), e.getView().getTitle()),
                                e.getView().getTitle() + " > " + e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0], String.valueOf(pa.getPassive().get(e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0])),
                                sti -> {
                                    pa.getPassive().put(e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0], Integer.valueOf(sti));
                                });
                        break;
                }
            });
            return;
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
                            "Grund Auswahl > Einstellungen > Spieler > Leben",String.valueOf(utilitys.einstellungen.getLeben()),
                            sti -> utilitys.einstellungen.setLeben(Double.valueOf(sti)));
                    break;
                case 11:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Lebenreg",String.valueOf(utilitys.einstellungen.getLebenreg()),
                            sti -> utilitys.einstellungen.setLebenreg(Double.valueOf(sti)));
                    break;
                case 19:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Ausdauer",String.valueOf(utilitys.einstellungen.getAusdauer()),
                            sti -> utilitys.einstellungen.setAusdauer(Double.valueOf(sti)));
                    break;
                case 20:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Ausdauerreg",String.valueOf(utilitys.einstellungen.getAusreg()),
                            sti -> utilitys.einstellungen.setAusreg(Double.valueOf(sti)));
                    break;
                case 28:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Mana",String.valueOf(utilitys.einstellungen.getMana()),
                            sti -> utilitys.einstellungen.setMana(Double.valueOf(sti)));
                    break;
                case 29:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Manareg",String.valueOf(utilitys.einstellungen.getManareg()),
                            sti -> utilitys.einstellungen.setManareg(Double.valueOf(sti)));
                    break;
                case 13:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffenschaden",String.valueOf(utilitys.einstellungen.getWaffenschaden()),
                            sti -> utilitys.einstellungen.setWaffenschaden(Double.valueOf(sti)));
                    break;
                case 14:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffen-Geschwindigkeit",String.valueOf(utilitys.einstellungen.getWaffengeschwindigkeit()),
                            sti -> utilitys.einstellungen.setWaffengeschwindigkeit(Double.valueOf(sti)));
                    break;
                case 22:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffen-crit-dmg",String.valueOf(utilitys.einstellungen.getWaffencritdmg()),
                            sti -> utilitys.einstellungen.setWaffencritdmg(Double.valueOf(sti)));
                    break;
                case 23:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Waffen-crit-chance",String.valueOf(utilitys.einstellungen.getWaffencritchance()),
                            sti -> utilitys.einstellungen.setWaffencritchance(Double.valueOf(sti)));
                    break;
                case 15:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeitsschaden",String.valueOf(utilitys.einstellungen.getFahigkeitsdmg()),
                            sti -> utilitys.einstellungen.setFahigkeitsdmg(Double.valueOf(sti)));
                    break;
                case 16:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeit-Geschwindigkeit",String.valueOf(utilitys.einstellungen.getFahigkeitsgeschwindigkeit()),
                            sti -> utilitys.einstellungen.setFahigkeitsgeschwindigkeit(Double.valueOf(sti)));
                    break;
                case 24:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeit-crit-dmg",String.valueOf(utilitys.einstellungen.getFahigkeitscritdmg()),
                            sti -> utilitys.einstellungen.setFahigkeitscritdmg(Double.valueOf(sti)));
                    break;
                case 25:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Fähigkeit-crit-chance",String.valueOf(utilitys.einstellungen.getWaffencritchance()),
                            sti -> utilitys.einstellungen.setFahigkeitscritchance(Double.valueOf(sti)));
                    break;
                case 32:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Bewegungsgeschwindigkeit",String.valueOf(utilitys.einstellungen.getBewegungsgeschwindigkeit()),
                            sti -> utilitys.einstellungen.setBewegungsgeschwindigkeit(Double.valueOf(sti)));
                    break;
                case 33:
                    extras.openStringAnvilgui(p,
                            Bukkit.createInventory(p,45, "Grund Auswahl > Einstellungen > Spieler"),
                            "Grund Auswahl > Einstellungen > Spieler > Skill-Slots",String.valueOf(utilitys.einstellungen.getSkillslots()),
                            sti -> utilitys.einstellungen.setSkillslots(Integer.valueOf(sti)));
                    break;
                case 36:
                    p.openInventory(Bukkit.createInventory(p,27, "Grund Auswahl > Einstellungen"));
                    break;
            }

        }
    }

    private void standartinv(Player p, InventoryClickEvent e, Inventory backinv, Runnable anvil, Runnable clickitem) {
        if(e.getInventory().getSize()-9==e.getSlot()){ //back
            p.openInventory(backinv);
        } else if(e.getInventory().getSize()-7==e.getSlot()){ //amboss
            anvil.run();
        } else if(e.getInventory().getSize()-5==e.getSlot()){ //book
            p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(), e.getView().getTitle().split(" : ")[0]));
        } else if(e.getInventory().getSize()-3==e.getSlot()){ //arrow
            p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(), e.getView().getTitle().split(" : ")[0]+" : "
                    +(e.getView().getTitle().split(" & ").length==2
                    ? e.getCurrentItem().getItemMeta().getDisplayName()+" & "+e.getView().getTitle().split(" & ")[1]
                    : e.getCurrentItem().getItemMeta().getDisplayName())));
        } else if(e.getInventory().getSize()-1==e.getSlot()){ //nametak
            extras.openStringAnvilgui(p,e.getInventory(),e.getView().getTitle()+" > Filter","Filter",
                    stri -> {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Rassensystem.getPlugin(), () -> {
                            p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(),e.getView().getTitle().split(" : ")[0]+" : 1 & "+stri));
                        },5L);
                    });
        } else { //item
            clickitem.run();
        }
    }
}
