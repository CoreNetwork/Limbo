package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.PrisonerStatus;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;

public class IncreaseCommand extends BaseLimboCommand {

	public IncreaseCommand()
	{
		permission = "increase";
		desc = "Increase time in Limbo";
		needPlayer = false;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		long timeToIncreaseInMilis = 0;
		boolean silent = false;
		boolean self = false;
		String time = "";
		Player player = null;
		if(args.length < 1 || args.length > 3)
		{
			Util.Message("Usage : /lim increase [<player>] <time> [silent]", sender);
			return;
		}
		
		if(args.length == 1)
		{
			if(sender instanceof Player)
			{
				player = (Player) sender;
				self = true;
			}
			else
			{
				Util.Message("You have to execute /lim increase <time>  as player", sender);
				return;
			}
			
			if(Util.isCorrectTimeFormat(args[0]))
			{
				timeToIncreaseInMilis = Util.parseTimeToMilis(args[0]);
				time = args[0];
			}
			else
			{
				Util.Message("<time> should be in correct format : 10, 10m, 10s, 10h", sender);
				return;
			}
		}
		else
		if(args.length == 2)
		{
			if(args[1].toLowerCase().equals("silent"))
			{
				if(sender instanceof Player)
				{
					player = (Player) sender;
					self = true;
				}
				else
				{
					Util.Message("You have to execute /lim increase <time> silent  as player", sender);
					return;
				}
				
				if(Util.isCorrectTimeFormat(args[0]))
				{
					timeToIncreaseInMilis = Util.parseTimeToMilis(args[0]);
					time = args[0];
				}
				else
				{
					Util.Message("<time> should be in correct format : 10, 10m, 10s, 10h", sender);
					return;
				}
				silent = true;
			}
			else if(Util.isCorrectTimeFormat(args[1]))
			{
				timeToIncreaseInMilis = Util.parseTimeToMilis(args[1]);
				time = args[1];
				
				player = LimboPlugin.instance.getServer().getPlayer(args[0]);
				if(player == null)
				{
					Util.Message("Could not find player called " + args[0], sender);
					return;
				}
			}
			else
			{
				Util.Message("Usage : /lim increase [<player>] <time> [silent]", sender);
				return;
			}
		}
		else
		{
			if(args[2].toLowerCase().equals("silent"))
			{
				silent = true;
			}
			else
			{
				Util.Message("Usage : /lim increase [<player>] <time> [silent]", sender);
				return;
			}
			
			if(Util.isCorrectTimeFormat(args[1]))
			{
				timeToIncreaseInMilis = Util.parseTimeToMilis(args[1]);
				time = args[1];
			}
			else
			{
				Util.Message("<time> should be in correct format : 10, 10m, 10s, 10h", sender);
				return;
			}
			
			player = LimboPlugin.instance.getServer().getPlayer(args[0]);
			if(player == null)
			{
				Util.Message("Could not find player called " + args[0], sender);
				return;
			}
		}
		
		if(LimboManager.getPrisonerStatus(player) != PrisonerStatus.DURING)
		{
			Util.Message(player.getName() + " is not in Limbo.", sender);
			return;
		}
		
		
		if(silent == false)
		{
			if(self)
			{
				Util.Message(Settings.MESSAGE_INCREASED_SELF.string().replace("<Time>", time), sender);
			}
			else
			{
				Util.Message(Settings.MESSAGE_INCREASED_SELF.string().replace("<Time>", time), player);
				Util.Message(Settings.MESSAGE_INCREASED.string().replace("<Player>", player.getName()).replace("<Time>", time), sender);
			}
		}
		
		LimboManager.modifyDuration(player, timeToIncreaseInMilis);
		
	}
}
