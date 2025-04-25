package me.marc3308.rassensystem;

import me.marc3308.rassensystem.objekts.Passive;
import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.objekts.einstellungen;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class utilitys {

    public static einstellungen einstellungen;
    public static ArrayList<Spezies> spezienliste = new ArrayList<>();
    public static ArrayList<Passive> passiveliste = new ArrayList<>();

    public static void loadeinstellungen(){

        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Grundeinstellungen.yml");
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        einstellungen = new einstellungen(
                grundconf.getInt("shk"),
                grundconf.getInt("shoa"),
                grundconf.getInt("rik"),
                grundconf.getInt("shfks"),
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
        grundconf.set("shfks",einstellungen.getSchadenfurkampfstart());
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
                spezienliste.add(new Spezies(
                        grundconf.getString(i+".tk"),
                        grundconf.getString(i+".ek"),
                        grundconf.getDouble(i+".l"),
                        grundconf.getDouble(i+".lr"),
                        grundconf.getDouble(i+".a"),
                        grundconf.getDouble(i+".ar"),
                        grundconf.getDouble(i+".m"),
                        grundconf.getDouble(i+".mr"),
                        (ArrayList<String>) grundconf.getList(i + ".p")
                        ));
            }
        }
    }

    public static void savespezies(){

        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Spezies.yml");
        cosfile.delete();
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        spezienliste.forEach(sp -> {
            grundconf.set(spezienliste.indexOf(sp)+".tk",sp.getTicker());
            grundconf.set(spezienliste.indexOf(sp)+".ek",sp.getErkennung());
            grundconf.set(spezienliste.indexOf(sp)+".l",sp.getLeben());
            grundconf.set(spezienliste.indexOf(sp)+".lr",sp.getLebenreg());
            grundconf.set(spezienliste.indexOf(sp)+".a",sp.getAusdauer());
            grundconf.set(spezienliste.indexOf(sp)+".ar",sp.getAusreg());
            grundconf.set(spezienliste.indexOf(sp)+".m",sp.getMana());
            grundconf.set(spezienliste.indexOf(sp)+".mr",sp.getManareg());
            grundconf.set(spezienliste.indexOf(sp)+".p",sp.getPassiven());
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
                grundconf.getConfigurationSection(i+".W").getKeys(false).forEach(m -> passive.put(m,grundconf.getInt(finalI +".W."+m))); //todo test if woks
                passiveliste.add(new Passive(
                        grundconf.getString(i+".tk"),
                        grundconf.getString(i+".ek"),
                        grundconf.getBoolean(i+".toggle"),
                        passive
                ));
            }
        }

        //wenn nicht vorhanden
        ArrayList<Passive> creatlist=new ArrayList<Passive>();
        creatlist.add(new Passive("test","test",false,new HashMap<String,Integer>(){{
            put("test",1);
            put("test2",1);
        }}));


        //neu hinzu alte weg
        creatlist.forEach(cp -> {
            if(!passiveliste.stream().filter(p -> p.getErkennung().equals(cp.getErkennung())).findAny().isPresent())passiveliste.add(cp);
        });
    }

    public static void savepassive(){
        File cosfile = new File("plugins/KMS Plugins/Rassensystem","Passive.yml");
        cosfile.delete();
        FileConfiguration grundconf= YamlConfiguration.loadConfiguration(cosfile);

        passiveliste.forEach(p -> {
            grundconf.set(passiveliste.indexOf(p)+".tk",p.getTicker());
            grundconf.set(passiveliste.indexOf(p)+".ek",p.getErkennung());
            grundconf.set(passiveliste.indexOf(p)+".toggle",p.getToggle());
            p.getPassive().forEach((m,d) -> grundconf.set(passiveliste.indexOf(p)+".W."+m,d));
        });

        try {
            grundconf.save(cosfile);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

}
