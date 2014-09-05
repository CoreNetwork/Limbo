package us.corenetwork.limbo;

import java.util.HashMap;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Prisoner;

public class LimboManager {

	
	public static void imprison(Player player)
	{
		Prisoner prisoner = new Prisoner(player.getUniqueId().toString(), Util.currentTime(), Util.parseTimeToMilis(Settings.DEFAULT_DURATION.string()), false, false, null, 0);
		LimboIO.insertPrisoner(prisoner);
	}
	
	public static void moveIn(final Player player)
	{
		Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_ENTRY.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
	}
	
	public static void release(Player player)
	{
		Prisoner prisoner = LimboIO.getPrisoner(player);
		prisoner.toRelease = true;
		LimboIO.updatePrisoner(prisoner);
	}
	
	public static void moveOut(final Player player)
	{
		//passing whatever, we only need an object with uuid
		LimboIO.deletePrisoner(new Prisoner(player.getUniqueId().toString(), 0, 0, false, false, null, 0));
		Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_EXIT.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
	}
	

	public static void respawnIn(final Player player)
	{
		Prisoner prisoner = LimboIO.getPrisoner(player);
		if(prisoner.spawnedOnce == false)
		{
			moveIn(player);
			prisoner.spawnedOnce = true;
			LimboIO.updatePrisoner(prisoner);
		}
		else
		{
			if(ChallengeManager.isPlayerTakingPart(player))
			{
				Util.RunCommands(Util.PrepareCommands(ChallengeManager.getRespawnCommands(prisoner.challenge), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
			}
			else
			{
				Util.RunCommands(Util.PrepareCommands(Settings.COMMANDS_ON_RESPAWN.stringList(), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
			}
		}
	}
	
	
	public static void modifyDuration(Player player, long modification)
	{
		Prisoner prisoner = LimboIO.getPrisoner(player);
		prisoner.duration += modification;
		LimboIO.updatePrisoner(prisoner);
	}
	
	public static PrisonerStatus getPrisonerStatus(Player player)
	{
		Prisoner prisoner = LimboIO.getPrisoner(player);
		if(prisoner == null)
		{
			return PrisonerStatus.OUTSIDE;
		}
		
		if(Util.currentTime() >= prisoner.startTime + prisoner.duration || prisoner.toRelease == true)
		{
			return PrisonerStatus.AFTER;
		}
		
		return PrisonerStatus.DURING;
	}

	public static long getMilisLeft(Player player)
	{
		Prisoner prisoner = LimboIO.getPrisoner(player);
		if(prisoner == null)
		{
			return 0;
		}
		else
		{
			long ticksLeft = prisoner.startTime + prisoner.duration - Util.currentTime();
			if(ticksLeft < 0)
				ticksLeft = 0;
			return ticksLeft;
		}
	}

}
