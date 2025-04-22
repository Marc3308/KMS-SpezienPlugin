package me.marc3308.rassensystem.eventlisteners;

import me.marc3308.rassensystem.ItemCreater;
import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import static me.marc3308.rassensystem.ItemCreater.getcon;

public class ondamage implements Listener {

    //public static HashMap<Player, Long> lastdmghit = new HashMap<>();
    public static ArrayList<EntityDamageEvent.DamageCause> coslist=new ArrayList<>();

    public ondamage(){

        coslist.add(EntityDamageEvent.DamageCause.PROJECTILE);
        coslist.add(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
        coslist.add(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
        coslist.add(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
        coslist.add(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK);
        coslist.add(EntityDamageEvent.DamageCause.POISON);
        coslist.add(EntityDamageEvent.DamageCause.SONIC_BOOM);
        coslist.add(EntityDamageEvent.DamageCause.THORNS);
        coslist.add(EntityDamageEvent.DamageCause.WITHER);

    }

    @EventHandler
    public void onschlagen(EntityDamageByEntityEvent e){

        if(!(e.getEntity() instanceof LivingEntity))return;
        if(e.getEntity() instanceof Player){
            Player pp= (Player) e.getEntity();
            pp.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "lastdmghit"), PersistentDataType.LONG,System.currentTimeMillis());
            //lastdmghit.put(pp,System.currentTimeMillis());
            iskamp(pp,e.getDamage());
        }

        if(!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player))return;

        Player p=e.getDamager() instanceof Player ? (Player) e.getDamager() : (Player) ((Projectile) e.getDamager()).getShooter();
        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "lastdmghit"), PersistentDataType.LONG,System.currentTimeMillis());

        //schlag kosten
        if(p.getInventory().getItemInMainHand()!=null)e.setDamage(schlagmitwaffe(p,e.getDamage()));

        //wenn du damage machst
        iskamp(p,e.getDamage());

        //get all werte von grund und klasse
        double alldmg = (e.getDamage() *((getcon(2).getDouble("Grundwerte"+".waffenschaden")+p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "waffenschaden"), PersistentDataType.DOUBLE))/100));
        double allcrit = ((getcon(2).getDouble("Grundwerte"+".waffencritdmg")+p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "waffencritdmg"), PersistentDataType.DOUBLE))/100);
        double allcritchange = (getcon(2).getDouble("Grundwerte"+".waffencritchance")+p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "waffencritchance"), PersistentDataType.DOUBLE));

        double ran = ThreadLocalRandom.current().nextDouble(1,101);

        if(ran<=allcritchange)alldmg*=allcrit;


        //res und schwächen
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)
                && p.getInventory().getItemInMainHand().hasItemMeta()){

            //create easy exes for datacontainer
            ItemStack item=p.getInventory().getItemInMainHand();
            ItemMeta meta =item.getItemMeta();
            PersistentDataContainer data=meta.getPersistentDataContainer();

            if(data.has(new NamespacedKey("waffenundrustung","schadensart"), PersistentDataType.STRING)){

                //schadensart
                String schadensart=data.get(new NamespacedKey("waffenundrustung","schadensart"), PersistentDataType.STRING);
                LivingEntity en= (LivingEntity) e.getEntity();

                switch (schadensart){
                    case "Slashing":
                        if(en.getEquipment().getHelmet()!=null && (en.getEquipment().getHelmet().getType().equals(Material.LEATHER_HELMET) || en.getEquipment().getHelmet().getType().equals(Material.CHAINMAIL_HELMET)))en.getEquipment().getHelmet().setDurability((short) (en.getEquipment().getHelmet().getDurability()+1));
                        if(en.getEquipment().getChestplate()!=null && (en.getEquipment().getChestplate().getType().equals(Material.LEATHER_CHESTPLATE) || en.getEquipment().getChestplate().getType().equals(Material.CHAINMAIL_CHESTPLATE)))en.getEquipment().getChestplate().setDurability((short) (en.getEquipment().getChestplate().getDurability()+1));
                        if(en.getEquipment().getLeggings()!=null && (en.getEquipment().getLeggings().getType().equals(Material.LEATHER_LEGGINGS) || en.getEquipment().getLeggings().getType().equals(Material.CHAINMAIL_LEGGINGS)))en.getEquipment().getLeggings().setDurability((short) (en.getEquipment().getLeggings().getDurability()+1));
                        if(en.getEquipment().getBoots()!=null && (en.getEquipment().getBoots().getType().equals(Material.LEATHER_BOOTS) || en.getEquipment().getBoots().getType().equals(Material.CHAINMAIL_BOOTS)))en.getEquipment().getBoots().setDurability((short) (en.getEquipment().getBoots().getDurability()+1));
                        break;
                    case "Piercing":
                        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))break;
                        en.setHealth(en.getHealth()-alldmg<=0 ? 0.0 : en.getHealth()-alldmg);
                        return;
                    case "Bludgeoning":
                        if(en.getEquipment().getHelmet()!=null && (en.getEquipment().getHelmet().getType().equals(Material.GOLDEN_HELMET)
                                || en.getEquipment().getHelmet().getType().equals(Material.IRON_HELMET))
                                || en.getEquipment().getHelmet().getType().equals(Material.DIAMOND_HELMET)
                                || en.getEquipment().getHelmet().getType().equals(Material.NETHERITE_HELMET))en.getEquipment().getHelmet().setDurability((short) (en.getEquipment().getHelmet().getDurability()+1));
                        if(en.getEquipment().getChestplate()!=null && (en.getEquipment().getChestplate().getType().equals(Material.GOLDEN_CHESTPLATE)
                                || en.getEquipment().getChestplate().getType().equals(Material.IRON_CHESTPLATE)
                                || en.getEquipment().getChestplate().getType().equals(Material.DIAMOND_CHESTPLATE)
                                || en.getEquipment().getChestplate().getType().equals(Material.NETHERITE_CHESTPLATE)))en.getEquipment().getChestplate().setDurability((short) (en.getEquipment().getChestplate().getDurability()+1));
                        if(en.getEquipment().getLeggings()!=null && (en.getEquipment().getLeggings().getType().equals(Material.GOLDEN_LEGGINGS)
                                || en.getEquipment().getLeggings().getType().equals(Material.IRON_LEGGINGS)
                                || en.getEquipment().getLeggings().getType().equals(Material.DIAMOND_LEGGINGS)
                                || en.getEquipment().getLeggings().getType().equals(Material.NETHERITE_LEGGINGS)))en.getEquipment().getLeggings().setDurability((short) (en.getEquipment().getLeggings().getDurability()+1));
                        if(en.getEquipment().getBoots()!=null && (en.getEquipment().getBoots().getType().equals(Material.GOLDEN_BOOTS)
                                || en.getEquipment().getBoots().getType().equals(Material.IRON_BOOTS)
                                || en.getEquipment().getBoots().getType().equals(Material.DIAMOND_BOOTS)
                                || en.getEquipment().getBoots().getType().equals(Material.NETHERITE_BOOTS)))en.getEquipment().getBoots().setDurability((short) (en.getEquipment().getBoots().getDurability()+1));
                        break;
                }

                if(data.has(new NamespacedKey("waffenundrustung","zusatzschadensart"), PersistentDataType.STRING)){

                    String zusatzschadensart=data.get(new NamespacedKey("waffenundrustung","zusatzschadensart"), PersistentDataType.STRING);
                    String prasse=p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)==null ? "no"
                            : p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING);

                    //resistenzen und schaden
                    if(!prasse.equals("no")){
                        int k=0;
                        while (k<1000){
                            k++;
                            if(getcon(2).getString(prasse+".resistzenzen"+"."+k)==null)break;
                            String resi= getcon(2).getString(prasse+".resistzenzen"+"."+k);
                            alldmg/=2;
                        }

                        k=0;
                        while (k<1000){
                            k++;
                            if(getcon(2).getString(prasse+".schwächen"+"."+k)==null)break;
                            String resi= getcon(2).getString(prasse+".schwächen"+"."+k);
                            alldmg*=2;
                        }
                    }

                    switch (zusatzschadensart){
                        case "Nekrotisch":
                            //make untot posible
                            en.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"nekro"), PersistentDataType.BOOLEAN,true);
                            new BukkitRunnable(){

                                @Override
                                public void run() {
                                    en.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(),"nekro"), PersistentDataType.BOOLEAN,false);
                                    cancel();
                                }
                            }.runTaskTimer(Rassensystem.getPlugin(),8*20,20);
                            break;
                        case "True":
                            en.setHealth(en.getHealth()-alldmg<=0 ? 0.0 : en.getHealth()-alldmg);
                            return;
                    }
                }
            }
        }


        e.setDamage(alldmg);

    }

    @EventHandler
    public void ondmg(EntityDamageEvent e){
        if(e.getEntity() instanceof Player && coslist.contains(e.getCause())){
            Player p=(Player) e.getEntity();
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "lastdmghit"), PersistentDataType.LONG,System.currentTimeMillis());
            if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE))e.setDamage(e.getDamage()*(getcon(2).getDouble("Grundkosten"+".Schadenimkampf")/100));
            iskamp(p,e.getDamage());
        }
    }

    public void iskamp(Player p,Double dmg){

        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "fightdmg"), PersistentDataType.DOUBLE))return; //todo test if goes

        //check to fight
        double gerade = p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "fightdmg"), PersistentDataType.DOUBLE)+dmg;
        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fightdmg"), PersistentDataType.DOUBLE,gerade);

        //when in fight
        if(gerade>= getcon(2).getInt("Grundwerte"+".kampfstartschaden")){
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fightdmg"), PersistentDataType.DOUBLE,0.0);
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE,0.0);

            //still in fight?
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(!p.isOnline()
                            || (System.currentTimeMillis()-p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "lastdmghit"),PersistentDataType.LONG))>(getcon(2).getInt("Grundwerte"+".kampfende")*1000)
                            || !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)
                            || !p.getGameMode().equals(GameMode.SURVIVAL)
                            || p.getHealth()<=0){
                        p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "infight"));
                        cancel();
                        return;
                    }
                    p.closeInventory();
                }
            }.runTaskTimer(Rassensystem.getPlugin(),0,20);
            return;
        }

        //cooldown so not always add
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!p.isOnline() || p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)){
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fightdmg"), PersistentDataType.DOUBLE,0.0);
                    cancel();
                    return;
                }
                p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "fightdmg"), PersistentDataType.DOUBLE,gerade-dmg);
                cancel();
                return;
            }
        }.runTaskTimer(Rassensystem.getPlugin(),10*20,20);
    }

    public double schlagmitwaffe(Player p, double restschaden){

        if(getcon(2).get("Grundkosten"+"."+p.getInventory().getItemInMainHand().getType())==null)return restschaden;

        //get kosten
        double kosten=getcon(2).getDouble("Grundkosten"+"."+p.getInventory().getItemInMainHand().getType());

        if(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE)<kosten)return restschaden*(getcon(1).getDouble("Grundkosten"+".Schadenwennkeinausdauer")/100);
        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE
                ,p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauernow"), PersistentDataType.DOUBLE)-kosten);

        return restschaden;
    }



}
