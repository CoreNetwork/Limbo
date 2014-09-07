package us.corenetwork.limbo;

import org.bukkit.entity.Player;
import us.corenetwork.limbo.io.Prisoner;
import us.corenetwork.limbo.io.Prisoners;

public class TimeChecker implements Runnable {

	public void run()
	{
		for(Prisoner prisoner : Prisoners.getAllPrisoners())
		{
			Player player = LimboPlugin.instance.getServer().getPlayer(Util.getUUIDFromString(prisoner.uuid));
			
			
			if(player != null && player.isOnline())
			{
				PrisonerStatus status = Prisoners.getStatus(prisoner);
				if(status == PrisonerStatus.DURING && prisoner.notify)
				{
					
					long minLeft = 1 + LimboManager.getMilisLeft(player) / 1000 / 60;
					if(prisoner.notificationTimes.contains(new Integer((int) minLeft)))
					{
						Util.Message(Settings.MESSAGE_NOTIFICATION.string().replace("<Time>", Util.getSimpleTimeMessage(LimboManager.getMilisLeft(player))), player);
						prisoner.notificationTimes.remove(new Integer((int) minLeft));
					}
				}
				
				if(status == PrisonerStatus.AFTER && player.isDead() == false)
				{
					LimboManager.moveOut(player, false);
				}
			}
		}
	}

}
