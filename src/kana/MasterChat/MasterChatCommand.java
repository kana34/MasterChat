package kana.MasterChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MasterChatCommand implements CommandExecutor{
	
	MasterChat plugin;
	public MasterChatCommand(MasterChat plugin){
		this.plugin = plugin;
	}
	
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		// Récupération du joueur qui envoie la commande
		//----------------------------------------------
		Player player = null;
    	if(sender instanceof Player){
    		player = (Player) sender;
    	}
    	
    	if(commandLabel.equalsIgnoreCase("mc")){
	        if(args.length == 0){
	        	sender.sendMessage(ChatColor.GOLD + "[MasterChat] " + ChatColor.WHITE + "Tapez /mc help");
	        	return true;
	        }
	        else if(args.length == 1){
	        	//--------------
	        	//---- HELP ----
	        	//--------------
	        	if(args[0].equalsIgnoreCase("help")){
	        		sender.sendMessage(ChatColor.WHITE + "----------- HELP Chat -----------");
					sender.sendMessage(ChatColor.WHITE + "/mc help" + ChatColor.GREEN + " Obtenir l'aide");
					sender.sendMessage(ChatColor.WHITE + "/mc reload" + ChatColor.GREEN + " Recharger la configuration");
		        	return true;
	        	}
	        	//----------------
	        	//---- RELOAD ----
	        	//----------------
	        	else if(args[0].equalsIgnoreCase("reload")){
	        		if(!Vault.permission.has(player, "masterchat.reload")){
	        			sender.sendMessage(ChatColor.RED + "[MasterChat] " + ChatColor.WHITE + "Vous n'avez pas la permission d'utiliser cette commande !");
	        			return false;
	        		}
	        		else{
		        		this.plugin.reloadConfig();
		        		sender.sendMessage(ChatColor.GREEN + "[MasterChat] " + ChatColor.WHITE + "Configuration rechargé !");
			        	return true;
	        		}
	        	}
	        	else{
	        		sender.sendMessage(ChatColor.RED + "[MasterChat] " + ChatColor.WHITE + "Commande inconnu !");
	        		return false;
	        	}
	        }
	        else{
	        	sender.sendMessage(ChatColor.RED + "[MasterChat] " + ChatColor.WHITE + "Commande inconnu !");
        		return false;        		
	        }
    	}
		return false;
	}
}
