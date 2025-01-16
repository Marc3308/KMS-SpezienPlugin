package me.marc3308.rassensystem.passiven;

import me.marc3308.rassensystem.ItemCreater;
import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

import static me.marc3308.rassensystem.ItemCreater.getcon;
import static me.marc3308.rassensystem.ItemCreater.isapassive;

public class andersessen implements Listener {

    public static ArrayList<Material> seeds=new ArrayList<>();
    public static ArrayList<Material> keinfleischliste=new ArrayList<>();
    public static ArrayList<Material> fleischliste=new ArrayList<>();
    public static ArrayList<Material> ungebratenlist=new ArrayList<>();

    public static Random random=new Random();

    public andersessen(){
        seeds.add(Material.PUMPKIN_SEEDS);
        seeds.add(Material.MELON_SEEDS);
        seeds.add(Material.WHEAT_SEEDS);

        ungebratenlist.add(Material.BEEF);
        ungebratenlist.add(Material.PORKCHOP);
        ungebratenlist.add(Material.MUTTON);
        ungebratenlist.add(Material.CHICKEN);
        ungebratenlist.add(Material.RABBIT);
        ungebratenlist.add(Material.COD);
        ungebratenlist.add(Material.SALMON);

        keinfleischliste.add(Material.APPLE);
        keinfleischliste.add(Material.BAKED_POTATO);
        fleischliste.add(Material.BEEF);
        keinfleischliste.add(Material.BEETROOT);
        keinfleischliste.add(Material.BEETROOT_SOUP);
        keinfleischliste.add(Material.BREAD);
        keinfleischliste.add(Material.CARROT);
        fleischliste.add(Material.CHICKEN);
        keinfleischliste.add(Material.CHORUS_FRUIT);
        fleischliste.add(Material.COD);
        fleischliste.add(Material.COOKED_BEEF);
        fleischliste.add(Material.COOKED_CHICKEN);
        fleischliste.add(Material.COOKED_COD);
        fleischliste.add(Material.COOKED_MUTTON);
        fleischliste.add(Material.COOKED_PORKCHOP);
        fleischliste.add(Material.COOKED_RABBIT);
        fleischliste.add(Material.COOKED_SALMON);
        keinfleischliste.add(Material.COOKIE);
        keinfleischliste.add(Material.DRIED_KELP);
        keinfleischliste.add(Material.HONEY_BOTTLE);
        keinfleischliste.add(Material.MELON_SLICE);
        keinfleischliste.add(Material.MUSHROOM_STEW);
        fleischliste.add(Material.MUTTON);
        fleischliste.add(Material.PORKCHOP);
        keinfleischliste.add(Material.POTATO);
        fleischliste.add(Material.PUFFERFISH);
        keinfleischliste.add(Material.PUMPKIN_PIE);
        fleischliste.add(Material.RABBIT);
        fleischliste.add(Material.RABBIT_STEW);
        fleischliste.add(Material.ROTTEN_FLESH);
        fleischliste.add(Material.SALMON);
        fleischliste.add(Material.SPIDER_EYE);
        keinfleischliste.add(Material.SUSPICIOUS_STEW);
        keinfleischliste.add(Material.SWEET_BERRIES);
        fleischliste.add(Material.TROPICAL_FISH);
    }

