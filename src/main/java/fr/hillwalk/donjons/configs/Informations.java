package fr.hillwalk.donjons.configs;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class Informations {


    private static File informationFile;
    private static File informationFileIn;
    private static FileConfiguration informations;


    public static void setup() {
        informationFileIn = new File(Bukkit.getServer().getPluginManager().getPlugin(UtilsRef.getNamePlugin()).getDataFolder(),"");
        informationFile = new File(informationFileIn, "informations.yml");


        if(!informationFileIn.exists()) {

            try {
                informationFileIn.mkdirs();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if(!informationFile.exists()) {
            try {
                informationFile.createNewFile();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        informations = YamlConfiguration.loadConfiguration(informationFile);


    }

    public static FileConfiguration getInfos() {
        return informations;
    }


    public static void save() {
        try {
            informations.save(informationFile);
        } catch (IOException e) {

            DonjonsMain.instance.getLogger().info("Save doesn't work ! We have a problem sir!");
        }
    }

    public static void reload() throws UnsupportedEncodingException {
        if (informationFile == null) {
            informationFile = new File(informationFileIn, "informations.yml");
        }
        informations = YamlConfiguration.loadConfiguration(informationFile);



    }
}