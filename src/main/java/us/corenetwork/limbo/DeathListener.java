package us.corenetwork.limbo;

import java.util.Date;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		Environment env = player.getWorld().getEnvironment();
		
		if(env == Environment.THE_END)
		{
			//Dead in limbo
		}
		else 
		{
			LimboIO.saveDeath(player.getUniqueId().toString(), (new Date()).getTime(), event.getDeathMessage());
		}
	}
	
	
	
	
}
