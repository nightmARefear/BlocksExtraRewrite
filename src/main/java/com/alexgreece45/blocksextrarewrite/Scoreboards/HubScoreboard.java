package com.alexgreece45.blocksextrarewrite.Scoreboards;

import com.alexgreece45.blocksextrarewrite.BlocksExtraRewrite;
import com.alexgreece45.blocksextrarewrite.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

public class HubScoreboard implements Listener {

    //private final BlocksExtraRewrite main = BlocksExtraRewrite.getInstance();
    private final BlocksExtraRewrite main;

    public HubScoreboard(BlocksExtraRewrite main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        createScoreboard(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        LobbyScoreboard board = new LobbyScoreboard(e.getPlayer().getUniqueId());
        if (board.hasID())
            board.stop();
    }

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard(); // new scoreboard because everything we do is custom
        // each scoreboard has a UNIQUE name
        Objective obj = board.registerNewObjective("HubScoreboard-1", "dummy");
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&1<< &2&1" +
                player.getDisplayName() + " &a&1>>"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score score = obj.getScore(ChatColor.BLUE + "=================");
        score.setScore(3); // position in hud

        Score score2 = obj.getScore(ChatColor.AQUA + "Online Players: " + ChatColor.DARK_AQUA + Bukkit.getOnlinePlayers().size());
        score2.setScore(2); // position in hud

        Score score3 = obj.getScore(ChatColor.AQUA + "Total mob kills: " + ChatColor.DARK_AQUA +
                player.getStatistic(Statistic.MOB_KILLS));
        score3.setScore(1);

        Score score4 = obj.getScore(ChatColor.AQUA + "Rank: " + ChatColor.DARK_AQUA +
                "Owner");
        score4.setScore(0); // position in hud

        player.setScoreboard(board);
    }

    // runnable
    public void start(Player player) {
        main.TASKID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {

            int count = 0;
            LobbyScoreboard board = new LobbyScoreboard(player.getUniqueId());

            @Override
            public void run() {
                if (!board.hasID())
                    board.setID(main.TASKID);
                if (count == 13)
                    count = 0;

                switch(count) {
                    case 0 :
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).
                                setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&1<< &2&1" +
                                        player.getDisplayName() + " &a&1>>"));
                        /*Score score = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(String.valueOf(1));
                        score.setScore(2);*/
                        break;
                    case 1 :
                        player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(ChatColor.LIGHT_PURPLE
                            + player.getDisplayName());
                        break;
                }
                count++;
            }
        }, 0, 2);
    }
}
