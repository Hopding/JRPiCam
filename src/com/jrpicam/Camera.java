package com.jrpicam;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Camera represents a Raspberry Pi Camera. It is a wrapper class that acts as a 
 * bridge between a Java application and the raspistill software for the 
 * Raspberry Pi.<br>
 * <br>
 * Usage Example:<br>
 * <pre>
 * {@code
 * String saveDir = "/home/pi/Pictures"; //Directory to save pictures to
 * Camera camera = new Camera(saveDir); //Create Camera object
 * camera.setWidth(150); //Set width property of Camera.
 * camera.setHeight(150); //Set height property of Camera.
 * camera.take("APicture.jpg"); //Take a picture and save it
 * }
 * </pre>
 * 
 * @author Andrew Dillon
 */
public class Camera {
	
	StringBuilder			command;
	String					saveDir;
	HashMap<String, String>	options	= new HashMap<String, String>();
									
	public Camera(String saveDir) {
		this.saveDir = saveDir;
	}
	
	/**
	 * Takes a picture and saves it under the specified name to the directory
	 * specified in the Camera's constructor. Name of picture must NOT include 
	 * spaces (whitespace characters) or saving and reading issues may arise.
	 * 
	 * @param pictureName name to save picture as.
	 * @return a File object representing the full path the picture was saved to.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public File take(String pictureName) throws IOException, InterruptedException {
		command = new StringBuilder();
		command.append("raspistill ");
		command.append("-o ");
		command.append(saveDir + File.separator + pictureName + " ");
		for (Map.Entry<String, String> entry : options.entrySet()) {
			if (!entry.getValue().equals("")) {
				command.append(entry.getValue());
			}
		}
		String[] commandArray = command.toString().split(" ");
		ProcessBuilder pb = new ProcessBuilder(commandArray);
		
		System.out.println("Executed this command:\n\t" + command.toString());
		pb.redirectErrorStream(true);
		pb.redirectOutput(
				new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Camera.out"));
				
		Process p = pb.start();
		p.waitFor();
		return new File(saveDir + File.separator + pictureName);
	}
	
	/**
	 * Takes a series of timelapsed photos for the specified time framse and saves 
	 * them under the specified filename to the directory passed to the Camera's constructor.
	 * Name of picture must NOT include spaces (whitespace characters) or saving and reading issues
	 * may arise. Length of time between image captures may be specified with the setTimeout() method.<br>
	 * <br>
	 * THIS METHOD WILL BLOCK THE THREAD UNTIL THE TIMELAPSE IS COMPLETE IF boolean wait ARGUMENT 
	 * IS SET TO TRUE.<br>
	 * 
	 * Most recent image in the series may be appended to the file specified in the 
	 * setLinkLatestImage() method. If this property is set, timelapse() will return a File
	 * object representing the full path to the file the timelapsed photos are being linked to.
	 * Otherwise, timelapse() will return null.<br>
	 * <br>
	 * When passing in a String for the photo's name, it is important to include "%04d" in the name.
	 * This is where the frame count number will be included in the file name. If the name does not 
	 * contain these characters, the frame count will be appended to the end of each file name.<br>
	 * 
	 * @param wait block the thread or not.
	 * @param pictureName name of each captured image.
	 * @param time period of time in milliseconds to timelapse for.
	 * @return a File the photos are being linked to, if the property is set.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public File timelapse(boolean wait, String pictureName, int time) throws IOException, InterruptedException {
		if (pictureName.contains("%04d"))
			pictureName += "%04d";
		command = new StringBuilder();
		command.append("raspistill ");
		command.append("-tl " + time + " ");
		command.append("-o ");
		command.append(saveDir + File.separator + pictureName + " ");
		for (Map.Entry<String, String> entry : options.entrySet()) {
			if (!entry.getValue().equals("")) {
				command.append(entry.getValue());
			}
		}
		String[] commandArray = command.toString().split(" ");
		ProcessBuilder pb = new ProcessBuilder(commandArray);
		
		//System.out.println("Executed this command:\n\t" + command.toString());	
		//pb.redirectErrorStream(true);
		//pb.redirectOutput(new File(System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Camera.out"));
		
		Process p = pb.start();
		if (wait)
			p.waitFor();
			
		if (!options.get("latest").equals("")) {
			return new File(saveDir + File.separator +
					options.get("latest").substring(4, options.get("latest").lastIndexOf(" ")));
		} else
			return null;
	}
	
	public void writeSettings() {
		ProcessBuilder pb = new ProcessBuilder(
				"raspistill", "-set");
	}
	
	/**
	 * Sets all options to their default settings. Overrides any previously
	 * set options.
	 */
	public void setToDefaults() {
		for (Map.Entry<String, String> entry : options.entrySet()) {
			entry.setValue("");
		}
	}
	
///////////////////////////////////////////////////////////////////////////////
///////////////////// PREVIEW PARAMETER COMMANDS///////////////////////////////
///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Turns off picture preview.
	 */
	public void turnOffPreview() {
		options.put("preview", "-n ");
	}
	
