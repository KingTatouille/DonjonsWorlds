package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.runnable.EndDonjons;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class MobDeathEvent implements Listener {

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent e){

        if(DonjonsMain.worlds.isEmpty())return;
        System.out.println("Je suis en preimier");
        if(!e.getMob().getLocation().getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))) return;
        System.out.println("Je suis en second");
        if(!e.getMob().getDisplayName().equalsIgnoreCase(DonjonsMain.mobs.get(0))) return;
        System.out.println("Je suis en Third");

        BukkitRunnable task = new EndDonjons();

        task.runTaskTimer(DonjonsMain.instance, 20L,  20L);



    }


}
