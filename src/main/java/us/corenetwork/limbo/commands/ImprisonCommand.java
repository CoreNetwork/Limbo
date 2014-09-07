package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;

public class ImprisonCommand extends BaseLimboCommand {

	public ImprisonCommand()
	{
		permission = "imprison";
		desc = "Sends target to limbo.";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		if(args.length != 1)
		{
			Util.Message("Usage: /lim imprison <player>", sender);
			return;
		}
		
		Player player = null;
		
		player = LimboPlugin.instance.getServer().getPlayer(args[0]);
		if(player == null)
		{
			Util.Message("Could not find player called " + args[0], sender);
			return;
		}
		
		switch (LimboManager.getPrisonerStatus(player))
		{
		case DURING:
			Util.Message(player.getName() + " is already in limbo.", sender);
			break;
		case AFTER:
			Util.Message(player.getName() + " is being released from limbo, you can't imprison him now.", sender);
			break;
		case OUTSIDE:
			LimboManager.imprison(player);
			if(player.isOnline() && player.isDead() == false)
			{
				LimboManager.moveIn(player);
			}
			Util.Message(player.getName() + " has been imprisoned.", sender);
			break;
		}
	}

}
