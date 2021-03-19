package fr.frivec.listeners.entities;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.frivec.commands.DevCommand;
import fr.frivec.core.statement.States;
import fr.frivec.core.teams.Teams;

public class EntityDamageListener implements Listener {
	
	@EventHandler
	public void onDamage(final EntityDamageEvent event) {
		
		if(event.getEntity().getType().equals(EntityType.PLAYER)) {
			
			final Player player = (Player) event.getEntity();
			
			//While the game is not started or is finished, I cancel all the damages
			if(States.isState(States.WAIT) || States.isState(States.END))
				
				event.setCancelled(true);
			
			//The game is running so we can modify the damage
			else if(States.isState(States.GAME)) {
				
				final DamageCause cause = event.getCause();
				
				if(cause.equals(DamageCause.FALL)) {
					
					if(player.isGliding())
						
						//To avoid the death by elytra, I divide the damage by 2
						event.setDamage(event.getDamage() / 2);
					
					else
						
						return;
					
				}
				
			}
			
		}else
			
			if(event.getEntity().getType().equals(EntityType.IRON_GOLEM) || event.getEntity().getType().equals(EntityType.VILLAGER))
				
				if(DevCommand.devMode)
					
					return;
		
				else
				
					event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onPlayerDamage(final EntityDamageByEntityEvent event) {
		
		if(States.isState(States.WAIT))
			
			return;
	
		//The damager is a player
		if(event.getDamager().getType().equals(EntityType.PLAYER)) {
			
			final Player damager = (Player) event.getDamager();
			
			//The entity is a player
			if(event.getEntity().getType().equals(EntityType.PLAYER)) {
				
				final Player victim = (Player) event.getEntity();
				
				//The player is in the same team as the damager. Cancel the damages
				if(Teams.isPlayerInTeam(victim, Teams.getPlayerTeam(damager)) || victim.hasMetadata("SPAWN_PROTECTION"))
					
					event.setCancelled(true);
				
			}
			
		}else
			
			return;
		
	}

}
