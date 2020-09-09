package fr.hillwalk.donjons.runnable;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.reflexion.Title;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
            if(UtilsRef.randomNumber(2) == 1){
                DonjonsMain.instance.getLogger().info("No players online !");
            }

            return;

        }


        if(ConfigManager.get().getBoolean("OpenPortail")){

        } else {
            Bukkit.broadcastMessage("Attention ! \nUn portail va apparaître dans : " + ChatColor.GOLD +  UtilsRef.timerMessage(DonjonsMain.instance.getConfig().getInt("durationSeconds")));
            ConfigManager.get().set("OpenPortail", true);
            ConfigManager.save();
        }
    }

}
