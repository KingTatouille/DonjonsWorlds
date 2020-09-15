package fr.hillwalk.donjons.runnable;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.configs.Mondes;
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

    int number = 1;


    @Override
    public void run() {
        if(number == 1) {
            Bukkit.broadcastMessage(DonjonsMain.prefix + Mondes.getMondes(DonjonsMain.worlds.get(0)).getString("boss.name") + ChatColor.WHITE + " vient de succomber à ses blessures !");
        }

       for(Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){
            player.sendTitle(ChatColor.GREEN + String.valueOf(number), null, 10, 40, 10);
       }

        if(number == 5){
            for(Player player : Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getPlayers()){

                    player.teleport(new Location(Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockX(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockY(), Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")).getSpawnLocation().getBlockZ()));
                    player.sendMessage(DonjonsMain.prefix + "Le donjon est terminé ! Vous avez été téléporté au spawn !");

            }

            unloadWorld();
            removeAll();
            this.cancel();

        }
        number++;
    }


    private void removeAll(){


            GenerationStructure.pasteChem();
            Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location", null);
            Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.x", null);
            Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.y", null);
            Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.z", null);
            Informations.getInfos().set("OpenPortail", false);
            Informations.getInfos().set("DiscoverArea", null);
            Informations.getInfos().set("summonedBoss", null);
            Informations.save();

        DonjonsMain.mobSpawn.clear();
        DonjonsMain.playerHits.clear();
        DonjonsMain.worlds.clear();
        DonjonsMain.mobLocation.clear();


    }

    private void unloadWorld(){

        Bukkit.getServer().unloadWorld(DonjonsMain.worlds.get(0), false);

    }




}
