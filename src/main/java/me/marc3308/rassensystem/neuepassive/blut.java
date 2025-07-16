package me.marc3308.rassensystem.neuepassive;

import me.marc3308.rassensystem.Rassensystem;
import me.marc3308.rassensystem.utilitys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

public class blut implements Listener {

    public static HashMap<UUID, Entity> lasthitmap = new HashMap<>();

    @EventHandler
    public void onhit(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player p && utilitys.kannpassive(p,"blutschlag"))p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "schnellschlag"), PersistentDataType.INTEGER,
                p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "schnellschlag"), PersistentDataType.INTEGER,0)+utilitys.passiveliste.get("blutschlag").getWerte().get("Stärke") > utilitys.passiveliste.get("blutschlag").getWerte().get("Max")
                        ? utilitys.passiveliste.get("blutschlag").getWerte().get("Max") : p.getPersistentDataContainer().getOrDefault(new NamespacedKey(Rassensystem.getPlugin(), "schnellschlag"), PersistentDataType.INTEGER,0)+utilitys.passiveliste.get("blutschlag").getWerte().get("Stärke"));
        if(e.getDamager() instanceof Player p && utilitys.kannpassive(p,"autoaim"))lasthitmap.put(p.getUniqueId(),e.getEntity());
    }

    @EventHandler
    public void ondeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Animals && e.getEntity().getKiller() instanceof Player p && utilitys.kannpassive(p,"fleischer")){
            List<ItemStack> d = new ArrayList<>(e.getDrops());
            List<ItemStack> multipliedDrops = new ArrayList<>();
            e.getDrops().clear();
            for (int i = 0; i<= utilitys.passiveliste.get("fleischer").getWerte().get("Stärke"); i++)multipliedDrops.addAll(d);
            multipliedDrops.forEach(i -> p.getWorld().dropItemNaturally(e.getEntity().getLocation(), i));
            p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(2));
        }
    }

    @EventHandler
    public void oneat(PlayerItemConsumeEvent e){
        Player p = e.getPlayer();
        if(!utilitys.kannpassive(p,"fleischliebhaber"))return;
        if(isMeat(e.getItem().getType()))p.setFoodLevel(Math.min(p.getFoodLevel() + utilitys.passiveliste.get("fleischliebhaber").getWerte().get("Stärke"), 20));
        if(isVegetable(e.getItem().getType()))p.setFoodLevel(Math.max(p.getFoodLevel() - utilitys.passiveliste.get("fleischliebhaber").getWerte().get("Stärke"), 0));
    }

    @EventHandler
    public void interact(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "infight"), PersistentDataType.DOUBLE)
                && utilitys.kannpassive(p,"autoaim") && blut.lasthitmap.containsKey(p.getUniqueId()) && !blut.lasthitmap.get(p.getUniqueId()).isDead()){
            if(new Random().nextInt(100) > utilitys.passiveliste.get("autoaim").getWerte().get("Chanche"))return;
            p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE,
                    p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), utilitys.Kostenwert(p)+"now"), PersistentDataType.DOUBLE)-utilitys.passiveliste.get("autoaim").getWerte().get("Kosten"));
            ((LivingEntity) lasthitmap.get(p.getUniqueId())).damage(wpdamage(p.getInventory().getItemInMainHand().getType()), p);
            // Calculate new yaw and pitch to face the target
            Location from = p.getEyeLocation();
            Location to = blut.lasthitmap.get(p.getUniqueId()).getLocation().add(0, blut.lasthitmap.get(p.getUniqueId()).getHeight() / 2, 0);

            Vector dir = to.toVector().subtract(from.toVector()).normalize();
            Location loc = p.getLocation().clone();

            float yaw = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
            float pitch = (float) Math.toDegrees(-Math.asin(dir.getY()));

            loc.setYaw(yaw);
            loc.setPitch(pitch);

            // Force player to look at the target
            p.teleport(loc);
        }
    }

    private double wpdamage(Material mat){
        return switch (mat) {
            case WOODEN_SWORD -> 4;
            case WOODEN_AXE,STONE_SWORD -> 5;
            case STONE_AXE,IRON_SWORD -> 6;
            case GOLDEN_SWORD -> 4.5;
            case GOLDEN_AXE -> 6.5;
            case IRON_AXE,DIAMOND_SWORD -> 7;
            case DIAMOND_AXE,NETHERITE_SWORD -> 8;
            case NETHERITE_AXE -> 9;
            default -> 1;
        };
    }

    private boolean isMeat(Material mat) {
        return mat == Material.COOKED_BEEF ||
                mat == Material.BEEF ||
                mat == Material.COOKED_PORKCHOP ||
                mat == Material.PORKCHOP ||
                mat == Material.COOKED_CHICKEN ||
                mat == Material.CHICKEN ||
                mat == Material.COOKED_MUTTON ||
                mat == Material.MUTTON ||
                mat == Material.COOKED_RABBIT ||
                mat == Material.RABBIT ||
                mat == Material.COOKED_COD ||
                mat == Material.COD ||
                mat == Material.COOKED_SALMON ||
                mat == Material.SALMON;
    }

    private boolean isVegetable(Material mat) {
        return mat == Material.CARROT ||
                mat == Material.POTATO ||
                mat == Material.APPLE ||
                mat == Material.GOLDEN_CARROT ||
                mat == Material.GOLDEN_APPLE ||
                mat == Material.BAKED_POTATO ||
                mat == Material.BEETROOT ||
                mat == Material.BEETROOT_SOUP ||
                mat == Material.SUSPICIOUS_STEW ||
                mat == Material.MUSHROOM_STEW;
    }
}
