package fr.frivec.core.hitboxs;

import org.bukkit.util.Vector;

public class HitBox {
	
	/**
	 * 
	 * Hitbox - Created by Frivec
	 * Not Used
	 * 
	 */
	
	private Vector max,
					min;
	
	private double length,
					height;
	
	public HitBox(final Vector min, final Vector max, final double length, final double height) {
		
		this.max = max;
		this.min = min;
		this.height = height;
		this.length = length;
		
	}

	public Vector getMax() {
		return max;
	}

	public void setMax(Vector max) {
		this.max = max;
	}

	public Vector getMin() {
		return min;
	}

	public void setMin(Vector min) {
		this.min = min;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
