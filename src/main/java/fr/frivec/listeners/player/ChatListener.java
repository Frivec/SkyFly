package fr.frivec.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.frivec.core.statement.States;
import fr.frivec.core.teams.Teams;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {
		
		final Player player = event.getPlayer();
		final Teams team = Teams.getPlayerTeam(player);
		String message = event.getMessage();
		
		event.setCancelled(true);
		
		if(team != null) {
			
			if(States.isState(States.GAME)) {
				
				if(team.equals(Teams.SPECTATOR)) {
					
					for(Player spectators : team.getPlayersObjects())
						
						spectators.sendMessage(team.getNameColor() + team.getPrefix() + player.getName() + "§r: " + message);
					
					return;
					
				}
				
				if(message.startsWith("!")) {
					
					message = message.replace("!", "");
					Bukkit.broadcastMessage("(global) " + team.getPrefix() + " " + player.getName() + "§r: " + message);
					return;
					
				}else {
					
					for(Player players : team.getPlayersObjects())
						
						players.sendMessage("(équipe) " + team.getPrefix() + " " + player.getName() + "§r: " + message);
					
					return;
					
				}
				
			}else
				
				Bukkit.broadcastMessage(team.getPrefix() + " " + player.getName() + "§r: " + message);
			
		}else
			
			if(!States.isState(States.GAME))
				
				Bukkit.broadcastMessage(player.getName() + ": " + message);
			
			else 
				
				player.sendMessage("§cUne erreur est survenue lorsque nous avons voulu récupérer votre équipe. Veuillez essayer de vous déconnecter-reconnecter.");
	}

}
