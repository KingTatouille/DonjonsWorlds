package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigMondes;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HitEntity implements Listener {

    @EventHandler
    public void onHitMythicMob(EntityDamageByEntityEvent e){

        if(DonjonsMain.worlds.isEmpty())return;
        if(!e.getEntity().getLocation().getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))) return;

        if(e.getEntity().getName().contains(ConfigMondes.getMondes(DonjonsMain.worlds.get(0)).getString("boss.name"))){
                for(Entity entity : e.getEntity().getNearbyEntities(20, 20, 20)){

                    for (Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){

                        if(!e.getEntity().getName().equalsIgnoreCase(player.getName()) && !DonjonsMain.playerHits.contains(player.getName())){
                            DonjonsMain.playerHits.add(player.getName());
                        }

                    }

            }
                System.out.println(DonjonsMain.playerHits);

        }

    }



}
