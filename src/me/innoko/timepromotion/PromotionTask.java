package me.innoko.timepromotion;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.entity.Player;

public class PromotionTask implements Runnable
{
	private TP plugin;

	public PromotionTask(TP instance)
	{
		plugin = instance;
	}

	@Override
	public void run()
	{
		for (Player player : plugin.getServer().getOnlinePlayers())
		{
			OverloadedWorldHolder worldHolder = TP.groups.getWorldsHolder().getDefaultWorld();
			User user = worldHolder.getUser(player.getName());
			String groupName = user.getGroupName();

			if (Config.containsGroup(groupName))
			{
				ResultSet result = TP.stats.database.select("SELECT * FROM " + TP.stats.PLAYERTABLE
						+ " WHERE name = ? LIMIT 1", new Object[]
				{ player.getName() });

				int requiredTime = Config.getRequiredTime(groupName);

				int days = 0, hours = 0, minutes = 0;

				try
				{
					result.next();
					days = (int) Math.ceil(result.getInt("PLAYTIME") / (24 * 3600));
					hours = (int) Math.ceil((result.getInt("PLAYTIME") - (24 * 3600 * days)) / 3600);
					minutes = (int) Math
							.ceil((result.getInt("PLAYTIME") - (24 * 3600 * days + 3600 * hours)) / 60);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}

				if (minutes >= requiredTime)
				{
					String promotion = Config.getPromotion(groupName);
					user.setGroup(worldHolder.getGroup(promotion));

					player.sendMessage(Config.getPromotionMessage(groupName));
				}
			}
		}
	}

}
