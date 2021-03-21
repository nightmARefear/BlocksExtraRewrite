package com.alexgreece45.blocksextrarewrite.commands;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

    final BlocksExtraRewrite main = BlocksExtraRewrite.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getLogger().info("Only players can use this command (/back).");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /back");
            return true;
        }

        // if the player is registered then teleport to position they last died
        // else, display an error message to their chat
        if (BlocksExtraRewrite.deathcoords.containsKey(player.getUniqueId()))
            if (BlocksExtraRewrite.cooldowns.containsKey(player.getUniqueId().toString()))
                if (BlocksExtraRewrite.cooldowns.get(player.getUniqueId().toString()) < System.currentTimeMillis())
                    try {
                        Double [] coords = getCoords(player);

                        player.sendMessage(ChatColor.GREEN + "You have been successfully teleported to your" +
                                " previous death location.");
                    } catch(Exception e) {
                        player.sendMessage(ChatColor.DARK_RED + "A fatal error occurred whilst the " +
                                "teleportation to your previous death location -- please contact an admin. ");
                        main.getLogger().info(e.toString());
                    }
        else
            player.sendMessage(ChatColor.RED + "You cannot teleport to your previous death location.");


        return true;
    }

    private boolean isRegistered(Player player) {
        return BlocksExtraRewrite.deathcoords.containsKey(player.getUniqueId());
    }

    private Double[] getCoords(Player player) {
        return BlocksExtraRewrite.deathcoords.get(player.getUniqueId());
    }
}
