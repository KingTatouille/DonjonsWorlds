package fr.hillwalk.donjons.runnable;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.reflexion.Title;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndDonjons extends BukkitRunnable {
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

    Title sendTitle = new Title();
    int number = 1;

    @Override
    public void run() {
        if(number == 1) {
            Bukkit.broadcastMessage(DonjonsMain.prefix + DonjonsMain.mobs.get(0) + ChatColor.WHITE + " vient de succomber à ses blessures !");
        }

       for(Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){
            sendTitle.sendTitle(player, "&4" + String.valueOf(number), 1, 1, 1, ChatColor.GREEN);
       }

        if(number == 5){
            for(Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){
                if(player.getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))){

                    player.teleport(new Location(Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockX(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockY(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockZ()));
                    player.sendMessage(DonjonsMain.prefix + "Le donjon est terminé ! Vous avez été téléporté au spawn !");

                }

            }
            removeAll();
            this.cancel();

        }
        number++;
    }


    public void removeAll(){


            GenerationStructure.pasteChem();
            ConfigManager.get().set("location", null);
            ConfigManager.get().set("location.x", null);
            ConfigManager.get().set("location.y", null);
            ConfigManager.get().set("location.z", null);
            ConfigManager.get().set("OpenPortail", false);
            ConfigManager.get().set("summonedBoss", null);
            ConfigManager.save();

        DonjonsMain.listPos.clear();
        DonjonsMain.undoShematic.clear();
        DonjonsMain.mobs.clear();
        DonjonsMain.mobSpawn.clear();
        DonjonsMain.playerHits.clear();
        DonjonsMain.worlds.clear();
        DonjonsMain.mobLocation.clear();


    }




}
