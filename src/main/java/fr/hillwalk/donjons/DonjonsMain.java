package fr.hillwalk.donjons;

import com.sk89q.worldedit.EditSession;
import fr.hillwalk.donjons.commands.Commands;
import fr.hillwalk.donjons.configs.ConfigInformations;
import fr.hillwalk.donjons.configs.ConfigMondes;
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
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DonjonsMain extends JavaPlugin {

    public static DonjonsMain instance;
    public static String prefix;

    public static HashMap<String, Location> mobSpawn = new HashMap<String, Location>();
    public static HashMap<World, Location> mobLocation = new HashMap<World, Location>();

    public static List<String> worlds = new ArrayList<String>();
    public static List<String> playerHits = new ArrayList<String>();


    @Override
    public void onEnable(){

        BukkitRunnable load = new TimerLoad();


        //Dire que l'instance est cette classe.
        instance = this;


        getLogger().info("is loaded!");

        //Sauvegarde de la config par défaut
        saveDefaultConfig();

        //Sauvegarde de la config informations
        saveResource("informations.yml", false);
        ConfigInformations.setup();

        //Setup de la config des mondes
        for (String str : getConfig().getStringList("worlds")){
            ConfigMondes.setup(str);

        }

        //Setup du monde principal
        ConfigMondes.setupPrincipalWorld(UtilsRef.principalWorld().getName());


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
        if(ConfigInformations.getInfos().getString("portail") == null){
            return;

        } else {
            GenerationStructure.pasteChem();
            ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location", null);
            ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.world", null);
            ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.x", null);
            ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.y", null);
            ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).set("portail.location.z", null);
            ConfigInformations.getInfos().set("OpenPortail", false);
            ConfigInformations.getInfos().set("DiscoverArea", false);
            ConfigInformations.getInfos().set("summonedBoss", null);
            ConfigInformations.save();
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
            getLogger().info("Les mondes sont maintenant inutilisables.");

        }



        getLogger().info("is unloaded!");

    }


}
