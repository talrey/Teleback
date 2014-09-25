/**
 * TelebackPlugin.java
 * back-teleporting plugin for the Arcane Survival server.
 * @author Morios (Mark Talrey)
 * @version a.0 for Minecraft 1.7.10
 */

package teleback;

//import java.io.FileInputStream;
//import java.io.ObjectInputStream;
//import java.io.FileOutputStream;
//import java.io.ObjectOutputStream;

//import java.util.Arrays;
//import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
//import org.bukkit.OfflinePlayer;
//import org.bukkit.World;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
//import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class TelebackPlugin extends JavaPlugin
{
	private HashMap<UUID, Location> backList = new HashMap<>();
	
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean ret = true;
		
		if (cmd.getName().equals("test"))
		{
			ret = testFunction(args, sender);
		}
		else if (cmd.getName().equals("back"))
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
						String com = "tp " + dest.getX() + " " + dest.getY() + " " + dest.getZ();
						ret = pl.performCommand(com);
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
		sender.sendMessage("arbl");
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
		@EventHandler
		public void detectTeleport (PlayerTeleportEvent e)
		{
			if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND))
			{
				backList.put(e.getPlayer().getUniqueId(),e.getPlayer().getLocation());
			}
		}
	}
}
