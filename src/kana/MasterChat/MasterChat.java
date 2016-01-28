package kana.MasterChat;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class MasterChat extends JavaPlugin implements Listener{
	
	private Logger logger = Logger.getLogger("Minecraft");
	public Plugin plugin;
	public MasterChatCommand commandL;
	public PluginCommand batchcommand;
	
	// ------------------
	// ---- onEnable ----
	// ------------------
	public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
    	
		pm.registerEvents(this, this);
		pm.registerEvents(new MasterChatListener(this), this);
		
		this.commandL = new MasterChatCommand(this);
        this.batchcommand = getCommand("mc");
        batchcommand.setExecutor(commandL);
                
        // On charge vault
        //----------------
    	Vault.load(this);
    	Vault.setupChat();
    	Vault.setupPermissions();
    	Vault.setupEconomy();
    	if (!Vault.setupEconomy()){
            logger.info(String.format("[%s] - Necessite Vault pour fonctionner!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }    	
    	
    	// On charge les configurations
    	//-----------------------------
    	this.loadConfig();
		 
    	// On charge les messages
    	//-----------------------
    	loadMessage();
    	
		logger.info("[MasterChat] Plugin charge parfaitement!");
    }
	
	// -------------------
	// ---- onDisable ----
	// -------------------
    public void onDisable(){
            logger.info("[MasterChat] Plugin desactive...");
    }
    
    // --------------------
 	// ---- loadConfig ----
 	// --------------------
    public void loadConfig(){           
    	
    	// On créé le fichier de configuration par default
    	//------------------------------------------------
    	this.getConfig().options().copyDefaults(true);	
    	this.saveConfig();
    	
    	// On créé le fichier de configuration tell.yml
    	//---------------------------------------------
    	
    	
    }
    
    // ---------------------
  	// ---- loadMessage ----
  	// ---------------------
    public void loadMessage(){
		List<String> annonce = this.getConfig().getStringList("annonce");
		int delaySeconde = getConfig().getInt("annonceDelay");
		int delayTicks = (int) (delaySeconde / 0.05);

		
		for(int i=0; i < annonce.size(); i++) {
			int num = i;
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
		        @Override
		        public void run() {
		        	getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', annonce.get(num)));		        	
		        }
		    }, delayTicks * i);
		}
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    scheduler.scheduleSyncDelayedTask(this, new Runnable() {
	        @Override
	        public void run() {
	        	loadMessage();		        	
	        }
	    }, delayTicks * annonce.size());
	}
	
    // -----------------------
  	// ---- formatMessage ----
  	// -----------------------
	public String formatMessage(Player player, String syntax){		
		syntax = syntax.replace("{player}", player.getName());
		syntax = syntax.replace("{world}", player.getWorld().getName());
		syntax = syntax.replace("{prefix}", Vault.chat.getGroupPrefix(player.getWorld(), Vault.permission.getPrimaryGroup(player)));
		syntax = syntax.replace("{suffix}", Vault.chat.getGroupSuffix(player.getWorld(), Vault.permission.getPrimaryGroup(player)));
		syntax = syntax.replace("{gamemode}", player.getGameMode().toString());
		syntax = syntax.replace("{healt}", String.valueOf(player.getHealth()));
		syntax = syntax.replace("{exp}", String.valueOf(player.getExp()));
		syntax = syntax.replace("{level}", String.valueOf(player.getLevel()));
		syntax = syntax.replace("{balance}", String.valueOf(Vault.economy.getBalance(player)));
		return syntax;
	}
}
