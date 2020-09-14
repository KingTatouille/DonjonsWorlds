package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigMondes;
import fr.hillwalk.donjons.runnable.EndDonjons;
import fr.hillwalk.donjons.utils.UtilsRef;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MobDeathEvent implements Listener {


    Player player;


    @EventHandler (priority = EventPriority.HIGH)
    public void onMythicMobDeath(MythicMobDeathEvent e){

        if(DonjonsMain.worlds.isEmpty())return;
        if(!e.getMob().getLocation().getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))) return;
        if(!e.getMob().getDisplayName().contains(ConfigMondes.getMondes(DonjonsMain.worlds.get(0)).getString("boss.name"))) return;

        BukkitRunnable task = new EndDonjons();


        for(Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){

            if(DonjonsMain.playerHits.contains(player.getName())){

                for(ItemStack item : e.getDrops()){

                    if(UtilsRef.randomNumber(2) == 1){
                    player.getInventory().addItem(item.clone());
                    }

                }


            }

        }
        e.getDrops().clear();


        task.runTaskTimer(DonjonsMain.instance, 20L,  20L);



    }



}
