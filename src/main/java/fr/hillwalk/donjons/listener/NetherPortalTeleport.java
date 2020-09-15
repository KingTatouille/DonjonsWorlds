package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import fr.hillwalk.donjons.utils.UtilsRef;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class NetherPortalTeleport implements Listener {


    @EventHandler (priority = EventPriority.NORMAL)
    public void onTeleportation(PlayerTeleportEvent e){

        if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL){

            if(DonjonsMain.worlds.isEmpty())return;

           World world = Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0));

            if(world == null){
                e.setCancelled(true);

                e.getPlayer().sendMessage("Le portail n'envoie vers aucun monde ! Regardez la liste dans la config.");
                return;
            }


            if(UtilsRef.inCuboid(e.getPlayer().getLocation(), new Location(UtilsRef.principalWorld(), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x"), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y"), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")), new Location(UtilsRef.principalWorld(),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") + 5,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") + 5,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z") + 5))) {

                try{


                    e.setTo(new Location(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), world.getSpawnLocation().getBlockX() ,world.getSpawnLocation().getBlockY() ,world.getSpawnLocation().getBlockZ()));
                    e.getPlayer().sendMessage(DonjonsMain.instance.prefix + "Vous venez d'être téléporté dans le monde : " + world.getName());

                    if(!Informations.getInfos().getBoolean("discoverArea")){
                        onSummonMob();
                        Informations.getInfos().set("discoverArea", true);
                    } else {
                        return;
                    }


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

        if(Informations.getInfos().getBoolean("summonedBoss")){

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
                    DonjonsMain.mobLocation.put(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), entity.getLocation());

                    Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.name", UtilsRef.colorInfo(entity.getName()));
                    Mondes.save(DonjonsMain.worlds.get(0));
                    Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.x", entity.getLocation().getBlockX());
                    Mondes.save(DonjonsMain.worlds.get(0));
                    Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.y", entity.getLocation().getBlockY());
                    Mondes.save(DonjonsMain.worlds.get(0));
                    Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.z", entity.getLocation().getBlockZ());
                    Mondes.save(DonjonsMain.worlds.get(0));
                } catch (InvalidMobTypeException e) {
                    e.printStackTrace();
                }

                Bukkit.broadcastMessage(DonjonsMain.prefix + ChatColor.GOLD + mobName.toUpperCase() + ChatColor.WHITE + " vient d'apparaître en : " + "x: " + DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockX() + " y: " + DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockY() + " z: " + DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockZ());
                Informations.getInfos().set("summonedBoss", true);
                Informations.save();

            }
        }, DonjonsMain.instance.getConfig().getLong("timing"));

    }


}
