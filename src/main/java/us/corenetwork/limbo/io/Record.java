package us.corenetwork.limbo.io;

public class Record {

	public String uuid;
	public String challenge;
	public long duration;
	
	public Record(String uuid, String challenge, long duration)
	{
		this.uuid = uuid;
		this.challenge = challenge;
		this.duration = duration;
	}
	
}
