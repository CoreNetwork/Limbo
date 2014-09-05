package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.ChallengeManager;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.PrisonerStatus;
import us.corenetwork.limbo.Util;

public class EnterChallengeCommand extends BaseLimboCommand {

	public EnterChallengeCommand()
	{
		permission = "enter";
		desc = "Notes the fact that player entered a challenge, starts tracking time.";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		if(args.length != 2)
		{
			Util.Message("Usage: /lim enter <player> <challenge>", sender);
			return;
		}
	
		Player player = null;
		
		player = LimboPlugin.instance.getServer().getPlayer(args[0]);
		if(player == null)
		{
			Util.Message("Could not find player called " + args[0], sender);
			return;
		}
		
		if(LimboManager.getPrisonerStatus(player) != PrisonerStatus.DURING)
		{
			Util.Message("You cannot participate in the challenge outside of Limbo", sender);
			return;
		}
		
		String challenge = args[1];
		
		if(ChallengeManager.challengeExists(challenge))
		{
			if(ChallengeManager.isPlayerTakingPart(player) == false) 
			{
				ChallengeManager.enterChallenge(player, challenge);
			}
			else
			{
				Util.Message("You cannot join two challenges at the same time!", sender);
				return;
			}
		}
		else
		{
			Util.Message("Specified challenge doesn't exist in config!", sender);
			return;
		}
	}

}
