package com.hopding.jrpicam.enums;

/**
 * Metering mode options.
 * <ul>
 * <li>{@link #AVERAGE}</li>
 * <li>{@link #SPOT}</li>
 * <li>{@link #BACKLIT}</li>
 * <li>{@link #MATRIX}</li>
 * </ul>
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
