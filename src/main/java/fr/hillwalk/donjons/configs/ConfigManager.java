package fr.hillwalk.donjons.configs;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class ConfigManager {


    private static File file;
    private static File fileIn;
    private static FileConfiguration player;


    public static void setup() {
        fileIn = new File(Bukkit.getServer().getPluginManager().getPlugin(UtilsRef.getNamePlugin()).getDataFolder(),"");
        file = new File(fileIn, "informations.yml");


        if(!fileIn.exists()) {

            try {
                fileIn.mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if(!file.exists()) {
            try {
                file.createNewFile();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        player = YamlConfiguration.loadConfiguration(file);


    }

    public static FileConfiguration get() {
        return player;
    }


    public static void save() {
        try {
            player.save(file);
        } catch (IOException e) {

            System.out.println("La sauvegarde ne peut s'effectuer.");
        }
    }

    public static void reload() throws UnsupportedEncodingException {
        if (file == null) {
            file = new File(fileIn, "informations.yml");
        }
        player = YamlConfiguration.loadConfiguration(file);



    }
}


