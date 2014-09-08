package us.corenetwork.limbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.IO;
import us.corenetwork.limbo.io.LimboIO;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;
import us.corenetwork.limbo.io.Record;

public class ChallengeManager {

	private static Map<String, ConfigurationSection> chMap;
	
	public static void reloadChMap()
	{
		ConfigurationSection chSection = IO.config.getConfigurationSection("Challenges");
		chMap = new HashMap<String, ConfigurationSection>();
		for(String key : chSection.getKeys(false))
		{
			chMap.put(key.toLowerCase(), chSection.getConfigurationSection(key));
		}
	}
	
	public static void enterChallenge(final Player player, String challenge)
	{
		updateChallenge(player, challenge, Util.currentTime());
		Util.RunCommands(Util.PrepareCommands(ChallengeManager.getEntryCommands(challenge), new HashMap<String, String>(){{put("<Player>", player.getName());}}));
	}
	
	public static void exitChallenge(final Player player, String challenge)
	{
		updateChallenge(player, "", 0);
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
		updateChallenge(player, "", 0);

		
		if(bestRecord != null && bestRecord.duration > duration)
		{
			Player prevBestPlayer = LimboPlugin.instance.getServer().getPlayer(UUID.fromString(bestRecord.uuid));
			
			List<String> msgList = chMap.get(challenge).getStringList("VictoryRecord");
			for(String msg : msgList)
			{
				Util.Broadcast(msg.replace("<Player>", player.getName()).replace("<PreviousPlayer>", prevBestPlayer.getName()).replace("<DetailedTime>",  
						Util.getDetailedTimeMessage(duration)).replace("<PreviousDetailedTime>",  Util.getDetailedTimeMessage(bestRecord.duration)));
			}
		}
		else
		{
			Util.Broadcast(chMap.get(challenge).getString("Victory").replace("<Player>", player.getName()).replace("<DetailedTime>",  Util.getDetailedTimeMessage(duration)));
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
		boolean isTakingPart;
		String chDb = Prisoners.getPrisoner(player).challenge;
		if(chDb == null)
			return false;
		if(chDb.equals(""))
			return false;
		return true;
	}
	
	public static boolean challengeExists(String challenge)
	{
		return chMap.get(challenge) != null;
	}	
	
	public static List<String> getRespawnCommands(String challenge)
	{
		return chMap.get(challenge).getStringList("CommandsOnRespawn");
	}
	public static List<String> getEntryCommands(String challenge)
	{
		return chMap.get(challenge).getStringList("CommandsOnEntry");
	}
	public static List<String> getExitCommands(String challenge)
	{
		return chMap.get(challenge).getStringList("CommandsOnExit");
	}
}
