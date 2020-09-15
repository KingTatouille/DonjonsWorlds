package fr.hillwalk.donjons.runnable;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

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
            BukkitRunnable loadSchematic = new SchematicLoad();
            loadSchematic.runTaskLater(DonjonsMain.instance, DonjonsMain.instance.getConfig().getLong("timing"));
            Bukkit.broadcastMessage("Attention ! \nUn portail va apparaître dans : " + ChatColor.GOLD +  UtilsRef.timerMessage(DonjonsMain.instance.getConfig().getInt("durationSeconds")));
            Informations.getInfos().set("OpenPortail", true);
            Informations.save();

        }
    }

}
