package us.corenetwork.limbo.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.corenetwork.limbo.LimboManager;
import us.corenetwork.limbo.LimboPlugin;
import us.corenetwork.limbo.PrisonerStatus;
import us.corenetwork.limbo.Settings;
import us.corenetwork.limbo.Util;
import us.corenetwork.limbo.io.LimboIO;

/**
 * Created by Ginaf on 2015-03-17.
 */
public class TutorialCommand extends BaseLimboCommand {

    public TutorialCommand()
    {
        permission = "tutorial";
        desc = "Notes the tutorial as completed for the player";
        needPlayer = false;
    }

    @Override
    public void run(CommandSender sender, String[] args)
    {
        if(args.length < 1 || args.length > 3)
        {
            Util.Message("Usage : /lim tutorial [<player>] [complete|reset] [silent]", sender);
            return;
        }

        Player player;
        boolean silent = false;
        boolean self = false;
        boolean complete = false;

        if(args.length == 1)
        {
            if(sender instanceof Player)
            {
                player = (Player) sender;
                self = true;
            }
            else
            {
                Util.Message("You have to execute /lim tutorial [complete|reset] as player", sender);
                return;
            }

            if(isProperArg(args[0]))
            {
                complete = isComplete(args[0]);
            }
            else
            {
                Util.Message("Usage : /lim tutorial [<player>] [complete|reset] [silent]", sender);
                return;
            }
        }
        else
        if(args.length == 2)
        {
            if(isSilent(args[1]))
            {
                if(sender instanceof Player)
                {
                    player = (Player) sender;
                    self = true;
                }
                else
                {
                    Util.Message("You have to execute /lim tutorial [complete|reset] silent  as player", sender);
                    return;
                }

                if(isProperArg(args[0]))
                {
                    complete = isComplete(args[0]);
                }
                else
                {
                    Util.Message("Usage : /lim tutorial [<player>] [complete|reset] [silent]", sender);
                    return;
                }
                silent = true;
            }
            else if(isProperArg(args[1]))
            {
                complete = isComplete(args[1]);

                player = LimboPlugin.instance.getServer().getPlayer(args[0]);
                if(player == null)
                {
                    Util.Message("Could not find player called " + args[0], sender);
                    return;
                }
            }
            else
            {
                Util.Message("Usage : /lim tutorial [<player>] [complete|reset] [silent]", sender);
                return;
            }
        }
        else
        {
            if(isSilent(args[2]))
            {
                silent = true;
            }
            else
            {
                Util.Message("Usage : /lim tutorial [<player>] [complete|reset] [silent]", sender);
                return;
            }

            if(isProperArg(args[1]))
            {
                complete = isComplete(args[1]);
            }
            else
            {
                Util.Message("Usage : /lim tutorial [<player>] [complete|reset] [silent]", sender);
                return;
            }

            player = LimboPlugin.instance.getServer().getPlayer(args[0]);
            if(player == null)
            {
                Util.Message("Could not find player called " + args[0], sender);
                return;
            }
        }

        if(silent == false)
        {
            if(complete)
            {
                if(self)
                {
                    Util.Message(Settings.MESSAGE_TUTORIAL_COMPLETE_SELF.string(), sender);
                }
                else
                {
                    Util.Message(Settings.MESSAGE_TUTORIAL_COMPLETE_SELF.string(), player);
                    Util.Message(Settings.MESSAGE_TUTORIAL_COMPLETE.string().replace("<Player>", player.getName()), sender);
                }
            }
            else
            {
                if(self)
                {
                    Util.Message(Settings.MESSAGE_TUTORIAL_RESET_SELF.string(), sender);
                }
                else
                {
                    Util.Message(Settings.MESSAGE_TUTORIAL_RESET_SELF.string(), player);
                    Util.Message(Settings.MESSAGE_TUTORIAL_RESET.string().replace("<Player>", player.getName()), sender);
                }
            }

        }

        LimboIO.setSeenTutorial(player, complete);
    }


    private boolean isComplete(String arg)
    {
        return arg.equalsIgnoreCase("complete");
    }

    private boolean isReset(String arg)
    {
        return arg.equalsIgnoreCase("reset");
    }

    private boolean isSilent(String arg)
    {
        return  arg.equalsIgnoreCase("silent");
    }

    private boolean isProperArg(String arg)
    {
        return isComplete(arg) || isReset(arg);
    }

}
