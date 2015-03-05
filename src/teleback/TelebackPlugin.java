/**
 * TelebackPlugin.java
 * back-teleporting plugin for the Arcane Survival server.
 * @author Morios (Mark Talrey)
 * @version 1.3 for Minecraft 1.8.*
 */

package teleback;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public final class TelebackPlugin extends JavaPlugin
{
	private HashMap<UUID, Location> backList = new HashMap<>();
	
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean ret = true;
		/*
		if (cmd.getName().equals("test"))
		{
			ret = testFunction(args, sender);
		}
		else*/ if (cmd.getName().equals("back"))
		{
			if (sender instanceof Player)
			{
				Player pl = (Player)sender;
				if (pl.hasPermission("TelebackPlugin.back"))
				{
					Location dest = backList.get(pl.getUniqueId());
					if (dest != null)
					{
						backList.put(pl.getUniqueId(), pl.getLocation());
						ret = pl.teleport(dest);
						//String com = "tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ();
						//ret = pl.performCommand(com);
					}
					else
					{
						sender.sendMessage("No location to teleport to!");
					}
				}
			}
			else
			{
				sender.sendMessage("You must be a player!");
				ret = false;
			}
		}
		return ret;
	}
	
	private boolean testFunction (String[] args, CommandSender sender)
	{
		sender.sendMessage("you shouldn't see this.");
		return true;
	}
	
	@Override
	public void onEnable ()
	{
		getServer().getPluginManager().registerEvents(new BackListener(), this);
	}
	
	@Override public void onDisable()
	{
		// ze goggles
	}
	
	public final class BackListener implements Listener
	{
		@EventHandler (priority = EventPriority.HIGH)
		public void detectTeleport (PlayerCommandPreprocessEvent pcpe)
		{
			String msg = pcpe.getMessage();
			if (msg.startsWith("/home") || msg.startsWith("/tp"))
			{
				//Bukkit.getLogger().info("teleport issued!");
				backList.put(pcpe.getPlayer().getUniqueId(),pcpe.getPlayer().getLocation());
			}
			else if (msg.startsWith("/back"))
			{
				// catch, do nothing
			}
			else
			{
				//Bukkit.getLogger().info(
				//	"[Teleback]: Teleport by this cause isn't handled for /back");
				//Bukkit.getLogger().info(msg);
			}
		}
	}
}
