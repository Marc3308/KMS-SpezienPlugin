package me.marc3308.rassensystem.Gui;

import me.marc3308.kMSCustemModels.KMSCustemModels;
import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.objekts.Passive;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

import static me.marc3308.rassensystem.utilitys.addpassivroster;

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
                                if(!utilitys.spezienliste.containsKey(tik)){
                                    utilitys.spezienliste.put(tik,new Spezies(tik,tik,0.0,0.0,0.0,0.0,0.0,0.0,1.0,new ArrayList<String>()));
                                    break;
                                }
                            }
                        });
            },() -> {
                if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                    p.openInventory(Bukkit.createInventory(p,45, "Grund Auswahl > Spezies > "
                            +e.getCurrentItem().getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING)));
                } else if(e.getAction().equals(InventoryAction.PICKUP_HALF)){
                    utilitys.spezienliste.remove(e.getCurrentItem().getPersistentDataContainer()
                            .get(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING));
                    p.openInventory(Bukkit.createInventory(p,54, e.getView().getTitle()));
                }
            });
        }

        if(e.getView().getTitle().split(" > ").length==3 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Spezies")
                && utilitys.spezienliste.containsKey(e.getView().getTitle().split(" > ")[2])){
            e.setCancelled(true);
            Spezies sp = utilitys.spezienliste.get(e.getView().getTitle().split(" > ")[2]);
            switch (e.getSlot()) {
                case 36:
                    p.openInventory(Bukkit.createInventory(p, 54, "Grund Auswahl > Spezies"));
                    return;
                case 32:
                    p.openInventory(Bukkit.createInventory(p, 54, e.getView().getTitle() + " > " + "Passiven"));
                    return;
                case 23:
                    extras.openStringAnvilgui(p,e.getInventory(),
                            e.getView().getTitle() + " > Größe", String.valueOf(sp.getGrose()), sti -> {
                                sp.setGrose(Double.parseDouble(sti));
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Rassensystem.getPlugin(), () -> {
                                    p.openInventory(e.getInventory());
                                }, 5L);
                            });
                    break;
                default:
                    int i = e.getSlot();
                    String tiker = i == 11 ? "l" : i == 12 ? "lr" : i == 20 ? "a" : i == 21 ? "ar" : i == 29 ? "m" : i==30 ? "mr" : "tk";
                    double wert = i == 11 ? sp.getLeben() : i == 12 ? sp.getLebenreg() : i == 20 ? sp.getAusdauer() : i == 21 ? sp.getAusreg() : i == 29 ? sp.getMana() : sp.getManareg();

                    if(tiker.equals("tk") && e.getAction().equals(InventoryAction.PICKUP_HALF)){
                        p.openInventory(Bukkit.createInventory(p,45, "CustemModel Editor > "+sp.getTicker()));
                        return;
                    }

                    extras.openStringAnvilgui(p, e.getInventory(),
                            e.getView().getTitle() + " > " + (tiker.equals("tk") ? "Ticker": extras.getCustemModel(tiker).getModelName()), (tiker.equals("tk") ? sp.getTicker(): ""), sti -> {
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
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Rassensystem.getPlugin(), () -> {
                                    p.openInventory(e.getInventory());
                                }, 5L);
                            });
                    break;
            }
        }

        if(e.getView().getTitle().split(" > ").length==4 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Spezies")
                && utilitys.spezienliste.containsKey(e.getView().getTitle().split(" > ")[2])){
            e.setCancelled(true);
            Spezies sp = utilitys.spezienliste.get(e.getView().getTitle().split(" > ")[2]);
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
                            utilitys.passiveliste.remove(e.getCurrentItem().getPersistentDataContainer()
                                    .get(new NamespacedKey(KMSCustemModels.getPlugin(),"kurzel"), PersistentDataType.STRING));
                            addpassivroster();
                            p.openInventory(Bukkit.createInventory(p,e.getInventory().getSize(), e.getView().getTitle()));
                        }
                    });
        }

        //einzelne passive
        if(e.getView().getTitle().split(" > ").length==3 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Passive")
                && utilitys.passiveliste.containsKey(e.getView().getTitle().split(" > ")[2])){
            Passive pa = utilitys.passiveliste.get(e.getView().getTitle().split(" > ")[2]);
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
                        p.openInventory(Bukkit.createInventory(p,27, "CustemModel Editor > "+pa.getTicker()));
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
                            e.getView().getTitle() + " > " + e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0], String.valueOf(pa.getWerte().get(e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0])),
                            sti -> {
                                pa.getWerte().put(e.getCurrentItem().getItemMeta().getDisplayName().split(":")[0], Integer.valueOf(sti));
                            });
                    break;
            }
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
                    utilitys.einstellungen.setKozeit(utilitys.einstellungen.getKozeit()-wert);
                    break;
                case 25:
                    utilitys.einstellungen.setKozeit(utilitys.einstellungen.getKozeit()+wert);
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
