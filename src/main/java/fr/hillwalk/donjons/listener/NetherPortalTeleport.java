package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.teleportation.GenerationSchematic;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import fr.hillwalk.donjons.teleportation.Selection;
import fr.hillwalk.donjons.utils.UtilsRef;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class NetherPortalTeleport implements Listener {


    @EventHandler (priority = EventPriority.NORMAL)
    public void onTeleportation(PlayerTeleportEvent e){

        if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL){

           if(DonjonsMain.worlds.isEmpty()){
               World world = UtilsRef.randomWorlds();
               DonjonsMain.worlds.add(world.getName());
           }


           World world = Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0));

            if(world == null){
                e.setCancelled(true);

                e.getPlayer().sendMessage("Le portail n'envoie vers aucun monde ! Regardez la liste dans la config.");
                return;
            }

            if(UtilsRef.inCuboid(e.getPlayer().getLocation(), DonjonsMain.listPos.get(UtilsRef.principalWorld()), new Location(UtilsRef.principalWorld(),
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockX() + 5,
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockY() + 5,
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockZ() + 5))) {

                try{


                    e.setTo(new Location(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), world.getSpawnLocation().getBlockX() ,world.getSpawnLocation().getBlockY() ,world.getSpawnLocation().getBlockZ()));
                    e.getPlayer().sendMessage(DonjonsMain.instance.prefix + "Vous venez d'être téléporté dans le monde : " + world.getName());
                    onSummonMob();

                } catch (NullPointerException ex){
                    ex.getStackTrace();
                    DonjonsMain.instance.getLogger().info("Please be carefull ! The world :" + world.getName() + " is null...");
                    e.getPlayer().sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo("&cPlease check the console for more informations."));
                }

            }

        } else {
            return;
        }

    }

    public void onSummonMob(){

        if(ConfigManager.get().getBoolean("summonedBoss")){

            return;

        }

        BukkitScheduler scheduler = DonjonsMain.instance.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(DonjonsMain.instance, new Runnable() {
            @Override
            public void run() {

                GenerationStructure generation = new GenerationStructure();

                String mobName = DonjonsMain.instance.getConfig().getStringList("summonBoss").get(UtilsRef.randomNumber(DonjonsMain.instance.getConfig().getStringList("summonBoss").size()));
                Location location = generation.findSafeLocationMob(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getSpawnLocation());

                DonjonsMain.mobSpawn.put(mobName, location);


                Location mob = DonjonsMain.mobSpawn.get(mobName);

                try {
                    Entity entity = MythicMobs.inst().getAPIHelper().spawnMythicMob(mobName, DonjonsMain.mobSpawn.get(mobName));
                    System.out.println(DonjonsMain.worlds.get(0));
                    DonjonsMain.mobLocation.put(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), entity.getLocation());

                    DonjonsMain.mobs.add(UtilsRef.colorInfo(entity.getName()));
                } catch (InvalidMobTypeException e) {
                    e.printStackTrace();
                }

                Bukkit.broadcastMessage(DonjonsMain.prefix + mobName + " vient d'apparaître en : " + "x: " + DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockX() + " y: " + DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockY() + " z: " + DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockZ());
                ConfigManager.get().set("summonedBoss", true);
                ConfigManager.save();

            }
        }, DonjonsMain.instance.getConfig().getLong("timing"));

    }


}
