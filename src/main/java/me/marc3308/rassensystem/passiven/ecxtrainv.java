package me.marc3308.rassensystem.passiven;

import me.marc3308.rassensystem.Rassensystem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static me.marc3308.rassensystem.ItemCreater.getcon;

public class ecxtrainv implements Listener {

    @EventHandler
    public void onclick(PlayerInteractEvent e){
        Player p=e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING))return;
        if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"), PersistentDataType.BOOLEAN))return;
        if(!p.getGameMode().equals(GameMode.SURVIVAL))return;
        if(!p.getInventory().getItemInMainHand().getType().equals(Material.AIR))return;
        if(e.getAction().isLeftClick())return;
        if(!p.isSneaking())return;

        //check if rasse has passive
        for(int i=0;i<25;i++){
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1))==null)return;
            if(getcon(2).getString(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(),"rasse"), PersistentDataType.STRING)+".passiven"+"."+(i+1)).equals("extrainv"))break;
        }
        Inventory extrainf= Bukkit.createInventory(p,getcon(9).getInt("extrainv"+".Größe"), getcon(9).getString("extrainv"+".AnzeigeName"));


        for (int i=0; i<getcon(9).getInt("extrainv"+".Größe");i++){
            if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"+i), PersistentDataType.STRING)){
                try {
                    extrainf.setItem(i,itemStackFromBase64(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"+i), PersistentDataType.STRING)));

                } catch (IOException | ClassNotFoundException es) {
                    es.printStackTrace();
                }
            }
        }
        p.openInventory(extrainf);

    }

    @EventHandler
    public void onclose(InventoryCloseEvent e){
        if(!e.getView().getTitle().equalsIgnoreCase(getcon(9).getString("extrainv"+".AnzeigeName")))return;
        if(!(e.getPlayer() instanceof  Player))return;
        Player p= (Player) e.getPlayer();

        for (int i=0; i<getcon(9).getInt("extrainv"+".Größe");i++){
            if(e.getInventory().getItem(i)!=null){
                try {
                    // Serialize the ItemStack to a Base64 string
                    p.getPersistentDataContainer().set(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"+i), PersistentDataType.STRING,itemStackToBase64(e.getInventory().getItem(i)));
                } catch (IOException es) {
                    es.printStackTrace();
                    p.sendMessage("Failed to save the ItemStack.");
                }
            } else {
                p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"+i));
            }
        }

    }

    @EventHandler
    public void ondeath(PlayerDeathEvent e){
        Player p=e.getPlayer();

        for (int i=0; i<getcon(9).getInt("extrainv"+".Größe");i++){
            if(p.getPersistentDataContainer().has(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"+i), PersistentDataType.STRING)){
                try {
                    p.getWorld().dropItemNaturally(p.getLocation(),itemStackFromBase64(p.getPersistentDataContainer().get(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"+i), PersistentDataType.STRING)));
                    p.getPersistentDataContainer().remove(new NamespacedKey(Rassensystem.getPlugin(), "extrainv"+i));
                } catch (IOException | ClassNotFoundException es) {
                    es.printStackTrace();
                }
            }
        }
    }





    // Convert an ItemStack to a Base64 String (serialization)
    public String itemStackToBase64(ItemStack item) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

        // Write the ItemStack to the output stream
        bukkitObjectOutputStream.writeObject(item);
        bukkitObjectOutputStream.close();

        // Encode the byte array to a Base64 string
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    // Convert a Base64 String back to an ItemStack (deserialization)
    public ItemStack itemStackFromBase64(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);

        // Read the ItemStack object from the input stream
        ItemStack item = (ItemStack) bukkitObjectInputStream.readObject();
        bukkitObjectInputStream.close();

        return item;
    }
}
