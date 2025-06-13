package me.marc3308.rassensystem;

import me.marc3308.kMSCustemModels.extras;
import me.marc3308.rassensystem.objekts.Passive;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.objekts.einstellungen;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class utilitys {

    public static einstellungen einstellungen;
    public static HashMap<String,Spezies> spezienliste = new HashMap<>();
    public static HashMap<String,Passive> passiveliste = new HashMap<>();

    public static void loadeinstellungen(){

        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Grundeinstellungen.yml");
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        einstellungen = new einstellungen(
                grundconf.getInt("shk"),
                grundconf.getInt("shoa"),
                grundconf.getInt("rik"),
                grundconf.getInt("ko"),
                grundconf.getInt("kmd"),
                grundconf.getInt("swk"),
                new HashMap<Material, Double>(){{
                    Arrays.stream(Material.values()).filter(m -> grundconf.contains("Wak."+m.name()))
                            .forEach(m -> put(m, grundconf.getDouble("Wak."+m.name())));
                }},
                grundconf.getDouble("l"),
                grundconf.getDouble("lr"),
                grundconf.getDouble("a"),
                grundconf.getDouble("ar"),
                grundconf.getDouble("m"),
                grundconf.getDouble("mr"),

                grundconf.getDouble("ws"),
                grundconf.getDouble("wg"),
                grundconf.getDouble("wcd"),
                grundconf.getDouble("wcc"),

                grundconf.getDouble("fs"),
                grundconf.getDouble("fg"),
                grundconf.getDouble("fcd"),
                grundconf.getDouble("fcc"),

                grundconf.getDouble("bw"),
                grundconf.getInt("sk")
        );
    }

    public static void saveeinstellungen(){

        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Grundeinstellungen.yml");
        cosfile.delete();
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        //save einstellungen
        grundconf.set("shk",einstellungen.getSchadenimkampf());
        grundconf.set("shoa",einstellungen.getSchadenohneausdauer());
        grundconf.set("rik",einstellungen.getRegenerationimkampf());
        grundconf.set("ko",einstellungen.getKozeit());
        grundconf.set("kmd",einstellungen.getKampfdauer());
        grundconf.set("swk",einstellungen.getStandartwaffenkosten());

        einstellungen.getWaffenschlagkosnte().forEach((m,d) -> grundconf.set("Wak."+m.name(),d));

        grundconf.set("l",einstellungen.getLeben());
        grundconf.set("lr",einstellungen.getLebenreg());
        grundconf.set("a",einstellungen.getAusdauer());
        grundconf.set("ar",einstellungen.getAusreg());
        grundconf.set("m",einstellungen.getMana());
        grundconf.set("mr",einstellungen.getManareg());

        grundconf.set("ws",einstellungen.getWaffenschaden());
        grundconf.set("wg",einstellungen.getWaffengeschwindigkeit());
        grundconf.set("wcd",einstellungen.getWaffencritdmg());
        grundconf.set("wcc",einstellungen.getWaffencritchance());

        grundconf.set("fs",einstellungen.getFahigkeitsdmg());
        grundconf.set("fg",einstellungen.getFahigkeitsgeschwindigkeit());
        grundconf.set("fcd",einstellungen.getFahigkeitscritdmg());
        grundconf.set("fcc",einstellungen.getFahigkeitscritchance());

        grundconf.set("bw",einstellungen.getBewegungsgeschwindigkeit());
        grundconf.set("sk",einstellungen.getSkillslots());

        try {
            grundconf.save(cosfile);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void loadspezies(){

        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Spezies.yml");
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        if (cosfile.exists()) {
            for (int i = 0; i < grundconf.getKeys(false).size(); i++) {
                spezienliste.put(grundconf.getString(i+".ek"),new Spezies(
                        grundconf.getString(i+".tk"),
                        grundconf.getString(i+".ek"),
                        grundconf.getDouble(i+".l"),
                        grundconf.getDouble(i+".lr"),
                        grundconf.getDouble(i+".a"),
                        grundconf.getDouble(i+".ar"),
                        grundconf.getDouble(i+".m"),
                        grundconf.getDouble(i+".mr"),
                        grundconf.getDouble(i+".g"),
                        (ArrayList<String>) grundconf.getList(i + ".p")
                        ));
            }
        }
    }

    public static void savespezies(){

        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Spezies.yml");
        cosfile.delete();
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        ArrayList<Spezies> splist = new ArrayList<>(spezienliste.values());

        splist.forEach(sp -> {
            grundconf.set(splist.indexOf(sp)+".tk",sp.getTicker());
            grundconf.set(splist.indexOf(sp)+".ek",sp.getErkennung());
            grundconf.set(splist.indexOf(sp)+".l",sp.getLeben());
            grundconf.set(splist.indexOf(sp)+".lr",sp.getLebenreg());
            grundconf.set(splist.indexOf(sp)+".a",sp.getAusdauer());
            grundconf.set(splist.indexOf(sp)+".ar",sp.getAusreg());
            grundconf.set(splist.indexOf(sp)+".m",sp.getMana());
            grundconf.set(splist.indexOf(sp)+".mr",sp.getManareg());
            grundconf.set(splist.indexOf(sp)+".g",sp.getGrose());
            grundconf.set(splist.indexOf(sp)+".p",sp.getPassiven());
        });

        try {
            grundconf.save(cosfile);
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public static void loadpassive(){

        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Passive.yml");
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);
        if (cosfile.exists()) {
            for (int i = 0; i < grundconf.getKeys(false).size(); i++) {
                HashMap<String,Integer> passive = new HashMap<>();
                int finalI = i;
                if(grundconf.getConfigurationSection(i+".W") != null)grundconf.getConfigurationSection(i+".W").getKeys(false).forEach(m -> passive.put(m,grundconf.getInt(finalI +".W."+m)));
                passiveliste.put(grundconf.getString(i+".ek"),new Passive(
                        grundconf.getString(i+".tk"),
                        grundconf.getString(i+".ek"),
                        grundconf.getBoolean(i+".toggle"),
                        passive
                ));
            }
        }

        addpassivroster();
    }

    public static void addpassivroster(){
        //wenn nicht vorhanden
        HashMap<String,Passive> creatlist=new HashMap<>();
        creatlist.put("flug",new Passive("flug","flug",false,new HashMap<String,Integer>(){{
            put("Kosten",2);
        }}));
        creatlist.put("nachtsicht",new Passive("nachtsicht","nachtsicht",true,new HashMap<String,Integer>()));






        creatlist.put("bambusessen",new Passive("bambusessen","bambusessen",false,new HashMap<String,Integer>(){{
            put("Essen",2);
            put("Sättigung",4);
            put("EffectStärke",5);
            put("EffectDauer",5);
        }}));
        creatlist.put("seedessen",new Passive("seedessen","seedessen",false,new HashMap<String,Integer>(){{
            put("Essen",2);
            put("Sättigung",4);
        }}));
        creatlist.put("rotfesheesen",new Passive("rotfesheesen","rotfesheesen",false,new HashMap<String,Integer>(){{
            put("Essen",2);
            put("Sättigung",4);
        }}));
        creatlist.put("rotfesheesen",new Passive("bambusessen","bambusessen",false,new HashMap<String,Integer>(){{
            put("Essen",2);
            put("Sättigung",4);
            put("EffectStärke",5);
            put("EffectDauer",5);
        }}));

        //neu hinzu alte weg
        creatlist.forEach((k,p) -> {
            if(!passiveliste.containsKey(k))passiveliste.put(k,p);
        });
    }

    public static void savepassive(){
        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Passive.yml");
        cosfile.delete();
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);
        ArrayList<Passive> plist = new ArrayList<Passive>(passiveliste.values());
        plist.forEach(p -> {
            grundconf.set(plist.indexOf(p)+".tk",p.getTicker());
            grundconf.set(plist.indexOf(p)+".ek",p.getErkennung());
            grundconf.set(plist.indexOf(p)+".toggle",p.getToggle());
            if(!p.getWerte().isEmpty())p.getWerte().forEach((m, d) -> grundconf.set(plist.indexOf(p)+".W."+m,d));
        });

        try {
            grundconf.save(cosfile);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static ItemStack getSpeziesitem(Spezies sp, Player p){
        return new ItemStack(Material.HONEY_BOTTLE){{
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(extras.getCustemModel(sp.getTicker()).getModelName());
            meta.setCustomModelData(extras.getCustemModel(sp.getTicker()).getModelData());
            meta.setLore(extras.getCustemModel(sp.getTicker()).getModelBeschreibung());
            meta.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"kurzel"), PersistentDataType.STRING,sp.getErkennung());
            meta.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"bind"), PersistentDataType.STRING,p.getUniqueId().toString());
            setItemMeta(meta);
        }};
    }

    public static String Kostenwert(Player p){
        Spezies sp = utilitys.spezienliste.get(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non"));
        double Maxmausdauer = utilitys.einstellungen.getAusdauer()*((sp.getAusdauer()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "ausdauer"), PersistentDataType.DOUBLE,0.0)+100)/100.0);
        double Maxmana = utilitys.einstellungen.getMana()*((sp.getMana()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "mana"), PersistentDataType.DOUBLE,0.0)+100)/100.0);
        return Maxmausdauer >= Maxmana ? "ausdauer" : "mana";
    }

    public static void showbar(Player p){

        double now = p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE);
        Spezies sp = utilitys.spezienliste.get(p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING,"Non"));
        double max = (Kostenwert(p).equals("mana") ? utilitys.einstellungen.getMana() : einstellungen.getAusdauer())*((sp.getMana()+p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)), PersistentDataType.DOUBLE,0.0)+100)/100.0);
        BossBar bar= Bukkit.createBossBar(new DecimalFormat("#.#").format(now)+"/"+(int) max, BarColor.RED, BarStyle.SEGMENTED_20);

        bar.setColor(now>=((max/100.0)*66.0) ? BarColor.GREEN
                : now>=((max/100.0)*33.0) ? BarColor.YELLOW
                : BarColor.RED);
        bar.setProgress(now>=((max/100.0)*90) ? 1.0
                : now>=((max/100.0)*80) ? 0.9
                : now>=((max/100.0)*70) ? 0.8
                : now>=((max/100.0)*60) ? 0.7
                : now>=((max/100.0)*50) ? 0.6
                : now>=((max/100.0)*40) ? 0.5
                : now>=((max/100.0)*30) ? 0.4
                : now>=((max/100.0)*20) ? 0.3
                : now>=((max/100.0)*10) ? 0.2
                : 0.1);
        bar.addPlayer(p);
        Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () -> bar.removeAll(), 20L);
    }

}
