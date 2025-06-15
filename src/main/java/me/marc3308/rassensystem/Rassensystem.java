package me.marc3308.rassensystem;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.Gui.guiclicker;
import me.marc3308.rassensystem.Gui.guiverteiler;
import me.marc3308.rassensystem.Gui.openeditorcommand;
import me.marc3308.rassensystem.command.getrasse;
import me.marc3308.rassensystem.eventlisteners.dringraseposion;
import me.marc3308.rassensystem.kampfsystem.KO;
import me.marc3308.rassensystem.kampfsystem.Kampf;
import me.marc3308.rassensystem.neuepassive.*;
import me.marc3308.rassensystem.objekts.Passive;
import me.marc3308.rassensystem.objekts.Spezies;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
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
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

public final class Rassensystem extends JavaPlugin implements Listener {


    public static Rassensystem plugin;
    private Team team;

    public static final HashMap<Player, Long> timermap=new HashMap<>();
    @Override
    public void onEnable() {

        //hide the names
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        team = scoreboard.getTeam("hidden_names");
        if (team == null) {
            team = scoreboard.registerNewTeam("hidden_names");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        //loop the werte
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                //alle spieler durchgehen
                Bukkit.getOnlinePlayers().forEach(p -> {

                    team.addEntry(p.getName());

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
                    } else if(!p.isDead() && p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "istko"), PersistentDataType.INTEGER)){
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
                            p.getLocation().getNearbyEntities(1,1,1).stream().filter(et -> et instanceof ArmorStand && ((ArmorStand) et).isSmall()).forEach(entity -> ((ArmorStand) entity).remove());
                            p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "istko"));
                            p.setHealth(p.getMaxHealth()/80.0);
                        }
                        if(wert>0)p.sendTitle(ChatColor.DARK_RED+""+wert,"");

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
                        if(!p.isDead() && utilitys.spezienliste.containsKey(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non"))){
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
                                            if(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)>=pas.getWerte().get("Kosten")) {
                                                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                                                        p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-pas.getWerte().get("Kosten"));
                                            } else {
                                                p.setGliding(false);
                                                p.setAllowFlight(true);
                                            }
                                            utilitys.showbar(p,20);
                                        } else if(p.isOnGround()){
                                            p.setAllowFlight(true);
                                        }
                                        break;
                                    case "nachtsicht":
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,20*15,0,false,false));
                                        break;
                                    case "wasseratmen":
                                        if(p.getLocation().getBlock().getType().equals(Material.WATER))p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,20*3,0,false,false));
                                        break;
                                    case "unterwassersspeed":
                                        if(p.getLocation().getBlock().getType().equals(Material.WATER))p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,20*3,0,false,false));
                                        break;
                                    case "federfall":
                                        if(p.getLocation().subtract(0,1,0).getBlock().getType().equals(Material.AIR) && p.getLocation().subtract(0,2,0).getBlock().getType().equals(Material.AIR)
                                                && p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)>=pas.getWerte().get("Kosten")) {
                                            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                                                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-pas.getWerte().get("Kosten"));
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,20*2,0,false,false));
                                            utilitys.showbar(p,20);
                                        }
                                        break;
                                    case "schattenstand":
                                        if(p.getLocation().getBlock().getLightLevel()<=7 && utilitys.timemap.containsKey(p.getUniqueId().toString())
                                                && utilitys.timemap.get(p.getUniqueId().toString())+(pas.getWerte().get("Stillstehedauer")*1000)<=System.currentTimeMillis()
                                                && p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)>=pas.getWerte().get("Kosten")){
                                            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                                                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-pas.getWerte().get("Kosten"));
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20*2,0,false,false));
                                            utilitys.showbar(p,20);
                                        }
                                        break;
                                    case "schattenspeed":
                                        if(p.getLocation().getBlock().getLightLevel()<=7)speedmult[0] += pas.getWerte().get("Stärke");
                                        break;
                                    case "doppelsprung":
                                        p.setAllowFlight(true);
                                        break;
                                    case "mediation":
                                        p.getNearbyEntities(1,1,1).stream().filter(ar -> ar instanceof ArmorStand && ((ArmorStand) ar).getPassengers().getFirst().equals(p) && ((ArmorStand) ar).getCustomName().equals("meditation")).findFirst().ifPresent(ar -> {
                                            pasivausdauer[0] += pas.getWerte().get("Stärke");
                                            passivleben[0] += pas.getWerte().get("Stärke");
                                            pasivmana[0] += pas.getWerte().get("Stärke");
                                        });
                                        break;
                                    case "autoernte":
                                        for (int x = -pas.getWerte().get("größe"); x <= pas.getWerte().get("größe"); x++) {
                                            for (int z = -pas.getWerte().get("größe"); z <= pas.getWerte().get("größe"); z++) {
                                                if(p.getLocation().add(x,0,z).getBlock().getBlockData() instanceof Ageable ag && ag.getAge()==ag.getMaximumAge()){
                                                    if(!utilitys.kannpassive(p,pa))continue;
                                                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                                                            p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-pas.getWerte().get("Kosten"));
                                                    p.getWorld().spawnParticle(Particle.CLOUD,p.getLocation().add(x,0,z), 3, 0.3, 0.3, 0.3);
                                                    p.getLocation().add(x,0,z).getBlock().getDrops().forEach(d -> p.getWorld().dropItemNaturally(p.getLocation(), d));
                                                    ag.setAge(0);
                                                    p.getLocation().add(x,0,z).getBlock().setBlockData(ag);
                                                }
                                            }
                                        }
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
        Bukkit.getPluginManager().registerEvents(new feuer(),this);
        Bukkit.getPluginManager().registerEvents(new wasser(),this);
        Bukkit.getPluginManager().registerEvents(new erde(),this);
        Bukkit.getPluginManager().registerEvents(new schatten(),this);
        Bukkit.getPluginManager().registerEvents(new licht(),this);
        Bukkit.getPluginManager().registerEvents(new wald(),this);

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
