package com.alexgreece45.blocksextrarewrite.listeners;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import com.alexgreece45.blocksextrarewrite.commands.BlocksMined;
import com.alexgreece45.blocksextrarewrite.commands.MiningPickaxe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private final BlocksExtraRewrite main = BlocksExtraRewrite.getPlugin(BlocksExtraRewrite.class);
    final BlocksMined bm = new BlocksMined();

    public BlockBreak() {

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        // registering player
        if (!bm.playerExists(player)) {
            main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".blocks." +
                    e.getBlock().getType().toString(), 1);

            // levelling system
            main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.generalLevel", 1);
            main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.exp", 1);
        } else {
            // registering player
            final int amount = main.blocks.getConfig().getInt("blocks_broken." + player.getUniqueId().toString() + ".blocks." +
                    e.getBlock().getType().toString());
            main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".blocks." +
                    e.getBlock().getType().toString(), (amount + 1));

            // levelling system
            final double generalLevelExp = getGeneralLevelExp(player);
            final double blockWeight = getBlockWeight(e.getBlock().getType().toString(), player);

            if (blockWeight == -1)
                return;

            player.sendMessage("blockweight = " + blockWeight); // to be removed
            main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.exp",
                    (generalLevelExp + blockWeight));

            player.sendMessage("exp: " + getGeneralLevelExp(player));

            if (doesQualify(player)) {
                player.sendMessage(ChatColor.YELLOW + "You have leveled up!");
                player.sendMessage(ChatColor.YELLOW + "New level: " + getGeneralLevel(player));
                final MiningPickaxe miningPickaxe = new MiningPickaxe();
                miningPickaxe.giveCustomPickaxe(player, getGeneralLevel(player), getGeneralLevelExp(player));
            }
        }

        //main.blocks.saveConfig();
    }

    public int getGeneralLevel(Player player) {
        if (!bm.playerExists(player)) {
            main.getLogger().info("The player + " + player.getDisplayName() + " is not registered in the blocks.yaml file.");
            return -1;
        }

        return main.blocks.getConfig().getInt("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.generalLevel");
    }

    public double getGeneralLevelExp(Player player) {
        if (!bm.playerExists(player)) {
            main.getLogger().info("The player + " + player.getDisplayName() + " is not registered in the blocks.yaml file.");
            return -1;
        }

        return main.blocks.getConfig().getDouble("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.exp");
    }

    private boolean incrementGeneralLevel(Player player) {
        if (!bm.playerExists(player)) {
            main.getLogger().info("The player + " + player.getDisplayName() + " is not registered in the blocks.yaml file.");
            return false;
        }

        final int level = getGeneralLevel(player);
        if (level == -1) return false;

        main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.generalLevel", (level + 1));
        //main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.exp", 0);
        return true;
    }

    /*private int topPlayerLevel(Player player) {
        if (!bm.playerExists(player)) {
            main.getLogger().info("The player + " + player.getDisplayName() + " is not registered in the blocks.yaml file.");
            return -1;
        }

        int highestLevel = -1;
        for (String levelName : main.blocks.getConfig().getConfigurationSection("blocks_broken." + player.getUniqueId().toString()
         + ".levels").getKeys(false)) {
            int level = main.blocks.getConfig().getInt("blocks_broken." + player.getUniqueId().toString() + "." + levelName);
            if (level > highestLevel)
                highestLevel = level;
        }

        return highestLevel;
    }*/

    /* A method that checks whether or not a specific player is qualified to get leveled up or not.
     * (Applies to the general level only)
     */
    private boolean doesQualify(Player player) {
        if (!bm.playerExists(player)) {
            return false;
        }

        double generalLevelExp = main.blocks.getConfig().getDouble("blocks_broken." + player.getUniqueId().toString() +
                ".levels.generalLevel.exp");
        if (generalLevelExp >= 20.0) {
            final int generalLevel = getGeneralLevel(player);
            if (generalLevel == -1) return false;

            double finalExp = generalLevelExp - 20.0;
            main.blocks.getConfig().set("blocks_broken." + player.getUniqueId().toString() + ".levels.generalLevel.exp", finalExp);
            incrementGeneralLevel(player);

            main.blocks.saveConfig(); // used so the custom pickaxe gets the correct exp value
            return true;
        }
        return false;
    }

    /* Gets the block weight, depending on its type; the rarer the block, the more the weight will be.
     *
     */
    private double getBlockWeight(String blockName, Player player) {
        String bName = blockName.toUpperCase();
        double weight = 0.0;
        try {
            Material block = Material.valueOf(bName);
            switch (bName) {
                case "DIAMOND_BLOCK" :
                    weight = 2;
                    break;
                case "GOLD_BLOCK" :
                    weight = 1.85;
                    break;
                case "IRON_BLOCK" :
                    weight = 1.65;
                    break;
                case "COBBLESTONE" :
                    weight = 1.02;
                    break;
                default:
                    weight = 0.0;
                    break;
            }
        } catch (IllegalArgumentException e) {
            main.getLogger().info("No such block \"" + blockName + "\".");
            player.sendMessage("No such block \"" + blockName + "\".");
            return -1;
        }
        return weight;
    }
}
