package fr.hillwalk.donjons.runnable;

import fr.hillwalk.donjons.teleportation.GenerationStructure;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class SchematicLoad extends BukkitRunnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */

    @Override
    public void run() {
        GenerationStructure ref = new GenerationStructure();
        try {
            ref.loadSchematic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
