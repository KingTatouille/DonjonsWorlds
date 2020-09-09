package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;

public class MobDeathEvent implements Listener {

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent e){

        if(DonjonsMain.worlds.isEmpty())return;
        if(!e.getMob().getLocation().getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))) return;
        if(!e.getMob().getDisplayName().equalsIgnoreCase(DonjonsMain.mobs.get(0))) return;


        final BukkitScheduler scheduler = DonjonsMain.instance.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(DonjonsMain.instance, new Runnable() {

            int number = 0;


            @Override
            public void run() {

                if(number == 0) {
                    Bukkit.broadcastMessage(DonjonsMain.prefix + DonjonsMain.mobs.get(0) + ChatColor.WHITE + " vient de succomber à ses blessures !");
                }

                Bukkit.broadcastMessage(DonjonsMain.prefix + "Vous allez être téléporté dans : " + ChatColor.GREEN + number);

                if(number == 5){
                   for(Player player : Bukkit.getServer().getOnlinePlayers()){
                       if(player.getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))){

                           player.teleport(new Location(Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockX(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockY(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockZ()));
                           player.sendMessage(DonjonsMain.prefix + "Le donjon est terminé ! Vous avez été téléporté au spawn !");

                       }

                   }
                    ConfigManager.get().set("OpenPortail", false);
                    ConfigManager.save();
                   scheduler.cancelTasks(DonjonsMain.instance);
                }
                number++;
            }

        }, 20L, 20L);

    }


    public void unloadWorld(){

        Bukkit.getServer().unloadWorld(DonjonsMain.worlds.get(0), false);
        DonjonsMain.instance.getLogger().info("The world : " +  DonjonsMain.worlds.get(0) + " is now unloaded.");


    }

}
