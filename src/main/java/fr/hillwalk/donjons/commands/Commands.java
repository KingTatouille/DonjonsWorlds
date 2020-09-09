package fr.hillwalk.donjons.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigManager;
import fr.hillwalk.donjons.teleportation.GenerationSchematic;
import fr.hillwalk.donjons.teleportation.GenerationStructure;
import fr.hillwalk.donjons.teleportation.Selection;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Commands implements CommandExecutor {


    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param cmd Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        GenerationStructure ref = new GenerationStructure();

        if(args.length == 0){


            try {
                ref.loadSchematic();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return true;
        }

        if(args[0].equalsIgnoreCase("undo")){

            if(DonjonsMain.undoShematic.isEmpty()){

                player.sendMessage("Aucun portail n'est arrivé dans ce monde.");
                return true;
            }


            //Il permet de faire le undo.
            DonjonsMain.undoShematic.get(UtilsRef.principalWorld()).undo(DonjonsMain.undoShematic.get(UtilsRef.principalWorld()));
            Bukkit.broadcastMessage("Le portail vient de disparaître !");

            return true;
        }

        if(args[0].equalsIgnoreCase("reload")){

            DonjonsMain.instance.saveDefaultConfig();
            DonjonsMain.instance.reloadConfig();

            try {
                ConfigManager.save();
                ConfigManager.reload();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            DonjonsMain.prefix = ChatColor.translateAlternateColorCodes('&', DonjonsMain.instance.getConfig().getString("prefix") + " ");

            player.sendMessage(DonjonsMain.prefix + ChatColor.GREEN + " reload effectué !");

            return true;
        }

        return false;
    }

}
