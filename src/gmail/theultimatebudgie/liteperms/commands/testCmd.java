package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.LitePerms;

import org.bukkit.command.CommandSender;

public class testCmd implements LiteCommand {

	@Override
	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args) {
		sender.sendMessage("LOL");
		if (!(args.length > 0)) {
			sender.sendMessage("Not enough args bro");
		}
	}

}
