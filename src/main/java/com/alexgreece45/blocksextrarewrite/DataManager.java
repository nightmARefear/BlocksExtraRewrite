package com.alexgreece45.blocksextrarewrite;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private final BlocksExtraRewrite plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    private final String fileName;

    public DataManager(BlocksExtraRewrite plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        // saves/initializes the config
        saveDefaultConfig();
    }

    // creates a yml file if it does not exist and check for
    // any yml issues
    public void reloadConfig() {
        // if the file does not exist; create it
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), fileName);

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        // VV we're getting the values from the yml file and use them
        InputStream defaultStream = this.plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    // to get data
    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();

        return this.dataConfig;
    }

    // when new data is added to the file
    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch(IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    // initialize the config (every time the server is run)
    public void saveDefaultConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), fileName);

        if (!this.configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
