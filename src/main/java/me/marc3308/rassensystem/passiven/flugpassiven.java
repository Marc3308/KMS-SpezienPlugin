package me.marc3308.rassensystem.passiven;

import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.marc3308.rassensystem.ItemCreater.*;

public class flugpassiven implements Listener {

    @EventHandler
    public void flugbienen(PlayerMoveEvent e){

        Player p=e.getPlayer();
        //just to be save doubeljump flugpassive
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        if(!isapassive(p,"bienenchimarenflug") && !isapassive(p,"papageichimarenflug") && !isapassive(p,"fledermauschimarenlug") && !isapassive(p,"doubeljump"))return;

        //check witch rasse
        String rasse=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING);

        if(rasse.equals("Bienenchimäre") && !p.getWorld().isClearWeather() && p.getLocation().getBlock().getY()>=p.getWorld().getHighestBlockAt(p.getLocation()).getY())return;
        if(rasse.equals("Fledermauschimäre") && p.getWorld().getTime()<=12000 && p.getLocation().getBlock().getY()>=p.getWorld().getHighestBlockAt(p.getLocation()).getY())return;
        if(rasse.equals("Papageichimäre") && p.getWorld().getTime()>=12000)return;

        //check no skill at the time
        if(p.getPersistentDataContainer().has(new NamespacedKey("arbeitundleben", "isactiv"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey("arbeitundleben", "iscasting"), PersistentDataType.STRING))return;


        // Check if the player is in the air
        if (!p.isOnGround() && !p.getAllowFlight()
                && !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "isfly"), PersistentDataType.DOUBLE)) {
            // Allow temporary flight so we can detect the jump
            p.setAllowFlight(true);
            Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () -> p.setAllowFlight(false), 10L);
            //problem with skills vl
        } else if(p.isOnGround() && p.getFallDistance()>3){
            p.damage((p.getFallDistance()-3));
        }
    }

    @EventHandler
    public void trytopassivfly(PlayerToggleFlightEvent e){

        Player p=e.getPlayer();
        if(!isapassive(p,"bienenchimarenflug") && !isapassive(p,"papageichimarenflug") && !isapassive(p,"fledermauschimarenlug"))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        p.setAllowFlight(false);
        e.setCancelled(true);

        //check no skill at the time
        if(p.getPersistentDataContainer().has(new NamespacedKey("arbeitundleben", "isactiv"), PersistentDataType.STRING))return; //&& p.getPersistentDataContainer().get(new NamespacedKey("arbeitundleben", "isactiv"), PersistentDataType.STRING).equals("fly")
        if(p.getPersistentDataContainer().has(new NamespacedKey("arbeitundleben", "iscasting"), PersistentDataType.STRING))return;

        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))return;
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "isfly"), PersistentDataType.BOOLEAN))return;

        String rasse=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING).equals("Bienenchimäre")
                ? "bienenchimarenflug" : p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING).equals("Fledermauschimäre")
                ? "fledermauschimarenlug" : "papageichimarenflug";

        //fly check
        if (!p.isOnGround()) {

            p.setAllowFlight(true);

            new BukkitRunnable(){
                @Override
                public void run() {

                    if(!p.isOnline() || p.isOnGround() || p.isSneaking()
                            || p.getPersistentDataContainer().has(new NamespacedKey("arbeitundleben", "isactiv"), PersistentDataType.STRING)
                            || p.getPersistentDataContainer().has(new NamespacedKey("arbeitundleben", "iscasting"), PersistentDataType.STRING)
                            || p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)
                            || (rasse.equals("fledermauschimarenlug") && p.getWorld().getTime()<=12000 && p.getLocation().getBlock().getY()>=p.getWorld().getHighestBlockAt(p.getLocation()).getY())){
                        p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "isfly"));
                        p.setGliding(false);
                        p.setAllowFlight(false);
                        cancel();
                        return;
                    }

                    //maby dann sogar doppelt so teuer
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "isfly"), PersistentDataType.DOUBLE,0.0);
                    p.setGliding(true);
                    p.setAllowFlight(true);
                    p.setFallDistance(0f);

                    String kostenwert=kostenrechnungswert(p);

                    double kosten=(getcon(9).getInt(rasse+".Kosten")/20.0);

                    double kostenwertnow=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE);
                    if(kostenwertnow - kosten >0) {
                        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE,(kostenwertnow-kosten));
                    } else {
                        p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "isfly"));
                        p.setGliding(false);
                        p.setAllowFlight(false);
                        cancel();
                    }

                }
            }.runTaskTimer(Rassensystem.getPlugin(),0,1);
        }
    }

    @EventHandler
    public void trytodublejump(PlayerToggleFlightEvent e){
        Player p=e.getPlayer();
        if(!isapassive(p,"doubeljump"))return;
        e.setCancelled(true);
        Player player = e.getPlayer();
        player.setAllowFlight(false);
        player.setFlying(false);
        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "isfly"), PersistentDataType.DOUBLE,1.0);
        player.setVelocity(player.getLocation().getDirection().multiply(getcon(9).getInt("doubeljump"+".Stärke")).setY(0.42));
        String kostenwert=kostenrechnungswert(p);

        double kostenwertnow=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE);
        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE,
                kostenwertnow - getcon(9).getInt("doubeljump"+".Kosten") > 0 ? kostenwertnow - getcon(9).getInt("doubeljump"+".Kosten") : 0);

        Bukkit.getScheduler().runTaskLater(Rassensystem.getPlugin(), () ->  p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "isfly")), 20*getcon(9).getInt("doubeljump"+".Stärke"));

    }

    @EventHandler
    public void hitit(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "isfly"), PersistentDataType.DOUBLE))return;
        if(!isapassive(p,"bienenchimarenflug") && !isapassive(p,"papageichimarenflug") && !isapassive(p,"fledermauschimarenlug"))return;
        String rasse=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING).equals("Bienenchimäre")
                ? "bienenchimarenflug" : p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING).equals("Fledermauschimäre")
                ? "fledermauschimarenlug" : "papageichimarenflug";

        int managrund= getcon(2).getInt("Grundwerte"+".mana");
        int ausdauergrund= getcon(2).getInt("Grundwerte"+".ausdauer");
        double Manarasse= getcon(2).getDouble(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".mana");
        double Ausdauerrasse= getcon(2).getDouble(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".ausdauer");
        double Ausdauemax = (ausdauergrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauer"), PersistentDataType.DOUBLE)) * ((100 + Ausdauerrasse) / 100);
        double Manamax = (managrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "mana"), PersistentDataType.DOUBLE)) * ((100 + Manarasse) / 100);
        String kostenwert=Ausdauemax>Manamax ? "ausdauer" : "mana";

        double kosten=getcon(9).getInt(rasse+".BoostKosten");

        double kostenwertnow=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE);
        if(kostenwertnow - kosten >0){
            p.setVelocity(p.getLocation().getDirection().multiply(getcon(9).getInt(rasse+".Stärke")));
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE,(kostenwertnow-kosten));
        }
    }
}
