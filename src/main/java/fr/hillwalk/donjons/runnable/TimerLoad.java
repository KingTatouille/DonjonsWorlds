package fr.hillwalk.donjons.runnable;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.generation.GenerationStructure;
import fr.hillwalk.donjons.generation.Selection;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TimerLoad extends BukkitRunnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */



    @Override
    public void run() {



        if(Bukkit.getServer().getOnlinePlayers().isEmpty()){
            if(!Informations.getInfos().getBoolean("OpenPortail")){
                if(UtilsRef.randomNumber(2) == 1){
                    DonjonsMain.instance.getLogger().info("No players online !");
                }
            }


            return;

        }


        if(!Informations.getInfos().getBoolean("OpenPortail")){

            if(DonjonsMain.instance.getConfig().getBoolean("portalSpawn")){

                if(Informations.getInfos().getString("SpawnPortal.name") == null){

                    DonjonsMain.instance.getLogger().warning("The portal is not set!");
                    return;

                }
                GenerationStructure ref = new GenerationStructure();
                    ref.startEvents();
                Informations.getInfos().set("OpenPortail", true);
                Informations.save();

            } else {

                BukkitRunnable loadSchematic = new SchematicLoad();
                loadSchematic.runTaskLater(DonjonsMain.instance, TimeUnit.SECONDS.toSeconds(DonjonsMain.instance.getConfig().getLong("timing")) * 20);

                String portail = UtilsRef.colorInfo(Messages.getMessages().getString("worlds.portal"));
                String replacePortal = portail.replaceAll("%seconds%", UtilsRef.timerMessage(DonjonsMain.instance.getConfig().getInt("timing")));


                Bukkit.broadcastMessage(DonjonsMain.prefix + replacePortal);
                Informations.getInfos().set("OpenPortail", true);
                Informations.save();

            }


        } else {
            return;
        }
    }

}
