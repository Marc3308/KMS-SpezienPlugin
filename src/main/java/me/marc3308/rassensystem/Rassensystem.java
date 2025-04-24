package me.marc3308.rassensystem;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Gui.guiclicker;
import me.marc3308.rassensystem.Gui.guiverteiler;
import me.marc3308.rassensystem.Gui.openeditorcommand;
import me.marc3308.rassensystem.command.getrasse;
import me.marc3308.rassensystem.command.rassencommand;
import me.marc3308.rassensystem.eventlisteners.Joinding;
import me.marc3308.rassensystem.eventlisteners.gui;
import me.marc3308.rassensystem.eventlisteners.howtorasse.itemclickevent;
import me.marc3308.rassensystem.eventlisteners.infight;
import me.marc3308.rassensystem.eventlisteners.ondamage;
import me.marc3308.rassensystem.passiven.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static me.marc3308.rassensystem.ItemCreater.getcon;

public final class Rassensystem extends JavaPlugin implements Listener {


    public static Rassensystem plugin;

    public static final HashMap<Player, Long> timermap=new HashMap<>();
    @Override
    public void onEnable() {

        //get the configs
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {


                File file = new File("plugins/KMS Plugins/Arbeitundleben","Skills.yml");
                FileConfiguration con= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(1,con);

                file = new File("plugins/KMS Plugins/Rassensystem","Rassen.yml");
                FileConfiguration con2= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(2,con2);

                file = new File("plugins/KMS Plugins/Klassensysteem","begleiterskilltree.yml");
                FileConfiguration con3= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(3,con3);

                file = new File("plugins/KMS Plugins/Klassensysteem","custemmodelds.yml");
                FileConfiguration con4= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(4,con4);

                file = new File("plugins/KMS Plugins/Klassensysteem","xp.yml");
                FileConfiguration con5= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(5,con5);


                file = new File("plugins/KMS Plugins/Klassensysteem","skilltree.yml");
                FileConfiguration con6= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(6,con6);


                file = new File("plugins/Arbeitundleben","Jobs.yml");
                FileConfiguration con7= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(7,con7);


                file = new File("plugins/Arbeitundleben","Clans.yml");
                FileConfiguration con8= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(8,con8);


                file = new File("plugins/KMS Plugins/Rassensystem","Rassenpassive.yml");
                FileConfiguration con9= YamlConfiguration.loadConfiguration(file);
                ItemCreater.conmap.put(9,con9);

            }
        },0,5*60*20);

        //loop the werte
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()){

                    //werte
                    int managrund=ItemCreater.getcon(2).getInt("Grundwerte"+".mana");
                    int lebengrund=ItemCreater.getcon(2).getInt("Grundwerte"+".leben");
                    int ausdauergrund=ItemCreater.getcon(2).getInt("Grundwerte"+".ausdauer");
                    double manareggrund=ItemCreater.getcon(2).getDouble("Grundwerte"+".manareg");
                    double lebenreggrund=ItemCreater.getcon(2).getDouble("Grundwerte"+".lebenreg");
                    double ausdauerreggrund=ItemCreater.getcon(2).getDouble("Grundwerte"+".ausdauerreg");

                    String num=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)==null ? "no"
                            : p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING);

                    double Ausdauerrasse=ItemCreater.getcon(2).getDouble(num+".ausdauer");
                    double Ausdauerregrasse=ItemCreater.getcon(2).getDouble(num+".ausdauerreg");
                    double Lebenrasse=ItemCreater.getcon(2).getDouble(num+".leben");
                    double Lebenregrasse=ItemCreater.getcon(2).getDouble(num+".lebenreg");
                    double Manarasse=ItemCreater.getcon(2).getDouble(num+".mana");
                    double Manaregrasse=ItemCreater.getcon(2).getDouble(num+".manareg");

                    //addables
                    int speedmult=100;
                    double pasivmana=0; // for later if i am funny
                    double pasivausdauer=0;
                    double passivleben=0;

                    //check if rasse has passive
                    for(int i=0;i<25;i++){
                        if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)break;
                        String passiv=getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1));
                        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), passiv), PersistentDataType.BOOLEAN)) {
                            switch (passiv){
                                case "nachtsicht":
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,20*15,0,false,false));
                                    break;
                                case "schnellerlaufen":
                                    if(p.isSprinting()){
                                        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "schnellerlaufennum"), PersistentDataType.INTEGER,
                                                p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "schnellerlaufennum"), PersistentDataType.INTEGER,0)+10);
                                        speedmult+=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "schnellerlaufennum"), PersistentDataType.INTEGER);
                                        if(speedmult>getcon(9).getInt("schnellerlaufen"+".Stärke"))speedmult=getcon(9).getInt("schnellerlaufen"+".Stärke");
                                    } else {
                                        p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "schnellerlaufennum"));
                                    }
                                    break;
                                case "fasterthenu":
                                    speedmult=getcon(9).getInt("fasterthenu"+".Stärke");
                                    break;
                                case "speedwennmehrlebenimumgreiß":
                                    for (Entity en : p.getNearbyEntities(getcon(9).getInt("speedwennmehrlebenimumgreiß"+".Radius"),
                                            getcon(9).getInt("speedwennmehrlebenimumgreiß"+".Radius"),getcon(9).getInt("speedwennmehrlebenimumgreiß"+".Radius"))){
                                        if(en instanceof Player && ((Player) en).getMaxHealth()>=(p.getMaxHealth()*1.25))speedmult=getcon(9).getInt("speedwennmehrlebenimumgreiß"+".Stärke");
                                    }
                                    break;
                                case "meditatin":
                                    pasivausdauer=((ausdauergrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauer"), PersistentDataType.DOUBLE)) * ((100 + Ausdauerrasse) / 100))
                                            *(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "passivmediataion"), PersistentDataType.INTEGER,0)/100.0);
                                    passivleben=p.getMaxHealth()*(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "passivmediataion"), PersistentDataType.INTEGER,0)/100.0);
                                    pasivmana = (managrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "mana"), PersistentDataType.DOUBLE)) * ((100 + Manarasse) / 100)
                                            *(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "passivmediataion"), PersistentDataType.INTEGER,0)/100.0);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    if(p.getHealth()<=0){
                        //check if player is turnd into a zomby
                        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"nekro"), PersistentDataType.BOOLEAN)
                                && p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"nekro"), PersistentDataType.BOOLEAN)){
                            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"nekro"), PersistentDataType.BOOLEAN,false);
                            ZombieVillager zom = (ZombieVillager) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE_VILLAGER);
                        }
                        return;
                    }

                    //crea oder keine seelenenergie mehr
                    if(p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)
                            || p.getPersistentDataContainer().getOrDefault(new NamespacedKey("klassensysteem", "seelenenergie"), PersistentDataType.INTEGER,100)<=0) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(""));
                        if(p.getGameMode().equals(GameMode.SURVIVAL)){
                            p.kickPlayer("Deine Seele ist aufgebraucht, mache bitte ein Ticket mit einem Neuen Charakter");
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "whitelist remove " + p.getName());
                            // Locate the player's .dat file
                            File datFile = new File(Bukkit.getWorlds().get(0).getWorldFolder(),
                                    "playerdata" + File.separator + p.getUniqueId() + ".dat");
                            File olddatFile = new File(Bukkit.getWorlds().get(0).getWorldFolder(),
                                    "playerdata" + File.separator + p.getUniqueId() + ".dat_old");
                            if (datFile.exists())datFile.delete();
                            if (olddatFile.exists())olddatFile.delete();
                        }
                    } else {

                        double MaxLeben = (lebengrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "leben"), PersistentDataType.DOUBLE)) * ((100 + Lebenrasse) / 100);
                        double Lebenreg = (MaxLeben/100.0)*(lebenreggrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "lebenreg"), PersistentDataType.DOUBLE)) * ((100 + Lebenregrasse) / 100);


                        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)){
                            Lebenreg-=(Lebenreg/100.0)*ItemCreater.getcon(2).getInt("Grundwerte"+".kampfregenerationsminus");
                        }

                        double Leben = p.getHealth() + Lebenreg + passivleben;
                        p.setMaxHealth(MaxLeben);
                        if (Leben > MaxLeben) Leben = MaxLeben;
                        if (MaxLeben > 20) {
                            p.setHealthScale(20);
                            p.setHealthScaled(true);
                        } else {
                            p.setHealthScaled(false);
                        }
                        p.setHealth(Leben);

                        double Ausdauemax = (ausdauergrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauer"), PersistentDataType.DOUBLE)) * ((100 + Ausdauerrasse) / 100);
                        double Ausdauerreg = (Ausdauemax/100.0)*(ausdauerreggrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauerreg"), PersistentDataType.DOUBLE)) * ((100 + Ausdauerregrasse) / 100);

                        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)){
                            Ausdauerreg-=(Ausdauerreg/100.0)*ItemCreater.getcon(2).getInt("Grundwerte"+".kampfregenerationsminus");
                        }

                        double Ausdauernow = p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE) + Ausdauerreg + pasivausdauer;
                        if (Ausdauernow > Ausdauemax) Ausdauernow = Ausdauemax;
                        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE, Ausdauernow);

                        double Manamax = (managrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "mana"), PersistentDataType.DOUBLE)) * ((100 + Manarasse) / 100);
                        double Manareg = (Manamax/100.0)*(manareggrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "manareg"), PersistentDataType.DOUBLE)) * ((100 + Manaregrasse) / 100);

                        if(p.getPersistentDataContainer().has(new NamespacedKey("arbeitundleben", "iscasting"), PersistentDataType.STRING))Manareg=0;
                        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)){
                            Manareg-=(Manareg/100.0)*ItemCreater.getcon(2).getInt("Grundwerte"+".kampfregenerationsminus");
                        }

                        double Mananow = p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "mananow"), PersistentDataType.DOUBLE) + Manareg + pasivmana;
                        if (Mananow > Manamax) Mananow = Manamax;
                        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "mananow"), PersistentDataType.DOUBLE, Mananow);

                        DecimalFormat smove = new DecimalFormat("#.##");

                        //test if it works
                        String lebenformat=Leben>(int) MaxLeben ? String.valueOf((int) MaxLeben) : smove.format(Leben);
                        String ausdauerformat=Ausdauernow>(int) Ausdauemax ? String.valueOf((int) Ausdauemax) : smove.format(Ausdauernow);
                        String manaformat=Mananow>(int) Manamax ? String.valueOf((int) Manamax) : smove.format(Mananow);

                        //custem anzeige
                        String werteanzeige = ItemCreater.getcon(4).getString("leben"+".Farbe") + "<"
                                + lebenformat + "/" + (int) MaxLeben + ">" + "   " + ItemCreater.getcon(4).getString("ausdauer"+".Farbe") + "<"
                                + ausdauerformat + "/" + (int) Ausdauemax + ">" + "   " + ItemCreater.getcon(4).getString("mana"+".Farbe")  + "<"
                                + manaformat + "/" + (int) Manamax + ">";

                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(werteanzeige));

                        //movement
                        double movmal = ItemCreater.getcon(2).getDouble("Grundwerte" + ".bewegungsgeschwindigkeit") + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "bewegungsgeschwindigkeit"), PersistentDataType.DOUBLE);

                        //standart geschwindikeit von minecraft
                        float standmove = 0.2f;
                        standmove *= (movmal / 100);
                        standmove *= (speedmult /100.0);
                        if (standmove > 1) standmove = 1;
                        p.setWalkSpeed(standmove);

                        //get attack speed
                        double standatackspeed = 4.0;
                        double atspeedmod = ItemCreater.getcon(2).getDouble("Grundwerte" + ".waffengeschwindigkeit") + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "waffengeschwindigkeit"), PersistentDataType.DOUBLE);
                        standatackspeed *= (atspeedmod / 100);
                        standatackspeed *=(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "amspeeden"), PersistentDataType.INTEGER,100)/100.0);

                        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(standatackspeed);

                        //check infight
                        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)){

                            BossBar bar= Bukkit.createBossBar("Im Kampf", BarColor.RED, BarStyle.SEGMENTED_10);
                            long timetillend=(ItemCreater.getcon(2).getInt("Grundwerte"+".kampfende")*1000);
                            long lashit=System.currentTimeMillis()-p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "lastdmghit"),PersistentDataType.LONG);
                            bar.setColor(lashit>=((timetillend/100.0)*66.0) ? BarColor.GREEN
                                    : lashit>=((timetillend/100.0)*33.0) ? BarColor.YELLOW
                                    : BarColor.RED);
                            bar.setProgress(lashit>=((timetillend/100.0)*90) ? 0.1
                                    : lashit>=((timetillend/100.0)*80) ? 0.2
                                    : lashit>=((timetillend/100.0)*70) ? 0.3
                                    : lashit>=((timetillend/100.0)*60) ? 0.4
                                    : lashit>=((timetillend/100.0)*50) ? 0.5
                                    : lashit>=((timetillend/100.0)*40) ? 0.6
                                    : lashit>=((timetillend/100.0)*30) ? 0.7
                                    : lashit>=((timetillend/100.0)*20) ? 0.8
                                    : lashit>=((timetillend/100.0)*10) ? 0.9
                                    : 1.0);
                            bar.addPlayer(p);
                            Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () -> bar.removeAll(), 20L);
                        }

                    }
                }

            }
        },0,20);


        // Plugin startup logic
        File file = new File("plugins/KMS Plugins/Rassensystem","Rassen.yml");
        FileConfiguration rasse= YamlConfiguration.loadConfiguration(file);

        plugin = this;

        Bukkit.getPluginManager().registerEvents(new gui(),this);
        Bukkit.getPluginManager().registerEvents(new infight(),this);
        Bukkit.getPluginManager().registerEvents(new Joinding(),this);
        Bukkit.getPluginManager().registerEvents(new ondamage(),this);
        Bukkit.getPluginManager().registerEvents(new itemclickevent(),this);
        Bukkit.getPluginManager().registerEvents(new ecxtrainv(),this);
        Bukkit.getPluginManager().registerEvents(new mobdinge(),this);
        Bukkit.getPluginManager().registerEvents(new meditation(),this);
        Bukkit.getPluginManager().registerEvents(new schaden(),this);
        Bukkit.getPluginManager().registerEvents(new onbewegen(),this);
        Bukkit.getPluginManager().registerEvents(new selbstsheren(),this);
        Bukkit.getPluginManager().registerEvents(new gerausche(),this);
        Bukkit.getPluginManager().registerEvents(new Hochelfen(),this);


        //rassenpassiven
        Bukkit.getPluginManager().registerEvents(new flugpassiven(),this);
        Bukkit.getPluginManager().registerEvents(new andersessen(),this);
        Bukkit.getPluginManager().registerEvents(new xpgain(),this);
        Bukkit.getPluginManager().registerEvents(new Luckeffeckt(),this);

        getCommand("spezienwahl").setExecutor(new rassencommand());
        getCommand("giverasse").setExecutor(new getrasse());


        //neu
        Bukkit.getPluginManager().registerEvents(new guiverteiler(),this);
        Bukkit.getPluginManager().registerEvents(new guiclicker(),this);
        getCommand("spezieseditor").setExecutor(new openeditorcommand());

        extras.adminlist.add("COMMAND_BLOCK;Grund Auswahl;27");
        extras.permisionlist.add("Spezienspezialist");

        //load the things
        utilitys.loadeinstellungen();

        //listen
        List<String> infos=new ArrayList<>();
        infos.add("This yml is for the rassen, there are two sections to this, the Grundwerte and the Rassen");
        infos.add("The Grundwerte are the stats every player has and begins with...");
        infos.add("The Rassen devines the stats of posible rasses");
        infos.add("For correckt use the Rassen: name, beschreibung, custemmoddeldata, leben, lebenreg, ausdauer, ausdauerreg, mana, manareg, resistzenzen, schwächen");
        infos.add("wichtig noch die grundwerte sind die grundwerte jedes spielers auf den dann die boni der klassen und rassen draufgerechnet werden");

        //pasivennamensliste
        if(rasse.get("Grundwerte")==null){

            rasse.set("Grundkosten"+".Schadenimkampf",90.0);
            rasse.set("Grundkosten"+".Schadenwennkeinausdauer",90.0);
            rasse.set("Grundkosten"+".DIAMOND_SWORD",1.0);

            rasse.set("Grundwerte"+".leben",20.0);
            rasse.set("Grundwerte"+".lebenreg",2.0);
            rasse.set("Grundwerte"+".ausdauer",20.0);
            rasse.set("Grundwerte"+".ausdauerreg",2.0);
            rasse.set("Grundwerte"+".mana",20.0);
            rasse.set("Grundwerte"+".manareg",2.0);

            rasse.set("Grundwerte"+".bewegungsgeschwindigkeit",100.0);

            rasse.set("Grundwerte"+".fahigkeitsschaden",100.0);
            rasse.set("Grundwerte"+".fahigkeitscritdmg",200.0);
            rasse.set("Grundwerte"+".fahigkeitscritchance",20.0);
            rasse.set("Grundwerte"+".fahigkeitsgeschwindigkeit",100.0);

            rasse.set("Grundwerte"+".waffenschaden",100.0);
            rasse.set("Grundwerte"+".waffencritdmg",200.0);
            rasse.set("Grundwerte"+".waffencritchance",20.0);
            rasse.set("Grundwerte"+".waffengeschwindigkeit",100.0);
            rasse.set("Grundwerte"+".skillslots",4);
            rasse.set("Grundwerte"+".kampfregenerationsminus",80);
            rasse.set("Grundwerte"+".kampfstartschaden",10);
            rasse.set("Grundwerte"+".kampfende",10);

            rasse.setComments("Grundwerte",infos);

            rasse.set("Mensch"+".name","Mensch");
            rasse.set("Mensch"+".beschreibung","Die Menschen sind menschlich");
            rasse.set("Mensch"+".custemmoddeldata",1);
            rasse.set("Mensch"+".leben",20.0);
            rasse.set("Mensch"+".lebenreg",0.2);
            rasse.set("Mensch"+".ausdauer",20.0);
            rasse.set("Mensch"+".ausdauerreg",0.2);
            rasse.set("Mensch"+".mana",20.0);
            rasse.set("Mensch"+".manareg",0.2);
            rasse.set("Mensch"+".resistzenzen"+".1","Feuer");
            rasse.set("Mensch"+".schwächen"+".1","Feuer");
            rasse.set("Mensch"+".passiven"+".1","nachtsicht");
            rasse.set("Mensch"+".passiven"+".2","highjump");


        }

        try {
            rasse.save(file);
        } catch (IOException i) {
            i.printStackTrace();
        }

        // Plugin startup logic
        file = new File("plugins/KMS Plugins/Rassensystem","Rassenpassive.yml");
        FileConfiguration passive= YamlConfiguration.loadConfiguration(file);

        if(passive.get("bienenchimarenflug")==null){

            passive.set("bienenchimarenflug"+".AnzeigeName","bienenflug");
            passive.set("bienenchimarenflug"+".Beschreibung","Toggelt das fliegen deiner rasse");
            passive.set("bienenchimarenflug"+".Kosten",2);
            passive.set("bienenchimarenflug"+".BoostKosten",4);
            passive.set("bienenchimarenflug"+".Stärke",5);
            passive.set("bienenchimarenflug"+".Block","PAPER");
            passive.set("bienenchimarenflug"+".Custemmoddeldatataken",5);
            passive.set("bienenchimarenflug"+".Farbe","§c");

            passive.set("papageichimarenflug"+".AnzeigeName","papageinflug");
            passive.set("papageichimarenflug"+".Beschreibung","Toggelt das fliegen deiner rasse");
            passive.set("papageichimarenflug"+".Kosten",2);
            passive.set("papageichimarenflug"+".BoostKosten",4);
            passive.set("papageichimarenflug"+".Stärke",5);
            passive.set("papageichimarenflug"+".Block","PAPER");
            passive.set("papageichimarenflug"+".Custemmoddeldatataken",5);
            passive.set("papageichimarenflug"+".Farbe","§c");

            passive.set("fledermauschimarenlug"+".AnzeigeName","fledermausflug");
            passive.set("fledermauschimarenlug"+".Beschreibung","Toggelt das fliegen deiner rasse");
            passive.set("fledermauschimarenlug"+".Kosten",2);
            passive.set("fledermauschimarenlug"+".BoostKosten",4);
            passive.set("fledermauschimarenlug"+".Stärke",5);
            passive.set("fledermauschimarenlug"+".Block","PAPER");
            passive.set("fledermauschimarenlug"+".Custemmoddeldatataken",5);
            passive.set("fledermauschimarenlug"+".Farbe","§c");

            passive.set("nachtsicht"+".AnzeigeName","Nachtsicht");
            passive.set("nachtsicht"+".Beschreibung","Toggelt das Nachtsicht deiner rasse");
            passive.set("nachtsicht"+".Block","PAPER");
            passive.set("nachtsicht"+".Custemmoddeldatataken",5);
            passive.set("nachtsicht"+".Farbe","§c");

            passive.set("bambusessen"+".AnzeigeName","Bambus Essen");
            passive.set("bambusessen"+".Beschreibung","Du schaffst es Bambus zu essen");
            passive.set("bambusessen"+".Essen",5);
            passive.set("bambusessen"+".Sättigung",5);
            passive.set("bambusessen"+".EffectStärke",5);
            passive.set("bambusessen"+".EffectDauer",5);
            passive.set("bambusessen"+".Block","PAPER");
            passive.set("bambusessen"+".Custemmoddeldatataken",5);
            passive.set("bambusessen"+".Farbe","§c");
            passive.set("bambusessen"+".NoDeaktivation",true);

            passive.set("seedessen"+".AnzeigeName","Seeds Essen");
            passive.set("seedessen"+".Beschreibung","Du schaffst es Seeds zu essen");
            passive.set("seedessen"+".Essen",5);
            passive.set("seedessen"+".Sättigung",5);
            passive.set("seedessen"+".Block","PAPER");
            passive.set("seedessen"+".Custemmoddeldatataken",5);
            passive.set("seedessen"+".Farbe","§c");

            passive.set("rotfesheesen"+".AnzeigeName","rotfesheesen Essen");
            passive.set("rotfesheesen"+".Beschreibung","Du schaffst es rotfesheesen zu essen");
            passive.set("rotfesheesen"+".Essen",5);
            passive.set("rotfesheesen"+".Sättigung",5);
            passive.set("rotfesheesen"+".Block","PAPER");
            passive.set("rotfesheesen"+".Custemmoddeldatataken",5);
            passive.set("rotfesheesen"+".Farbe","§c");

            passive.set("wiederkauer"+".AnzeigeName","wiederkauer");
            passive.set("wiederkauer"+".Beschreibung","Du schaffst es wiederkauer zu essen und zu scheißen");
            passive.set("wiederkauer"+".Essen",5);
            passive.set("wiederkauer"+".Sättigung",5);
            passive.set("wiederkauer"+".Dropchanche",50);
            passive.set("wiederkauer"+".Block","PAPER");
            passive.set("wiederkauer"+".Custemmoddeldatataken",5);
            passive.set("wiederkauer"+".WiedergekauterBlock","STONE");
            passive.set("wiederkauer"+".WiedergekauterName","WiederKauer");
            passive.set("wiederkauer"+".WiedergekauterCustemmoddeldatataken",123);
            passive.set("wiederkauer"+".WiedergekauterBeschreibung","JUM JUM");

            passive.set("kannfleisch"+".AnzeigeName","Nur Fleisch");
            passive.set("kannfleisch"+".Beschreibung","Du kannst nur Fleisch Essen");
            passive.set("kannfleisch"+".EffectStärke",5);
            passive.set("kannfleisch"+".EffectDauer",5);
            passive.set("kannfleisch"+".Block","PAPER");
            passive.set("kannfleisch"+".Custemmoddeldatataken",5);
            passive.set("kannfleisch"+".NoDeaktivation",true);

            passive.set("kannkeinfleisch"+".AnzeigeName","Kein Fleisch");
            passive.set("kannkeinfleisch"+".Beschreibung","Du kannst kein Fleisch essen");
            passive.set("kannkeinfleisch"+".EffectStärke",5);
            passive.set("kannkeinfleisch"+".EffectDauer",5);
            passive.set("kannkeinfleisch"+".Block","PAPER");
            passive.set("kannkeinfleisch"+".Custemmoddeldatataken",5);
            passive.set("kannkeinfleisch"+".NoDeaktivation",true);

            passive.set("ungebratenfleischwienormales"+".AnzeigeName","Lecker FrischFleisch");
            passive.set("ungebratenfleischwienormales"+".Beschreibung","Esse Ungebratenes Fleisch wie normales");
            passive.set("ungebratenfleischwienormales"+".Essen",5);
            passive.set("ungebratenfleischwienormales"+".Sättigung",5);
            passive.set("ungebratenfleischwienormales"+".Block","PAPER");
            passive.set("ungebratenfleischwienormales"+".Custemmoddeldatataken",5);

            passive.set("keinekekse"+".AnzeigeName","Kekse Find ich nicht so Toll");
            passive.set("keinekekse"+".Beschreibung","iss und stirb");
            passive.set("keinekekse"+".EffectStärke",5);
            passive.set("keinekekse"+".EffectDauer",5);
            passive.set("keinekekse"+".Block","PAPER");
            passive.set("keinekekse"+".Custemmoddeldatataken",5);

            passive.set("schnellerlaufen"+".AnzeigeName","schnellerlaufen");
            passive.set("schnellerlaufen"+".Beschreibung","Laufe Schneller je länger du sprintest");
            passive.set("schnellerlaufen"+".Stärke",200);
            passive.set("schnellerlaufen"+".Block","PAPER");
            passive.set("schnellerlaufen"+".Custemmoddeldatataken",5);

            passive.set("extrainv"+".AnzeigeName","Extra Inventar");
            passive.set("extrainv"+".Beschreibung","Sneaken und rechtsklick auf den Boden mit lehrer hand");
            passive.set("extrainv"+".Größe",9);
            passive.set("extrainv"+".Block","PAPER");
            passive.set("extrainv"+".Custemmoddeldatataken",5);

            passive.set("grassfresser"+".AnzeigeName","Grass Essen");
            passive.set("grassfresser"+".Beschreibung","Ich bin hey durch grünes zeug am Boden");
            passive.set("grassfresser"+".Essen",5);
            passive.set("grassfresser"+".Sättigung",5);
            passive.set("grassfresser"+".Block","PAPER");
            passive.set("grassfresser"+".Custemmoddeldatataken",5);

            passive.set("speedwennmehrlebenimumgreiß"+".AnzeigeName","Lebens Renner");
            passive.set("speedwennmehrlebenimumgreiß"+".Beschreibung","Mehr Leben wenn jemand mit mehr leben um dich");
            passive.set("speedwennmehrlebenimumgreiß"+".Stärke",120);
            passive.set("speedwennmehrlebenimumgreiß"+".Radius",10);
            passive.set("speedwennmehrlebenimumgreiß"+".Block","PAPER");
            passive.set("speedwennmehrlebenimumgreiß"+".Custemmoddeldatataken",5);

            passive.set("heilungdurchtot"+".AnzeigeName","Heil dich wenn was Tötest");
            passive.set("heilungdurchtot"+".Beschreibung","Ich töte sachen gerne");
            passive.set("heilungdurchtot"+".Stärke",110);
            passive.set("heilungdurchtot"+".Block","PAPER");
            passive.set("heilungdurchtot"+".Custemmoddeldatataken",5);

            passive.set("fasterthenu"+".AnzeigeName","Schneller Sprinter");
            passive.set("fasterthenu"+".Beschreibung","Du bist schneller als andere");
            passive.set("fasterthenu"+".Stärke",200);
            passive.set("fasterthenu"+".Block","PAPER");
            passive.set("fasterthenu"+".Custemmoddeldatataken",5);

            passive.set("wenigruckstos"+".AnzeigeName","Weniger Rückstos");
            passive.set("wenigruckstos"+".Beschreibung","Du bekommst weniger rückstos");
            passive.set("wenigruckstos"+".Stärke",50);
            passive.set("wenigruckstos"+".Block","PAPER");
            passive.set("wenigruckstos"+".Custemmoddeldatataken",5);

            passive.set("creepergoawai"+".AnzeigeName","Creeperspray");
            passive.set("creepergoawai"+".Beschreibung","Creepers meiden dich");
            passive.set("creepergoawai"+".Block","PAPER");
            passive.set("creepergoawai"+".Custemmoddeldatataken",5);

            passive.set("meditatin"+".AnzeigeName","Meditation");
            passive.set("meditatin"+".Beschreibung","Meditir mal");
            passive.set("meditatin"+".Stärke",20);
            passive.set("meditatin"+".Block","PAPER");
            passive.set("meditatin"+".Custemmoddeldatataken",5);

            passive.set("doubeljump"+".AnzeigeName","Jump");
            passive.set("doubeljump"+".Beschreibung","Meditir mal");
            passive.set("doubeljump"+".Stärke",20);
            passive.set("doubeljump"+".Kosten",3);
            passive.set("doubeljump"+".Block","PAPER");
            passive.set("doubeljump"+".Custemmoddeldatataken",5);

            passive.set("schweinemagen"+".AnzeigeName","schweinemagen Essen");
            passive.set("schweinemagen"+".Beschreibung","Ich bin hey schweinemagen grünes zeug am Boden");
            passive.set("schweinemagen"+".Essen",5);
            passive.set("schweinemagen"+".Sättigung",5);
            passive.set("schweinemagen"+".Block","PAPER");
            passive.set("schweinemagen"+".Custemmoddeldatataken",5);

            passive.set("slowfall"+".AnzeigeName","slowfall");
            passive.set("slowfall"+".Beschreibung","slowfall mal");
            passive.set("slowfall"+".Block","PAPER");
            passive.set("slowfall"+".Custemmoddeldatataken",5);

            passive.set("xporbgain"+".AnzeigeName","xporbgain");
            passive.set("xporbgain"+".Beschreibung","xporbgain mal");
            passive.set("xporbgain"+".Stärke",120);
            passive.set("xporbgain"+".Block","PAPER");
            passive.set("xporbgain"+".Custemmoddeldatataken",5);

            passive.set("skilltreexpgain"+".AnzeigeName","skilltreexpgain");
            passive.set("skilltreexpgain"+".Beschreibung","skilltreexpgain mal");
            passive.set("skilltreexpgain"+".Stärke",120);
            passive.set("skilltreexpgain"+".Block","PAPER");
            passive.set("skilltreexpgain"+".Custemmoddeldatataken",5);

            passive.set("nosliponice"+".AnzeigeName","nosliponice");
            passive.set("nosliponice"+".Beschreibung","nosliponice mal");
            passive.set("nosliponice"+".Block","PAPER");
            passive.set("nosliponice"+".Custemmoddeldatataken",5);

            passive.set("nopulverschneesinken"+".AnzeigeName","nopulverschneesinken");
            passive.set("nopulverschneesinken"+".Beschreibung","nopulverschneesinken mal");
            passive.set("nopulverschneesinken"+".Block","PAPER");
            passive.set("nopulverschneesinken"+".Custemmoddeldatataken",5);

            passive.set("selbstscheren"+".AnzeigeName","selbstscheren");
            passive.set("selbstscheren"+".Beschreibung","selbstscheren mal");
            passive.set("selbstscheren"+".Block","PAPER");
            passive.set("selbstscheren"+".Cooldown",10);
            passive.set("selbstscheren"+".Stärke",30);
            passive.set("selbstscheren"+".Custemmoddeldatataken",5);

            passive.set("highjump"+".AnzeigeName","highjump");
            passive.set("highjump"+".Beschreibung","highjump mal");
            passive.set("highjump"+".Block","PAPER");
            passive.set("highjump"+".Cooldown",10);
            passive.set("highjump"+".EffectStärke",5);
            passive.set("highjump"+".EffectDauer",5);
            passive.set("highjump"+".Custemmoddeldatataken",5);

            passive.set("wenigfalschaden"+".AnzeigeName","wenigfalschaden");
            passive.set("wenigfalschaden"+".Beschreibung","wenigfalschaden mal");
            passive.set("wenigfalschaden"+".Block","PAPER");
            passive.set("wenigfalschaden"+".Schadeninporzent",50);
            passive.set("wenigfalschaden"+".Custemmoddeldatataken",5);

            passive.set("wenigergift"+".AnzeigeName","wenigergift");
            passive.set("wenigergift"+".Beschreibung","wenigergift mal");
            passive.set("wenigergift"+".Block","PAPER");
            passive.set("wenigergift"+".Schadeninporzent",50);
            passive.set("wenigergift"+".Custemmoddeldatataken",5);

            passive.set("noabbausound"+".AnzeigeName","noabbausound");
            passive.set("noabbausound"+".Beschreibung","noabbausound mal");
            passive.set("noabbausound"+".Block","PAPER");
            passive.set("noabbausound"+".Custemmoddeldatataken",5);

            passive.set("noaufsammelsound"+".AnzeigeName","noaufsammelsound");
            passive.set("noaufsammelsound"+".Beschreibung","noaufsammelsound mal");
            passive.set("noaufsammelsound"+".Block","PAPER");
            passive.set("noaufsammelsound"+".Custemmoddeldatataken",5);

            passive.set("hochelfenunsichtbar"+".AnzeigeName","Hochelfenunsichtbar");
            passive.set("hochelfenunsichtbar"+".Beschreibung","Hochelfenunsichtbar mal");
            passive.set("hochelfenunsichtbar"+".Block","PAPER");
            passive.set("hochelfenunsichtbar"+".Kosten",3);
            passive.set("hochelfenunsichtbar"+".Custemmoddeldatataken",5);

            passive.set("hochelfenunsichtbar"+".AnzeigeName","Hochelfenunsichtbar");
            passive.set("hochelfenunsichtbar"+".Beschreibung","Hochelfenunsichtbar mal");
            passive.set("hochelfenunsichtbar"+".Block","PAPER");
            passive.set("hochelfenunsichtbar"+".Kosten",3);
            passive.set("hochelfenunsichtbar"+".Custemmoddeldatataken",5);

            passive.set("luckpassive"+".AnzeigeName","luckpassive");
            passive.set("luckpassive"+".Beschreibung","luckpassive mal");
            passive.set("luckpassive"+".Block","PAPER");
            passive.set("luckpassive"+".Kosten",3);
            passive.set("luckpassive"+".Ausweichchancheinprozent",66);
            passive.set("luckpassive"+".lootingstufe",10);
            passive.set("luckpassive"+".Custemmoddeldatataken",5);
        }

        try {
            passive.save(file);
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        System.out.println("Rassenplugin is workling finneee");

        //savesgrundeinstellungen
        utilitys.saveeinstellungen();

    }

    public static Rassensystem getPlugin() {
        return plugin;
    }

}
