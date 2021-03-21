package com.alexgreece45.blocksextrarewrite.commands;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlocksMined implements CommandExecutor {

    private final BlocksExtraRewrite main = BlocksExtraRewrite.getPlugin(BlocksExtraRewrite.class);

    public BlocksMined () {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getLogger().info("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args.length > 2) {
            player.sendMessage(ChatColor.RED + "Usage (#1): /blocks total\n/Usage (#2): /blocks stats blockName");
            return true;
        }

        if (args[0].equalsIgnoreCase("total")) {
            if (args.length == 1) {
                getAllBlockStats(player);
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /blocks total");
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("stats")) {
            if (args.length == 2)
                getBlockStats(player, args[1]);
            else
                player.sendMessage(ChatColor.RED + "Usage: /blocks stats blockName");

            return true;
        }

        return true;
    }

    protected void getAllBlockStats(Player player) {
        if (!playerExists(player)) {
            player.sendMessage(ChatColor.RED + "You haven't broken any blocks yet!");
            return;
        }

        for (String blockName : main.blocks.getConfig().getConfigurationSection("blocks_broken." + player.getUniqueId().toString() + ".blocks").getKeys(false)) {
            int amount = main.blocks.getConfig().getInt("blocks_broken." + player.getUniqueId().toString() + ".blocks." + blockName);
            player.sendMessage(ChatColor.DARK_AQUA + blockName + ": " + amount);
        }
    }

    protected void getBlockStats(Player player, String blockName) {
        if (blockName == null) {
            player.sendMessage(ChatColor.RED + "An error has occurred.");
            return;
        }

        if (!playerExists(player)) {
            player.sendMessage(ChatColor.RED + "You haven't broken any blocks yet!");
            return;
        }

        blockName = blockName.toUpperCase();

        if (!(main.blocks.getConfig().contains("blocks_broken." + player.getUniqueId().toString() + ".blocks." + blockName))) {
            player.sendMessage(ChatColor.RED + "You haven't broken any " + blockName + " blocks yet.");
            return;
        }

        int amount = main.blocks.getConfig().getInt("blocks_broken." + player.getUniqueId().toString() + ".blocks." + blockName);
        player.sendMessage(ChatColor.DARK_AQUA + "" + blockName + ": " + amount);
    }

     public boolean playerExists(Player player) {
         return main.blocks.getConfig().contains("blocks_broken." + player.getUniqueId().toString());
     }
}
