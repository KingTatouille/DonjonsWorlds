package fr.hillwalk.donjons.commands;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Informations;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.configs.Mondes;
import fr.hillwalk.donjons.generation.Selection;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

            if(Informations.getInfos().getBoolean("OpenPortail") == false){

                player.sendMessage(UtilsRef.colorInfo(DonjonsMain.prefix + UtilsRef.colorInfo(Messages.getMessages().getString("commands.principalCommand"))));
                return true;
            }

        }



        if(args[0].equalsIgnoreCase("create")){

            if(!player.isOp() || !player.hasPermission("wd.reload") || !player.hasPermission("wd.admin")){
                player.sendMessage(UtilsRef.colorInfo(Messages.getMessages().getString("errors.reload")));
                return true;
            }

            if(args.length == 0){
                player.sendMessage(DonjonsMain.prefix + "Syntax: /worlddungeons create <name>");
                return true;

            }


                if(DonjonsMain.selection1.isEmpty() && DonjonsMain.selection2.isEmpty()){

                    player.sendMessage(DonjonsMain.prefix + "You need to select two position before do this.");
                    return true;
                }

                player.sendMessage(DonjonsMain.prefix + "The portal is created! Now you need to wait the ");
                Informations.getInfos().set("SpawnPortal.name", args[1]);
                Informations.save();
                Informations.getInfos().set("SpawnPortal.min.x", DonjonsMain.selection1.get(player).getBlockX());
                Informations.save();
                Informations.getInfos().set("SpawnPortal.min.y", DonjonsMain.selection1.get(player).getBlockY());
                Informations.save();
                Informations.getInfos().set("SpawnPortal.min.z", DonjonsMain.selection1.get(player).getBlockZ());
                Informations.save();
                Informations.getInfos().set("SpawnPortal.max.x", DonjonsMain.selection2.get(player).getBlockX());
                Informations.save();
                Informations.getInfos().set("SpawnPortal.max.y", DonjonsMain.selection2.get(player).getBlockY());
                Informations.save();
                Informations.getInfos().set("SpawnPortal.max.z", DonjonsMain.selection2.get(player).getBlockZ());
                Informations.save();

            Location loc1 = new Location(UtilsRef.principalWorld(),
                    Informations.getInfos().getInt("SpawnPortal.min.x"),
                    Informations.getInfos().getInt("SpawnPortal.min.y"),
                    Informations.getInfos().getInt("SpawnPortal.min.z"));
            Location loc2 = new Location(UtilsRef.principalWorld(),
                    Informations.getInfos().getInt("SpawnPortal.max.x"),
                    Informations.getInfos().getInt("SpawnPortal.max.y"),
                    Informations.getInfos().getInt("SpawnPortal.max.z"));

            Selection sel = new Selection();
            sel.selectedArea(loc1, loc2);

            return true;
        }

        if(args[0].equalsIgnoreCase("pos")){

           if(args[1].equalsIgnoreCase("portal")){

               if(Informations.getInfos().getBoolean("OpenPortail") == false){

                   if(DonjonsMain.worlds.isEmpty()){

                       player.sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo(Messages.getMessages().getString("worlds.noPortal")));
                       return true;

                   }

                   if(!player.getWorld().getName().equalsIgnoreCase(DonjonsMain.worlds.get(0))){

                    player.sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo(Messages.getMessages().getString("worlds.noPortal")));
                    return true;
                   } else {

                       player.sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo(Messages.getMessages().getString("worlds.notGoodWorld")));

                   }
                   return true;

               }

               String portal = UtilsRef.colorInfo(Messages.getMessages().getString("commands.portal"));
               String replace = portal.replaceAll("%portal_location_X%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.x")))
                       .replaceAll("%portal_location_Y%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.y")))
                       .replaceAll("%portal_location_Z%", String.valueOf(Mondes.getMondes(UtilsRef.principalWorld().getName()).getInt("portail.location.z")));


             //We send the message to the player
            player.sendMessage(DonjonsMain.prefix + replace);
            return true;
           }

           if(args[1].equalsIgnoreCase("boss")){

               if(DonjonsMain.mobLocation.isEmpty()){

                   if(Mondes.getMondes(player.getWorld().getName()).getString("boss.location.x") == null){

                       player.sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo(Messages.getMessages().getString("worlds.noBoss")));
                       return true;
                   } else {

                       player.sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo(Messages.getMessages().getString("worlds.notGoodWorld")));

                   }
                return true;

               }

               String boss = UtilsRef.colorInfo(Messages.getMessages().getString("commands.boss"));
               String replace = boss.replaceAll("%boss_location_X%", String.valueOf(Mondes.getMondes(DonjonsMain.worlds.get(0)).getInt("boss.location.x")))
                       .replaceAll("%boss_location_Y%", String.valueOf(Mondes.getMondes(DonjonsMain.worlds.get(0)).getInt("boss.location.y")))
                       .replaceAll("%boss_location_Z%", String.valueOf(Mondes.getMondes(DonjonsMain.worlds.get(0)).getInt("boss.location.z")));


               //We send a message to the player
               player.sendMessage(DonjonsMain.prefix + replace);

               return true;
           }
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")){

            if(!player.isOp() || !player.hasPermission("wd.reload") || !player.hasPermission("wd.admin")){
                player.sendMessage(UtilsRef.colorInfo(Messages.getMessages().getString("errors.reload")));
                return true;
            }
            
            DonjonsMain.instance.saveDefaultConfig();
            DonjonsMain.instance.reloadConfig();

            try {
                Informations.reload();
                Informations.save();

                Messages.reload();
                Messages.save();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            UtilsRef.reset();


            DonjonsMain.prefix = ChatColor.translateAlternateColorCodes('&', DonjonsMain.instance.getConfig().getString("prefix") + " ");

            player.sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo(Messages.getMessages().getString("admin.reload")));

            return true;
        }

        return false;
    }

}
