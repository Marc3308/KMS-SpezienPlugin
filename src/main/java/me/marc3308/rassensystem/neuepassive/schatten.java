package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;


public class schatten implements Listener {

    @EventHandler
    public void onschadow(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if((e.getFrom().getY()!=e.getTo().getY() || e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ()!=e.getTo().getZ())
                && utilitys.kannpassive(p,"schattenstand"))utilitys.timemap.put(p.getUniqueId().toString(),System.currentTimeMillis());
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "verwirrungpassiv"), PersistentDataType.INTEGER))
            p.setVelocity(new Vector(Math.cos(new Random().nextDouble()*Math.PI*2)*utilitys.passiveliste.get("verwirrung").getWerte().get("Stärke")/100.0
                    ,utilitys.passiveliste.get("verwirrung").getWerte().get("Stärke")>100 ? new Random().nextInt(10) : -1,Math.sin(new Random().nextDouble()*Math.PI*2)*utilitys.passiveliste.get("verwirrung").getWerte().get("Stärke")/100.0));
    }

    @EventHandler
    public void tar(EntityTargetEvent e){
        if(e.getTarget() instanceof Player p && utilitys.kannpassive(p,"untotenliebe") && e.getEntity() instanceof Zombie)e.setCancelled(true);
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player pix && utilitys.kannpassive(pix,"verwirrung") && e.getDamager() instanceof Player p && new Random().nextInt(100)<= utilitys.passiveliste.get("verwirrung").getWerte().get("Chance")) {
            pix.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                    pix.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-utilitys.passiveliste.get("verwirrung").getWerte().get("Kosten"));
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "verwirrungpassiv"), PersistentDataType.INTEGER,utilitys.passiveliste.get("verwirrung").getWerte().get("Dauer"));
        }
    }

    @EventHandler
    public void onArmorStandDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof ArmorStand ar && ar.getCustomName() != null && ar.getCustomName().equals("donttakeme"))e.getDrops().clear();
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e) {
        if( e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals("donttakeme"))e.setCancelled(true);

    }

    public static void shadowaperat(double leben, Location loc, Player p) {
        // 2. ArmorStand an der Position des Spielers spawnen
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setGravity(false); // Optional: falls du willst, dass der Stand nicht fällt
        armorStand.setMaxHealth(p.getMaxHealth());
        armorStand.setHealth(leben);
        armorStand.setCustomNameVisible(false);
        armorStand.setCustomName("donttakeme");
        armorStand.getAttribute(Attribute.SCALE).setBaseValue(p.getAttribute(Attribute.SCALE).getBaseValue());

        armorStand.getEquipment().setHelmet(new ItemStack(Material.PLAYER_HEAD, 1) {{
            SkullMeta meta = (SkullMeta) getItemMeta();
            meta.setOwningPlayer(p);
            setItemMeta(meta);
        }});
        armorStand.getEquipment().setChestplate(p.getInventory().getChestplate().getType().equals(Material.AIR) ? new ItemStack(Material.LEATHER_CHESTPLATE) : p.getInventory().getChestplate());
        armorStand.getEquipment().setLeggings(p.getInventory().getLeggings().getType().equals(Material.AIR) ? new ItemStack(Material.LEATHER_LEGGINGS) : p.getInventory().getLeggings());
        armorStand.getEquipment().setBoots(p.getInventory().getBoots().getType().equals(Material.AIR) ? new ItemStack(Material.LEATHER_BOOTS) : p.getInventory().getBoots());

        armorStand.getNearbyEntities(10,10,10).stream().filter(e -> e instanceof Mob mb && mb.getTarget() != null && mb.getTarget().equals(p))
                .forEach(e -> {
                    if(e instanceof Mob le) le.setTarget(armorStand);
                });

        new BukkitRunnable(){
            @Override
            public void run() {
                if(!p.isOnline() || armorStand.isDead() || !p.isSneaking() || p.isDead() || !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "schattenklon"+"mak"), PersistentDataType.BOOLEAN)){
                    armorStand.remove();
                    p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "schattenklon"+"mak"));
                    cancel();
                    return;
                }
            }
        }.runTaskTimer(Rassensystem.getPlugin(), 0, 20);
    }

}
