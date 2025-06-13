package me.marc3308.rassensystem.Gui;

import me.marc3308.kMSCustemModels.KMSCustemModels;
import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.objekts.Passive;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.utilitys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class guiverteiler implements Listener {

    @EventHandler
    public void onopen(InventoryOpenEvent e) {

        Inventory inv = e.getInventory();

        //zurück button
        ItemStack back = new ItemStack(Material.BARRIER) {{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Zurück");
            meta.setLore(new ArrayList<String>() {{
                add("Hier klicken um zurück zu gehen");
            }});
            setItemMeta(meta);
        }};

        //start ding
        if(e.getView().getTitle().equalsIgnoreCase("Grund Auswahl")) {
            inv.setItem(11,new ItemStack(Material.ALLAY_SPAWN_EGG){{
                ItemMeta meta= getItemMeta();
                meta.setDisplayName(ChatColor.GREEN +"Spezies");
                meta.setLore(new ArrayList<String>(){{
                    add("Hier klicken um alle vorhandenen");
                    add("Spezies zu sehen, zu editieren oder");
                    add("zu neue zu erstellen/löschen");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(13,new ItemStack(Material.COMMAND_BLOCK){{
                ItemMeta meta= getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW+"Grund Einstellungen");
                meta.setLore(new ArrayList<String>(){{
                    add("Hier klicken um alle vorhandenen");
                    add("Einstellungen zu sehen und zu editieren");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(15,new ItemStack(Material.BOOK){{
                ItemMeta meta= getItemMeta();
                meta.setDisplayName(ChatColor.RED+"Passive Fähigkeiten");
                meta.setLore(new ArrayList<String>(){{
                    add("Hier klicken um alle vorhandenen");
                    add("passiven Fähigkeiten zu sehen und zu editieren");
                }});
                setItemMeta(meta);
            }});
            inv.setItem(18, back);
            return;
        }

        //durchforster
        if(e.getView().getTitle().split(" > ")[0].equalsIgnoreCase("Grund Auswahl")) {

            if (e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Einstellungen")){
                inv.setItem(12, new ItemStack(Material.DIAMOND_SWORD) {{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "Kampf grund werte");
                    meta.setLore(new ArrayList<String>() {{
                        add("Klick to Edit:");
                        add("§f§nSchaden im Kampf:§r         "+ utilitys.einstellungen.getSchadenimkampf()+"%  §f§nMax Kampfdauer:§r "+ utilitys.einstellungen.getKampfdauer()+"sec");
                        add("§f§nSchaden ohne Ausdauer:§r "+ utilitys.einstellungen.getSchadenohneausdauer()+"%  §f§nKo Zeit:§r "+ ((int) utilitys.einstellungen.getKozeit()/60)+":"+utilitys.einstellungen.getKozeit()%60+"sec");
                        add("§f§nRegeneration im Kampf:§r   "+ utilitys.einstellungen.getRegenerationimkampf()+"%  §f§nWaffenkosten:§r "+ utilitys.einstellungen.getStandartwaffenkosten());
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(14, new ItemStack(Material.ARMOR_STAND) {{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(ChatColor.YELLOW + "Spieler grund werte");
                    meta.setLore(new ArrayList<String>() {{
                        add("Klick to Edit:");
                        add("§cLeben:§r "+utilitys.einstellungen.getLeben()+"  "+"§6Ws:§r"+utilitys.einstellungen.getWaffenschaden());
                        add("§cLebenreg:§r "+utilitys.einstellungen.getLebenreg()+"  "+"§6Wg:§r"+utilitys.einstellungen.getWaffengeschwindigkeit());
                        add("§2Ausdauer:§r "+utilitys.einstellungen.getAusdauer()+"  "+"§6Wcd:§r"+utilitys.einstellungen.getWaffencritdmg());
                        add("§2Ausdauerreg:§r "+utilitys.einstellungen.getAusreg()+"  "+"§6Wcc:§r"+utilitys.einstellungen.getWaffencritchance());
                        add("§9Mana:§r "+utilitys.einstellungen.getMana()+"  "+"§5Fs:§r"+utilitys.einstellungen.getFahigkeitsdmg());
                        add("§9Manareg:§r "+utilitys.einstellungen.getManareg()+"  "+"§5Fg:§r"+utilitys.einstellungen.getFahigkeitsgeschwindigkeit());
                        add("§7Bewegungsgesch:§r "+utilitys.einstellungen.getBewegungsgeschwindigkeit()+"  "+"§5Fcd:§r"+utilitys.einstellungen.getFahigkeitscritdmg());
                        add("§bSkill-Slots:§r "+utilitys.einstellungen.getSkillslots()+"  "+"§5Fcc:§r"+utilitys.einstellungen.getFahigkeitscritchance());
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(18, back);
                return;
            }

            if (e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Einstellungen > Kampf")){

                inv.setItem(10,new ItemStack(Material.DIAMOND_SWORD) {{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§9§nSchadens Reduzierung im Kampf:§r "+ utilitys.einstellungen.getSchadenimkampf()+"%");
                    meta.setLore(new ArrayList<String>() {{
                        add("§9Der wert gibt an wie viel % schaden man");
                        add("§9im Kampf bekommt");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(11,getplusminusblock(false,true,true));
                inv.setItem(12,getplusminusblock(true,true,true));

                inv.setItem(19,new ItemStack(Material.WOODEN_SWORD){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§e§nSchaden ohne ausdauer:§r "+ utilitys.einstellungen.getSchadenohneausdauer()+"%");
                    meta.setLore(new ArrayList<String>() {{
                        add("§eDen Schaden den der spieler anrichtet");
                        add("§ewenn seine ausdauer lehr ist");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(20,getplusminusblock(false,true,true));
                inv.setItem(21,getplusminusblock(true,true,true));

                inv.setItem(28,new ItemStack(Material.HEART_POTTERY_SHERD){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§4§nRegeneration im Kampf:§r "+ utilitys.einstellungen.getRegenerationimkampf()+"%");
                    meta.setLore(new ArrayList<String>(){{
                        add("§4Die % an Regeneration die Spieler");
                        add("§4wärend eines kampfes bekommen");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(29,getplusminusblock(false,true,true));
                inv.setItem(30,getplusminusblock(true,true,true));



                inv.setItem(14,new ItemStack(Material.CLOCK){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§2§nKampfdauer:§r "+ utilitys.einstellungen.getKampfdauer()+"sec");
                    meta.setLore(new ArrayList<String>() {{
                        add("§2Die Zeit die der Spieler im Kampf ist");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(15,getplusminusblock(false,false,true));
                inv.setItem(16,getplusminusblock(true,false,true));

                inv.setItem(23,new ItemStack(Material.NETHER_STAR){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§7§nKo Zeit:§r "+ ((int) utilitys.einstellungen.getKozeit()/60)+":"+utilitys.einstellungen.getKozeit()%60+"sec");
                    meta.setLore(new ArrayList<String>(){{
                        add("§7Die Dauer die Ein Spieler Ko ist");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(24,getplusminusblock(false,false,true));
                inv.setItem(25,getplusminusblock(true,false,true));

                inv.setItem(32,new ItemStack(Material.WOODEN_AXE){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§6§nStandart Waffenkosten:§r "+ utilitys.einstellungen.getStandartwaffenkosten());
                    meta.setLore(new ArrayList<String>(){{
                        add("§6Die standart ausdauer die der Spieler");
                        add("§6verliert wenn er mit einer waffe");
                        add("§6angreift");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(33,getplusminusblock(false,false,true));
                inv.setItem(34,getplusminusblock(true,false,true));

                inv.setItem(40,new ItemStack(Material.DIAMOND_AXE){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§5§nWaffen Ausdauer Kosten");
                    meta.setLore(new ArrayList<String>(){{
                        add("Hier drücken für das Menü in dem man ");
                        add("§5die ausdauer die der Spieler");
                        add("§5verliert wenn er mit einer waffe");
                        add("§5angreift");
                    }});
                    setItemMeta(meta);
                }});

                inv.setItem(45, back);
                return;
            }

            if (e.getView().getTitle().split(" : ")[0].equalsIgnoreCase("Grund Auswahl > Einstellungen > Kampf > Waffen")){
                setStandartarray(inv,e.getView().getTitle().split(" : ").length==1 ? 1 : Integer.valueOf(e.getView().getTitle().split(" : ")[1]));

                //get all items allready existing
                ArrayList<Material> items = new ArrayList<Material>(utilitys.einstellungen.getWaffenschlagkosnte().keySet());

                for (int i=0;i<6;i++){
                    int num=(Integer.valueOf(inv.getItem(inv.getSize()-3).getItemMeta().getDisplayName())-2)*6;
                    int finalI = i;
                    int point = i==0 ? 10 : i==1 ? 14 : i==2 ? 19 : i==3 ? 23 : i==4 ? 28 : 32;

                    if(items.size()<=num+i)break;

                    inv.setItem(point,new ItemStack(items.get(num+finalI)){{
                        ItemMeta meta= getItemMeta();
                        meta.displayName(Component.translatable(items.get(num+finalI).getItemTranslationKey()).color(NamedTextColor.YELLOW).decorate(TextDecoration.UNDERLINED)
                                .append(Component.text(": " +new DecimalFormat("#.#").format(utilitys.einstellungen.getWaffenschlagkosnte().get(items.get(num+ finalI)))).color(NamedTextColor.WHITE).decoration(TextDecoration.UNDERLINED,false)));
                        meta.setLore(new ArrayList<String>(){{
                            add("§6Die ausdauer die der Spieler verliert");
                            add("§6wenn er mit dieser waffe angreift");
                            add("");
                            add("Lincksklick um den wert auf 1 zu setzen");
                            add("Rechtsklick um das Item zu enfernen");
                        }});
                        setItemMeta(meta);
                    }});
                    inv.setItem(point+1,getplusminusblock(false,false,false));
                    inv.setItem(point+2,getplusminusblock(true,false,false));
                }
                return;
            }

            if(e.getView().getTitle().split(" : ")[0].equalsIgnoreCase("Grund Auswahl > Spezies")){
                int Seitenzahl = e.getView().getTitle().split(" : ").length==1 ? 1 : Integer.valueOf(e.getView().getTitle().split(" : ")[1].split(" & ")[0]);
                setStandartarray(inv,Seitenzahl);

                ArrayList<Spezies> thislist= new ArrayList<Spezies>(utilitys.spezienliste.values());

                //filter system
                if(e.getView().getTitle().split(" & ").length==2){
                    String filter = e.getView().getTitle().split(" & ")[1];
                    thislist.sort(Comparator.comparingInt(pl -> getSimilarityScore(extras.getCustemModel(pl.getTicker()).getModelName() , filter)));
                }

                for(Spezies sp : thislist){
                    if (thislist.indexOf(sp) >= 44 * (Seitenzahl-1) && thislist.indexOf(sp) <= 44 * Seitenzahl) {
                        inv.setItem(inv.firstEmpty(), new ItemStack(extras.getCustemModel(sp.getTicker()).getModelBlock()){{
                            ItemMeta meta = getItemMeta();
                            meta.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING, sp.getErkennung());
                            meta.setDisplayName(extras.getCustemModel(sp.getTicker()).getModelName());
                            meta.setLore(new ArrayList<String>(extras.getCustemModel(sp.getTicker()).getModelBeschreibung()) {{
                                add("");
                                add("Ticker:§f "+sp.getTicker());
                                add("§cLeben:§f "+sp.getLeben()+"%   §cLebenreg:§f "+sp.getLebenreg()+"%");
                                add("§eAusdauer:§f "+sp.getAusdauer()+"%   §eAusdauerreg:§f "+sp.getAusreg()+"%");
                                add("§9Mana:§f "+sp.getMana()+"%   §9Manareg:§f "+sp.getManareg()+"%");
                                add("§6Größe:§f "+sp.getGrose());
                                add("Passiven:");
                                sp.getPassiven().forEach(pa -> add((sp.getPassiven().indexOf(pa)+1)+". "+extras.getCustemModel(utilitys.passiveliste.get(pa).getTicker()).getModelName()));
                                add("");
                                add("§eLinksklick um die Spezies zu editieren");
                                add("§cRechtsklick um die Spezies zu löschen");
                            }});
                            meta.setCustomModelData(extras.getCustemModel(sp.getTicker()).getModelData());
                            setItemMeta(meta);
                        }});
                    }
                }
                return;
            }

            //einzelne spezien
            if(e.getView().getTitle().split(" > ").length==3 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Spezies")
                    && utilitys.spezienliste.containsKey(e.getView().getTitle().split(" > ")[2])){
                Spezies sp = utilitys.spezienliste.get(e.getView().getTitle().split(" > ")[2]);
                for (int i=0; i<6;i++){
                    String tiker=i==0 ? "l" : i==1 ? "lr" : i==2 ? "a" : i==3 ? "ar" : i==4 ? "m" : "mr";
                    double wert = i==0 ? sp.getLeben() : i==1 ? sp.getLebenreg() : i==2 ? sp.getAusdauer() : i==3 ? sp.getAusreg() : i==4 ? sp.getMana() : sp.getManareg();
                    inv.setItem(i==0 ? 11 : i==1 ? 12 : i==2 ? 20 : i==3 ? 21 : i==4 ? 29 : 30,
                            new ItemStack(extras.getCustemModel(tiker).getModelBlock()){{
                                ItemMeta meta = getItemMeta();
                                meta.setDisplayName("§nSpezies "+extras.getCustemModel(tiker).getModelName()+":§r "+ (wert>0 ? "+"+new DecimalFormat("#.#").format(wert) : new DecimalFormat("#.#").format(wert))+"%");
                                meta.setCustomModelData(extras.getCustemModel(tiker).getModelData());
                                meta.setLore(new ArrayList<String>() {{
                                    add("§fDas Grund "+extras.getCustemModel(tiker).getModelName()+" des Spielers");
                                    add("");
                                    add("§eLinksklick um den wert zu verändern");
                                }});
                                setItemMeta(meta);
                            }});
                }

                inv.setItem(14,new ItemStack(extras.getCustemModel(sp.getTicker()).getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(extras.getCustemModel(sp.getTicker()).getModelName());
                    meta.setCustomModelData(extras.getCustemModel(sp.getTicker()).getModelData());
                    meta.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING, sp.getTicker());
                    meta.setLore(new ArrayList<String>(extras.getCustemModel(sp.getTicker()).getModelBeschreibung()) {{
                        add("Ticker:§f "+sp.getTicker());
                        add("");
                        add("§eLinksklick um Ticker zu editieren");
                        add("§aRechtsklick zum Ticker zu springen");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(23,new ItemStack(Material.ARMOR_STAND){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§6§nGröße:§r "+sp.getGrose());
                    meta.setLore(new ArrayList<String>() {{
                        add("§6Die Größe des Spielers");
                        add("");
                        add("§eLinksklick um die größe zu editieren");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(32,new ItemStack(extras.getCustemModel("pa").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName(extras.getCustemModel("pa").getModelName());
                    meta.setCustomModelData(extras.getCustemModel("pa").getModelData());
                    meta.setLore(new ArrayList<>(){{
                        add("Passiven:");
                        sp.getPassiven().forEach(pa -> add((sp.getPassiven().indexOf(pa)+1)+". "+extras.getCustemModel(utilitys.passiveliste.get(pa).getTicker()).getModelName()));
                        add("");
                        add("§eLinksklick um die passiven zu editieren");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(36,back);
            }

            if(e.getView().getTitle().split(" > ").length==4 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Spezies")
                    && utilitys.spezienliste.containsKey(e.getView().getTitle().split(" > ")[2])){
                Spezies sp = utilitys.spezienliste.get(e.getView().getTitle().split(" > ")[2]);
                int Seitenzahl = e.getView().getTitle().split(" : ").length==1 ? 1 : Integer.valueOf(e.getView().getTitle().split(" : ")[1].split(" & ")[0]);
                setStandartarray(inv,Seitenzahl);
                inv.setItem(inv.getSize()-7,new ItemStack(Material.AIR));

                ArrayList<Passive> thislist= new ArrayList<Passive>(utilitys.passiveliste.values());

                //filter system
                if(e.getView().getTitle().split(" & ").length==2){
                    String filter = e.getView().getTitle().split(" & ")[1];
                    thislist.sort(Comparator.comparingInt(pa -> getSimilarityScore(extras.getCustemModel(pa.getTicker()).getModelName() , filter)));
                }

                for(Passive pa : thislist){
                    if (thislist.indexOf(pa) >= 15 * (Seitenzahl-1) && thislist.indexOf(pa) <= 15 * Seitenzahl) {
                        inv.setItem((1+(thislist.indexOf(pa)*3)-(thislist.indexOf(pa)*(Seitenzahl-1))), new ItemStack(extras.getCustemModel(pa.getTicker()).getModelBlock()){{
                            ItemMeta meta = getItemMeta();
                            meta.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING, pa.getErkennung());
                            meta.setDisplayName(extras.getCustemModel(pa.getTicker()).getModelName());
                            meta.setLore(new ArrayList<String>(extras.getCustemModel(pa.getTicker()).getModelBeschreibung()) {{
                                add("");
                                add("Ticker:§f "+pa.getTicker());
                                add("Togglebar:§f "+(pa.getToggle() ? "§aJa" : "§cNein"));
                                add("Werte:");
                                pa.getWerte().forEach((s, i) -> add(s+": "+i));
                                add("");
                                add("§eLinksklick um zur Passiven zu springen");
                            }});
                            meta.setCustomModelData(extras.getCustemModel(pa.getTicker()).getModelData());
                            setItemMeta(meta);
                        }});
                        inv.setItem(2+(thislist.indexOf(pa)*3)-(thislist.indexOf(pa)*(Seitenzahl-1)),new ItemStack(sp.getPassiven().contains(pa.getErkennung()) ? Material.GREEN_CONCRETE : Material.RED_CONCRETE){{
                            ItemMeta meta = getItemMeta();
                            meta.setDisplayName((sp.getPassiven().contains(pa.getErkennung()) ? "§aHinzugefügt" : "§cEntfernt"));
                            meta.setLore(new ArrayList<String>() {{
                                add("§fHier klicken um die Passive hinzuzufügen/zu entfernen");
                            }});
                            setItemMeta(meta);
                        }});
                    }
                }
            }

            if(e.getView().getTitle().split(" : ")[0].equalsIgnoreCase("Grund Auswahl > Passive")){
                int Seitenzahl = e.getView().getTitle().split(" : ").length==1 ? 1 : Integer.valueOf(e.getView().getTitle().split(" : ")[1].split(" & ")[0]);
                setStandartarray(inv,Seitenzahl);
                inv.setItem(inv.getSize()-7,new ItemStack(Material.AIR));

                ArrayList<Passive> thislist= new ArrayList<Passive>(utilitys.passiveliste.values());

                //filter system
                if(e.getView().getTitle().split(" & ").length==2){
                    String filter = e.getView().getTitle().split(" & ")[1];
                    thislist.sort(Comparator.comparingInt(pa -> getSimilarityScore(extras.getCustemModel(pa.getTicker()).getModelName() , filter)));
                }

                for(Passive pa : thislist){
                    if (thislist.indexOf(pa) >= 44 * (Seitenzahl-1) && thislist.indexOf(pa) <= 44 * Seitenzahl) {
                        inv.setItem(inv.firstEmpty(), new ItemStack(extras.getCustemModel(pa.getTicker()).getModelBlock()){{
                            ItemMeta meta = getItemMeta();
                            meta.getPersistentDataContainer().set(new NamespacedKey(KMSCustemModels.getPlugin(),"kurzel"), PersistentDataType.STRING, pa.getErkennung());
                            meta.setDisplayName(extras.getCustemModel(pa.getTicker()).getModelName());
                            meta.setLore(new ArrayList<String>(extras.getCustemModel(pa.getTicker()).getModelBeschreibung()) {{
                                add("");
                                add("Ticker:§f "+pa.getTicker());
                                add("Togglebar:§f "+(pa.getToggle() ? "§aJa" : "§cNein"));
                                add("Werte:");
                                pa.getWerte().forEach((s, i) -> add(s+": "+i));
                                add("");
                                add("§eLinksklick um die Passive zu editieren");
                                add("§cRechtsklick um alles zurückzusetzen");
                            }});
                            meta.setCustomModelData(extras.getCustemModel(pa.getTicker()).getModelData());
                            setItemMeta(meta);
                        }});
                    }
                }
                return;
            }

            //einzelne passive
            if(e.getView().getTitle().split(" > ").length==3 && e.getView().getTitle().split(" > ")[1].equalsIgnoreCase("Passive")
                    && utilitys.passiveliste.containsKey(e.getView().getTitle().split(" > ")[2])){
                Passive pa = utilitys.passiveliste.get(e.getView().getTitle().split(" > ")[2]);
                pa.getWerte().forEach((s, i) ->{
                    inv.setItem(inv.firstEmpty(), new ItemStack(Material.YELLOW_CONCRETE){{
                        ItemMeta meta = getItemMeta();
                        meta.setDisplayName(s+":§f "+ new DecimalFormat("#.#").format(i));
                        meta.setLore(new ArrayList<String>() {{
                            add("");
                            add("§eLinksklick um den wert zu verändern");
                        }});
                        setItemMeta(meta);
                    }});
                });

                inv.setItem(20,new ItemStack(pa.getToggle() ? Material.GREEN_CONCRETE : Material.RED_CONCRETE){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§nPassive Togglebar:§r "+(pa.getToggle() ? "§aJa" : "§cNein"));
                    meta.setLore(new ArrayList<String>() {{
                        add("§fHier klicken um den passive toggeln zu ändern");
                    }});
                    setItemMeta(meta);
                }});

                inv.setItem(22,new ItemStack(extras.getCustemModel(pa.getTicker()).getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING, pa.getTicker());
                    meta.setDisplayName(extras.getCustemModel(pa.getTicker()).getModelName());
                    meta.setCustomModelData(extras.getCustemModel(pa.getTicker()).getModelData());
                    meta.setLore(new ArrayList<String>(extras.getCustemModel(pa.getTicker()).getModelBeschreibung()) {{
                        add("Ticker:§f "+pa.getTicker());
                        add("");
                        add("§eLinksklick um Ticker zu editieren");
                        add("§aRechtsklick zum Ticker zu springen");
                    }});
                    setItemMeta(meta);
                }});

                inv.setItem(18,back);
                return;
            }

            if (e.getView().getTitle().equalsIgnoreCase("Grund Auswahl > Einstellungen > Spieler")){

                //werte
                inv.setItem(10, new ItemStack(extras.getCustemModel("l").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§c§nSpieler Leben:§r "+ utilitys.einstellungen.getLeben());
                    meta.setCustomModelData(extras.getCustemModel("l").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§9Das Grund Leben des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(11, new ItemStack(extras.getCustemModel("lr").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§c§nSpieler Lebens-Regeneration:§r "+ utilitys.einstellungen.getLebenreg()+"%");
                    meta.setCustomModelData(extras.getCustemModel("lr").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§cDie Grund Lebenreg des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(19, new ItemStack(extras.getCustemModel("a").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§2§nSpieler Ausdauer:§r "+ utilitys.einstellungen.getAusdauer());
                    meta.setCustomModelData(extras.getCustemModel("a").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§2Die Grund Ausdauer des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(20, new ItemStack(extras.getCustemModel("ar").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§2§nSpieler Ausdauer-Regeneration:§r "+ utilitys.einstellungen.getAusreg()+"%");
                    meta.setCustomModelData(extras.getCustemModel("ar").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§2Die Grund Ausdauerreg des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                //sec reihe
                inv.setItem(28, new ItemStack(extras.getCustemModel("m").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§9§nSpieler Mana:§r "+ utilitys.einstellungen.getMana());
                    meta.setCustomModelData(extras.getCustemModel("m").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§9Das Grund Leben des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(29, new ItemStack(extras.getCustemModel("mr").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§9§nSpieler Mana-Regeneration:§r "+ utilitys.einstellungen.getManareg()+"%");
                    meta.setCustomModelData(extras.getCustemModel("mr").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§9Die Grund Manareg des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});

                //waffen
                inv.setItem(13, new ItemStack(extras.getCustemModel("ws").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§6§nSpieler Waffenschaden:§r "+ utilitys.einstellungen.getWaffenschaden()+"%");
                    meta.setCustomModelData(extras.getCustemModel("ws").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§6Der Grund Waffenschaden des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(14, new ItemStack(extras.getCustemModel("wg").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§6§nSpieler Waffen-Geschwindigkeit:§r "+ utilitys.einstellungen.getWaffengeschwindigkeit()+"%");
                    meta.setCustomModelData(extras.getCustemModel("wg").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§6Die Grund Waffen-Geschwindigkeit des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(22, new ItemStack(extras.getCustemModel("wcd").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§6§nSpieler Waffen-crit-dmg:§r "+ utilitys.einstellungen.getWaffencritdmg()+"%");
                    meta.setCustomModelData(extras.getCustemModel("wcd").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§6Der Grund Waffen-crit-dmg des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(23, new ItemStack(extras.getCustemModel("wcc").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§6§nSpieler Waffen-crit-chance:§r "+ utilitys.einstellungen.getWaffencritchance()+"%");
                    meta.setCustomModelData(extras.getCustemModel("wcc").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§6Die Grund Waffen-crit-chance des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});

                //fähigkeiten
                inv.setItem(15, new ItemStack(extras.getCustemModel("fs").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§5§nSpieler Fähigkeit-Schaden:§r "+ utilitys.einstellungen.getFahigkeitsdmg()+"%");
                    meta.setCustomModelData(extras.getCustemModel("fs").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§5Der Grund Fähigkeit-Schaden des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(16, new ItemStack(extras.getCustemModel("fg").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§5§nSpieler Fähigkeit-Geschwindigkeit:§r "+ utilitys.einstellungen.getFahigkeitsgeschwindigkeit()+"%");
                    meta.setCustomModelData(extras.getCustemModel("fg").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§5Die Grund Fähigkeit-Geschwindigkeit des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(24, new ItemStack(extras.getCustemModel("fcd").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§5§nSpieler Fähigkeit-crit-dmg:§r "+ utilitys.einstellungen.getFahigkeitscritdmg()+"%");
                    meta.setCustomModelData(extras.getCustemModel("fcd").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§5Der Grund Fähigkeit-crit-dmg des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(25, new ItemStack(extras.getCustemModel("fcc").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§5§nSpieler Fähigkeit-crit-chance:§r "+ utilitys.einstellungen.getFahigkeitscritchance()+"%");
                    meta.setCustomModelData(extras.getCustemModel("fcc").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§5Die Grund Fähigkeit-crit-chance des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});

                //zusatz
                inv.setItem(32, new ItemStack(extras.getCustemModel("bw").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§7§nSpieler Bewegungsgeschwindigkeit:§r "+ utilitys.einstellungen.getBewegungsgeschwindigkeit()+"%");
                    meta.setCustomModelData(extras.getCustemModel("bw").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§7Die Grund Bewegungsgeschwindigkeit des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});
                inv.setItem(33, new ItemStack(extras.getCustemModel("sk").getModelBlock()){{
                    ItemMeta meta = getItemMeta();
                    meta.setDisplayName("§b§nSpieler Skill-Slots:§r "+ utilitys.einstellungen.getSkillslots());
                    meta.setCustomModelData(extras.getCustemModel("sk").getModelData());
                    meta.setLore(new ArrayList<String>() {{
                        add("§bDie Grund Skill-Slots des Spielers");
                        add("");
                        add("§eLinksklick um den wert zu verändern");
                    }});
                    setItemMeta(meta);
                }});

                inv.setItem(36,back);
            }
            return;
        }
    }

    private ItemStack getplusminusblock(boolean isgreen, boolean isproz, boolean isfull){
        return new ItemStack(isgreen ? Material.GREEN_CONCRETE : Material.RED_CONCRETE){{
            ItemMeta meta= getItemMeta();
            meta.setDisplayName((isgreen ? "§a+" : "§c-") + (isfull ? "1 | 10" : " 1 | 0.1") + (isproz ? "%" : ""));
            meta.setLore(new ArrayList<String>(){{
                add((isgreen ? "§a" : "§c")+"Linksklick um den wert um 1"+ (isproz ? "%" : "") + (isproz ? " zu erhöhen" : " zu verringern"));
                add((isgreen ? "§a" : "§c")+"Rechtsklick um den wert um "+(isfull ? "10" : "0.1") + (isproz ? "%" : "") + (isproz ? " zu erhöhen" : " zu verringern"));
            }});
            setItemMeta(meta);
        }};
    }

    private void setStandartarray(Inventory inv,int Seite){
        //Back button
        inv.setItem(inv.getSize()-9,new ItemStack(Material.BARRIER) {{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Zurück");
            meta.setLore(new ArrayList<String>() {{
                add("Hier klicken um zurück zu gehen");
            }});
            setItemMeta(meta);
        }});

        inv.setItem(inv.getSize()-7,new ItemStack(Material.ANVIL){{
            ItemMeta meta= getItemMeta();
            meta.setDisplayName(ChatColor.GREEN+"Hinzufügen");
            meta.setLore(new ArrayList<String>(){{
                add("Hier klicken um ein Item hinzuzufügen");
            }});
            setItemMeta(meta);
        }});

        inv.setItem(inv.getSize()-5,new ItemStack(Material.BOOK){{
            ItemMeta meta= getItemMeta();
            meta.setDisplayName(ChatColor.GRAY+"Seite: 1");
            setItemMeta(meta);
        }});

        inv.setItem(inv.getSize()-3,new ItemStack(Material.ARROW){{
            ItemMeta meta= getItemMeta();
            meta.setDisplayName(String.valueOf(Seite+1));
            setItemMeta(meta);
        }});

        inv.setItem(inv.getSize()-1,new ItemStack(Material.NAME_TAG){{
            ItemMeta meta= getItemMeta();
            meta.setDisplayName(ChatColor.GREEN+"Filter");
            meta.setLore(new ArrayList<String>(){{
                add("Hier klicken um nach einem Begriff zu Filtern");
            }});
            setItemMeta(meta);
        }});

    }

    private static int getSimilarityScore(String name, String target) {
        // Lower score = better match
        name = name.toLowerCase();
        target = target.toLowerCase();

        if (name.equals(target)) return 0; // perfect match
        if (name.startsWith(target)) return 1;
        if (name.contains(target)) return 2;

        // fallback: sort alphabetically (or by edit distance if you want more accuracy)
        return 3;
    }

}
