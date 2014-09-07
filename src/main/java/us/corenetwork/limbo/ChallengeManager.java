package us.corenetwork.limbo;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.IO;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;
import us.corenetwork.limbo.io.Record;

public class ChallengeManager {

	public static void enterChallenge(final Player player, String challenge)
	{
		updateChallenge(player, challenge, Util.currentTime());
		Util.RunCommands(Util.PrepareCommands(ChallengeManager.getEntryCommands(challenge), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
	}
	
	public static void exitChallenge(final Player player, String challenge)
	{
		updateChallenge(player, null, 0);
		Util.RunCommands(Util.PrepareCommands(ChallengeManager.getExitCommands(challenge), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
	}
	
	private static void updateChallenge(Player player, String challenge, long time)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		prisoner.challenge = challenge;
		prisoner.challengeStartTime = time;
		Prisoners.save(prisoner);
	}
	
	public static void finishChallenge(Player player, String challenge)
	{
		Prisoner prisoner = Prisoners.getPrisoner(player);
		long duration = Util.currentTime() - prisoner.challengeStartTime;
		Record bestRecord = LimboIO.getBestRecord(challenge);
		
		LimboIO.insertRecord(new Record(player.getUniqueId().toString(), challenge, duration));
		updateChallenge(player, null, 0);

		
		if(bestRecord != null && bestRecord.duration > duration)
		{
			Player prevBestPlayer = LimboPlugin.instance.getServer().getPlayer(UUID.fromString(bestRecord.uuid));
			
			List<String> msgList = IO.config.getStringList("Challenges."+challenge+".VictoryRecord");
			for(String msg : msgList)
			{
				Util.Broadcast(msg.replace("<Player>", player.getName()).replace("<PreviousPlayer>", prevBestPlayer.getName()).replace("<DetailedTime>",  
						Util.getDetailedTimeMessage(duration)).replace("<PreviousDetailedTime>",  Util.getDetailedTimeMessage(bestRecord.duration)));
			}
		}
		else
		{
			Util.Broadcast(IO.config.getString("Challenges."+challenge+".Victory").replace("<Player>", player.getName()).replace("<DetailedTime>",  Util.getDetailedTimeMessage(duration)));
		}
	}
	
	public static boolean isPlayerTakingPartIn(Player player, String challenge)
	{
		String dbCh = Prisoners.getPrisoner(player).challenge;
		if(dbCh == null)
			return false;
		else
			return dbCh.equals(challenge);
	}
	
	public static boolean isPlayerTakingPart(Player player)
	{
		return Prisoners.getPrisoner(player).challenge != null;
	}
	
	public static boolean challengeExists(String challenge)
	{
		return IO.config.get("Challenges." + challenge) != null;
	}	
	
	public static List<String> getRespawnCommands(String challenge)
	{
		return IO.config.getStringList("Challenges."+challenge+".CommandsOnRespawn");
	}
	public static List<String> getEntryCommands(String challenge)
	{
		return IO.config.getStringList("Challenges."+challenge+".CommandsOnEntry");
	}
	public static List<String> getExitCommands(String challenge)
	{
		return IO.config.getStringList("Challenges."+challenge+".CommandsOnExit");
	}
}
