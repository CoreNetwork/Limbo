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
			
			PrisonerStatus status = Prisoners.getStatus(prisoner);
			if(player != null && status == PrisonerStatus.AFTER && player.isOnline() && player.isDead() == false)
			{
				LimboManager.moveOut(player);
			}
		}
	}

}
