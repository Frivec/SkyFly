package fr.frivec.core.entities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

public class ShopVillager {
	
	/**
	 * 
	 * ShopVillager - Class created by Frivec
	 * 
	 * Spawn an entity villager to create a shop.
	 * 
	 */
	
	private String name;
	private Location location;
	
	private Villager villager;
	
	public ShopVillager(final Location location) {
		
		this.location = location;
		this.name = "§6§lMagasin";
		
	}
	
	/*
	 * 
	 * Spawn an entity villager
	 * 
	 */
	public void spawn() {
		
		this.villager = (Villager) this.location.getWorld().spawnEntity(this.location, EntityType.VILLAGER);
		this.villager.setAI(false);
		this.villager.setCustomName(this.name);
		this.villager.setCustomNameVisible(true);
		this.villager.setProfession(Profession.TOOLSMITH);
		this.villager.setInvulnerable(true);
		
	}
	
	public void remove() {
		
		if(this.villager != null) 
		
			this.villager.remove();
		
	}
	
	public Villager getVillager() {
		return villager;
	}
	
}
