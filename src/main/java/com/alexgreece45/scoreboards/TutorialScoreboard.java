package com.alexgreece45.scoreboards;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

public class TutorialScoreboard implements CommandExecutor {

    BlocksExtraRewrite main = BlocksExtraRewrite.getPlugin(BlocksExtraRewrite.class);

    public TutorialScoreboard() {

    }

    // /createscoreboard
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            main.getLogger().info("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        ScoreboardManager scoreboardManager;


        return true;
    }
}
