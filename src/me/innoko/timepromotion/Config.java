package me.innoko.timepromotion;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config
{
	private static TP plugin;

	public static int UPDATE_DELAY;

	public static void loadConfig(TP plugin) throws IOException, InvalidConfigurationException
	{
		Config.plugin = plugin;

		FileConfiguration config = plugin.getConfig();

		File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");

		if (!configFile.exists())
		{
			configFile.getParentFile().mkdirs();
			configFile.createNewFile();
		}

		config.load(configFile);

		YamlConfiguration defaultConfig = new YamlConfiguration();
		defaultConfig.load(plugin.getResource("config.yml"));

		config.setDefaults(defaultConfig);
		config.options().copyDefaults(true);

		config.save(configFile);
	}

	public static boolean containsGroup(String groupName)
	{
		return plugin.getConfig().getConfigurationSection("ranks").contains(groupName);
	}

	public static String getPromotion(String groupName)
	{
		return plugin.getConfig().getString("ranks." + groupName + ".promotion");
	}

	public static int getRequiredTime(String groupName)
	{
		return plugin.getConfig().getInt("ranks." + groupName + ".time");
	}

	public static String getPromotionMessage(String groupName)
	{
		String message = plugin.getConfig().getString("ranks." + groupName + ".message");

		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
