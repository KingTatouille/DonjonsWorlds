package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.generation.GenerationStructure;
import fr.hillwalk.donjons.utils.UtilsRef;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.TimeUnit;

public class NetherPortalTeleport implements Listener {


    @EventHandler (priority = EventPriority.NORMAL)
    public void onTeleportation(PlayerTeleportEvent e){

        if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL){

            if(DonjonsMain.worlds.isEmpty())return;

           World world = Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0));

            if(world == null){
                e.setCancelled(true);

                if(e.getPlayer().isOp()){

                    e.getPlayer().sendMessage(UtilsRef.colorInfo(Messages.getMessages().getString("errors.errorWorld")));

                }

                return;
            }


            if(!DonjonsMain.instance.getConfig().getBoolean("portalSpawn")){

            if(UtilsRef.inCuboid(e.getPlayer().getLocation(), new Location(UtilsRef.principalWorld(), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x"), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y"), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")), new Location(UtilsRef.principalWorld(),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") + 5,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") + 5,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z") + 5))) {

                try{

                       e.setTo(new Location(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), world.getSpawnLocation().getBlockX() ,world.getSpawnLocation().getBlockY() ,world.getSpawnLocation().getBlockZ()));

                       String teleportation = UtilsRef.colorInfo(Messages.getMessages().getString("teleportation.teleportationToWorld"));
                       String replaceTeleportation = teleportation.replaceAll("%world_name%", world.getName());


                       e.getPlayer().sendMessage(DonjonsMain.instance.prefix + replaceTeleportation);


                       //Section sound
                       if(DonjonsMain.instance.getConfig().getBoolean("changeWorld.enable")){

                           e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(DonjonsMain.instance.getConfig().getString("changeWorld.sound")), (float) DonjonsMain.instance.getConfig().getDouble("changeWorld.volume"), (float) DonjonsMain.instance.getConfig().getDouble("changeWorld.pitch"));

                       }

                       //Section Teleportation discover
                       if(!Informations.getInfos().getBoolean("DiscoverArea")){
                           onSummonMob();
                           Informations.getInfos().set("DiscoverArea", true);
                       } else {
                           return;
                       }




                } catch (NullPointerException ex){
                    ex.getStackTrace();
                    DonjonsMain.instance.getLogger().info("Please be carefull ! The world :" + world.getName() + " is null...");

                    if(e.getPlayer().isOp()){
                    e.getPlayer().sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo("&cPlease check the console for more informations."));
                    }
                }

                }
            } else {

                if(DonjonsMain.worlds.isEmpty()){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(DonjonsMain.prefix + Messages.getMessages().getString("errors.theWorld"));
                }

                Location locationWorld = new Location(UtilsRef.principalWorld(), Informations.getInfos().getInt("SpawnPortal.min.x"),Informations.getInfos().getInt("SpawnPortal.min.y"), Informations.getInfos().getInt("SpawnPortal.min.z"));

                if(UtilsRef.around(e.getPlayer().getLocation().getChunk(), 2).contains(locationWorld.getChunk())) {
                    System.out.println("Je suis bien dedans !");

                    try{

                        e.setTo(new Location(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), world.getSpawnLocation().getBlockX() ,world.getSpawnLocation().getBlockY() ,world.getSpawnLocation().getBlockZ()));

                        String teleportation = UtilsRef.colorInfo(Messages.getMessages().getString("teleportation.teleportationToWorld"));
                        String replaceTeleportation = teleportation.replaceAll("%world_name%", world.getName());


                        e.getPlayer().sendMessage(DonjonsMain.instance.prefix + replaceTeleportation);


                        //Section sound
                        if(DonjonsMain.instance.getConfig().getBoolean("changeWorld.enable")){

                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(DonjonsMain.instance.getConfig().getString("changeWorld.sound")), (float) DonjonsMain.instance.getConfig().getDouble("changeWorld.volume"), (float) DonjonsMain.instance.getConfig().getDouble("changeWorld.pitch"));

                        }

                        //Section Teleportation discover
                        if(!Informations.getInfos().getBoolean("DiscoverArea")){
                            onSummonMob();
                            Informations.getInfos().set("DiscoverArea", true);
                        } else {
                            return;
                        }




                    } catch (NullPointerException ex){
                        ex.getStackTrace();
                        DonjonsMain.instance.getLogger().info("Please be carefull ! The world :" + world.getName() + " is null...");

                        if(e.getPlayer().isOp()){
                            e.getPlayer().sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo("&cPlease check the console for more informations."));
                        }
                    }

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

                try {

                    String mobName = DonjonsMain.instance.getConfig().getStringList("summonBoss").get(UtilsRef.randomNumber(DonjonsMain.instance.getConfig().getStringList("summonBoss").size()));

                    if(DonjonsMain.instance.getConfig().getBoolean("bossSpawn.enabled")){

                        World world = Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0));


                        Location location = new Location(world,
                                DonjonsMain.instance.getConfig().getInt("bossSpawn." + DonjonsMain.worlds.get(0) + ".x"),
                                DonjonsMain.instance.getConfig().getInt("bossSpawn." + DonjonsMain.worlds.get(0) + ".y"),
                                DonjonsMain.instance.getConfig().getInt("bossSpawn." + DonjonsMain.worlds.get(0) + ".z"));

                        DonjonsMain.mobSpawn.put(mobName, location);

                        Entity entity = MythicMobs.inst().getAPIHelper().spawnMythicMob(mobName, DonjonsMain.mobSpawn.get(mobName));
                        DonjonsMain.mobLocation.put(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), entity.getLocation());


                    } else {

                        GenerationStructure generation = new GenerationStructure();
                        Location location = generation.findSafeLocationMob(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)).getSpawnLocation());

                        DonjonsMain.mobSpawn.put(mobName, location);


                        Location mob = DonjonsMain.mobSpawn.get(mobName);

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

                    }

                    switch (DonjonsMain.instance.getConfig().getString("spawnBoss.method")){
                        case "TITLE":

                            String title = UtilsRef.colorInfo(DonjonsMain.instance.getConfig().getString("spawnBoss.customTitle"));
                            String repalceTitle = title.replaceAll("%mobName%", mobName)
                                    .replaceAll("%mob_location_X%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockX()))
                                    .replaceAll("%mob_location_Y%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockY()))
                                    .replaceAll("%mob_location_Z%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockZ()));



                            if(DonjonsMain.instance.getConfig().getString("BossAppear.customSubTitle") == "NULL") {

                                for(Player player : Bukkit.getServer().getOnlinePlayers()){

                                    player.sendTitle(repalceTitle, null, DonjonsMain.instance.getConfig().getInt("spawnBoss.fadeIn"),
                                            DonjonsMain.instance.getConfig().getInt("spawnBoss.stay"),
                                            DonjonsMain.instance.getConfig().getInt("spawnBoss.fadeOut"));

                                }

                            } else {

                                String subTitle = UtilsRef.colorInfo(DonjonsMain.instance.getConfig().getString("spawnBoss.customSubTitle"));
                                String replaceSubTitle = subTitle.replaceAll("%mob_name%", mobName)
                                        .replaceAll("%mob_location_X%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockX()))
                                        .replaceAll("%mob_location_Y%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockY()))
                                        .replaceAll("%mob_location_Z%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockZ()));

                                for(Player player : Bukkit.getServer().getOnlinePlayers()){

                                    player.sendTitle(repalceTitle, replaceSubTitle,DonjonsMain.instance.getConfig().getInt("spawnBoss.fadeIn"),
                                            DonjonsMain.instance.getConfig().getInt("spawnBoss.stay"),
                                            DonjonsMain.instance.getConfig().getInt("spawnBoss.fadeOut"));

                                }

                            }

                            break;

                        case "BROADCAST" :

                            String messageBroadcast = DonjonsMain.instance.getConfig().getString("spawnBoss.customMessage");
                            String replacemessageBroadcast = messageBroadcast.replaceAll("%mob_name%", mobName)
                                    .replaceAll("%mob_location_X%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockX()))
                                    .replaceAll("%mob_location_Y%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockY()))
                                    .replaceAll("%mob_location_Z%", String.valueOf(DonjonsMain.mobLocation.get(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0))).getBlockZ()));


                            Bukkit.broadcastMessage(UtilsRef.colorInfo(replacemessageBroadcast));
                            break;


                    }
                } catch (InvalidMobTypeException e) {
                    e.printStackTrace();
                }



                Informations.getInfos().set("summonedBoss", true);
                Informations.save();

            }
        }, TimeUnit.SECONDS.toSeconds(DonjonsMain.instance.getConfig().getLong("timing")) * 20);

    }


}
