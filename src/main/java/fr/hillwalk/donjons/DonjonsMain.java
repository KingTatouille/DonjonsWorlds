package fr.hillwalk.donjons;

import com.sk89q.worldedit.EditSession;
import fr.hillwalk.donjons.commands.Commands;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.listener.MobDeathEvent;
import fr.hillwalk.donjons.listener.NetherPortalTeleport;
import fr.hillwalk.donjons.runnable.SchematicLoad;
import fr.hillwalk.donjons.runnable.TimerLoad;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import fr.hillwalk.donjons.utils.UtilsRef;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DonjonsMain extends JavaPlugin {

    public static DonjonsMain instance;
    public static String prefix;
    public static HashMap<World, Location> listPos = new HashMap<World, Location>();

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
        ConfigManager.setup();

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

        //On load la schematic



        //Si le serveur s'est éteint sans avoir complété le donjon
        if(ConfigManager.get().getString("location") == null){
            return;

        } else {
            GenerationStructure.pasteChem();
            ConfigManager.get().set("location", null);
            ConfigManager.get().set("location.x", null);
            ConfigManager.get().set("location.y", null);
            ConfigManager.get().set("location.z", null);
            ConfigManager.get().set("OpenPortail", false);
            ConfigManager.get().set("summonedBoss", null);
            ConfigManager.save();
        }



    }





    @Override
    public void onDisable(){
        
        if(!listPos.isEmpty()){
        ConfigManager.get().set("location.x", listPos.get(UtilsRef.principalWorld()).getBlockX());
        ConfigManager.get().set("location.y", listPos.get(UtilsRef.principalWorld()).getBlockY());
        ConfigManager.get().set("location.z", listPos.get(UtilsRef.principalWorld()).getBlockZ());
        ConfigManager.save();
        }


        getLogger().info("is unloaded!");

    }


}
