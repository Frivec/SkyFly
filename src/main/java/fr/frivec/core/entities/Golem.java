package fr.frivec.core.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.frivec.core.teams.Teams;

public class Golem {
	
	/*
	 * 
	 * Golem - Created by Frivec
	 * Create a Golem entity server-sided for the points of a team
	 * 
	 */
	
	private Teams team;
	private String name;
	private Location location;
	
	private org.bukkit.entity.Golem golem;
	
	public Golem(final String name, final Teams teams) {
		
		this.team = teams;
		this.name = name;
		
		if(this.team.equals(Teams.RED))
			
			this.location = new Location(Bukkit.getWorld("world"), -27.5, 159.0, 143.5, -90.0f, 0.0f);
		
		else
			
			this.location = new Location(Bukkit.getWorld("world"), 28.5, 159.0, -142.5, 90.0f, 0.0f);
		
	}
	
	/**
	 * 
	 * Spawn the entitty at the location defined before.
	 * 
	 */	
	public void spawn() {
		
		this.golem = (org.bukkit.entity.Golem) Bukkit.getWorld("world").spawnEntity(this.location, EntityType.IRON_GOLEM);
		this.golem.setAI(false);
		this.golem.setCustomName(this.name);
		this.golem.setCustomNameVisible(true);
		this.golem.setInvulnerable(true);
		this.golem.teleport(this.location);
		
	}
	
	public void remove() {
		
		if(this.golem != null) 
		
			this.golem.remove();
		
	}
	
	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public org.bukkit.entity.Golem getGolem() {
		return golem;
	}

}
