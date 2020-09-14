package fr.hillwalk.donjons.commands;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.ConfigInformations;
import fr.hillwalk.donjons.configs.ConfigMondes;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if(args.length == 0){

            if(ConfigInformations.getInfos().getBoolean("OpenPortail") == false){

                player.sendMessage(UtilsRef.colorInfo(DonjonsMain.prefix + "Aucun donjon n'est apparu !"));
                return true;
            }

        }


        if(args[0].equalsIgnoreCase("pos")){

           if(args[1].equalsIgnoreCase("portail")){

               if(ConfigInformations.getInfos().getBoolean("OpenPortail") == false){

                   if(!player.getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))){

                    player.sendMessage(DonjonsMain.prefix + "Aucun portail n'est présent dans ce monde.");
                    return true;
                   } else {

                       player.sendMessage(DonjonsMain.prefix + "Vous êtes dans un monde où cet événement ne peut se produire.");

                   }
                   return true;

               }

            player.sendMessage(UtilsRef.colorInfo(DonjonsMain.prefix + "Les coordonnées sont :\nx : &6" +
                    ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x") +
                    " &fy : &6" + ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y") +
                    " &fz : &6" + ConfigMondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")));
            return true;
           }

           if(args[1].equalsIgnoreCase("boss")){

               if(DonjonsMain.mobLocation.isEmpty()){

                   if(ConfigMondes.getMondes(player.getWorld().getName()).getString("boss.location.x") == null){

                       player.sendMessage(DonjonsMain.prefix + "Aucun boss n'est présent dans ce monde.");
                       return true;
                   } else {

                       player.sendMessage(DonjonsMain.prefix + "Vous êtes dans un monde où cet événement ne peut se produire.");

                   }
                return true;

               }

               player.sendMessage(UtilsRef.colorInfo(DonjonsMain.prefix + "Les dernières coordonnées du boss sont :\n" + "x : &6" +
                       ConfigMondes.getMondes(DonjonsMain.worlds.get(0)).getInt("boss.location.x") +
                       " &fy : &6" + ConfigMondes.getMondes(DonjonsMain.worlds.get(0)).getInt("boss.location.y") +
                       " &fz: &6" + ConfigMondes.getMondes(DonjonsMain.worlds.get(0)).getInt("boss.location.z")));

               return true;
           }
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")){

            DonjonsMain.instance.saveDefaultConfig();
            DonjonsMain.instance.reloadConfig();

            try {
                ConfigInformations.reload();
                ConfigInformations.save();
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
