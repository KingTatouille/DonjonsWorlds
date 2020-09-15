package fr.hillwalk.donjons.configs;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Messages {


    private static File messageFiles;
    private static File messageFilesIn;
    private static FileConfiguration messages;


    public static void setup() {
        messageFilesIn = new File(Bukkit.getServer().getPluginManager().getPlugin(UtilsRef.getNamePlugin()).getDataFolder(),"messages");
        messageFiles = new File(messageFilesIn, "messages.yml");


        if(!messageFilesIn.exists()) {

            try {
                DonjonsMain.instance.saveResource("messages/messages.yml", false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if(!messageFiles.exists()) {
            try {
                messageFiles.createNewFile();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        messages = YamlConfiguration.loadConfiguration(messageFiles);


    }


    public static FileConfiguration getMessages() {
        return messages;
    }


    public static void save() {

        try {
            messages.save(messageFiles);
        } catch (IOException e) {

            DonjonsMain.instance.getLogger().info("Save doesn't work ! We have a problem sir!");
        }
    }

    public static void reload() throws UnsupportedEncodingException {
        if (messageFiles == null) {
            messageFiles = new File(messageFilesIn, "informations.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messageFiles);



    }
}
