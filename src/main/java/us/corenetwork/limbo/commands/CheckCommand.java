package us.corenetwork.limbo.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;

public class CheckCommand extends BaseLimboCommand{

	public CheckCommand()
	{
		permission = "check";
		desc = "Retuns the status or time left in limbo of caller/specified player.";
		needPlayer = false;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		OfflinePlayer player = null;
		if(args.length == 0)
		{
			if(sender instanceof Player)
			{
				player = (Player) sender;
			}
			else
			{
				Util.Message("You have to execute /lim check as player", sender);
				return;
			}
			 
		}
		else
		if(args.length > 1)
		{
			Util.Message("Usage : /lim check [<player>]", sender);
			return;
		}
		else
		{
			player = LimboPlugin.instance.getServer().getOfflinePlayer(args[0]);
			if(player == null)
			{
				Util.Message("Could not find player called " + args[0], sender);
				return;
			}
		}
		
		long milisLeft = LimboManager.getMilisLeft(player);
		
		String message;
		switch (LimboManager.getPrisonerStatus(player))
		{
		case OUTSIDE:
			message = Settings.MESSAGE_CHECK_PLAYER_NEG.string();

			if(player.isOnline())
			{
				Player onlinePlayer = player.getPlayer();
				if (onlinePlayer.getWorld().getEnvironment() == Environment.THE_END)
				{
					LimboManager.runOutCommands(onlinePlayer, false);
				}
			}
			break;
		default:
			if(args.length == 0)
				message = Settings.MESSAGE_CHECK_SELF.string();
			else
				message = Settings.MESSAGE_CHECK_PLAYER.string();
			break;	
		}
		
		String timeMessage = Util.getDetailedTimeMessage(milisLeft);
		
		
		message = message.replace("<Player>", player.getName());
		message = message.replace("<Time>", timeMessage);
		
		Util.Message(message, sender);
		
	}
}
