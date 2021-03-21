package com.alexgreece45.blocksextrarewrite.listeners;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Collections;
import java.util.List;

public class PlayerDeath implements Listener {

    final BlocksExtraRewrite main = BlocksExtraRewrite.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Location location = player.getLocation();
        Double[] coords = new Double[] {location.getX(), location.getY(), location.getZ()};

        // save player position to the hashmap
        player.sendMessage("Your location:\n" + "\ncoords: x: " + coords[0] + ", y: " +
                coords[1] + ", z: " + coords[2]);

        BlocksExtraRewrite.deathcoords.put(player.getUniqueId(), coords);
    }
}
