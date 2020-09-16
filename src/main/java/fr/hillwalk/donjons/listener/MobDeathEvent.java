package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.runnable.EndDonjons;
import fr.hillwalk.donjons.runnable.TimerLater;
import fr.hillwalk.donjons.runnable.TimerLoad;
import fr.hillwalk.donjons.utils.UtilsRef;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class MobDeathEvent implements Listener {


    Player player;


    @EventHandler (priority = EventPriority.HIGH)
    public void onMythicMobDeath(MythicMobDeathEvent e){

        if(DonjonsMain.worlds.isEmpty())return;
        if(!e.getMob().getLocation().getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))) return;
        if(!e.getMob().getDisplayName().contains(Mondes.getMondes(DonjonsMain.worlds.get(0)).getString("boss.name"))) return;

        BukkitRunnable task = new TimerLater();


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


        String word = Messages.getMessages().getString("worlds.deadBoss");
        String replace = word.replaceAll("%mob_name%", Mondes.getMondes(DonjonsMain.worlds.get(0)).getString("boss.name"));

        Bukkit.broadcastMessage(DonjonsMain.prefix + replace);

        if(DonjonsMain.instance.getConfig().getBoolean("spawnPortalSound.enable")){
            for(Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){
                player.playSound(player.getLocation(), Sound.valueOf(DonjonsMain.instance.getConfig().getString("spawnPortalSound.sound")), (float) DonjonsMain.instance.getConfig().getDouble("spawnPortalSound.volume"), (float) DonjonsMain.instance.getConfig().getDouble("spawnPortalSound.pitch"));
            }
        }

        task.runTaskLater(DonjonsMain.instance, TimeUnit.SECONDS.toSeconds(DonjonsMain.instance.getConfig().getLong("bossDeathTeleportation.delay")) * 20);



    }



}
