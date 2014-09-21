package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import us.corenetwork.limbo.ChallengeManager;
import us.corenetwork.limbo.Util;
import us.corenetwork.limbo.io.LimboIO;

public class ResetCommand extends BaseLimboCommand {

	
	public ResetCommand()
	{
		permission = "reset";
		desc = "Clears a scoreboard for a challenge.";
		needPlayer = false;
	}
	
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		if(args.length != 1)
		{
			Util.Message("Usage: /lim reset <challenge>", sender);
			return;
		}
		
		String challenge = args[0].toLowerCase();
		
		if(ChallengeManager.challengeExists(challenge) == false)
		{
			Util.Message("Challenge not found!", sender);
			return;
		}

		LimboIO.resetChallenge(challenge);
		
		Util.Message(challenge + " records cleared.", sender);
	}

}
