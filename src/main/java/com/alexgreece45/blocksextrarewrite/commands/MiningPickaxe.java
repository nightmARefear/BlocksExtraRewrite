package com.alexgreece45.blocksextrarewrite.commands;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import com.alexgreece45.blocksextrarewrite.listeners.BlockBreak;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MiningPickaxe implements CommandExecutor {

    BlocksExtraRewrite main = BlocksExtraRewrite.getInstance();
    BlocksMined bm = new BlocksMined();
    BlockBreak bb = new BlockBreak();

    public MiningPickaxe() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getLogger().info("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /getminingpickaxe");
            return true;
        }

        if (BlocksExtraRewrite.cooldowns.containsKey(player.getUniqueId()))
            if (BlocksExtraRewrite.cooldowns.get(player.getUniqueId()) > System.currentTimeMillis()) {
                long seconds = (BlocksExtraRewrite.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                long minutes;
                if (seconds % 60 == 0) {
                    minutes = seconds / 60;
                    player.sendMessage(ChatColor.RED + "You can use this command again in " + minutes + " minutes.");
                } else {
                    minutes = seconds / 60;
                    seconds = seconds - minutes * 60;
                    player.sendMessage(ChatColor.RED + "You can use this command again in " + minutes + " minutes, and " + seconds + " seconds.");
                }

                return true;
            }

        BlocksExtraRewrite.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

        if (!bm.playerExists(player))
            giveMiningPickaxe(player);
        else
            giveCustomPickaxe(player, bb.getGeneralLevel(player), bb.getGeneralLevelExp(player));

        return true;
    }

    private void giveMiningPickaxe(Player player) {
        ItemStack diaPick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta diaMeta = diaPick.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        lore.add(ChatColor.YELLOW + "Levels: 1");
        lore.add(ChatColor.RED + "Exp: 0");

        diaMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "MINING PICKAXE");
        diaMeta.setLore(lore);

        diaPick.setItemMeta(diaMeta);

        diaPick.addUnsafeEnchantment(Enchantment.DIG_SPEED, 20);
        diaPick.addUnsafeEnchantment(Enchantment.DURABILITY, 2);

        player.setItemInHand(diaPick);

        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "You've received the mining pickaxe!");
    }

    public void giveCustomPickaxe(Player player, int generalLevel, double generalLevelExp) {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta pickMeta = pick.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        player.setItemInHand(new ItemStack(Material.AIR));

        lore.add("");
        lore.add(ChatColor.YELLOW + "Levels: " + generalLevel);
        lore.add(ChatColor.RED + "Exp: " + generalLevelExp);

        pickMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "MINING PICKAXE");
        pickMeta.setLore(lore);

        pick.setItemMeta(pickMeta);

        pick.addUnsafeEnchantment(Enchantment.DIG_SPEED, 20);
        pick.addUnsafeEnchantment(Enchantment.DURABILITY, 2);

        player.setItemInHand(pick);
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Your mining pickaxe has been upgraded to level " + generalLevel + ".");
    }
}
