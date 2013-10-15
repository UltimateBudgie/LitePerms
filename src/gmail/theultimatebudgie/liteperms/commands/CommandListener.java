package gmail.theultimatebudgie.liteperms.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import gmail.theultimatebudgie.liteperms.LitePerms;

public class CommandListener implements CommandExecutor{
	LitePerms plugin;
	
	public CommandListener (LitePerms plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lp")){
			if (args.length > 0) {
				try {
					Class<?> clazz = Class.forName("gmail.theultimatebudgie.liteperms.commands." + args[0].toLowerCase() + "Cmd");
					LiteCommand ltcmd = (LiteCommand) clazz.newInstance();
					
					String[] newArgs = new String[args.length - 1];
					if (args.length > 1) {
						for (int i = 0; i < newArgs.length; i++) {
							newArgs[i] = args[i+1];
						}
					}
					ltcmd.invokeCommand(plugin, sender, newArgs);
					return true;
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					sender.sendMessage(LitePerms.WARNING + "Sorry, that command was not found.");
				}
				
			}
			printCommandsList(sender);
			return true;
		}
		
		
		return false;
	}
	
	private void printCommandsList(CommandSender sender) {
		String message = LitePerms.INFO + ChatColor.GRAY + "List of available commands:\n" + ChatColor.RESET +
							"/lp promote (PlayerName) - Promote a player (if possible).\n" +
							"/lp demote (PlayerName) - Demote a player (if possible).\n" +
							"/lp setgroup (PlayerName) (GroupName) - Sets the player to the specified group" +
							"/lp getgroup (PlayerName) - Gets a player's group." +
							"/lp addperm [Group/Player] (GroupName/PlayerName) (Permission) - Adds the specificed permission to the Group or Player\n" +
							"/lp setprefix [Player/Group] (PlayerName/GroupName) \"(*OPTIONAL*:Prefix)\" - Sets or removes a player's/group's chat-prefix" +
							"/lp getprefix [Group/Player] (GroupName/PlayerName) - Gets a group's/player's chat-prefix" +
						LitePerms.INFO + ChatColor.GRAY + "List completed.";
							
		sender.sendMessage(message);
	}

}