	/**
	 * Turns on picture preview. Note the preview will be superimposed over the top of 
	 * any other windows/graphics.
	 */
	public void turnOnPreview() {
		options.put("preview", "");
	}
	
	/**
	 * This runs the preview windows using the full resolution capture mode. Maximum frames 
	 * per second in this mode is 15fps and the preview will have the same field of view as 
	 * the capture. Captures should happen more quickly as no mode change should be required. 
	 * This feature is currently under development.
	 */
	public void setFullPreviewOn() {
		options.put("fullpreview", "-fp ");
	}
	
	/**
	 * Turns off fullpreview mode.
	 */
	public void setFullPreviewOff() {
		options.put("fullpreview", "");
	}
	
	/**
	 * Defines the size and location on the screen that the preview window will be placed. 
	 * Note the preview will be superimposed over the top of any other windows/graphics.
	 * 
	 * @param x coordinate for upper right corner of preview window.
	 * @param y coordinate for upper right corner of preview window. 
	 * @param w width of preview window.
	 * @param h height of preview window.
	 */
	public void turnOnPreview(int x, int y, int w, int h) {
		options.put("preview", "-p " + x + "," + y + "," + w + "," + h + " ");
	}
	
	/**
	 * Turn fullscreen preview on or off.
	 * 
	 * @param fullscreen turn on/off fullscreen.
	 */
	public void setPreviewFullscreen(boolean fullscreen) {
		if (fullscreen) {
			options.put("fullscreen", "-f ");
		} else if (!fullscreen) {
			options.put("fullscreen", "");
		}
	}
	
	/**
	 * Sets the preview window's opacity (0 to 255). Opacity values lower than 0
	 * will set opacity to 0, values greater than 255 will set opacity to 255.
	 * 
	 * @param opacity
	 */
	public void setPreviewOpacity(int opacity) {
		if (opacity > 255)
			opacity = 255;
		else if (opacity < 0)
			opacity = 0;
		options.put("opacity", "-op " + opacity + " ");
	}
///////////////////////////////////////////////////////////////////////////////
///////////////// END OF PREVIEW PARAMETER COMMANDS ///////////////////////////
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
///////////////////// IMAGE PARAMETER COMMANDS ////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets sharpness of image (-100 to 100), default value is 0. Sharpness values
	 * lower than -100 will set sharpness to -100, values greater than 100 will set
	 * sharpness to 100.
	 * 
	 * @param sharpness
	 */
	public void setSharpness(int sharpness) {
		if (sharpness > 100)
			sharpness = 100;
		else if (sharpness < -100)
			sharpness = -100;
		options.put("sharpness", "-sh " + sharpness + " ");
	}
	
	/**
	 * Sets contrast of image (-100 to 100), default value is 0. Contrast values lower
	 * than -100 will set contrast to -100, values greater than 100 will set contrast
	 * to 100.
	 * 
	 * @param contrast
	 */
	public void setContrast(int contrast) {
		if (contrast > 100)
			contrast = 100;
		else if (contrast < -100)
			contrast = -100;
		options.put("constast", "-co " + contrast + " ");
	}
	
