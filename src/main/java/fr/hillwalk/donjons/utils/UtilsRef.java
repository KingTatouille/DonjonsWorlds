package fr.hillwalk.donjons.utils;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.generation.GenerationStructure;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UtilsRef {



    //Get the plugin name
    public static String getNamePlugin(){

        return DonjonsMain.instance.getName();

    }



    public static Configuration config(){


        return DonjonsMain.instance.getConfig();
    }

    public static int randomNumber(int number){
        Random rand = new Random();

        return rand.nextInt(number);
    }


    public static String colorInfo(String str){

        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String randomWorlds(){

        List<String> listWorlds = new ArrayList<>();

        for(String params : DonjonsMain.instance.getConfig().getStringList("worlds")){
            listWorlds.add(params);
        }

        return listWorlds.get(randomNumber(listWorlds.size()));

    }

    public static World principalWorld(){


        return Bukkit.getServer().getWorld(DonjonsMain.instance.getConfig().getString("world"));
    }

    /*

        Crédits : https://www.spigotmc.org/threads/converting-ticks-to-min-hours-days.346046/

     */

    public static String timerMessage(int secondsx) {
        int days = (int) TimeUnit.SECONDS.toDays(secondsx);
        int hours = (int) (TimeUnit.SECONDS.toHours(secondsx) - TimeUnit.DAYS.toHours(days));
        int minutes = (int) (TimeUnit.SECONDS.toMinutes(secondsx) - TimeUnit.HOURS.toMinutes(hours)
                - TimeUnit.DAYS.toMinutes(days));
        int seconds = (int) (TimeUnit.SECONDS.toSeconds(secondsx) - TimeUnit.MINUTES.toSeconds(minutes)
                - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days));

        if (days != 0) {
            return days + Messages.getMessages().getString("schedule.days") + " " + hours + Messages.getMessages().getString("schedule.hours") + " " + minutes + Messages.getMessages().getString("schedule.minutes") + " " + seconds + Messages.getMessages().getString("schedule.seconds");
        } else {
            if (hours != 0) {
                return hours + Messages.getMessages().getString("schedule.hours")+ " " + minutes + Messages.getMessages().getString("schedule.minutes")+ " " + seconds + Messages.getMessages().getString("schedule.seconds");
            } else {
                if (minutes != 0) {
                    return minutes + Messages.getMessages().getString("schedule.minutes")+ " " + seconds + Messages.getMessages().getString("schedule.seconds");
                } else {
                    return seconds + " " + Messages.getMessages().getString("schedule.seconds");
                }
            }

        }
    }


    public static boolean inCuboid(Location origin, Location l1, Location l2){
        return new IntRange(l1.getX(), l2.getX()).containsDouble(origin.getX())
                && new IntRange(l1.getY(), l2.getY()).containsDouble(origin.getY())
                &&  new IntRange(l1.getZ(), l2.getZ()).containsDouble(origin.getZ());
    }



    public static void reset(){

        File schematic = new File(DonjonsMain.instance.getDataFolder().getAbsoluteFile() + "/schematics/save.schematic");

        GenerationStructure.pasteChem();
        Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location", null);
        Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.world", null);
        Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.x", null);
        Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.y", null);
        Mondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.z", null);
        Mondes.save(UtilsRef.principalWorld().getName());
        if(!DonjonsMain.worlds.isEmpty()){
        Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.name", null);
            Mondes.save(DonjonsMain.worlds.get(0));
        Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location", null);
            Mondes.save(DonjonsMain.worlds.get(0));
        Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.x", null);
            Mondes.save(DonjonsMain.worlds.get(0));
        Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.y", null);
            Mondes.save(DonjonsMain.worlds.get(0));
        Mondes.getMondes(DonjonsMain.worlds.get(0)).set("boss.location.z", null);
        Mondes.save(DonjonsMain.worlds.get(0));
        }
        Informations.getInfos().set("OpenPortail", false);
        Informations.getInfos().set("DiscoverArea", false);
        Informations.getInfos().set("summonedBoss", null);
        Informations.save();

        DonjonsMain.taskId.clear();
        DonjonsMain.mobSpawn.clear();
        DonjonsMain.playerHits.clear();
        DonjonsMain.worlds.clear();
        DonjonsMain.mobLocation.clear();

    }

    //Crédit https://www.spigotmc.org/threads/getting-chunks-around-a-center-chunk-within-a-specific-radius.422279/

    public static Collection<Chunk> around(Chunk origin, int radius) {
        World world = origin.getWorld();

        int length = (radius * 2) + 1;
        Set<Chunk> chunks = new HashSet<>(length * length);

        int cX = origin.getX();
        int cZ = origin.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                chunks.add(world.getChunkAt(cX + x, cZ + z));
            }
        }
        return chunks;
    }


}

