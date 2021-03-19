package fr.frivec.core.utils;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.frivec.spigot.cuboid.Cuboid;
import fr.frivec.spigot.item.ItemCreator;

public class SkyFlyUtils {
	
	public static final Location lobby = new Location(Bukkit.getWorld("world"), 86.5, 160.5, 2.5, 90, 4),
							center = new Location(Bukkit.getWorld("world"), 0.5, 111, 0.5, 90, 4),
							red = new Location(Bukkit.getWorld("world"), 0.5, 162.5, 153.5, 180, 0),
							blue = new Location(Bukkit.getWorld("world"), 0.5, 162.5, -152.5, 0, 0);
	
	public static final Cuboid lobbyZone = new Cuboid(new Location(Bukkit.getWorld("world"), 63.5, 150, 23.5), new Location(Bukkit.getWorld("world"), 112.5, 195, -21));
	
	public static final ItemStack feather = new ItemCreator(Material.TRIPWIRE_HOOK, 1).setDisplayName("§7Clef").setLores(Arrays.asList("§7§oApportez cette clef sur votre île", "§7§oet marquez un point.")).build();
	
	public static void spawnKey() {
		
		Bukkit.getWorld("world").dropItem(center.clone(), feather).setVelocity(new Vector(0, 0, 0));
		
	}
	
}
