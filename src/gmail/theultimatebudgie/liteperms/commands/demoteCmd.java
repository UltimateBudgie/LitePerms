package gmail.theultimatebudgie.liteperms.commands;

import gmail.theultimatebudgie.liteperms.Group;
import gmail.theultimatebudgie.liteperms.LitePerms;
import gmail.theultimatebudgie.liteperms.LitePlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class demoteCmd implements LiteCommand {

	@Override
	public void invokeCommand(LitePerms plugin, CommandSender sender, String[] args) {
		if (args.length <= 0 ) {
			sender.sendMessage(LitePerms.WARNING + LitePerms.USAGE + "Correct Usage: /lp demote (PlayerName)");
			return;
		}
		
		String pName = args[0];
		Player target = Bukkit.getPlayerExact(pName);
		if (target == null) {
			Group g = plugin.getYaml().getPlayerGroup(pName);
			if (g.getParent() != null) {
				plugin.getYaml().setPlayerGroup(pName, g.getParent());
				sender.sendMessage(LitePerms.INFO + pName + " is offline but will be demoted on next login.");
			} else {
				sender.sendMessage(LitePerms.WARNING + pName + "'s group has no Parent Group and therefore they could not be demoted.");
			}
			return;
		}
		
		LitePlayer litePlayer = LitePlayer.getPlayerByName(pName);
		if (litePlayer.getGroup().getParent() != null) {
			litePlayer.setGroup(litePlayer.getGroup().getParent());
			plugin.getYaml().setPlayerGroup(pName, litePlayer.getGroup());
			litePlayer.updatePermissions();
			sender.sendMessage(LitePerms.INFO + LitePerms.SUCCESS + pName + " has been demoted.");
		} else {
			sender.sendMessage(LitePerms.WARNING + pName + "'s group has no Parent Group and therefore they could not be demote.");
		}
		
		return;
	}

}
