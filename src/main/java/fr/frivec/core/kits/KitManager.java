package fr.frivec.core.kits;

import fr.frivec.core.kits.abstracts.AbstractKit;

public enum KitManager {
	
	/*
	 * 
	 * KitManager - Created by Frivec
	 * Manage all the kits available in the game
	 * 
	 */
	
	PYROMANIAC("Pyromancien", new Pyromaniac()),
	VAMPIRE("Vampire", new Vampire()),
	HEALTH("Vie", new Health());
	
	private String name;
	private AbstractKit kit;
	
	/**
	 * Constructor to define a kit
	 * @param name - String: Name of the kit
	 * @param kit - AbstractKit: AbstractKit instance
	 */
	private KitManager(final String name, final AbstractKit kit) {
		
		this.name = name;
		this.kit = kit;
		
	}
	
	/**
	 * Get a kit by its name
	 * @param kitName - String: Kit name
	 * @return kit instance if exists or null
	 */
	public static KitManager getKitByName(final String kitName) {
		
		for(KitManager kits : values())
			
			if(kits.getName().equalsIgnoreCase(kitName))
				
				return kits;
		
		return null;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AbstractKit getKit() {
		return kit;
	}

	public void setKit(AbstractKit kit) {
		this.kit = kit;
	}

}