	/**
	 * Sets brightness of image (0 to 100), default value is 50. 0 is black, 100 is white.
	 * Brightness values lower than 0 will set brightness to 0, values greater than 100 will 
	 * set brightness to 100.
	 * 
	 * @param brightness
	 */
	public void setBrightness(int brightness) {
		if (brightness > 100)
			brightness = 100;
		else if (brightness < 0)
			brightness = 0;
		options.put("brightness", "-br " + brightness + " ");
	}
	
	/**
	 * Sets colour saturation of image (-100 to 100), default is 0. Saturation values lower
	 * than -100 will set saturation to -100, values greater than 100 will set saturation to
	 * 100.
	 * 
	 * @param saturation
	 */
	public void setSaturation(int saturation) {
		if (saturation > 100)
			saturation = 100;
		else if (saturation < -100)
			saturation = -100;
		options.put("saturation", "-sa " + saturation + " ");
	}
	
	/**
	 * WARNING: OPERATION NOT YET SUPPORTED BY raspistill SOFTWARE. OPTION MAY STILL BE SET, 
	 * BUT WILL HAVE NO EFFECT ON THE IMAGE UNTIL SUPPORT IS ADDED TO raspistill.<br>
	 * <br>
	 * Sets ISO of Camera (100 to 800).
	 * 
	 * @param iso
	 */
	public void setISO(int iso) {
		options.put("ISO", "-ISO " + iso + " ");
	}
	
	//THIS IS ONLY USED FOR VIDEOS, NOT PICTURES
	private void turnOnVStab() {
		options.put("vstab", "-vs ");
	}
	
	//THIS IS ONLY USED FOR VIDEOS, NOT PICTURES
	public void turnOffVStab() {
		options.put("vstab", "");
	}
	
	//MAKE METHOD FOR EVCOMPENSATION (-25 - 25?)
	
	/**
	 * Sets exposure mode of Camera. Certain modes may not be supported
	 * by raspistill software, depending on camera tuning.
	 * 
	 * @param exposure an Exposure enum specifying the desired mode.
	 */
	public void setExposure(Exposure exposure) {
		options.put("exposure", "-ex " + exposure.toString() + " ");
	}
	
	/**
	 * Sets Automatic White Balance (AWB) mode. Certain modes may not
	 * be supported by raspistill software, depending on camera type.
	 * 
	 * @param awb an AWB enum specifying the desired AWB setting.
	 */
	public void setAWB(AWB awb) {
		options.put("awb", "-awb " + awb.toString() + " ");
	}
	
	/**
	 * Sets an effect to be applied to image. Certain settings may not be 
	 * supported by raspistill software, depending on circumstances.
	 * 
	 * @param imageEffect an ImageEffect enum specifying the desired effect.
	 */
	public void setImageEffect(ImageEffect imageEffect) {
		options.put("imxfx", "-ifx " + imageEffect.toString() + " ");
	}
	
	/**
	 * Sets colour effect of Camera. The specified U and V parameters (0 to 255) 
	 * are applied to the U and Y channels of the image. 
	 * For example, setColourEffect(128, 128) should result in a monochrome image.
	 * 
	 * @param U
	 * @param V
	 */
	public void setColourEffect(int U, int V) {
		if (U > 255)
			U = 255;
		else if (U < 0)
			U = 0;
		if (V > 255)
			V = 255;
		else if (V < 0)
			V = 0;
		options.put("colfx", "-cfx " + U + ":" + V + " ");
	}
	
	/**
	 * Sets metering mode used for preview and capture.
	 * 
	 * @param meteringMode a MeteringMode enum specifying the desired mode.
	 */
	public void setMeteringMode(MeteringMode meteringMode) {
		options.put("metering", "-mm " + meteringMode.toString() + " ");
	}
	
	/**
	 * Sets the rotation of the image in viewfinder and resulting image. This 
	 * can take any value from 0 upwards, but due to hardware constraints only 
	 * 0, 90, 180 and 270 degree rotations are supported.
	 * 
	 * @param rotation
	 */
	public void setRotation(int rotation) {
		if (rotation > 359)
			while (rotation > 359)
				rotation = rotation - 360;
		else if (rotation < 0)
			while (rotation < 0)
				rotation = rotation + 360;
		options.put("rotation", "-rot " + rotation + " ");
	}
	
