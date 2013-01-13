package me.innoko.timepromotion;

import java.io.IOException;
import java.util.logging.Logger;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.maniacraft.statsandachievements.SaAPlugin;

public class TP extends JavaPlugin
{
	public static Logger log;
	public static PluginManager pm;
	public static BukkitScheduler bs;

	public static SaAPlugin stats;
	public static GroupManager groups;

	@Override
	public void onDisable()
	{
		log.info("TimePromotion by InnoKo is now disabled.");
	}

	@Override
	public void onEnable()
	{
		log = Logger.getLogger("Minecraft");
		pm = getServer().getPluginManager();
		bs = getServer().getScheduler();

		try
		{
			Config.loadConfig(this);
		}
		catch (IOException | InvalidConfigurationException e)
		{
			log.warning("Error loading TimePromotion config!");
		}

		if (!loadStats())
		{
			log.warning("Error loading StatsAndAchievements!");
		}

		if (!loadGroups())
		{
			log.warning("Error loading Essentials GroupManager!");
		}

		bs.runTaskTimer(this, new PromotionTask(this), 0L, Config.UPDATE_DELAY * 20L);

		log.info("TimePromotion by InnoKo is now enabled.");
	}

	public boolean loadStats()
	{
		stats = (SaAPlugin) pm.getPlugin("StatsAndAchievements");

		if (stats == null || !stats.isEnabled())
		{
			return false;
		}

		return true;
	}

	public boolean loadGroups()
	{
		groups = (GroupManager) pm.getPlugin("GroupManager");

		if (groups == null || !groups.isEnabled())
		{
			return false;
		}

		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (label.equalsIgnoreCase("timepromotion"))
		{
			if (sender.isOp() && args.length >= 1 && args[0].equalsIgnoreCase("reload"))
			{
				try
				{
					Config.loadConfig(this);

					sender.sendMessage(ChatColor.GREEN + "TimePromotion reloaded.");
				}
				catch (IOException | InvalidConfigurationException e)
				{
					sender.sendMessage(ChatColor.RED + "Error loading TimePromotion config!");
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
