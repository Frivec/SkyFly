package fr.frivec.core.kits;

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

public class Vampire extends AbstractKit {
	
	public Vampire() {
		
		super("Vampire", 4, 5000, new ItemCreator(Material.REDSTONE, 1).setDisplayName("§aVampire").setLores("Ce talent de vampirisme vous permet", "de dérober la vie de vos adversaires", "", "§aNiveau Max:", "§330% de chance de récupérer 1/2 coeur en", "§3frappant un ennemi.").build());
	}
	
	@Override
	public void getItems(Player player) {}
	
	@EventHandler
	public void onPlayerAttack(final EntityDamageByEntityEvent event) {
		
		if(event.getDamager() == null && !event.getDamager().getType().equals(EntityType.PLAYER))
			
			return;
		
		final Player player = (Player) event.getDamager();
		
		if(!SkyFlyPlayer.getSFPlayer(player).getKit().equals(KitManager.VAMPIRE.getKit()))
			
			return;
		
		if(player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType().name().contains("SWORD")) {
			
			final Entity entity = event.getEntity();
			
			if(entity.getType().equals(EntityType.PLAYER)) {
				
				if(Teams.getPlayerTeam((Player) entity).equals(Teams.getPlayerTeam(player)))
					
					return;
				
				final Random random = new Random();
				final int n = random.nextInt(100) + 1;
				
				if(n <= 30)
					
					if(player.getHealth() != 20)
						
						player.setHealth(player.getHealth() + 1);
				
					else
						
						return;
				
				else
					
					return;
				
			}
			
		}
		
	}

}