	/**
	 * Flips the preview and saved image horizontally.
	 */
	public void setHorizontalFlipOn() {
		options.put("hflip", "-hf ");
	}
	
	/**
	 * Turns off horizontal flip.
	 */
	public void setHorizontalFlipOff() {
		options.put("hflip", "");
	}
	
	/**
	 * Flips the preview and saved image vertically.
	 */
	public void setVerticalFlipOn() {
		options.put("vflip", "-vf ");
	}
	
	/**
	 * Turns off vertical flip.
	 */
	public void setVerticalFlipOff() {
		options.put("vflip", "");
	}
	
	/**
	 * Allows the specification of the area of the sensor to be used as the source 
	 * for the preview and capture. This is defined as x,y for the top left corner, 
	 * and a width and height, all values in normalised coordinates (0.0-1.0). So to 
	 * set a ROI at half way across and down the sensor, and a width and height of a 
	 * quarter of the sensor use :<br>
	 * setRegionOfInterest(0.5, 0.5, 0.25, 0.25)
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param d
	 */
	public void setRegionOfInterest(double x, double y, double w, double d) {
		if (x > 1.0)
			x = 1.0;
		else if (x < 0.0)
			x = 0.0;
		if (y > 1.0)
			y = 1.0;
		else if (y < 0.0)
			y = 0.0;
		if (w > 1.0)
			w = 1.0;
		else if (w < 0.0)
			w = 0.0;
		if (d > 1.0)
			d = 1.0;
		else if (d < 0.0)
			d = 0.0;
		options.put("roi", "-roi " + x + "," + y + "," + w + "," + d + " ");
	}
	
	/**
	 * Set the shutter speed to the specified value (in microseconds). There 
	 * is currently an upper limit of approximately 6000000us (6000ms, 6s). 
	 * Shutter speed values passed in that are higher than 6000000 will be
	 * regarded as 6000000.
	 * 
	 * @param speed
	 */
	public void setShutter(int speed) {
		if (speed > 6000000)
			speed = 6000000;
		if (speed < 0)
			speed = 0;
		options.put("shutter", "-ss " + speed + " ");
	}
	
	//MAKE METHOD FOR AWBGAINS DISPLAY
	
	/**
	 * Sets the Dynamic Range Compression of image. DRC changes the images by 
	 * increasing the range of dark areas of the image, and decreasing the 
	 * brighter areas. This can improve the image in low light areas. 
	 * 
	 * @param drc a DRC enum specifying the desired DRC level.
	 */
	public void setDRC(DRC drc) {
		options.put("drc", "-drc " + drc.toString() + " ");
	}
	
	//MAKE METHOD FOR DISPLAYING STATS
	
	//MAKE METHOD FOR ANNOTATING PICTURE WITH FLAGS AND TEXT
	
	//MAKE METHOD FOR STEREOSCOPIC IMAGES
	
