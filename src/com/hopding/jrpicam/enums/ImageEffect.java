package com.hopding.jrpicam.enums;

/**
 * Image effect option settings.
 * 
 * @author Andrew Dillon
 */
public enum ImageEffect {
	
	/**
	 * No effect (default)
	 */
	NONE,
	
	/**
	 * Negate the image.
	 */
	NEGATIVE,
	
	/**
	 * Solarise the image.
	 */
	SOLARISE,
	
	/**
	 * Posterise the image.
	 */
	POSTERISE,
	
	/**
	 * Whiteboard effect.
	 */
	WHITEBOARD,
	
	/**
	 * Blackboard effect.
	 */
	BLACKBOARD,
	
	/**
	 * Sketch style effect.
	 */
	SKETCH,
	
	/**
	 * Denoise the image.
	 */
	DENOISE,
	
	/**
	 * Emboss the image.
	 */
	EMBOSS,
	
	/**
	 * Apply an oil paint style effect.
	 */
	OILPAINT,
	
	/**
	 * Hatch sketch style.
	 */
	HATCH,
	
	/**
	 * 
	 */
	GPEN,
	
	/**
	 * A pastel style effect.
	 */
	PASTEL,
	
	/**
	 * A watercolour style effect.
	 */
	WATERCOLOUR,
	
	/**
	 * Film grain style effect.
	 */
	FILM,
	
	/**
	 * Blur the image.
	 */
	BLUR,
	
	/**
	 * Colour saturate the image.
	 */
	SATURATION,
	
	/**
	 * Not currently implemented.
	 */
	COLOURSWAP,
	
	/**
	 * Not currently implemented.
	 */
	WASHEDOUT,
	
	/**
	 * Not currently implemented.
	 */
	COLOURPOINT,
	
	/**
	 * Not currently implemented.
	 */
	COLOURBALANCE,
	
	/**
	 * Not currently implemented.
	 */
	CARTOON;
	
	/**
	 * Returns enum in lowercase.
	 */
	public String toString() {
		String id = name();
		return id.toLowerCase();
	}
}
