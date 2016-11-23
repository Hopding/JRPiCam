package com.hopding.jrpicam.enums;

/**
 * Image effect option settings.
 *
 * <ul>
 * <li>{@link #BLACKBOARD}</li>
 * <li>{@link #BLUR}</li>
 * <li>{@link #CARTOON}</li>
 * <li>{@link #COLOURBALANCE}</li>
 * <li>{@link #COLOURPOINT}</li>
 * <li>{@link #COLOURSWAP}</li>
 * <li>{@link #DENOISE}</li>
 * <li>{@link #EMBOSS}</li>
 * <li>{@link #FILM}</li>
 * <li>{@link #GPEN}</li>
 * <li>{@link #HATCH}</li>
 * <li>{@link #NEGATIVE}</li>
 * <li>{@link #NONE}</li>
 * <li>{@link #OILPAINT}</li>
 * <li>{@link #PASTEL}</li>
 * <li>{@link #POSTERISE}</li>
 * <li>{@link #SATURATION}</li>
 * <li>{@link #SKETCH}</li>
 * <li>{@link #SOLARISE}</li>
 * <li>{@link #WASHEDOUT}</li>
 * <li>{@link #WATERCOLOUR}</li>
 * <li>{@link #WHITEBOARD}</li>
 * </ul>
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
