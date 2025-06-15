//package me.marc3308.rassensystem.passiven;
//
//import me.marc3308.rassensystem.Rassensystem;
//import org.bukkit.Material;
//import org.bukkit.NamespacedKey;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.persistence.PersistentDataType;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import static me.marc3308.rassensystem.ItemCreater.*;
//
//public class Hochelfen implements Listener {
//
//    @EventHandler
//    public void onschiftright(PlayerInteractEvent e){
//        Player p=e.getPlayer();
//        //should stop the player from doing stuff
//        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE)
//                && p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE)>1 ){
//            barremove(p);
//            p.removePotionEffect(PotionEffectType.INVISIBILITY);
//            p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"));
//            p.setSneaking(false);
//            return;
//        }
//
//        //is passive?
//        if(!isapassive(p,"hochelfenunsichtbar"))return;
//        if(!p.isSneaking() || e.getAction().isLeftClick())return;
//        if(!p.getInventory().getItemInMainHand().getType().equals(Material.AIR))return;
//
//        //set the stage
//        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE,0.0);
//        p.setSneaking(false);
//        bar(p,kostenrechnungswert(p),getcon(9).getString("hochelfenunsichtbar"+".AnzeigeName"));
//
//        //run it
//        new BukkitRunnable(){
//            @Override
//            public void run() {
//
//                if(!p.isOnline() || !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE)){
//                    p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"));
//                    cancel();
//                    return;
//                }
//
//                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE
//                        ,p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE)+1);
//
//                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20*3,10,false,false));
//
//                String kostenwert=kostenrechnungswert(p);
//                double kosten=getcon(9).getInt("hochelfenunsichtbar"+".Kosten");
//
//                double kostenwertnow=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE);
//                if(kostenwertnow - kosten > 0) {
//                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE,(kostenwertnow-kosten));
//                } else {
//                    p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"));
//                    cancel();
//                }
//            }
//        }.runTaskTimer(Rassensystem.getPlugin(),0,20);
//    }
//
//    @EventHandler
//    public void ondmg(EntityDamageEvent e){
//        if(!(e.getEntity() instanceof Player))return;
//        Player p= (Player) e.getEntity();
//        //should stop the player from doing stuff
//        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE)
//                && p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"), PersistentDataType.DOUBLE)>1 ){
//            p.removePotionEffect(PotionEffectType.INVISIBILITY);
//            p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "hochelfenpassiv"));
//            p.setSneaking(false);
//            return;
//        }
//    }
//}
