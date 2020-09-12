package fr.hillwalk.donjons;

import com.sk89q.worldedit.EditSession;
import fr.hillwalk.donjons.commands.Commands;
import fr.hillwalk.donjons.configs.ConfigInformations;
import fr.hillwalk.donjons.configs.ConfigMondes;
import fr.hillwalk.donjons.listener.MobDeathEvent;
import fr.hillwalk.donjons.listener.NetherPortalTeleport;
import fr.hillwalk.donjons.runnable.TimerLoad;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
    public static HashMap<World, EditSession> undoShematic = new HashMap<>();
    public static HashMap<World, Location> mobLocation = new HashMap<World, Location>();

    public static List<String> worlds = new ArrayList<String>();
    public static List<UUID> playerHits = new ArrayList<UUID>();
    public static List<String> mobs = new ArrayList<String>();


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

        //Instanciation du prefix
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix") + " ");

        //On invoque le timer
        load.runTaskTimer(this, getConfig().getLong("startTiming"), getConfig().getLong("repeatTiming"));



        //Si le serveur s'est éteint sans avoir complété le donjon
        if(ConfigInformations.getInfos().getString("portail") == null){
            return;

        } else {
            GenerationStructure.pasteChem();
            ConfigMondes.getMondes(worlds.get(0)).set("portail.location", null);
            ConfigMondes.getMondes(worlds.get(0)).set("portail.location.world", null);
            ConfigMondes.getMondes(worlds.get(0)).set("portail.location.x", null);
            ConfigMondes.getMondes(worlds.get(0)).set("portail.location.y", null);
            ConfigMondes.getMondes(worlds.get(0)).set("portail.location.z", null);
            ConfigInformations.getInfos().set("OpenPortail", false);
            ConfigInformations.getInfos().set("summonedBoss", null);
            ConfigInformations.save();
        }



    }





    @Override
    public void onDisable(){

        getLogger().info("is unloaded!");

    }


}
