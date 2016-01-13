package com.hopding.jrpicam.enums;

/**
 * Encoding type options.
 * 
 * @author Andrew Dillon
 */
public enum Encoding {
	
	JPG,
	
	BMP,
	
	GIF,
	
	PNG;
	
	/**
	 * Returns the enum in lowercase.
	 */
	public String toString() {
		String id = name();
		return id.toLowerCase();
	}
}
