package fr.frivec.core.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.frivec.spigot.managers.AbstractManager;
import fr.frivec.spigot.menus.MenuManager;

public class Managers extends AbstractManager {

	private List<AbstractManager> managers;
	
	private MenuManager menuManager;
	private PlayerManager playerManager;
	
	public Managers(JavaPlugin javaPlugin) {
		
		super(javaPlugin);
		
		init();
	}

	@Override
	public void onConnect(Player player) {
		
		for(AbstractManager manager : this.managers)
			
			manager.onConnect(player);
		
	}

	@Override
	public void onDisable() {
	
		for(AbstractManager manager : this.managers)
				
			manager.onDisable();
		
	}

	@Override
	public void onDisconnect(Player player) {
		
		for(AbstractManager manager : this.managers)
			
			manager.onDisconnect(player);
		
	}
	
	private void init() {
		
		this.managers = new ArrayList<>();
		
		this.menuManager = new MenuManager(this.javaPlugin);
		this.managers.add(this.menuManager);
		
		this.playerManager = new PlayerManager(this.javaPlugin);
		this.managers.add(this.playerManager);
		
	}
	
	public MenuManager getMenuManager() {
		return menuManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}

}
