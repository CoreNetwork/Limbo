package us.corenetwork.limbo;

public enum Settings {

	DEBUG("Debug", false),
	
	MESSAGE_NO_PERMISSION("Messages.NoPermission", "No permission!"),
	MESSAGE_CONFIGURATION_RELOADED("Messages.ConfigurationReloaded", "Configuration reloaded successfully!");
	
	private String name;
	private Object def;
	
	private Settings(String Name, Object Def)
	{
		name = Name;
		def = Def;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Object getDefault()
	{
		return def;
	}
	
	public String string()
	{
		return (String) IO.config.get(name, def);
	}

	public Boolean bool()
	{
		return (Boolean) IO.config.get(name, def);
	}
}
