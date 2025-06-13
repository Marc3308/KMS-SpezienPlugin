package me.marc3308.rassensystem;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Gui.guiclicker;
import me.marc3308.rassensystem.Gui.guiverteiler;
import me.marc3308.rassensystem.Gui.openeditorcommand;
import me.marc3308.rassensystem.command.getrasse;
import me.marc3308.rassensystem.eventlisteners.howtorasse.dringraseposion;
import me.marc3308.rassensystem.kampfsystem.KO;
import me.marc3308.rassensystem.kampfsystem.Kampf;
import me.marc3308.rassensystem.neuepassive.flug;
import me.marc3308.rassensystem.objekts.Passive;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.passiven.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

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
                //alle spieler durchgehen
                Bukkit.getOnlinePlayers().forEach(p -> {

                    //crea oder keine seelenenergie mehr
                    if(p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)
                            || p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "seelenenergie"), PersistentDataType.INTEGER,100)<=0) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(""));
                        if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                            p.kickPlayer("Deine Seele ist aufgebraucht, mache bitte ein Ticket mit einem Neuen Charakter");
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "whitelist remove " + p.getName());
                            // Locate the player's .dat file
                            File datFile = new File(Bukkit.getWorlds().get(0).getWorldFolder(),
                                    "playerdata" + File.separator + p.getUniqueId() + ".dat");
                            File olddatFile = new File(Bukkit.getWorlds().get(0).getWorldFolder(),
                                    "playerdata" + File.separator + p.getUniqueId() + ".dat_old");
                            if (datFile.exists()) datFile.delete();
                            if (olddatFile.exists()) olddatFile.delete();
                        }
                    } else if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)){
                        int wert = p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE,20*3,20,false,false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*3,20,false,false));
                        //chanche hight
                        ArmorStand ar=p.getWorld().spawn(p.getLocation().add(0,1,0),ArmorStand.class);
                        ar.setInvulnerable(true);
                        ar.setCustomName("KO");
                        ar.setCustomNameVisible(true);
                        ar.setGravity(false);
                        ar.setVisible(false);
                        ar.setSmall(true);
                        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> ar.remove(),21L);
                        if(p.getLocation().getBlock().getType().name().endsWith("_BED")){
                            p.sleep(p.getLocation(),true);
                            wert/=3;
                            if(wert<10){
                                p.getLocation().getNearbyEntities(1,1,1).stream().filter(et -> et instanceof ArmorStand && ((ArmorStand) et).isSmall()).forEach(entity -> ((ArmorStand) entity).remove());
                                p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "istko"));
                                p.setHealth(p.getMaxHealth()/80.0);
                            }
                        }
                        if(wert>0)p.sendTitle(ChatColor.DARK_RED+""+(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)),"");

                        if(wert-1<=0 && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)){
                            p.getLocation().getNearbyEntities(1,1,1).stream().filter(et -> et instanceof ArmorStand && ((ArmorStand) et).isSmall()).forEach(entity -> ((ArmorStand) entity).remove());
                            p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,20*20,20,false,false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,20*60,1,false,false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,20*60,1,false,false));
                            p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "istko"));
                            p.setHealth(p.getMaxHealth()/20.0);
                        } else if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)){
                            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER,wert-1);
                        }
                    } else {
                        //alle spezies durchgehen
                        if(utilitys.spezienliste.containsKey(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non"))){
                            Spezies sp = utilitys.spezienliste.get(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non"));
                            //grundwerte
                            double MaxLeben = utilitys.einstellungen.getLeben()*((sp.getLeben()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "leben"), PersistentDataType.DOUBLE,0.0)+100)/100.0);
                            double Maxmausdauer = utilitys.einstellungen.getAusdauer()*((sp.getAusdauer()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "ausdauer"), PersistentDataType.DOUBLE,0.0)+100)/100.0);
                            double Maxmana = utilitys.einstellungen.getMana()*((sp.getMana()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "mana"), PersistentDataType.DOUBLE,0.0)+100)/100.0);

                            //extrawerte
                            final int[] speedmult = {100};
                            final double[] pasivmana = {0};
                            final double[] pasivausdauer = {0};
                            final double[] passivleben = {0};

                            //passive
                            sp.getPassiven().stream().filter(pa -> p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), pa), PersistentDataType.BOOLEAN,true)).forEach(pa ->{
                                //pa ist erkennung
                                Passive pas =utilitys.passiveliste.get(pa);
                                switch (pa){
                                    case "flug":
                                        if(p.isGliding()){
                                            p.setAllowFlight(false);
                                            if(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)>=utilitys.passiveliste.get("flug").getWerte().get("Kosten")) {
                                                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                                                        p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-utilitys.passiveliste.get("flug").getWerte().get("Kosten"));
                                            } else {
                                                p.setGliding(false);
                                                p.setAllowFlight(true);
                                            }
                                            utilitys.showbar(p);
                                        } else if(p.isOnGround()){
                                            p.setAllowFlight(true);
                                        }
                                        break;
                                    case "nachtsicht":
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,20*15,0,false,false));
                                        break;
                                    case "schnellerlaufen":
                                        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "schnellerlaufennum"), PersistentDataType.INTEGER,
                                                p.isSprinting() ? p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "schnellerlaufennum"), PersistentDataType.INTEGER,0)+10 : 0);
                                        speedmult[0] +=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "schnellerlaufennum"), PersistentDataType.INTEGER);
                                        if(speedmult[0]>pas.getWerte().get("Stärke"))speedmult[0]=pas.getWerte().get("Stärke");
                                        break;
                                    case "fasterthenu":
                                        speedmult[0]+=pas.getWerte().get("Stärke");
                                        break;
                                    case "speedwennmehrlebenimumgreiß":
                                        for (Entity en : p.getNearbyEntities(pas.getWerte().get("Radius"),pas.getWerte().get("Radius"),pas.getWerte().get("Radius"))){
                                            if(en instanceof Player && ((Player) en).getMaxHealth()>=(p.getMaxHealth()*1.25))
                                                speedmult[0] +=pas.getWerte().get("Stärke");
                                        }
                                        break;
                                    case "meditatin":
                                        pasivausdauer[0] += pas.getWerte().get("Stärke");
                                        passivleben[0] += pas.getWerte().get("Stärke");
                                        pasivmana[0] += pas.getWerte().get("Stärke");
                                        break;
                                }
                            });

                            //wert zusammenrechnung
                            double audauernow=p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE,0.0)+
                                    ((Maxmausdauer/100.0)*(utilitys.einstellungen.getAusreg()+sp.getAusreg()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "ausdauerreg"), PersistentDataType.DOUBLE,0.0)+pasivausdauer[0]))
                                            *(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE) ? utilitys.einstellungen.getRegenerationimkampf()/100.0 : 1 );
                            audauernow=audauernow>=Maxmausdauer ? Maxmausdauer : audauernow;
                            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE,audauernow);
                            double mananow=p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "mananow"), PersistentDataType.DOUBLE,0.0)+
                                    ((Maxmausdauer/100.0)*(utilitys.einstellungen.getManareg()+sp.getManareg()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "manareg"), PersistentDataType.DOUBLE,0.0)+pasivmana[0]))
                                            *(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE) ? utilitys.einstellungen.getRegenerationimkampf()/100.0 : 1 );
                            mananow=mananow>=Maxmana ? Maxmana : mananow;
                            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "mananow"), PersistentDataType.DOUBLE,mananow);
                            double leben=p.getHealth()+
                                    ((MaxLeben/100.0)*(utilitys.einstellungen.getLebenreg()+sp.getLebenreg()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "lebenreg"), PersistentDataType.DOUBLE,0.0)+passivleben[0]))
                                            *(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE) ? utilitys.einstellungen.getRegenerationimkampf()/100.0 : 1 );
                            leben=leben>=MaxLeben ? MaxLeben : leben;
                            p.setMaxHealth(MaxLeben);
                            p.setHealth(leben);
                            if (MaxLeben > 20) {
                                p.setHealthScale(20);
                                p.setHealthScaled(true);
                            } else {
                                p.setHealthScaled(false);
                            }
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                                    "§c<"+new DecimalFormat("#.#").format(leben)+"/"+(int) MaxLeben+"> " +
                                            "§a<"+new DecimalFormat("#.#").format(audauernow)+"/"+(int) Maxmausdauer+"> "+
                                            "§9<"+new DecimalFormat("#.#").format(mananow)+"/"+(int) Maxmana+">"));

                            //movement
                            double movmal =utilitys.einstellungen.getBewegungsgeschwindigkeit() + p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "bewegungsgeschwindigkeit"), PersistentDataType.DOUBLE,0.0);

                            //standart geschwindikeit von minecraft
                            double standmove = 0.2 * (movmal/100) * (speedmult[0] /100.0);
                            p.setWalkSpeed(standmove>1 ? 1 :(float) standmove);

                            //get attack speed
                            double standatackspeed = 4.0 * ((utilitys.einstellungen.getWaffengeschwindigkeit() + p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "waffengeschwindigkeit"), PersistentDataType.DOUBLE,0.0)) / 100);
                            standatackspeed *=(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "amspeeden"), PersistentDataType.INTEGER,100)/100.0); //wtf is das?

                            p.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(standatackspeed);
                            p.getAttribute(Attribute.SCALE).setBaseValue(sp.getGrose());

                            //check infight
                            if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)){

                                double kampfzeit = p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE) > utilitys.einstellungen.getKampfdauer() ?
                                        utilitys.einstellungen.getKampfdauer() : p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE);
                                double dauer = utilitys.einstellungen.getKampfdauer();

                                BossBar bar= Bukkit.createBossBar("Im Kampf "+(int) kampfzeit, BarColor.RED, BarStyle.SEGMENTED_10);

                                bar.setColor(kampfzeit>=((dauer/100.0)*66.0) ? BarColor.RED
                                        : kampfzeit>=((dauer/100.0)*33.0) ? BarColor.YELLOW
                                        : BarColor.GREEN);
                                bar.setProgress(kampfzeit>=((dauer/100.0)*90) ? 1.0
                                        : kampfzeit>=((dauer/100.0)*80) ? 0.9
                                        : kampfzeit>=((dauer/100.0)*70) ? 0.8
                                        : kampfzeit>=((dauer/100.0)*60) ? 0.7
                                        : kampfzeit>=((dauer/100.0)*50) ? 0.6
                                        : kampfzeit>=((dauer/100.0)*40) ? 0.5
                                        : kampfzeit>=((dauer/100.0)*30) ? 0.4
                                        : kampfzeit>=((dauer/100.0)*20) ? 0.3
                                        : kampfzeit>=((dauer/100.0)*10) ? 0.2
                                        : 0.1);
                                bar.addPlayer(p);
                                Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () -> bar.removeAll(), 20L);

                                //update and remove if neseseray
                                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE,kampfzeit-1.0);
                                if(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)<=0.0)p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "infight"));
                            }
                        }
                    }
                });
            }
        },0,20);

        plugin = this;

        Bukkit.getPluginManager().registerEvents(new ecxtrainv(),this);
        Bukkit.getPluginManager().registerEvents(new mobdinge(),this);
        Bukkit.getPluginManager().registerEvents(new meditation(),this);
        Bukkit.getPluginManager().registerEvents(new schaden(),this);
        Bukkit.getPluginManager().registerEvents(new onbewegen(),this);
        Bukkit.getPluginManager().registerEvents(new selbstsheren(),this);
        Bukkit.getPluginManager().registerEvents(new gerausche(),this);
        Bukkit.getPluginManager().registerEvents(new Hochelfen(),this);


        //rassenpassiven
        //Bukkit.getPluginManager().registerEvents(new andersessen(),this);
        Bukkit.getPluginManager().registerEvents(new xpgain(),this);
        Bukkit.getPluginManager().registerEvents(new Luckeffeckt(),this);



        //neu
        Bukkit.getPluginManager().registerEvents(new dringraseposion(),this);
        Bukkit.getPluginManager().registerEvents(new guiverteiler(),this);
        Bukkit.getPluginManager().registerEvents(new guiclicker(),this);
        getCommand("spezieseditor").setExecutor(new openeditorcommand());
        getCommand("givespezies").setExecutor(new getrasse());

        extras.adminlist.add("COMMAND_BLOCK;Grund Auswahl;27");
        extras.permisionlist.add("Spezienspezialist");

        //load the things
        utilitys.loadeinstellungen();
        utilitys.loadspezies();
        utilitys.loadpassive();

        //kampf system
        Bukkit.getPluginManager().registerEvents(new Kampf(),this);
        Bukkit.getPluginManager().registerEvents(new KO(),this);

        //lisener passiven
        Bukkit.getPluginManager().registerEvents(new flug(),this);


