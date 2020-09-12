package fr.hillwalk.donjons.utils;

import fr.hillwalk.donjons.DonjonsMain;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
            return days + "jours " + hours + "heures " + minutes + "minutes " + seconds + "secondes";
        } else {
            if (hours != 0) {
                return hours + "heures " + minutes + "minutes " + seconds + "secondes";
            } else {
                if (minutes != 0) {
                    return minutes + "minutes " + seconds + "secondes";
                } else {
                    return seconds + " secondes";
                }
            }

        }
    }


    public static boolean inCuboid(Location origin, Location l1, Location l2){
        return new IntRange(l1.getX(), l2.getX()).containsDouble(origin.getX())
                && new IntRange(l1.getY(), l2.getY()).containsDouble(origin.getY())
                &&  new IntRange(l1.getZ(), l2.getZ()).containsDouble(origin.getZ());
    }



}

