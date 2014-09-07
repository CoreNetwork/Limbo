package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;

public class NotifyCommand extends BaseLimboCommand {

	public NotifyCommand()
	{
		permission = "notify";
		desc = "Toggle Limbo time notifications for current visit.";
		needPlayer = false;
	}
	
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = null;
		boolean self;
		if(args.length > 1)
		{
			Util.Message("Usage: /lim notify [<player>]", sender);
		}
		
		
		if(args.length == 0)
		{
			if(sender instanceof Player)
			{
				player = (Player) sender;
				self = true;
			}
			else
			{
				Util.Message("You have to execute /lim notify as player!", sender);
				return;
			}
		}
		else
		{
			player = LimboPlugin.instance.getServer().getPlayer(args[0]);
			self = false;
		}
		
		if(player == null)
		{
			Util.Message("Could not find player called " + args[0], sender);
			return;
		}
		
		if(self == false && sender instanceof Player && ((Player) sender).getUniqueId().equals(player.getUniqueId()))
		{
			self = true;
		}
		
		
		switch (LimboManager.getPrisonerStatus(player))
		{
		case DURING:
			Prisoner prisoner = Prisoners.getPrisoner(player);
			
			prisoner.notify = !prisoner.notify;
			Prisoners.save(prisoner);
			
			if(prisoner.notify)
			{
				Util.Message(Settings.MESSAGE_NOTIFICATION_ON.string(), sender);
				if(self == false)
				{
					Util.Message(Settings.MESSAGE_NOTIFICATION_ON.string(), player);
				}
			}
			else
			{
				Util.Message(Settings.MESSAGE_NOTIFICATION_OFF.string(), sender);
				if(self == false)
				{
					Util.Message(Settings.MESSAGE_NOTIFICATION_ON.string(), player);
				}
			}
			break;
		case AFTER:
			Util.Message(Settings.MESSAGE_NOTIFICATION_CANT_USE.string(), sender);
			break;
		case OUTSIDE:
			Util.Message(Settings.MESSAGE_NOTIFICATION_CANT_USE.string(), sender);
			break;
		}
	}

}
