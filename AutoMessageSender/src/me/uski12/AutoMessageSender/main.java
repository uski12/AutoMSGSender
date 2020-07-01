package me.uski12.AutoMessageSender;
import java.util.List;
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
		this.saveDefaultConfig();
	}
	
	@Override
	public void onDisable() {
		task.cancel();
	}
	private BukkitTask task;
	
	public void msgSender() {
		List<String> list = this.getConfig().getStringList("messages"); //Gets a list of messages from the config.yml file
		task = new BukkitRunnable() {
			public void run() {
				int rndmNum = (int) Math.floor((Math.random() * list.size())); //Random number generator
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', list.get(rndmNum))); //broadcasts the messages from config.yml
			}		
		}.runTaskTimer(this, 200, (this.getConfig().getInt("interval")*20));
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof ConsoleCommandSender) {
			if(label.equalsIgnoreCase("amstart")) {
				Bukkit.getConsoleSender().sendMessage("AutoMessageSender has started.");
				msgSender();
			}
			if(label.equalsIgnoreCase("amstop")) {
				if(task.isCancelled())
					Bukkit.getConsoleSender().sendMessage("AutoMessageSender is not running.");
				else {
					Bukkit.getConsoleSender().sendMessage("AutoMessageSender has stopped successfully.");	
					task.cancel();
				}
			}
			if(label.equalsIgnoreCase("amreload")) {
				if(task.isCancelled()) {
					Bukkit.getConsoleSender().sendMessage("Reloaded config.yml.");
					this.reloadConfig(); 
				}
				else {
					task.cancel();	
					this.reloadConfig();
					Bukkit.getConsoleSender().sendMessage("Reloaded config.yml for AutoMessageSender and restarted AutoMessageSender successfully.");
					msgSender();
				}
			}
				
		}
		return false;
	}

}