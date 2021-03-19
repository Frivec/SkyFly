package fr.frivec.core.kits;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import fr.frivec.core.kits.abstracts.AbstractKit;
import fr.frivec.spigot.item.ItemCreator;

public class Health extends AbstractKit {
	
	/**
	 * 
	 * Health - Created by Frivec
	 * Kit that give two heart containers to the player
	 * 
	 */
	
	/**
	 * 
	 * The kit is defined here by using the AbstractKit constructor
	 * 
	 */
	public Health() {
		
		super("Vie", 5, 5000, new ItemCreator(Material.APPLE, 1).setDisplayName("§aVie").setLores(Arrays.asList("Pour ceux qui perdent de la vie très vite.", "", "§aNiveau Max: ", "§3Vous donne 2 coeurs supplémentaires")).build());
		
	}
	
	/**
	 * 
	 * Gives the the two heart containers to the player
	 * 
	 */
	@Override
	public void getItems(Player player) {
		
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24.0);
		player.setHealth(24.0);
		
	}

}
