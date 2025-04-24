package me.marc3308.rassensystem;

import me.marc3308.rassensystem.objekts.Spezies;
import me.marc3308.rassensystem.objekts.einstellungen;
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
}
