package fr.frivec.core.kits;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.frivec.core.kits.abstracts.AbstractKit;
import fr.frivec.core.player.SkyFlyPlayer;
import fr.frivec.core.teams.Teams;
import fr.frivec.spigot.item.ItemCreator;

public class Pyromaniac extends AbstractKit {
	
	/*
	 * 
	 * Pyromaniac - Created by Frivec
	 * Kit given the ability to set a player on fire when attacking him.
	 * 
	 */
	
	/**
	 * 
	 * Define the kit using the constructor of AbstractKit
	 * 
	 */
	public Pyromaniac() {
		
		super("Pyromancien", 3, 5000, new ItemCreator(Material.FLINT_AND_STEEL, 1).setDisplayName("§6Pyromancien").setLores(Arrays.asList("§aCe talent vous permet de vous enflammer !", "", "§aNiveau Max:", "§340% de chance d'enflammer", "votre adversaire en le frappant.")).build());
		
	}
	
	@Override
	public void getItems(Player player) {/*Not used*/}
	
	@EventHandler
	public void onPlayerAttack(final EntityDamageByEntityEvent event) {
		
		if(event.getDamager() == null && !event.getDamager().getType().equals(EntityType.PLAYER))
			
			return;
		
		final Player player = (Player) event.getDamager();
		
		if(!SkyFlyPlayer.getSFPlayer(player).getKit().equals(KitManager.PYROMANIAC.getKit()))
			
			return;
		
		if(player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType().name().contains("SWORD")) {
			
			final Entity entity = event.getEntity();
			
			if(entity.getType().equals(EntityType.PLAYER)) {
				
				if(Teams.getPlayerTeam((Player) entity).equals(Teams.getPlayerTeam(player)))
					
					return;
				
				final Random random = new Random();
				final int n = random.nextInt(100) + 1;
				
				if(n <= 40)
					
					entity.setFireTicks(20*2);
				
				else
					
					return;
				
			}
			
		}else
			
			return;
		
	}
	
}
