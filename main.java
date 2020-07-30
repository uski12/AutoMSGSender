package me.uski12.AutoMessageSender;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class main extends JavaPlugin{
	@Override
	public void onEnable() {
		if(this.getConfig().getBoolean("autoStart")) //checks config.yml for the value of autoStart, and accordingly starts the plugin
			msgSender();
	}
	
	@Override
	public void onDisable() {
	}
	
	private BukkitTask task;
	
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if(sender instanceof ConsoleCommandSender) {
				if(args[0].equalsIgnoreCase("start")) 
					if(task.isCancelled()) {
						msgSender();
						Bukkit.getConsoleSender().sendMessage("AMS has started.");
					}
					else
						Bukkit.getConsoleSender().sendMessage("AMS is already running!");
				if(args[0].equalsIgnoreCase("stop")) {
					if(task.isCancelled())
						Bukkit.getConsoleSender().sendMessage("AMS is not running.");
					else 
						Bukkit.getConsoleSender().sendMessage("AMS has been stopped."); 
					task.cancel();
				}
				if(args[0].equalsIgnoreCase("reload")) {
					this.reloadConfig();
					if(task.isCancelled())
						Bukkit.getConsoleSender().sendMessage("Reloaded config.yml for AutoMessageSender. AMS is NOT running.");
					else {
						msgSender();
						Bukkit.getConsoleSender().sendMessage("Reloaded config.yml for AutoMessageSender. AMS has been restarted.");
					} 
				}
				if(args[0].equalsIgnoreCase("list")) 
					Bukkit.getConsoleSender().sendMessage(StringUtils.join(this.getConfig().getList("messages"), "\n"));
		}
				if(args[0].equalsIgnoreCase("broadcast"))
					if(this.getConfig().getBoolean("manualBroadcastCommand"))
						if(sender.hasPermission("main.broadcastCMD"))
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, " ").substring(10)));
						else
							sender.sendMessage(ChatColor.RED + "You do not have permissions to use /ams broadcast.");	
		return false;
	}


	public void msgSender() {
		final List<String> list = this.getConfig().getStringList("messages"); //gets the list of messages from the config.yml file
		this.getConfig();
		if(this.getConfig().getBoolean("random")) {
			task = new BukkitRunnable() {
				public void run() {
					final int rndmNum = (int) Math.floor((Math.random() * list.size())); // random number generator
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', list.get(rndmNum))); // broadcasts the messages from config.yml
			}
		}.runTaskTimer(this, 200, (this.getConfig().getInt("interval") * 20));
	} else {
		task = new BukkitRunnable() {
			int i = 0;

			public void run() {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', list.get(i))); // broadcasts the messages pulled from the config.yml file
					i++;
					if (i == list.size())
						i = 0;
				}
			}.runTaskTimer(this, 200, (this.getConfig().getInt("interval") * 20));
		}
	}
}