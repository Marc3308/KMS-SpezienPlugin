package me.marc3308.rassensystem.eventlisteners;

import me.marc3308.rassensystem.ItemCreater;
import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class Joinding implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player p=e.getPlayer();
        p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "isfly"));

        //NEU!!!
        p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "infight"));

        //grundwerte
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "waffenschaden"), PersistentDataType.DOUBLE)){

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "bewegungsgeschwindigkeit"), PersistentDataType.DOUBLE,0.0);

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "waffengeschwindigkeit"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "waffenschaden"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "waffencritdmg"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "waffencritchance"), PersistentDataType.DOUBLE,0.0);

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fahigkeitsgeschwindigkeit"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fahigkeitsschaden"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fahigkeitscritdmg"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fahigkeitscritchance"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fightdmg"), PersistentDataType.DOUBLE,0.0);
        }

        //ausdauer
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE)){

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE,0.0);

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauer"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauerreg"), PersistentDataType.DOUBLE,0.0);


        }

        //Leben
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "leben"), PersistentDataType.DOUBLE)){

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "leben"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "lebenreg"), PersistentDataType.DOUBLE,0.0);


        }

        //mana
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "mananow"), PersistentDataType.DOUBLE)){

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "mananow"), PersistentDataType.DOUBLE,0.0);

            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "mana"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "manareg"), PersistentDataType.DOUBLE,0.0);


        }

        //chanche hight
        p.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(
                ItemCreater.getcon(2).get(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".SpielerGröße") !=null
                        ? ItemCreater.getcon(2).getDouble(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".SpielerGröße")
                        : 1);


    }
}