    @EventHandler
    public void onbambus(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "bambusessen"), PersistentDataType.BOOLEAN))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.BAMBOO))return;
        if(e.getAction().isLeftClick())return;
        if(p.getFoodLevel()>=20)return;


        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return;
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("bambusessen"))break;
        }

        p.setFoodLevel(p.getFoodLevel() + ItemCreater.getcon(9).getInt("bambusessen"+".Sättigung")>20 ? 20 : p.getFoodLevel()+ ItemCreater.getcon(9).getInt("bambusessen"+".Essen"));
        p.setSaturation(p.getSaturation()+ ItemCreater.getcon(9).getInt("bambusessen"+".Sättigung")>40 ? 40 : p.getSaturation()+ ItemCreater.getcon(9).getInt("bambusessen"+".Sättigung"));

        if(p.getInventory().getItemInMainHand().getAmount()>1){
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
        } else {
            p.getInventory().setItemInMainHand(null);
        }

    }

    @EventHandler
    public void seedfeed(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "seedessen"), PersistentDataType.BOOLEAN))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(!seeds.contains(p.getInventory().getItemInMainHand().getType()))return;
        if(e.getAction().isLeftClick())return;
        if(p.getFoodLevel()>=20)return;


        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return;
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("seedessen"))break;
        }

        p.setFoodLevel(p.getFoodLevel() + ItemCreater.getcon(9).getInt("seedessen"+".Sättigung")>20 ? 20 : p.getFoodLevel()+ ItemCreater.getcon(9).getInt("seedessen"+".Essen"));
        p.setSaturation(p.getSaturation()+ ItemCreater.getcon(9).getInt("seedessen"+".Sättigung")>40 ? 40 : p.getSaturation()+ ItemCreater.getcon(9).getInt("seedessen"+".Sättigung"));

        if(p.getInventory().getItemInMainHand().getAmount()>1){
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
        } else {
            p.getInventory().setItemInMainHand(null);
        }

    }

    @EventHandler
    public void roteesen(PlayerItemConsumeEvent e){
        Player p=e.getPlayer();
        if(!isapassive(p,"rotfesheesen"))return;
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.ROTTEN_FLESH))return;
        if(p.getFoodLevel()>=20)return;
        e.setCancelled(true);

        p.setFoodLevel(p.getFoodLevel() + ItemCreater.getcon(9).getInt("rotfesheesen"+".Sättigung")>20 ? 20 : p.getFoodLevel()+ ItemCreater.getcon(9).getInt("rotfesheesen"+".Essen"));
        p.setSaturation(p.getSaturation()+ ItemCreater.getcon(9).getInt("rotfesheesen"+".Sättigung")>40 ? 40 : p.getSaturation()+ ItemCreater.getcon(9).getInt("rotfesheesen"+".Sättigung"));

        if(p.getInventory().getItemInMainHand().getAmount()>1){
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
        } else {
            p.getInventory().setItemInMainHand(null);
        }

    }

    @EventHandler
    public void wiederkauer(PlayerItemConsumeEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "wiederkauer"), PersistentDataType.BOOLEAN))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(!fleischliste.contains(e.getItem().getType()) && !keinfleischliste.contains(e.getItem().getType()))return;

        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return;
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("wiederkauer"))break;
        }

        //chanche that its not droping
        if(random.nextInt(0,101)>=getcon(9).getInt("wiederkauer"+".Dropchanche"))return;

        try {
            ItemStack wiederkauer=new ItemStack(Material.valueOf(ItemCreater.getcon(9).getString("wiederkauer"+".WiedergekauterBlock")));
            ItemMeta wieder_meta=wiederkauer.getItemMeta();
            wieder_meta.setDisplayName(ItemCreater.getcon(9).getString("wiederkauer"+".WiedergekauterName"));
            wieder_meta.setCustomModelData(ItemCreater.getcon(9).getInt("wiederkauer"+".WiedergekauterCustemmoddeldatataken"));
            ArrayList<String> wieder_lore=new ArrayList<>();
            wieder_lore.add(ItemCreater.getcon(9).getString("wiederkauer"+".WiedergekauterBeschreibung"));
            wieder_meta.setLore(wieder_lore);
            wiederkauer.setItemMeta(wieder_meta);
            p.getWorld().dropItemNaturally(p.getLocation(),wiederkauer);

        } catch (Error s){
            System.out.println("Wieder kauer Block error");
        }


    }

    @EventHandler
    public void wiederkaueressen(PlayerInteractEvent e){
        Player p=e.getPlayer();

        ItemStack wiederkauer=new ItemStack(Material.valueOf(ItemCreater.getcon(9).getString("wiederkauer"+".WiedergekauterBlock")));
        ItemMeta wieder_meta=wiederkauer.getItemMeta();
        wieder_meta.setDisplayName(ItemCreater.getcon(9).getString("wiederkauer"+".WiedergekauterName"));
        wieder_meta.setCustomModelData(ItemCreater.getcon(9).getInt("wiederkauer"+".WiedergekauterCustemmoddeldatataken"));
        ArrayList<String> wieder_lore=new ArrayList<>();
        wieder_lore.add(ItemCreater.getcon(9).getString("wiederkauer"+".WiedergekauterBeschreibung"));
        wieder_meta.setLore(wieder_lore);
        wiederkauer.setItemMeta(wieder_meta);
        wiederkauer.setAmount(p.getInventory().getItemInMainHand().getAmount());

        if(!p.getInventory().getItemInMainHand().equals(wiederkauer))return;
        e.setCancelled(true);
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)){
            p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,20*20,3,false,false));
            p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,20*20,3,false,false));
            if(p.getInventory().getItemInMainHand().getAmount()>1){
                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
            } else {
                p.getInventory().setItemInMainHand(null);
            }
            return;
        }

        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "wiederkauer"), PersistentDataType.BOOLEAN))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(p.getFoodLevel()>=20)return;



        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null){
                p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,20*20,3,false,false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,20*20,3,false,false));
                if(p.getInventory().getItemInMainHand().getAmount()>1){
                    p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                } else {
                    p.getInventory().setItemInMainHand(null);
                }
                return;
            }
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("wiederkauer"))break;
        }

        p.setFoodLevel(p.getFoodLevel() + ItemCreater.getcon(9).getInt("wiederkauer"+".Sättigung")>20 ? 20 : p.getFoodLevel()+ ItemCreater.getcon(9).getInt("wiederkauer"+".Essen"));
        p.setSaturation(p.getSaturation()+ ItemCreater.getcon(9).getInt("wiederkauer"+".Sättigung")>40 ? 40 : p.getSaturation()+ ItemCreater.getcon(9).getInt("wiederkauer"+".Sättigung"));

        if(p.getInventory().getItemInMainHand().getAmount()>1){
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
        } else {
            p.getInventory().setItemInMainHand(null);
        }

    }

    @EventHandler
    public void onessen(PlayerItemConsumeEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(e.getItem().getType().equals(Material.GOLDEN_APPLE) || e.getItem().getType().equals(Material.ENCHANTED_GOLDEN_APPLE) || e.getItem().getType().equals(Material.GOLDEN_CARROT))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;

        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return;
            if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin()
                    ,getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))), PersistentDataType.BOOLEAN)){
                if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("schweinemagen")){
                    p.removePotionEffect(PotionEffectType.HUNGER);
                    p.removePotionEffect(PotionEffectType.NAUSEA);
                    p.removePotionEffect(PotionEffectType.POISON);
                    p.setFoodLevel(p.getFoodLevel() + ItemCreater.getcon(9).getInt("schweinemagen"+".Sättigung")>20 ? 20 : p.getFoodLevel()+ ItemCreater.getcon(9).getInt("schweinemagen"+".Essen"));
                    p.setSaturation(p.getSaturation()+ ItemCreater.getcon(9).getInt("schweinemagen"+".Sättigung")>40 ? 40 : p.getSaturation()+ ItemCreater.getcon(9).getInt("schweinemagen"+".Sättigung"));
                    if(p.getInventory().getItemInMainHand().getAmount()>1){
                        p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                    } else {
                        p.getInventory().setItemInMainHand(null);
                    }
                }
                if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("kannfleisch")){
                    p.removePotionEffect(PotionEffectType.HUNGER);
                    if(keinfleischliste.contains(e.getItem().getType()))p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA
                            ,20*ItemCreater.getcon(9).getInt("kannfleisch"+".EffectDauer"),ItemCreater.getcon(9).getInt("kannfleisch"+".EffectStärke"),false,false));
                }
                if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("kannkeinfleisch")){
                    if(fleischliste.contains(e.getItem().getType()))p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA
                            ,20*ItemCreater.getcon(9).getInt("kannkeinfleisch"+".EffectDauer"),ItemCreater.getcon(9).getInt("kannkeinfleisch"+".EffectStärke"),false,false));
                }
                if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("bambusessen")){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA
                            ,20*ItemCreater.getcon(9).getInt("bambusessen"+".EffectDauer"),ItemCreater.getcon(9).getInt("bambusessen"+".EffectStärke"),false,false));
                }
                if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("keinekekse")){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA
                            ,20*ItemCreater.getcon(9).getInt("keinekekse"+".EffectDauer"),ItemCreater.getcon(9).getInt("keinekekse"+".EffectStärke"),false,false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER
                            ,20*ItemCreater.getcon(9).getInt("keinekekse"+".EffectDauer"),ItemCreater.getcon(9).getInt("keinekekse"+".EffectStärke"),false,false));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.POISON
                            ,20*ItemCreater.getcon(9).getInt("keinekekse"+".EffectDauer"),ItemCreater.getcon(9).getInt("keinekekse"+".EffectStärke"),false,false));
                }
                if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("ungebratenfleischwienormales")){
                    if(ungebratenlist.contains(e.getItem().getType())){
                        e.setCancelled(true);
                        p.setFoodLevel(p.getFoodLevel() + ItemCreater.getcon(9).getInt("ungebratenfleischwienormales"+".Sättigung")>20 ? 20 : p.getFoodLevel()+ ItemCreater.getcon(9).getInt("ungebratenfleischwienormales"+".Essen"));
                        p.setSaturation(p.getSaturation()+ ItemCreater.getcon(9).getInt("ungebratenfleischwienormales"+".Sättigung")>40 ? 40 : p.getSaturation()+ ItemCreater.getcon(9).getInt("ungebratenfleischwienormales"+".Sättigung"));
                        if(p.getInventory().getItemInMainHand().getAmount()>1){
                            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                        } else {
                            p.getInventory().setItemInMainHand(null);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void grassfresser(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "grassfresser"), PersistentDataType.BOOLEAN))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(e.getAction().isLeftClick())return;
        if(e.getClickedBlock()==null)return;
        if(!e.getClickedBlock().getType().equals(Material.GRASS_BLOCK))return;
        if(p.getFoodLevel()>=20)return;

        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return;
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("grassfresser"))break;
        }

        p.setFoodLevel(p.getFoodLevel() + ItemCreater.getcon(9).getInt("grassfresser"+".Sättigung")>20 ? 20 : p.getFoodLevel()+ ItemCreater.getcon(9).getInt("grassfresser"+".Essen"));
        p.setSaturation(p.getSaturation()+ ItemCreater.getcon(9).getInt("grassfresser"+".Sättigung")>40 ? 40 : p.getSaturation()+ ItemCreater.getcon(9).getInt("grassfresser"+".Sättigung"));
        p.getWorld().setBlockData(e.getClickedBlock().getLocation(),Material.DIRT.createBlockData());
    }
}
