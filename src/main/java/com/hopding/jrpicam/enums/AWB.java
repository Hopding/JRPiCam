package com.hopding.jrpicam.enums;

/**
 * AWB setting options.
 * <ul>
 * <li>{@link #OFF}</li>
 * <li>{@link #AUTO}</li>
 * <li>{@link #SUN}</li>
 * <li>{@link #CLOUD}</li>
 * <li>{@link #SHADE}</li>
 * <li>{@link #TUNGSTEN}</li>
 * <li>{@link #FLUORESCENT}</li>
 * <li>{@link #INCANDESCENT}</li>
 * <li>{@link #FLASH}</li>
 * <li>{@link #HORIZON}</li>
 * </ul>
 *
 * @author Andrew Dillon
 */
public enum AWB {
	
	/**
	 * Turns off white balance calculation.
	 */
	OFF,
	
	/**
	 * Automatic mode (default)
	 */
	AUTO,
	
	/**
	 * Sunny mode.
	 */
	SUN,
	
	/**
	 * Cloudy mode.
	 */
	CLOUD,
	
	/**
	 * Shaded mode.
	 */
	SHADE,
	
	/**
	 * Tungsten lighting mode.
	 */
	TUNGSTEN,
	
	/**
	 * Fluorescent lighting mode.
	 */
	FLUORESCENT,
	
	/**
	 * Incandescent lighting mode.
	 */
	INCANDESCENT,
	
	/**
	 * Flash mode.
	 */
	FLASH,
	
	/**
	 * Horizon mode.
	 */
	HORIZON;
	
	/**
	 * Returns enum in lowercase.
	 */
	public String toString() {
		String id = name();
		return id.toLowerCase();
	}
}
