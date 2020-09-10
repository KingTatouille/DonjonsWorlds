package fr.hillwalk.donjons.teleportation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
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
import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class GenerationStructure {


    public HashSet<Material> blockPrevent = new HashSet<>();

    {
        for(String block : DonjonsMain.instance.getConfig().getStringList("materials")){
            if(blockPrevent.containsAll(DonjonsMain.instance.getConfig().getStringList("materials"))){
                break;
            }

            blockPrevent.add(Material.valueOf(block));

        }
    }


    public void loadSchematic() throws IOException {

        Location location = findSafeLocation(Bukkit.getServer().getWorld(UtilsRef.principalWorld().getName()).getSpawnLocation());

        File schematic = new File(DonjonsMain.instance.getDataFolder().getAbsoluteFile() + "/schematics/portal.schematic");

        if(!schematic.exists()){

            DonjonsMain.instance.getLogger().info("The schematic doesn't not exist.");

            return;
        }

        /*

            On crée une copy de la zone avant de faire quoi que ce soit

         */

        World world = new BukkitWorld(UtilsRef.principalWorld());

        BlockVector3 min = BlockVector3.at(DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockX(),
                DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockY(),
                DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockZ());
        BlockVector3 max = BlockVector3.at(DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockX() + 7,
                DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockY() + 256,
                DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockZ() - 5);

        GenerationSchematic.copy(world, min, max);

         /*

            On met la schematic

         */

        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession( new BukkitWorld(DonjonsMain.listPos.get(UtilsRef.principalWorld()).getWorld()), -1)) {

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockX(),
                                DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockY(),
                                DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockZ()))
                        .ignoreAirBlocks(false)
                        .build();


                Bukkit.broadcastMessage(UtilsRef.colorInfo("&cAttention ! \n&fUn portail vient d'apparaître en "
                        + "x: &2" + DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockX()
                        + " &fy: &2" + DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockY()
                        + " &fz: &2" + DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockZ()));



                DonjonsMain.undoShematic.put(UtilsRef.principalWorld(), editSession);
                Operations.complete(operation);


                /*

                    Après que la structure soit générée

                 */


            } catch (WorldEditException e) {
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
            Location loc1 = new Location(UtilsRef.principalWorld(),
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockX() + 4,
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockY() + 4,
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockZ() + 2);
            Location loc2 = new Location(UtilsRef.principalWorld(),
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockX() + 3,
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockY() + 2,
                    DonjonsMain.listPos.get(UtilsRef.principalWorld()).getBlockZ() + 2);

            Selection sel = new Selection();
            sel.selectedArea(loc1, loc2);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static void pasteChem(){

        File schematic = new File(DonjonsMain.instance.getDataFolder().getAbsoluteFile() + "/schematics/save.schematic");

        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession( new BukkitWorld(UtilsRef.principalWorld()), -1)) {

                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(ConfigManager.get().getInt("location.x"),
                                ConfigManager.get().getInt("location.y"),
                                ConfigManager.get().getInt("location.z")))
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

        DonjonsMain.listPos.put(randomLocation.getWorld(), randomLocation);

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
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        //Check to see if the surroundings are safe or not
        if(DonjonsMain.instance.getConfig().getBoolean("reverse")){
            return (blockPrevent.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
        } else {
            return !(blockPrevent.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
        }
    }

}
