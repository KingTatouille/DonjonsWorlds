package fr.hillwalk.donjons.configs;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Mondes {


    private static File mondesFiles;
    private static File mondesFilesIn;
    private static FileConfiguration mondes;


    public static void setup(String params) {
        mondesFilesIn = new File(Bukkit.getServer().getPluginManager().getPlugin(UtilsRef.getNamePlugin()).getDataFolder(),"mondes");
        mondesFiles = new File(mondesFilesIn, params+".yml");


        if(!mondesFilesIn.exists()) {

            try {
                mondesFilesIn.mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if(!mondesFiles.exists()) {
            try {
                mondesFiles.createNewFile();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        getMondes(params).set("name", params);
        save(params);

        mondes = YamlConfiguration.loadConfiguration(mondesFiles);


    }

    public static void setupPrincipalWorld(String params) {
        mondesFilesIn = new File(Bukkit.getServer().getPluginManager().getPlugin(UtilsRef.getNamePlugin()).getDataFolder(),"mondes");
        mondesFiles = new File(mondesFilesIn, params+".yml");


        if(!mondesFilesIn.exists()) {

            try {
                mondesFilesIn.mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if(!mondesFiles.exists()) {
            try {
                mondesFiles.createNewFile();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        getMondes(params).set("name", params);
        save(params);

        mondes = YamlConfiguration.loadConfiguration(mondesFiles);


    }

    public static FileConfiguration getMondes(String params) {

        mondesFiles = new File(mondesFilesIn, params+".yml");

        return mondes = YamlConfiguration.loadConfiguration(mondesFiles);
    }


    public static void save(String str) {
        mondesFilesIn = new File(Bukkit.getServer().getPluginManager().getPlugin(UtilsRef.getNamePlugin()).getDataFolder(),"mondes");
        mondesFiles = new File(mondesFilesIn, str+".yml");

        try {
            mondes.save(mondesFiles);
        } catch (IOException e) {

            DonjonsMain.instance.getLogger().info("Save doesn't work ! We have a problem sir!");
        }
    }

    public static void reload(String str) throws UnsupportedEncodingException {
        if (mondesFiles == null) {

                mondesFiles = new File(mondesFilesIn, str+".yml");

        }
        mondes = YamlConfiguration.loadConfiguration(mondesFiles);



    }
}
