package us.corenetwork.limbo.io;

public class Death {

	public String uuid;
	public long deathTime;
	public String deathMessage;
	
	public Death(String uuid, long deathTime, String deathMessage)
	{
		this.uuid = uuid;
		this.deathTime = deathTime;
		this.deathMessage = deathMessage;
	}
	
	
}
