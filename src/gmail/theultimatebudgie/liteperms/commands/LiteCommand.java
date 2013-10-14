package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.LitePerms;

import org.bukkit.command.CommandSender;

public interface LiteCommand {

	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args);
	
}
