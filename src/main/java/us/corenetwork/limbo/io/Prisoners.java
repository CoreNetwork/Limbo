package us.corenetwork.limbo.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.Logs;
import us.corenetwork.limbo.PrisonerStatus;
import us.corenetwork.limbo.Util;

public class Prisoners {

	private static Map<String, Prisoner> prisoners;
	
	static
	{
		prisoners = Collections.synchronizedMap(new HashMap<String, Prisoner>());
	}
	public static void readAll()
	{
		for(Prisoner p : LimboIO.getPrisoners())
		{
			prisoners.put(p.uuid, p);
		}
	}
	
	public static void saveAll()
	{
		for(Prisoner p : prisoners.values())
			LimboIO.updatePrisoner(p);
	}
	
	public static void add(Prisoner prisoner)
	{
		prisoners.put(prisoner.uuid, prisoner);
		LimboIO.insertPrisoner(prisoner);
	}
	
	public static void save(Prisoner prisoner)
	{
		LimboIO.updatePrisoner(prisoner);
	}
	
	public static Prisoner getPrisoner(OfflinePlayer player)
	{
		return prisoners.get(player.getUniqueId().toString());
	}
	
	public static List<Prisoner> getAllPrisoners()
	{
		return new ArrayList<Prisoner>(prisoners.values());
	}
	
	public static void remove(Prisoner prisoner)
	{
		Prisoner deleted = prisoners.remove(prisoner.uuid);
		LimboIO.deletePrisoner(prisoner);
	}

	public static int getTotalCount()
	{
		return prisoners.size();
	}
	
	public static int getActiveCount()
	{
		int count = 0;
		for(Prisoner p : prisoners.values())
		{
			if(getStatus(p) == PrisonerStatus.DURING)
				count++;
		}
		return count;
	}
	
	public static PrisonerStatus getStatus(Prisoner prisoner)
	{
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
}
