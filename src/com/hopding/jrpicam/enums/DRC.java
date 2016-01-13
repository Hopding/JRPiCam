package com.hopding.jrpicam.enums;

/**
 * DRC mode options.
 * 
 * @author Andrew Dillon
 */
public enum DRC {
	
	OFF,
	
	LOW,
	
	MEDIUM,
	
	HIGH;
	
	/**
	 * Returns enum as lowercase.
	 */
	public String toString() {
		String id = name();
		return id.toLowerCase();
	}
}
