package fr.hillwalk.donjons;

import fr.hillwalk.donjons.commands.Commands;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.listener.HitEntity;
import fr.hillwalk.donjons.listener.MobDeathEvent;
import fr.hillwalk.donjons.listener.NetherPortalTeleport;
import fr.hillwalk.donjons.runnable.TimerLoad;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DonjonsMain extends JavaPlugin {

    public static DonjonsMain instance;
    public static String prefix;

    public static HashMap<String, Location> mobSpawn = new HashMap<String, Location>();
    public static HashMap<World, Location> mobLocation = new HashMap<World, Location>();

    public static List<String> worlds = new ArrayList<String>();
    public static List<String> playerHits = new ArrayList<String>();
    BukkitRunnable load = new TimerLoad();


    @Override
    public void onEnable(){


        //Dire que l'instance est cette classe.
        instance = this;


        getLogger().info("is loaded!");

        //Sauvegarde de la config par défaut
        saveDefaultConfig();

        //Sauvegarde de la config informations
        saveResource("informations.yml", false);
        Informations.setup();

        //Sauvegarde de la config informations
        Messages.setup();

        //Save schematics
        saveResource("schematics/portal.schematic", true);

        //Setup de la config des mondes
        for (String str : getConfig().getStringList("worlds")){
            Mondes.setup(str);

        }

        //Setup du monde principal
        Mondes.setupPrincipalWorld(UtilsRef.principalWorld().getName());


        //Registre des commandes
        getCommand("randomdonjon").setExecutor(new Commands());

        //Registre des évents
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new NetherPortalTeleport(), this);
        pm.registerEvents(new MobDeathEvent(), this);
        pm.registerEvents(new HitEntity(), this);

        //Instanciation du prefix
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix") + " ");

        //On invoque le timer
        load.runTaskTimer(this, getConfig().getLong("startTiming"), getConfig().getLong("repeatTiming"));



        //Si le serveur s'est éteint sans avoir complété le donjon
        if(Informations.getInfos().getString("portail") == null){
            return;

        } else {
            UtilsRef.reset();
        }



    }





    @Override
    public void onDisable(){

        if(!worlds.isEmpty()){
            Bukkit.getServer().unloadWorld(DonjonsMain.worlds.get(0), false);
        } else {

            for (String str : getConfig().getStringList("worlds")){
                Bukkit.getServer().unloadWorld(str, false);
            }
            getLogger().fine(Messages.getMessages().getString("worlds.disabled"));

        }



        getLogger().info("is unloaded!");

    }


}
