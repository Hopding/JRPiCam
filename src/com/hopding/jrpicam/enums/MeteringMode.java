package com.hopding.jrpicam.enums;

/**
 * Metering mode options.
 * 
 * @author Andrew Dillon
 */
public enum MeteringMode {
	
	/**
	 * Average the whole frame for metering.
	 */
	AVERAGE,
	
	/**
	 * Spot metering.
	 */
	SPOT,
	
	/**
	 * Assume a backlit image.
	 */
	BACKLIT,
	
	/**
	 * Matrix metering.
	 */
	MATRIX;
	
	/**
	 * Returns enum in lowercase.
	 */
	public String toString() {
		String id = name();
		return id.toLowerCase();
	}
}
