package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.reflexion.Title;
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
import java.util.Timer;
import java.util.TimerTask;

import static sun.audio.AudioPlayer.player;

public class MobDeathEvent implements Listener {

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent e){

        if(DonjonsMain.worlds.isEmpty())return;
        if(!e.getMob().getLocation().getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))) return;
        if(!e.getMob().getDisplayName().equalsIgnoreCase(DonjonsMain.mobs.get(0))) return;

        final Title sendTitle = new Title();


        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){

            int number = 1;

            @Override
            public void run(){
                if(number == 0) {
                    Bukkit.broadcastMessage(DonjonsMain.prefix + DonjonsMain.mobs.get(0) + ChatColor.WHITE + " vient de succomber à ses blessures !");
                }


                for(Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){
                 sendTitle.sendTitle(player, String.valueOf(number++), 5, 5, 5, ChatColor.GREEN);
                }
                if(number == 5){
                    for(Player player : Bukkit.getServer().getOnlinePlayers()){
                        if(player.getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))){

                            player.teleport(new Location(Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockX(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockY(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockZ()));
                            player.sendMessage(DonjonsMain.prefix + "Le donjon est terminé ! Vous avez été téléporté au spawn !");

                        }

                    }
                    ConfigManager.get().set("OpenPortail", false);
                    ConfigManager.save();
                    timer.cancel();
                }
                number++;
            }
        }, 1000, 1000);

    }


}