//        if(passive.get("bienenchimarenflug")==null){
//
//            passive.set("wiederkauer"+".AnzeigeName","wiederkauer");
//            passive.set("wiederkauer"+".Beschreibung","Du schaffst es wiederkauer zu essen und zu scheißen");
//            passive.set("wiederkauer"+".Essen",5);
//            passive.set("wiederkauer"+".Sättigung",5);
//            passive.set("wiederkauer"+".Dropchanche",50);
//            passive.set("wiederkauer"+".Block","PAPER");
//            passive.set("wiederkauer"+".Custemmoddeldatataken",5);
//            passive.set("wiederkauer"+".WiedergekauterBlock","STONE");
//            passive.set("wiederkauer"+".WiedergekauterName","WiederKauer");
//            passive.set("wiederkauer"+".WiedergekauterCustemmoddeldatataken",123);
//            passive.set("wiederkauer"+".WiedergekauterBeschreibung","JUM JUM");
//
//            passive.set("kannfleisch"+".AnzeigeName","Nur Fleisch");
//            passive.set("kannfleisch"+".Beschreibung","Du kannst nur Fleisch Essen");
//            passive.set("kannfleisch"+".EffectStärke",5);
//            passive.set("kannfleisch"+".EffectDauer",5);
//            passive.set("kannfleisch"+".Block","PAPER");
//            passive.set("kannfleisch"+".Custemmoddeldatataken",5);
//            passive.set("kannfleisch"+".NoDeaktivation",true);
//
//            passive.set("kannkeinfleisch"+".AnzeigeName","Kein Fleisch");
//            passive.set("kannkeinfleisch"+".Beschreibung","Du kannst kein Fleisch essen");
//            passive.set("kannkeinfleisch"+".EffectStärke",5);
//            passive.set("kannkeinfleisch"+".EffectDauer",5);
//            passive.set("kannkeinfleisch"+".Block","PAPER");
//            passive.set("kannkeinfleisch"+".Custemmoddeldatataken",5);
//            passive.set("kannkeinfleisch"+".NoDeaktivation",true);
//
//            passive.set("ungebratenfleischwienormales"+".AnzeigeName","Lecker FrischFleisch");
//            passive.set("ungebratenfleischwienormales"+".Beschreibung","Esse Ungebratenes Fleisch wie normales");
//            passive.set("ungebratenfleischwienormales"+".Essen",5);
//            passive.set("ungebratenfleischwienormales"+".Sättigung",5);
//            passive.set("ungebratenfleischwienormales"+".Block","PAPER");
//            passive.set("ungebratenfleischwienormales"+".Custemmoddeldatataken",5);
//
//            passive.set("keinekekse"+".AnzeigeName","Kekse Find ich nicht so Toll");
//            passive.set("keinekekse"+".Beschreibung","iss und stirb");
//            passive.set("keinekekse"+".EffectStärke",5);
//            passive.set("keinekekse"+".EffectDauer",5);
//            passive.set("keinekekse"+".Block","PAPER");
//            passive.set("keinekekse"+".Custemmoddeldatataken",5);
//
//            passive.set("schnellerlaufen"+".AnzeigeName","schnellerlaufen");
//            passive.set("schnellerlaufen"+".Beschreibung","Laufe Schneller je länger du sprintest");
//            passive.set("schnellerlaufen"+".Stärke",200);
//            passive.set("schnellerlaufen"+".Block","PAPER");
//            passive.set("schnellerlaufen"+".Custemmoddeldatataken",5);
//
//            passive.set("extrainv"+".AnzeigeName","Extra Inventar");
//            passive.set("extrainv"+".Beschreibung","Sneaken und rechtsklick auf den Boden mit lehrer hand");
//            passive.set("extrainv"+".Größe",9);
//            passive.set("extrainv"+".Block","PAPER");
//            passive.set("extrainv"+".Custemmoddeldatataken",5);
//
//            passive.set("grassfresser"+".AnzeigeName","Grass Essen");
//            passive.set("grassfresser"+".Beschreibung","Ich bin hey durch grünes zeug am Boden");
//            passive.set("grassfresser"+".Essen",5);
//            passive.set("grassfresser"+".Sättigung",5);
//            passive.set("grassfresser"+".Block","PAPER");
//            passive.set("grassfresser"+".Custemmoddeldatataken",5);
//
//            passive.set("speedwennmehrlebenimumgreiß"+".AnzeigeName","Lebens Renner");
//            passive.set("speedwennmehrlebenimumgreiß"+".Beschreibung","Mehr Leben wenn jemand mit mehr leben um dich");
//            passive.set("speedwennmehrlebenimumgreiß"+".Stärke",120);
//            passive.set("speedwennmehrlebenimumgreiß"+".Radius",10);
//            passive.set("speedwennmehrlebenimumgreiß"+".Block","PAPER");
//            passive.set("speedwennmehrlebenimumgreiß"+".Custemmoddeldatataken",5);
//
//            passive.set("heilungdurchtot"+".AnzeigeName","Heil dich wenn was Tötest");
//            passive.set("heilungdurchtot"+".Beschreibung","Ich töte sachen gerne");
//            passive.set("heilungdurchtot"+".Stärke",110);
//            passive.set("heilungdurchtot"+".Block","PAPER");
//            passive.set("heilungdurchtot"+".Custemmoddeldatataken",5);
//
//            passive.set("fasterthenu"+".AnzeigeName","Schneller Sprinter");
//            passive.set("fasterthenu"+".Beschreibung","Du bist schneller als andere");
//            passive.set("fasterthenu"+".Stärke",200);
//            passive.set("fasterthenu"+".Block","PAPER");
//            passive.set("fasterthenu"+".Custemmoddeldatataken",5);
//
//            passive.set("wenigruckstos"+".AnzeigeName","Weniger Rückstos");
//            passive.set("wenigruckstos"+".Beschreibung","Du bekommst weniger rückstos");
//            passive.set("wenigruckstos"+".Stärke",50);
//            passive.set("wenigruckstos"+".Block","PAPER");
//            passive.set("wenigruckstos"+".Custemmoddeldatataken",5);
//
//            passive.set("creepergoawai"+".AnzeigeName","Creeperspray");
//            passive.set("creepergoawai"+".Beschreibung","Creepers meiden dich");
//            passive.set("creepergoawai"+".Block","PAPER");
//            passive.set("creepergoawai"+".Custemmoddeldatataken",5);
//
//            passive.set("meditatin"+".AnzeigeName","Meditation");
//            passive.set("meditatin"+".Beschreibung","Meditir mal");
//            passive.set("meditatin"+".Stärke",20);
//            passive.set("meditatin"+".Block","PAPER");
//            passive.set("meditatin"+".Custemmoddeldatataken",5);
//
//            passive.set("doubeljump"+".AnzeigeName","Jump");
//            passive.set("doubeljump"+".Beschreibung","Meditir mal");
//            passive.set("doubeljump"+".Stärke",20);
//            passive.set("doubeljump"+".Kosten",3);
//            passive.set("doubeljump"+".Block","PAPER");
//            passive.set("doubeljump"+".Custemmoddeldatataken",5);
//
//            passive.set("schweinemagen"+".AnzeigeName","schweinemagen Essen");
//            passive.set("schweinemagen"+".Beschreibung","Ich bin hey schweinemagen grünes zeug am Boden");
//            passive.set("schweinemagen"+".Essen",5);
//            passive.set("schweinemagen"+".Sättigung",5);
//            passive.set("schweinemagen"+".Block","PAPER");
//            passive.set("schweinemagen"+".Custemmoddeldatataken",5);
//
//            passive.set("slowfall"+".AnzeigeName","slowfall");
//            passive.set("slowfall"+".Beschreibung","slowfall mal");
//            passive.set("slowfall"+".Block","PAPER");
//            passive.set("slowfall"+".Custemmoddeldatataken",5);
//
//            passive.set("xporbgain"+".AnzeigeName","xporbgain");
//            passive.set("xporbgain"+".Beschreibung","xporbgain mal");
//            passive.set("xporbgain"+".Stärke",120);
//            passive.set("xporbgain"+".Block","PAPER");
//            passive.set("xporbgain"+".Custemmoddeldatataken",5);
//
//            passive.set("skilltreexpgain"+".AnzeigeName","skilltreexpgain");
//            passive.set("skilltreexpgain"+".Beschreibung","skilltreexpgain mal");
//            passive.set("skilltreexpgain"+".Stärke",120);
//            passive.set("skilltreexpgain"+".Block","PAPER");
//            passive.set("skilltreexpgain"+".Custemmoddeldatataken",5);
//
//            passive.set("nosliponice"+".AnzeigeName","nosliponice");
//            passive.set("nosliponice"+".Beschreibung","nosliponice mal");
//            passive.set("nosliponice"+".Block","PAPER");
//            passive.set("nosliponice"+".Custemmoddeldatataken",5);
//
//            passive.set("nopulverschneesinken"+".AnzeigeName","nopulverschneesinken");
//            passive.set("nopulverschneesinken"+".Beschreibung","nopulverschneesinken mal");
//            passive.set("nopulverschneesinken"+".Block","PAPER");
//            passive.set("nopulverschneesinken"+".Custemmoddeldatataken",5);
//
//            passive.set("selbstscheren"+".AnzeigeName","selbstscheren");
//            passive.set("selbstscheren"+".Beschreibung","selbstscheren mal");
//            passive.set("selbstscheren"+".Block","PAPER");
//            passive.set("selbstscheren"+".Cooldown",10);
//            passive.set("selbstscheren"+".Stärke",30);
//            passive.set("selbstscheren"+".Custemmoddeldatataken",5);
//
//            passive.set("highjump"+".AnzeigeName","highjump");
//            passive.set("highjump"+".Beschreibung","highjump mal");
//            passive.set("highjump"+".Block","PAPER");
//            passive.set("highjump"+".Cooldown",10);
//            passive.set("highjump"+".EffectStärke",5);
//            passive.set("highjump"+".EffectDauer",5);
//            passive.set("highjump"+".Custemmoddeldatataken",5);
//
//            passive.set("wenigfalschaden"+".AnzeigeName","wenigfalschaden");
//            passive.set("wenigfalschaden"+".Beschreibung","wenigfalschaden mal");
//            passive.set("wenigfalschaden"+".Block","PAPER");
//            passive.set("wenigfalschaden"+".Schadeninporzent",50);
//            passive.set("wenigfalschaden"+".Custemmoddeldatataken",5);
//
//            passive.set("wenigergift"+".AnzeigeName","wenigergift");
//            passive.set("wenigergift"+".Beschreibung","wenigergift mal");
//            passive.set("wenigergift"+".Block","PAPER");
//            passive.set("wenigergift"+".Schadeninporzent",50);
//            passive.set("wenigergift"+".Custemmoddeldatataken",5);
//
//            passive.set("noabbausound"+".AnzeigeName","noabbausound");
//            passive.set("noabbausound"+".Beschreibung","noabbausound mal");
//            passive.set("noabbausound"+".Block","PAPER");
//            passive.set("noabbausound"+".Custemmoddeldatataken",5);
//
//            passive.set("noaufsammelsound"+".AnzeigeName","noaufsammelsound");
//            passive.set("noaufsammelsound"+".Beschreibung","noaufsammelsound mal");
//            passive.set("noaufsammelsound"+".Block","PAPER");
//            passive.set("noaufsammelsound"+".Custemmoddeldatataken",5);
//
//            passive.set("hochelfenunsichtbar"+".AnzeigeName","Hochelfenunsichtbar");
//            passive.set("hochelfenunsichtbar"+".Beschreibung","Hochelfenunsichtbar mal");
//            passive.set("hochelfenunsichtbar"+".Block","PAPER");
//            passive.set("hochelfenunsichtbar"+".Kosten",3);
//            passive.set("hochelfenunsichtbar"+".Custemmoddeldatataken",5);
//
//            passive.set("hochelfenunsichtbar"+".AnzeigeName","Hochelfenunsichtbar");
//            passive.set("hochelfenunsichtbar"+".Beschreibung","Hochelfenunsichtbar mal");
//            passive.set("hochelfenunsichtbar"+".Block","PAPER");
//            passive.set("hochelfenunsichtbar"+".Kosten",3);
//            passive.set("hochelfenunsichtbar"+".Custemmoddeldatataken",5);
//
//            passive.set("luckpassive"+".AnzeigeName","luckpassive");
//            passive.set("luckpassive"+".Beschreibung","luckpassive mal");
//            passive.set("luckpassive"+".Block","PAPER");
//            passive.set("luckpassive"+".Kosten",3);
//            passive.set("luckpassive"+".Ausweichchancheinprozent",66);
//            passive.set("luckpassive"+".lootingstufe",10);
//            passive.set("luckpassive"+".Custemmoddeldatataken",5);
//        }

    }

    @Override
    public void onDisable() {
        System.out.println("Rassenplugin is workling finneee");

        //save shit
        utilitys.saveeinstellungen();
        utilitys.savespezies();
        utilitys.savepassive();

    }

    public static Rassensystem getPlugin() {
        return plugin;
    }

}
