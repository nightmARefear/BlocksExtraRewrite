package com.alexgreece45.scoreboards;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class HUD implements CommandExecutor {

    BlocksExtraRewrite main = BlocksExtraRewrite.getPlugin(BlocksExtraRewrite.class);

    public HUD() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getLogger().info("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /createhud");
            return true;
        }

        if (createHUD(player))
            player.sendMessage(ChatColor.YELLOW + "The hud has been successfully created!");

        return true;
    }


    public boolean createHUD(Player player) {
        try {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();
            Team team1 = board.registerNewTeam("team1");
            team1.setPrefix("prefix#1");

            Team team2 = board.registerNewTeam("team2");
            team2.setPrefix("prefix#2");

            Objective objective = board.registerNewObjective("test", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("Display name");
            //objective.

            return true;
        } catch (Exception e) {
            main.getLogger().info("The following exception occurred: " + e);
            return false;
        }
    }
}
