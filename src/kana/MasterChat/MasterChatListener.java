package kana.MasterChat;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MasterChatListener implements Listener{
	
	MasterChat plugin;
	public MasterChatListener(MasterChat plugin){
		this.plugin = plugin;
	}
	
	// --------------------
	// ---- PlayerChat ----
	// --------------------
	@org.bukkit.event.EventHandler(priority=EventPriority.NORMAL)
    public void PlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		event.setCancelled(true);
		
		// On vérifie la permission d'utiliser le chat
		//--------------------------------------------
		if(player.hasPermission("masterchat.use")){	
			String syntax = this.plugin.getConfig().getString("syntax");
			String newSyntax = this.plugin.formatMessage(player, syntax);			
			
			if(player.hasPermission("masterchat.color")){
				newSyntax = ChatColor.translateAlternateColorCodes('&', newSyntax) + ChatColor.translateAlternateColorCodes('&', event.getMessage());
			}
			else{
				newSyntax = ChatColor.translateAlternateColorCodes('&', newSyntax) + ChatColor.WHITE + event.getMessage();
			}
			
			for (Player p : Bukkit.getOnlinePlayers()){
				List<String> playerBlackList = this.plugin.getConfig().getStringList("blacklist");
				
				if(playerBlackList.contains(player.getName())){
					return;
				}
				else{
					p.sendMessage(newSyntax);
					
					if (event.getMessage().contains(p.getDisplayName()) && p.hasPermission("masterchat.sound")){
					        p.getWorld().playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 0.0F);
					}
				}
			}			
		}
		// Si le joueur n a pa le droit d'utiliser le chat
		//------------------------------------------------
		else{
			player.sendMessage(ChatColor. RED + "[Chat] " + ChatColor.WHITE + "Vous n'avez pas la permission d'utiliser le chat");
			return;
		}
	}
	
	// ----------------------
	// ---- onPlayerJoin ----
	// ----------------------
	@org.bukkit.event.EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		
		// Si le joueur est déjà venu sur le serveur
		//------------------------------------------
		if(e.getPlayer().hasPlayedBefore() && player.hasPermission("masterchat.join")) {
			List<String> messageJoin = this.plugin.getConfig().getStringList("messageJoin");
			
			for(int i=0; i<messageJoin.size(); i++){
				String newMessageJoin = this.plugin.formatMessage(player, messageJoin.get(i));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', newMessageJoin));
			}		    
		}
		// Si le joueur est nouveau sur le serveur
		//----------------------------------------
		else{
			List<String> messageFirstJoin = this.plugin.getConfig().getStringList("messageFirstJoin");
			
			for(int i=0; i<messageFirstJoin.size(); i++){
				String newMessageFirstJoin = this.plugin.formatMessage(player, messageFirstJoin.get(i));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', newMessageFirstJoin));
			}
		}
	}
}
