package me.marc3308.rassensystem;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemCreater {

    public static HashMap<Integer,FileConfiguration> conmap=new HashMap<>();


    public static ItemStack itcreater(String rasse, Player p){

        if(ItemCreater.getcon(2).get(rasse)==null) {

            ItemStack test =new ItemStack(Material.DIRT);
            ItemMeta test_meta= test.getItemMeta();
            test_meta.setDisplayName(rasse);
            test.setItemMeta(test_meta);

            return test;
        }

        ItemStack item=new ItemStack(Material.PAPER);
        ItemMeta item_meta= item.getItemMeta();
        item_meta.setDisplayName(ItemCreater.getcon(2).getString(rasse+".name"));
        item_meta.setCustomModelData(ItemCreater.getcon(2).getInt(rasse+".custemmoddeldata"));
        ArrayList<String> item_lore=new ArrayList<>();
        if(p!=null)item_lore.add(p.getUniqueId().toString());
        item_lore.add(rasse);
        item_lore.add(ItemCreater.getcon(2).getString(rasse+".beschreibung"));

        if(ItemCreater.getcon(2).get(rasse+".leben")!=null)      item_lore.add("Leben: "+(int) (ItemCreater.getcon(2).getDouble(rasse+".leben")+100)+"%");
        if(ItemCreater.getcon(2).get(rasse+".lebenreg")!=null)   item_lore.add("Lebensregeneration: "+(ItemCreater.getcon(2).getDouble(rasse+".lebenreg")+100)+"%");
        if(ItemCreater.getcon(2).get(rasse+".ausdauer")!=null)   item_lore.add("Ausdauer: "+(int) (ItemCreater.getcon(2).getDouble(rasse+".ausdauer")+100)+"%");
        if(ItemCreater.getcon(2).get(rasse+".ausdauerreg")!=null)item_lore.add("Ausdauerregeneration: "+(ItemCreater.getcon(2).getDouble(rasse+".ausdauerreg")+100)+"%");
        if(ItemCreater.getcon(2).get(rasse+".mana")!=null)       item_lore.add("Mana: "+(int) (ItemCreater.getcon(2).getDouble(rasse+".mana")+100)+"%");
        if(ItemCreater.getcon(2).get(rasse+".manareg")!=null)    item_lore.add("Manaregeneration: "+(ItemCreater.getcon(2).getDouble(rasse+".manareg")+100)+"%");


        int k=0;
        while (k<1000){
            k++;
            if(ItemCreater.getcon(2).getString(rasse+".resistzenzen"+"."+k)==null)break;
            String resi=ItemCreater.getcon(2).getString(rasse+".resistzenzen"+"."+k);
            resi+="-Resistänz";
            item_lore.add(resi);
        }

        k=0;
        while (k<1000){
            k++;
            if(ItemCreater.getcon(2).getString(rasse+".schwächen"+"."+k)==null)break;
            String resi=ItemCreater.getcon(2).getString(rasse+".schwächen"+"."+k);
            resi+="-Schwäche";
            item_lore.add(resi);
        }

        //passiven
        for(int i=0;i<25;i++){
            if(getcon(2).getString(rasse+".passiven"+"."+(i+1))==null)break;
            item_lore.add("Passive "+(i+1)+": "+getcon(9).getString(getcon(2).getString(rasse+".passiven"+"."+(i+1))+".AnzeigeName"));
        }

        item_meta.setLore(item_lore);
        item.setItemMeta(item_meta);

        return item;
    }

    public static FileConfiguration getcon(Integer num){

        return conmap.get(num);

    }

    public static Boolean isapassive(Player p,String rn){
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return false;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), rn), PersistentDataType.BOOLEAN))return false;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return false;

        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return false;
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals(rn)){
                if(getcon(9).get(rn+".Kosten")==null)return true;
                String kostenwert=kostenrechnungswert(p);

                if( p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE)<(getcon(9).getInt(rn+".Kosten")))return false;
                return true;
            }
        }
        throw new IllegalArgumentException("There was a Error with ItemCreater isapassive"+rn);
    }

    public static String kostenrechnungswert(Player p){

        int managrund= getcon(2).getInt("Grundwerte"+".mana");
        int ausdauergrund= getcon(2).getInt("Grundwerte"+".ausdauer");
        double Manarasse= getcon(2).getDouble(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".mana");
        double Ausdauerrasse= getcon(2).getDouble(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".ausdauer");
        double Ausdauemax = (ausdauergrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "ausdauer"), PersistentDataType.DOUBLE)) * ((100 + Ausdauerrasse) / 100);
        double Manamax = (managrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "mana"), PersistentDataType.DOUBLE)) * ((100 + Manarasse) / 100);
        return Ausdauemax>Manamax ? "ausdauer" : "mana";
    }

    public static void bar(Player p, String kostenwert, String passivname){

        //setup bar
        BossBar bar= Bukkit.createBossBar(passivname, BarColor.GREEN, BarStyle.SEGMENTED_10);
        bar.setProgress(1.0);
        bar.addPlayer(p);

        //get max wert
        int wertgrund= getcon(2).getInt("Grundwerte"+"."+kostenwert);
        double wertrasse=getcon(2).getDouble(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+"."+kostenwert);
        double wertmax = kostenwert=="leben" ? p.getMaxHealth() : (wertgrund + p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), kostenwert), PersistentDataType.DOUBLE)) * ((100 + wertrasse) / 100);

        p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "bar"), PersistentDataType.BOOLEAN,true);

        new BukkitRunnable(){
            @Override
            public void run() {

                if(!p.isOnline() || !p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "bar"), PersistentDataType.BOOLEAN)){
                    bar.setVisible(false);
                    bar.removePlayer(p);
                }

                double kostenwertnow=kostenwert=="leben" ? p.getHealth() : p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), kostenwert+"now"), PersistentDataType.DOUBLE);


                bar.setColor(kostenwertnow<=((wertmax/100.0)*33) ? BarColor.RED
                        : kostenwertnow<=((wertmax/100.0)*66) ? BarColor.YELLOW
                        : BarColor.GREEN);
                bar.setProgress(kostenwertnow<=((wertmax/100.0)*10) ? 0.1
                        : kostenwertnow<=((wertmax/100.0)*20) ? 0.2
                        : kostenwertnow<=((wertmax/100.0)*30) ? 0.3
                        : kostenwertnow<=((wertmax/100.0)*40) ? 0.4
                        : kostenwertnow<=((wertmax/100.0)*50) ? 0.5
                        : kostenwertnow<=((wertmax/100.0)*60) ? 0.6
                        : kostenwertnow<=((wertmax/100.0)*70) ? 0.7
                        : kostenwertnow<=((wertmax/100.0)*80) ? 0.8
                        : kostenwertnow<=((wertmax/100.0)*90) ? 0.9
                        : 1.0);

            }
        }.runTaskTimer(Rassensystem.getPlugin(),0,20);
    }

    public static void barremove(Player p){
        p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "bar"));
    }
}
