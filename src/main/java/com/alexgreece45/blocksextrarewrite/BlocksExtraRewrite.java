package com.alexgreece45.blocksextrarewrite;

import com.alexgreece45.blocksextrarewrite.commands.Back;
import com.alexgreece45.blocksextrarewrite.commands.BlocksMined;
import com.alexgreece45.blocksextrarewrite.commands.MiningPickaxe;
import com.alexgreece45.blocksextrarewrite.listeners.BlockBreak;
import com.alexgreece45.blocksextrarewrite.Scoreboards.HubScoreboard;
import com.alexgreece45.blocksextrarewrite.listeners.PlayerDeath;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BlocksExtraRewrite extends JavaPlugin {

    public DataManager blocks;
    private DataManager cooldownsData;
    public DataManager deathcoordsData;
    //private DataManager homeData;

    public static Map<UUID, Long> cooldowns = new HashMap<>();
    public static Map<UUID, Double[]> deathcoords = new HashMap<UUID, Double[]>();
    //public static Map<UUID, List<double[]>> home = new HashMap<UUID, List<double[]>>();

    private static BlocksExtraRewrite instance = null;

    public int TASKID;
    private final HubScoreboard hs = new HubScoreboard(this);

    public static BlocksExtraRewrite getInstance() {
        return instance == null ? instance = BlocksExtraRewrite.getPlugin(BlocksExtraRewrite.class) : instance;
    }

    @Override
    public void onEnable() {

        loadYamlFiles();
        if (cooldownsData.getConfig().contains("cooldowns") && deathcoordsData.getConfig().contains("coords"))
            saveYamlFilesToHashmaps();

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                hs.createScoreboard(online);
                hs.start(online);
            }
        }

        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // blocks
        blocks.saveConfig();
        saveHashMapsToYamlFiles();

        // deathcoords
        //deathcoordsData.saveConfig();
    }

    private void registerCommands() {
        getServer().getPluginCommand("blocks").setExecutor(new BlocksMined());
        getServer().getPluginCommand("getminingpickaxe").setExecutor(new MiningPickaxe());
        //getServer().getPluginCommand("createhud").setExecutor(new HUD());
        //getServer().getPluginCommand("createscoreboard").setExecutor(new TutorialScoreboard());
        getServer().getPluginCommand("back").setExecutor(new Back());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new HubScoreboard(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
    }

    // loads this plugin's yaml files to their respective hashmaps
    private void saveYamlFilesToHashmaps() {
        // cooldownsData
        Long cooldown;
        if (cooldownsData.getConfig().get("cooldowns") != null)
            for (String uuid : cooldownsData.getConfig().getConfigurationSection("cooldowns.").getKeys(false)) {
                cooldown = cooldownsData.getConfig().getLong("cooldowns." + uuid);
                cooldowns.put(UUID.fromString(uuid), cooldown);
            }

        // deathcoordsData
        Double[] coords = new Double[3];
        if (deathcoordsData.getConfig().get("coords") != null) {
            for (String uuid : deathcoordsData.getConfig().getConfigurationSection("coords.").getKeys(false)) {
                coords[0] = deathcoordsData.getConfig().getDouble("coords." + uuid + ".x");
                coords[1] = deathcoordsData.getConfig().getDouble("coords." + uuid + ".y");
                coords[2] = deathcoordsData.getConfig().getDouble("coords." + uuid + ".z");
                deathcoords.put(UUID.fromString(uuid), coords);
            }
        }
        // homeData
        /*Location location;
        if (homeData.getConfig().get("coords") != null) {
            for (String uuid : homeData.getConfig().getConfigurationSection("coords.").getKeys(false)) {
                location = homeData.getConfig().get
            }
        }*/

    }

    private void saveHashMapsToYamlFiles() {
        // cooldowns
        if (!cooldowns.isEmpty()) {
            for (Map.Entry<UUID, Long> entry : cooldowns.entrySet()) {
                cooldownsData.getConfig().set("cooldowns." + entry.getKey(), entry.getValue());
            }

            cooldownsData.saveConfig();
            this.getLogger().info("Successfully saved \"cooldowns\" hashmap to yaml file!");
        } else {
            this.getLogger().info("The deathcoords hashmap was empty, so no modifications have been made " +
                    "to the \"cooldowns.yml\"");
        }

        // deathcoords
        if (!deathcoords.isEmpty()) {
            for (Map.Entry<UUID, Double[]> entry : deathcoords.entrySet()) {
                deathcoordsData.getConfig().set("coords." + entry.getKey() + ".x", entry.getValue()[0]);
                deathcoordsData.getConfig().set("coords." + entry.getKey() + ".y", entry.getValue()[1]);
                deathcoordsData.getConfig().set("coords." + entry.getKey() + ".z", entry.getValue()[2]);
            }

            deathcoordsData.saveConfig();
            this.getLogger().info("Successfully saved \"deathcoords\" hashmap to yaml file!");
        } else {
            this.getLogger().info("The deathcoords hashmap was empty, so no modifications have been made " +
                    "to the \"deathcoords.yml\"");
        }
    }

    // loads this plugin's yaml files
    private void loadYamlFiles() {
        blocks = new DataManager(this, "blocks.yml");
        cooldownsData = new DataManager(this, "cooldowns.yml");
        deathcoordsData = new DataManager(this, "deathcoords.yml");
    }
}
