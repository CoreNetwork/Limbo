package us.corenetwork.limbo.commands;

import org.bukkit.command.CommandSender;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;
import us.corenetwork.limbo.io.IO;

public class ReloadCommand extends BaseLimboCommand {

	public ReloadCommand()
	{
		permission = "reload";
		desc = "Reload limbo config.";
		needPlayer = false;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		IO.LoadSettings();
		Util.Message(Settings.MESSAGE_CONFIGURATION_RELOADED.string(), sender);
	}

}