	//MAKE METHOD FOR PREVIEW PARAMETER COMMANDS (Bottom of raspistill -? output)
	
///////////////////////////////////////////////////////////////////////////////
/////////////////// ENG OF IMAGE PARAMETER COMMANDS ///////////////////////////
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
/////////////////// APPLICATION SPECIFIC SETTINGS /////////////////////////////
///////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * Sets width of image.
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		options.put("width", "-w " + width + " ");
	}
	
	/**
	 * Sets height of image.
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		options.put("height", "-h " + height + " ");
	}
	
	/**
	 * Sets quality of image (0 to 100), 75 is recommended for usual
	 * purposes. Quality values lower than 0 will set quality to 0, values
	 * greater than 100 will set quality to 100.
	 * 
	 * @param quality
	 */
	public void setQuality(int quality) {
		if (quality > 100)
			quality = 100;
		else if (quality < 0)
			quality = 0;
		options.put("quality", "-q " + quality + " ");
	}
	
	/**
	 * Appends raw Bayer data from the Camera to the image's JPEG metadata.
	 * 
	 * @param add turn on/off bayer data.
	 */
	public void setAddRawBayer(boolean add) {
		if (add) {
			options.put("raw", "-r ");
		} else if (!add) {
			options.put("raw", "");
		}
	}
	
	/**
	 * Links latest image to the specified file. Intended for use with timelapse
	 * photography. Allows the latest image in a series of time lapse photos to 
	 * be easily accessed while the lapse is still running at the specified file, 
	 * while still saving all photos with their unique file names.
	 * 
	 * @param link turn on/off linking.
	 * @param fileName file name to link images to.
	 */
	public void setLinkLatestImage(boolean link, String fileName) {
		if (link) {
			options.put("latest", "-l " + fileName + " ");
		} else if (!link) {
			options.put("latest", "");
		}
	}
	
	/**
	 * Sets the Camera's timeout. Camera waits the specified number of milliseconds 
	 * before taking the photograph. Default value is 5 seconds. 
	 * 
	 * @param time
	 */
	public void setTimeout(int time) {
		options.put("timeout", "-t " + time + " ");
	}
	
	/**
	 * Allows specification of the thumbnail image inserted in to the image file. 
	 * If not specified, defaults are a size of 64x48 at quality 35.
	 * 
	 * @param x
	 * @param y
	 * @param quality
	 */
	public void setThumbnailParams(int x, int y, int quality) {
		options.put("thumb", "-th " + x + ":" + y + ":" + quality + " ");
	}
	
	/**
	 * Turns off image thumbnails. Reduces image's file size slightly.
	 */
	public void turnOffThumbnail() {
		options.put("thumb", "-th none");
	}
	
	//MAKE METHOD FOR DEMO MODE
	
	/**
	 * Sets the encoding for images. Default is JPG. Note that unaccelerated image 
	 * types (gif, png, bmp) will take much longer to save than JPG which is hardware 
	 * accelerated. Also note that the filename suffix is completely ignored when deciding 
	 * the encoding of a file.
	 * 
	 * @param encoding an Encoding enum specifying the desired image encoding.
	 */
	public void setEncoding(Encoding encoding) {
		options.put("encoding", "-e " + encoding.toString() + " ");
	}
	
	//ADD EXIF DATA OPTION HERE, NEED TO TEST ON RPI TERMINAL TO KNOW HOW TO IMPLEMENT
	
	//DON'T THINK KEYPRESS (-k) OPTION IS NECESSARY?
	
	//DON'T KNOW WHAT THE SIGUSR1 (-s) OPTION DOES OR IS FOR?
	
	//ADD METHOD FOR -gl OPTION
	
	//ADD METHOD FOR -gc CAPTURE OPTION
	
	/**
	 * Selects which camera to use (on a multicamera system). 0 or 1. Default is 0.
	 * Values lower than 0 will select camera 0, values higher than 1 will select
	 * camera 1.
	 * 
	 * @param camNumber
	 */
	public void selectCamera(int camNumber) {
		if (camNumber < 0)
			camNumber = 0;
		else if (camNumber > 1)
			camNumber = 1;
		options.put("camselect", "-cs " + camNumber + " ");
	}
	
	/**
	 * 
	 */
	public void enableBurst() {
		options.put("burst", "-bm ");
	}
	
	/**
	 * 
	 */
	public void disableBurst() {
		options.put("burst", "");
	}
	
	//ADD A METHOD SUPPORTING THE --mode OPTION, WILL NEED AN ENUM PROBLY
	
	public void setDateTimeOn() {
		options.put("datetime", "-dt ");
	}
	
	public void setDateTimeOff() {
		options.put("datetime", "");
	}
	
	public void setTimestampOn() {
		options.put("timestamp", "-ts ");
	}
	
	public void setTimestampOff() {
		options.put("timestamp", "");
	}
///////////////////////////////////////////////////////////////////////////////
///////////////// END OF APPLICATION SPECIFIC SETTINGS ////////////////////////
///////////////////////////////////////////////////////////////////////////////	
}
