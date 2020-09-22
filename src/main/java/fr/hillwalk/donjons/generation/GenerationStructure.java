package fr.hillwalk.donjons.generation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class GenerationStructure {


    public HashSet<Material> blockPrevent = new HashSet<Material>();

    {
        for(String block : DonjonsMain.instance.getConfig().getStringList("materials")){
            if(blockPrevent.containsAll(DonjonsMain.instance.getConfig().getStringList("materials"))){
                break;
            }

            blockPrevent.add(Material.valueOf(block));

        }
    }


    public void loadSchematic() throws IOException {
        DonjonsMain.worlds.add(UtilsRef.randomWorlds());

        try{
            Location location = findSafeLocation(Bukkit.getServer().getWorld(UtilsRef.principalWorld().getName()).getSpawnLocation());
        } catch(NullPointerException ex){
            ex.getStackTrace();
            DonjonsMain.instance.getLogger().warning("The name of this world: " + UtilsRef.principalWorld().getName() + " have a problem...");
        }


        File schematic = new File(DonjonsMain.instance.getDataFolder().getAbsoluteFile() + "/schematics/portal.schematic");

        if(!schematic.exists()){

            DonjonsMain.instance.getLogger().warning("The schematic doesn't not exist.");

            return;
        }

        /*

            On crée une copy de la zone avant de faire quoi que ce soit

         */

        World world = new BukkitWorld(UtilsRef.principalWorld());

        BlockVector3 min = BlockVector3.at(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x"),
                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") - 15,
                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z"));
        BlockVector3 max = BlockVector3.at(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") + 8,
                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") + 15,
                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z") + 6);

            GenerationSchematic.copy(world, min, max);

         /*

            On met la schematic

         */

        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession( new BukkitWorld(UtilsRef.principalWorld()), -1)) {

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x"),
                                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y"),
                                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")))
                        .ignoreAirBlocks(false)
                        .build();


                switch (DonjonsMain.instance.getConfig().getString("spawnPortal.method")){
                    case "TITLE":

                        String title = UtilsRef.colorInfo(DonjonsMain.instance.getConfig().getString("spawnPortal.customTitle"));
                        String repalceTitle = title.replaceAll("%portal_location_X%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x")))
                                .replaceAll("%portal_location_Y%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y")))
                                .replaceAll("%portal_location_Z%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")));


                        if(DonjonsMain.instance.getConfig().getString("spawnPortal.customSubTitle") == "NULL") {

                            for(Player player : Bukkit.getServer().getOnlinePlayers()){

                                player.sendTitle(repalceTitle, null, DonjonsMain.instance.getConfig().getInt("spawnPortal.fadeIn"),
                                        DonjonsMain.instance.getConfig().getInt("spawnPortal.stay"),
                                        DonjonsMain.instance.getConfig().getInt("spawnPortal.fadeOut"));

                            }

                        } else {

                            String subTitle = UtilsRef.colorInfo(DonjonsMain.instance.getConfig().getString("spawnPortal.customSubTitle"));
                            String replaceSubTitle = subTitle.replaceAll("%portal_location_X%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x")))
                                    .replaceAll("%portal_location_Y%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y")))
                                    .replaceAll("%portal_location_Z%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")));

                            for(Player player : Bukkit.getServer().getOnlinePlayers()){

                                player.sendTitle(repalceTitle, replaceSubTitle,DonjonsMain.instance.getConfig().getInt("spawnBoss.fadeIn"),
                                        DonjonsMain.instance.getConfig().getInt("spawnBoss.stay"),
                                        DonjonsMain.instance.getConfig().getInt("spawnBoss.fadeOut"));

                            }

                        }


                        break;

                    case "BROADCAST" :

                        String messageBroadcast = DonjonsMain.instance.getConfig().getString("spawnPortal.customMessage");
                        String replacemessageBroadcast = messageBroadcast.replaceAll("%portal_location_X%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x")))
                                .replaceAll("%portal_location_Y%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y")))
                                .replaceAll("%portal_location_Z%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")));


                        Bukkit.broadcastMessage(DonjonsMain.prefix + UtilsRef.colorInfo(replacemessageBroadcast));
                        break;


                }


                Operations.complete(operation);

                if(DonjonsMain.instance.getConfig().getBoolean("spawnPortalSound.enable")){
                    for(Player player : Bukkit.getServer().getOnlinePlayers()){
                        player.playSound(player.getLocation(), Sound.valueOf(DonjonsMain.instance.getConfig().getString("spawnPortalSound.sound")), (float) DonjonsMain.instance.getConfig().getDouble("spawnPortalSound.volume"), (float) DonjonsMain.instance.getConfig().getDouble("spawnPortalSound.pitch"));
                    }
                }

                createRegion();

                //Loading du monde
                Bukkit.getServer().createWorld((new WorldCreator(DonjonsMain.worlds.get(0))));



            } catch (WorldEditException e) {

                e.printStackTrace();
            }

            //Section du portail
            Location loc1 = new Location(UtilsRef.principalWorld(),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") + 4,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") + 4,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z") + 2);
            Location loc2 = new Location(UtilsRef.principalWorld(),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") + 3,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") + 2,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z") + 2);

            Selection sel = new Selection();
            sel.selectedArea(loc1, loc2);


            //Section de la dirt
            Location location1 = new Location(UtilsRef.principalWorld(),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x"),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y"),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z"));
            Location location2 = new Location(UtilsRef.principalWorld(),
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") + 7,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") - 15,
                    Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z") + 4);

            Selection addDirt = new Selection();
            sel.selectBelow(location1, location2);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startEvents() {
        DonjonsMain.worlds.add(UtilsRef.randomWorlds());


        switch (DonjonsMain.instance.getConfig().getString("portalSpawnLocal.method")){
            case "TITLE":


                if(DonjonsMain.instance.getConfig().getString("portalSpawnLocal.customSubTitle") == "NULL") {

                    for(Player player : Bukkit.getServer().getOnlinePlayers()){

                        player.sendTitle(DonjonsMain.instance.getConfig().getString("portalSpawnLocal.customTitle"), null, DonjonsMain.instance.getConfig().getInt("portalSpawnLocal.fadeIn"),
                                DonjonsMain.instance.getConfig().getInt("portalSpawnLocal.stay"),
                                DonjonsMain.instance.getConfig().getInt("portalSpawnLocal.fadeOut"));

                    }

                } else {

                    for(Player player : Bukkit.getServer().getOnlinePlayers()){

                        player.sendTitle(DonjonsMain.instance.getConfig().getString("portalSpawnLocal.customTitle"), DonjonsMain.instance.getConfig().getString("portalSpawnLocal.customSubTitle"),DonjonsMain.instance.getConfig().getInt("portalSpawnLocal.fadeIn"),
                                DonjonsMain.instance.getConfig().getInt("portalSpawnLocal.stay"),
                                DonjonsMain.instance.getConfig().getInt("portalSpawnLocal.fadeOut"));

                    }

                }


                break;

            case "BROADCAST" :

                Bukkit.broadcastMessage(DonjonsMain.prefix + UtilsRef.colorInfo(DonjonsMain.instance.getConfig().getString("portalSpawnLocal.customMessage")));
                break;


        }


        if(DonjonsMain.instance.getConfig().getBoolean("spawnPortalSound.enable")){
            for(Player player : Bukkit.getServer().getOnlinePlayers()){
                player.playSound(player.getLocation(), Sound.valueOf(DonjonsMain.instance.getConfig().getString("spawnPortalSound.sound")), (float) DonjonsMain.instance.getConfig().getDouble("spawnPortalSound.volume"), (float) DonjonsMain.instance.getConfig().getDouble("spawnPortalSound.pitch"));
            }
        }

        //Loading du monde
        Bukkit.getServer().createWorld((new WorldCreator(DonjonsMain.worlds.get(0))));

    }


    public static void pasteChem(){

        File schematic = new File(DonjonsMain.instance.getDataFolder().getAbsoluteFile() + "/schematics/save.schematic");

        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession( new BukkitWorld(UtilsRef.principalWorld()), -1)) {

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x"),
                                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") - 15,
                                Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")))
                        .ignoreAirBlocks(false)
                        .build();

                Operations.complete(operation);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }



    /*


                Crédit : https://youtu.be/15ma_0wmAP4


     */

    public Location randomTeleport(Location location){

        Random random = new Random();


        String negative = String.valueOf(DonjonsMain.instance.getConfig().getInt("minZ"));
        String replaceNegative = negative.replaceAll("-", "");
        String negative1 = String.valueOf(DonjonsMain.instance.getConfig().getInt("minX"));
        String replaceNegative1 = negative.replaceAll("-", "");

        int calculX = random.nextInt((Integer.valueOf(replaceNegative1) + DonjonsMain.instance.getConfig().getInt("maxX")) / (random.nextInt(3) + 2));
        int calculZ = random.nextInt((Integer.valueOf(replaceNegative) + DonjonsMain.instance.getConfig().getInt("maxZ")) / (random.nextInt(3) + 2));

        int x = 0;
        int y = 150;
        int z = 0;

        int r = random.nextInt(4) + 1;

        if(r == 1){

            x = -calculX;
            z = calculZ;
        } else if(r == 2){
            x = calculX;
            z = -calculZ;
        } else if(r == 3){
            x = -calculX;
            z = -calculZ;
        }
        else if(r == 4){
            x = calculX;
            z = calculZ;
        }

        Location randomLoc = new Location(Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world")), x,y,z);

        y = randomLoc.getWorld().getHighestBlockYAt(randomLoc);
        randomLoc.setY(y);


        return randomLoc;
    }

    public Location randomTeleportMob(){

        Random random = new Random();


        String negative = String.valueOf(DonjonsMain.instance.getConfig().getInt("boss.minZ"));
        String replaceNegative = negative.replaceAll("-", "");
        String negative1 = String.valueOf(DonjonsMain.instance.getConfig().getInt("boss.minX"));
        String replaceNegative1 = negative.replaceAll("-", "");

        int calculX = random.nextInt((Integer.valueOf(replaceNegative1) + DonjonsMain.instance.getConfig().getInt("boss.maxX")) / (random.nextInt(3) + 2));
        int calculZ = random.nextInt((Integer.valueOf(replaceNegative) + DonjonsMain.instance.getConfig().getInt("boss.maxZ")) / (random.nextInt(3) + 2));

        int x = 0;
        int y = 150;
        int z = 0;

        int r = random.nextInt(4) + 1;

        if(r == 1){

            x = -calculX;
            z = calculZ;
        } else if(r == 2){
            x = calculX;
            z = -calculZ;
        } else if(r == 3){
            x = -calculX;
            z = -calculZ;
        }
        else if(r == 4){
            x = calculX;
            z = calculZ;
        }

        Location randomLoc = new Location(Bukkit.getServer().getWorld(DonjonsMain.worlds.get(0)), x,y,z);

        y = randomLoc.getWorld().getHighestBlockYAt(randomLoc);
        randomLoc.setY(y + 1);


        return randomLoc;
    }


    //On trouve la zone safe
    public Location findSafeLocation(Location location){



        Location randomLocation = randomTeleport(location);

        while (!isLocationSafe(randomLocation)){
            //Keep looking for a safe location
            randomLocation = randomTeleport(location);
        }



        if(DonjonsMain.instance.getConfig().getBoolean("portalSpawn.enabled")) {

            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.world", DonjonsMain.instance.getConfig().getString("portalSpawn.world"));
            Mondes.save(UtilsRef.principalWorld().getName());
            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.x", DonjonsMain.instance.getConfig().getInt("portalSpawn.x"));
            Mondes.save(UtilsRef.principalWorld().getName());
            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.y", DonjonsMain.instance.getConfig().getInt("portalSpawn.y"));
            Mondes.save(UtilsRef.principalWorld().getName());
            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.z", DonjonsMain.instance.getConfig().getInt("portalSpawn.z"));
            Mondes.save(UtilsRef.principalWorld().getName());


        } else {

            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.world", randomLocation.getWorld().getName());
            Mondes.save(UtilsRef.principalWorld().getName());
            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.x", randomLocation.getBlockX());
            Mondes.save(UtilsRef.principalWorld().getName());
            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.y", randomLocation.getBlockY());
            Mondes.save(UtilsRef.principalWorld().getName());
            Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.z", randomLocation.getBlockZ());
            Mondes.save(UtilsRef.principalWorld().getName());

        }


        return randomLocation;
    }

    public Location findSafeLocationMob(Location location){

        location = randomTeleportMob();

        while (!isLocationSafe(location)){
            //Keep looking for a safe location
            location = randomTeleportMob();
        }

        return location;
    }

    //On voit si elle est capable d'accueillir un joueur.
    public boolean isLocationSafe(Location location){

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();



        //Blocs aux alentours
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x - 7, y - 1, z - 6);
        Block above = location.getWorld().getBlockAt(x + 7, y + 1, z + 6);

        //Check to see if the surroundings are safe or not
        if(DonjonsMain.instance.getConfig().getBoolean("reverse")){
            return blockPrevent.contains(below.getType()) || !block.getType().isFlammable() || !block.getType().isBurnable()
                    || !above.getType().isFlammable() || !above.getType().isBurnable();
        } else {
            return !(blockPrevent.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
        }
    }

    public void createRegion(){

        BlockVector3 minimum = BlockVector3.at(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x"), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y"), Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z"));
        BlockVector3 maximum = BlockVector3.at(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") + 5, Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") + 256, Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z") + 5);
        ProtectedRegion region = new ProtectedCuboidRegion("portail", minimum, maximum);
        region.setFlag(Flags.VINE_GROWTH, StateFlag.State.DENY);
        region.setFlag(Flags.BUILD, StateFlag.State.DENY);
        region.setFlag(Flags.PVP, StateFlag.State.DENY);
        region.setFlag(Flags.TNT, StateFlag.State.DENY);
        region.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
        region.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);


        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(new BukkitWorld(UtilsRef.principalWorld()));
        regions.addRegion(region);

    }


}
