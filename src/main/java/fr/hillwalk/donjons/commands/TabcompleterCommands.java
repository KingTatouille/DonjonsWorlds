package fr.hillwalk.donjons.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabcompleterCommands implements TabCompleter {

    private final String[] COMMANDS = { "pos", "reload", "create" };


    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param alias   The alias used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {



            /*

                                        SECTION POUR COMPLETER LE /RTP

             */

            List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
            Collections.sort(completions);



            if(args[0].equalsIgnoreCase("pos")) {
                if(args.length == 2){
                    List<String> commands = new ArrayList<>();
                    commands.add("boss");
                    commands.add("portal");

                    return commands;
                }
            }

             if(args[0].equalsIgnoreCase("create")) {
                 if(args.length == 2){
                     List<String> commands = new ArrayList<>();
                     commands.add("<name>");

                     return commands;
                }
            }



            return completions;
    }
}
