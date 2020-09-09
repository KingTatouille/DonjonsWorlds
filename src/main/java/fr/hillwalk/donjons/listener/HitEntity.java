package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
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
        if(!e.getEntity().getName().equalsIgnoreCase(DonjonsMain.mobs.get(0))) return;


            Player player = (Player) e.getEntity();


            //Quand le joueur touche le monstre il sera mit dans la
            if(!DonjonsMain.playerHits.contains(player.getUniqueId())){

                DonjonsMain.playerHits.add(player.getUniqueId());
                System.out.println(DonjonsMain.playerHits);

        }

    }

}
