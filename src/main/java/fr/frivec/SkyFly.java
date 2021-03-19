package fr.frivec;

import java.text.SimpleDateFormat;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import fr.frivec.commands.DevCommand;
import fr.frivec.core.managers.Managers;
import fr.frivec.core.statement.States;
import fr.frivec.core.tasks.GameTask;
import fr.frivec.core.teams.Teams;
import fr.frivec.listeners.entities.EntityDamageListener;
import fr.frivec.listeners.entities.PlayerInteractListener;
import fr.frivec.listeners.inventory.InventoryClickListener;
import fr.frivec.listeners.player.ChatListener;
import fr.frivec.listeners.player.PlayerDeathListener;
import fr.frivec.listeners.player.PlayerJoinListener;
import fr.frivec.listeners.player.PlayerMoveListener;
import fr.frivec.listeners.player.PlayerPickUpListener;
import fr.frivec.listeners.utils.UtilsListener;
import fr.frivec.spigot.API;
import fr.frivec.spigot.listeners.ListenerManager;
import fr.frivec.spigot.packets.manager.PacketsManager;
import fr.frivec.spigot.scoreboard.ScoreboardPattern;

public class SkyFly extends JavaPlugin {
	
	private static SkyFly instance;
	
	private ListenerManager listenerManager;
	private Managers managers;
	private PacketsManager manager;
	
	private ScoreboardPattern waitBoard,
								gameBoard;
	
//	private HitBoxTask task;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		States.setCurrentState(States.WAIT);
		
		this.listenerManager = API.getInstance().getListenerManager();
		this.listenerManager.registerListener(new PlayerJoinListener());
		this.listenerManager.registerListener(new InventoryClickListener());
		this.listenerManager.registerListener(new UtilsListener());
		this.listenerManager.registerListener(new EntityDamageListener());
		this.listenerManager.registerListener(new PlayerMoveListener());
		this.listenerManager.registerListener(new PlayerPickUpListener());
		this.listenerManager.registerListener(new PlayerDeathListener());
		this.listenerManager.registerListener(new PlayerInteractListener());
		this.listenerManager.registerListener(new ChatListener());
		
		this.managers = new Managers(this);
		this.manager = new PacketsManager();
		
		this.waitBoard = new ScoreboardPattern("§bSkyFly");
		this.waitBoard.setLine(0, "§e");
		this.waitBoard.setLine(1, "§cEn attente...");
		this.waitBoard.setLine(2, "§i");
		this.waitBoard.setLine(3, "§6Plugin par Frivec");
		
		this.gameBoard = new ScoreboardPattern("§bSkyFly");
		this.gameBoard.setLine(0, "§e");
		this.gameBoard.setLine(1, "Scores: §c" + Teams.RED.getPoints() + " §r- §b" + Teams.BLUE.getPoints());
		this.gameBoard.setLine(2, "§b");
		this.gameBoard.setLine(3, "§e§lStatistiques:");
		this.gameBoard.setLine(4, "§9");
		this.gameBoard.setLine(5, "  -§aKills: §r0");
		this.gameBoard.setLine(6, "  -§aPoints: §r0");
		this.gameBoard.setLine(7, "§l");
		this.gameBoard.setLine(8, "Temps: §a" + new SimpleDateFormat("mm:ss").format(GameTask.getTimer() * 1000));
		this.gameBoard.setLine(9, "§m");
		this.gameBoard.setLine(10, "§6Plugin par Frivec");
		
//		this.task = new HitBoxTask();
//		this.task.start();
		
		new DevCommand();
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
//		HitBoxTask.running = false;
		
		for(Teams team : Teams.values()) {
			
			if(!team.equals(Teams.SPECTATOR) && !States.isState(States.WAIT)) {
				
				team.getVillager().remove();
				team.getGolem().remove();
				
			}
			
		}
		
		for(Entity entities : this.getServer().getWorld("world").getEntities())
			
			if(entities.getType().equals(EntityType.DROPPED_ITEM))
				
				entities.remove();
		
		super.onDisable();
	}
	
	public ListenerManager getListenerManager() {
		
		return this.listenerManager;
		
	}
	
	public static SkyFly getInstance() {
		return instance;
	}
	
	public Managers getManagers() {
		return managers;
	}
	
	public ScoreboardPattern getGameBoard() {
		return gameBoard;
	}
	
	public ScoreboardPattern getWaitBoard() {
		return waitBoard;
	}
	
	public PacketsManager getPacketsManager() {
		return manager;
	}
	
}
