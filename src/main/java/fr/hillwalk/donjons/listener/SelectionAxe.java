package fr.hillwalk.donjons.listener;

import fr.hillwalk.donjons.DonjonsMain;
import fr.hillwalk.donjons.configs.Messages;
import fr.hillwalk.donjons.utils.UtilsRef;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SelectionAxe implements Listener {

        @EventHandler (priority = EventPriority.NORMAL)
        public void axeSelection(PlayerInteractEvent e){


            ItemStack item = e.getItem();

            if(e.getPlayer().isOp() || e.getPlayer().hasPermission("wd.admin") || e.getPlayer().hasPermission("wd.selection")){
                try{
                if(item.getType() == Material.IRON_AXE){

                 if(e.getAction() == Action.LEFT_CLICK_BLOCK){
                     e.setCancelled(true);

                    if(DonjonsMain.selection1.isEmpty()){

                        DonjonsMain.selection1.put(e.getPlayer(), e.getClickedBlock().getLocation());
                        e.getPlayer().sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo("You selected pos1 x: " + DonjonsMain.selection1.get(e.getPlayer()).getBlockX()) + " y: "+ DonjonsMain.selection1.get(e.getPlayer()).getBlockY() + " z: " + DonjonsMain.selection1.get(e.getPlayer()).getBlockZ());

                    } else {
                        
                        DonjonsMain.selection1.clear();


                        DonjonsMain.selection1.put(e.getPlayer(), e.getClickedBlock().getLocation());
                        e.getPlayer().sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo("You selected pos1 x: " + DonjonsMain.selection1.get(e.getPlayer()).getBlockX()) + " y: "+ DonjonsMain.selection1.get(e.getPlayer()).getBlockY() + " z: " + DonjonsMain.selection1.get(e.getPlayer()).getBlockZ());

                    }

                }

                if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
                    e.setCancelled(true);


                    if(DonjonsMain.selection2.isEmpty()){

                        DonjonsMain.selection2.put(e.getPlayer(), e.getClickedBlock().getLocation());
                        e.getPlayer().sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo("You selected pos2 x: " + DonjonsMain.selection2.get(e.getPlayer()).getBlockX()) + " y: "+ DonjonsMain.selection2.get(e.getPlayer()).getBlockY() + " z: " + DonjonsMain.selection2.get(e.getPlayer()).getBlockZ());

                    } else {

                        DonjonsMain.selection2.clear();


                        DonjonsMain.selection2.put(e.getPlayer(), e.getClickedBlock().getLocation());
                        e.getPlayer().sendMessage(DonjonsMain.prefix + UtilsRef.colorInfo("You selected pos2 x: " + DonjonsMain.selection2.get(e.getPlayer()).getBlockX()) + " y: "+ DonjonsMain.selection2.get(e.getPlayer()).getBlockY() + " z: " + DonjonsMain.selection2.get(e.getPlayer()).getBlockZ());
                    }

                }
                }


                } catch (NullPointerException ex){
                    ex.getStackTrace();

                }
            }
        }




}
