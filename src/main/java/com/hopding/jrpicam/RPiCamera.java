package com.hopding.jrpicam;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import com.hopding.jrpicam.enums.AWB;
import com.hopding.jrpicam.enums.DRC;
import com.hopding.jrpicam.enums.Encoding;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.enums.ImageEffect;
import com.hopding.jrpicam.enums.MeteringMode;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

/**
 * RPiCamera is used to access the Raspberry Pi Camera and take still photos.
 * There are a variety of different settings that may be changed to modify images
 * taken by an RPiCamera instance, such as width, height, AWB, DRC, and the resulting 
 * image's encoding type (jpg, bmp, png, gif).
 * <p>
 * Although it provides additional features and support for Java specific operations, 
 * RPiCamera is essentially a wrapper class for executing the raspistill software from 
 * within a Java application. Thus it is essential that all Raspberry Pis using the JRPiCam
 * library have properly configured and working raspistill software.
 * <p>
 * Usage Example:
 * <pre>
 *{@code
 * // Directory to save pictures to
 * String saveDir = "/home/pi/Pictures";
 *
 * // Create RPiCamera instance
 * RPiCamera camera = new RPiCamera(saveDir)
 *     .setWidth(150)   // Set width property of RPiCamera.
 *     .setHeight(150); // Set height property of RPiCamera.
 *
 * // Take a picture and save it as "/home/pi/Pictures/APicture.jpg"
 * camera.takeStill("APicture.jpg"); 
 *}
 * </pre>
 * 
 * @author Andrew Dillon
 */
public class RPiCamera {
	
	private String						prevCommand;
	private String						saveDir;
	private HashMap<String, String[]>	options	= new HashMap<>();
	private ProcessBuilder				pb;
	private Process						p;
	private static final int DEFAULT_WIDTH  = 500;
	private static final int DEFAULT_HEIGHT = 500;
								
	/**
	 * Creates new RPiCamera. The resulting RPiCamera's save directory will be set to
     * "/home/pi/Pictures" and will have a default image width and height of 500.
	 * @throws FailedToRunRaspistillException 
	 */
	public RPiCamera() throws FailedToRunRaspistillException {
		this("/home/pi/Pictures");
	}
	
	/**
	 * Creates new RPiCamera and sets its save directory. The resulting RPiCamera will have a
     * default image width and height of 500.
	 * 
	 * @param saveDir A String specifying the directory for RPiCamera to save images.
	 * @throws FailedToRunRaspistillException 
	 */
	public RPiCamera(String saveDir) throws FailedToRunRaspistillException {
		this.saveDir = saveDir;
		try {
			pb = new ProcessBuilder("raspistill");
			pb.start();
		} catch (IOException e) {
			// The IOException was most likely thrown because raspistill isn't installed
			// and/or configured properly, so throw FailedToRunRaspistillException to 
			// indicate that.
			throw new FailedToRunRaspistillException(
					"RPiCamera failed to run raspistill. The JRPiCam library relies on"
							+ "raspistill to function. Please ensure it is installed and configured"
							+ "on your system.");
		}
		//  Set default width and height of images
        this.setWidth(DEFAULT_WIDTH);
		this.setHeight(DEFAULT_HEIGHT);
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////// Image Taking Methods //////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Takes an image of the specified width and height and saves it under the 
	 * specified name to the RPiCamera's save directory ("/home/pi/Pictures" by 
	 * default). The image's encoding will be the same as the RPiCamera's encoding 
	 * setting (JPEG by default).
	 * <p>
	 * Usage Example:
	 * <pre>
	 *{@code
	 * // Create an RPiCamera instance using default constructor, which sets the 
	 * // save directory to "/home/pi/Pictures".
	 * RPiCamera piCamera = new RPiCamera()
	 *     .setEncoding(Encoding.PNG); // Change encoding from JPEG to PNG
     *
	 * // Take a 500x500 PNG image and save it as "/home/pi/Pictures/AStillImage.png"
	 * piCamera.takeStill("AStillImage.png", 500, 500);
	 *}
	 * </pre>
	 * 
	 * @param pictureName A String containing the name to save picture under.
	 * @param width An int specifying the width of the image to take.
	 * @param height An int specifying the height of the image to take.
	 * @return A File object representing the full path the picture was saved to.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public File takeStill(String pictureName, int width, int height) throws IOException, InterruptedException {
		List<String> command = new ArrayList<>();
		command.add("raspistill");
		command.add("-o");
		command.add(saveDir + File.separator + pictureName);
		command.add("-w");
		command.add("" + width);
		command.add("-h");
		command.add("" + height);
		for (Map.Entry<String, String[]> entry : options.entrySet()) {
			if (entry.getValue() != null        &&
                !"width".equals(entry.getKey()) &&
                !"height".equals(entry.getKey())) {
                command.addAll(Arrays.asList(entry.getValue()));
			}
		}
		prevCommand = command.toString();
		pb = new ProcessBuilder(command);
		
// 		System.out.println("Executed this command:\n\t" + command.toString());
// 		pb.redirectErrorStream(true);
// 		pb.redirectOutput(
// 				new File(System.getProperty("user.home") + File.separator +
// 						"Desktop" + File.separator + "RPiCamera.out"));
		
		p = pb.start();
		p.waitFor();
		return new File(saveDir + File.separator + pictureName);
	}
	
	/**
	 * Takes an image and saves it under the specified name to the RPiCamera's save 
	 * directory ("/home/pi/Pictures" by default). The image's encoding will be the 
	 * same as the RPiCamera's encoding setting (JPEG by default).
	 * <p>
	 * Usage Example:
	 * <pre>
	 *{@code
	 * // Create an RPiCamera instance using default constructor, which sets the
	 * // save directory to "/home/pi/Pictures".
	 * RPiCamera piCamera = new RPiCamera()
	 *     .setEncoding(Encoding.PNG); // Change encoding from JPEG to PNG
     *
	 * // Take a PNG image and save it as "/home/pi/Pictures/AStillImage.png"
	 * piCamera.takeStill("AStillImage.png");
	 *}
	 * </pre>
	 * 
	 * @param pictureName A String containing the name to save picture under.
	 * @return A File object representing the full path the picture was saved to.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public File takeStill(String pictureName) throws IOException, InterruptedException {
		return takeStill(pictureName,
				Integer.parseInt(options.get("width")[1]),
				Integer.parseInt(options.get("height")[1]));
	}
	
	/**
	 * Takes an image of the specified width and height and stores it in a BufferedImage
	 * object. The resulting image is NOT saved anywhere in the Pi's memory. The image's 
	 * encoding will be the same as the RPiCamera's encoding setting (JPEG by default).
	 * <p>
	 * Usage Example:
	 * <pre>
	 *{@code
	 * // Create an RPiCamera instance using default constructor, which sets the 
	 * // save directory to "/home/pi/Pictures".
	 * RPiCamera piCamera = new RPiCamera()
	 *     .setEncoding(Encoding.PNG); // Change encoding from JPEG to PNG
     *
	 * // Take a 500x500 PNG image and store it in a BufferedImage
	 * BufferedImage buffImg = piCamera.takeBufferedStill(500, 500);
	 *}
	 * </pre>
	 * 
	 * @param width An int specifying width of image to take.
	 * @param height An int specifying height of image to take.
	 * @return A BufferedImage containing the image.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public BufferedImage takeBufferedStill(int width, int height) throws IOException, InterruptedException {
		List<String> command = new ArrayList<>();
		command.add("raspistill");
		command.add("-o");
		command.add("-v");
		command.add("-w");
		command.add("" + width);
		command.add("-h");
		command.add("" + height);
		for (Map.Entry<String, String[]> entry : options.entrySet()) {
			if (entry.getValue() != null        &&
                !entry.getKey().equals("width") &&
                !entry.getKey().equals("height")) {
                Collections.addAll(command, entry.getValue());
			}
		}
		prevCommand = command.toString();
		pb = new ProcessBuilder(command);
		
// 		System.out.println("Executed this command:\n\t" + command.toString());
// 		pb.redirectErrorStream(true);
// 		pb.redirectOutput(
// 				new File(System.getProperty("user.home") + File.separator + 
// 				"Desktop" + File.separator + "RPiCamera.out"));
		
		p = pb.start();
		BufferedImage bi = ImageIO.read(p.getInputStream());
// 		--------------------------------------------------------------------------
// 		This code can be used to specify an ImageReader - perhaps for a specific 
// 		type of image - in place of the previous line:
// 
// 		ImageInputStream iis = ImageIO.createImageInputStream(p.getInputStream());
// 		Iterator<?> imgReaders = ImageIO.getImageReadersByFormatName("png");
// 		ImageReader reader = (ImageReader) imgReaders.next();
// 		reader.setInput(iis, true); // May need to set this to false...
// 		ImageReadParam param = reader.getDefaultReadParam();
// 		BufferedImage bi = reader.read(0, param);
// 		--------------------------------------------------------------------------
		p.getInputStream().close();
		return bi;
	}
	
	/**
	 * Takes an image and stores it in a BufferedImage object. The resulting image is 
	 * NOT saved anywhere in the Pi's memory. The image's encoding will be the same as 
	 * the RPiCamera's encoding setting (JPEG by default).
	 * <p>
	 * Usage Example:
	 * <pre>
	 *{@code
	 * // Create an RPiCamera instance using default constructor, which sets the
	 * // save directory to "/home/pi/Pictures".
	 * RPiCamera piCamera = new RPiCamera()
	 *     .setEncoding(Encoding.PNG); // Change encoding from JPEG to PNG
     *
	 * // Take a PNG image and store it in a BufferedImage
	 * BufferedImage buffImg = piCamera.takeBufferedStill();
	 *}
	 * </pre>
	 * @return A BufferedImage containing the image.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public BufferedImage takeBufferedStill() throws IOException, InterruptedException {
		return takeBufferedStill(
				Integer.parseInt(options.get("width")[1]),
				Integer.parseInt(options.get("height")[1]));
	}
	
	/**
	 * Captures an image and returns the RGB values of that image. Images taken in this manner
	 * are not encoded, rather rather their RGB values are stored in an int array. Indexes 0 - 2 
	 * of the array make up the red, green, and blue values of the upper left pixel in the image. 
	 * The pixels are added to the array from left to right, top to bottom.
	 * <p>
	 * If an image's dimensions are not multiples of 16, then black padding will be added onto 
	 * the right and bottom sides of an image until both the width and height are multiples of 16
	 * (e.g. a 500x500 image would have a black band 12 pixels thick added to the right and bottom
	 * sides to achieve dimensions of 512x512, the closest multiple of 16 to 500).
	 * <p>
	 * The boolean keepPadding parameter may be set to false to remove this padding. Note that this does
	 * require extra computations to be performed, so it has the potential to slightly reduce the fps
	 * a Pi is capable of. However, this has not yet been officially tested for. If the padding is kept,
	 * then it is the responsibility of the caller to determine what the dimensions of the resulting image
	 * will be, and adjust their processing as such (e.g. If the padding is kept on a 500x500 image, the 
	 * array that is returned will contain 786,432 elements, as opposed to the 750,000 one would expect from
	 * a 500x500 image. This is because the image stored in the returned array is not 500x500, but 512x512 and
	 * the caller of this method must take that into account when processing the image).
	 * <p>
	 * Usage Example:
	 * <pre>
	 *{@code
	 * // Creating and setting up an RPiCamera
	 * RPiCamera piCamera = new RPiCamera()
	 *      .turnOffPreview()
	 *      .setTimeout(1);
	 *
	 * // Capture an image and store its RGB values, not preserving the border
	 * int[] rgbVals = piCamera.takeStillAsRGB(500, 500, false);
	 *
	 * // Create a BufferedImage from the stored RGB values
	 * BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
	 * WritableRaster raster = (WritableRaster) image.getData();
	 * raster.setPixels(0, 0, 500, 500, rgbVals);
	 * image.setData(raster);
	 *
	 * // Write the BufferedImage to a file as a JPEG
	 * File file = new File("/home/pi/Desktop/A Cool Image.jpg");
	 * ImageIO.write(image, "jpg", file);
	 * }
	 * </pre>
	 * 
	 * @param width An int specifying the width of the image to take (ideally a multiple of 16).
	 * @param height An int specifying the height of the image to take (ideally a multiple of 16).
	 * @param keepPadding A boolean indicating whether or not to preserve the padding on an image.
	 * @return An int array containing the image's RGB values.
	 * @throws IOException
	 */
	public int[] takeStillAsRGB(int width, int height, boolean keepPadding) throws IOException {
		List<String> command = new ArrayList<>();
		command.add("raspiyuv");
		command.add("-rgb");
		command.add("-o");
		command.add("-v");
		command.add("-w");
		command.add("" + width);
		command.add("-h");
		command.add("" + height);
		for (Map.Entry<String, String[]> entry : options.entrySet()) {
			if (entry.getValue() != null        &&
                !entry.getKey().equals("width") &&
                !entry.getKey().equals("height")) {
                Collections.addAll(command, entry.getValue());
			}
		}
		prevCommand = command.toString();
		pb = new ProcessBuilder(command);
		
// 		System.out.println("Executed this command:\n\t" + command.toString());
// 		pb.redirectErrorStream(true);
// 		pb.redirectOutput(
// 				new File(System.getProperty("user.home") + File.separator + 
// 				"Desktop" + File.separator + "RPiCamera.out"));
		
		p = pb.start();
		BufferedInputStream inputStream = new BufferedInputStream(p.getInputStream());
		
		// Calculate the width and height of the image with padding, if dimensions 
		// aren't multiples of 16
		int paddedWidth = width;
		int paddedHeight = height;
		
		int widthRemainder = width % 16;
		if (widthRemainder != 0)
			paddedWidth = width + 16 - widthRemainder;
			
		int heightRemainder = height % 16;
		if (heightRemainder != 0)
			paddedHeight = height + 16 - heightRemainder;
			
		// Create an int array to hold RGB values of the image, size will differ
		// depending upon whether or not the image will have padding
		int[] rgbVals;
		if (!keepPadding)
			rgbVals = new int[width * height * 3]; // times three because each pixel is made up of three bytes, 
													// and each element holds one byte (an R, G, or B value)
		else
			rgbVals = new int[paddedWidth * paddedHeight * 3];
			
		int rgbData;
		int pos = 0;
		
		// Read the image into the array, but avoid storing pixels that make up padding
		if (!keepPadding) {
			int storedBytes = 0;
			int columnPos = 1;
			int areaWithoutPadding = width * height;
			while ((rgbData = inputStream.read()) != -1) {
				if ((columnPos / 3d) <= width) {
					rgbVals[storedBytes] = rgbData;
					storedBytes++;
				}
				columnPos++;
				if (columnPos == (paddedWidth * 3) + 1)
					columnPos = 1;
				if ((storedBytes / 3d) == areaWithoutPadding)
					break;
			}
		}
		// Just read the image into the array, and don't worry about the padding
		else {
			while ((rgbData = inputStream.read()) != -1) {
				rgbVals[pos] = rgbData;
				pos++;
			}
		}
		inputStream.close();
		return rgbVals;
	}
	
	/**
	 * Captures an image and returns the RGB values of that image. Images taken in this manner
	 * are not encoded, rather rather their RGB values are stored in an int array. Indexes 0 - 2 
	 * of the array make up the red, green, and blue values of the upper left pixel in the image. 
	 * The pixels are added to the array from left to right, top to bottom.
	 * <p>
	 * If an image's dimensions are not multiples of 16, then black padding will be added onto 
	 * the right and bottom sides of an image until both the width and height are multiples of 16
	 * (e.g. a 500x500 image would have a black band 12 pixels thick added to the right and bottom
	 * sides to achieve dimensions of 512x512, the closest multiple of 16 to 500).
	 * <p>
	 * The boolean keepPadding parameter may be set to false to remove this padding. Note that this does
	 * require extra computations to be performed, so it has the potential to slightly reduce the fps
	 * a Pi is capable of. However, this has not yet been officially tested for. If the padding is kept,
	 * then it is the responsibility of the caller to determine what the dimensions of the resulting image
	 * will be, and adjust their processing as such (e.g. If the padding is kept on a 500x500 image, the 
	 * array that is returned will contain 786,432 elements, as opposed to the 750,000 one would expect from
	 * a 500x500 image. This is because the image stored in the returned array is not 500x500, but 512x512 and
	 * the caller of this method must take that into account when processing the image).
	 * <p>
	 * Usage Example:
	 * <pre>
	 *{@code
	 * // Creating and setting up an RPiCamera
	 * RPiCamera piCamera = new RPiCamera()
	 *     .turnOffPreview()
	 *     .setTimeout(1);
	 *
	 * // Capture an image and store its RGB values, not preserving the border
	 * int[] rgbVals = piCamera.takeStillAsRGB(500, 500, false);
	 *
	 * // Create a BufferedImage from the stored RGB values
	 * BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
	 * WritableRaster raster = (WritableRaster) image.getData();
	 * raster.setPixels(0, 0, 500, 500, rgbVals);
	 * image.setData(raster);
	 *
	 * // Write the BufferedImage to a file as a JPEG
	 * File file = new File("/home/pi/Desktop/A Cool Image.jpg");
	 * ImageIO.write(image, "jpg", file);
	 *}
	 * </pre>
	 * 
	 * @param keepPadding A boolean indicating whether or not to preserve the padding on an image.
	 * @return An int array containing the image's RGB values.
	 * @throws IOException
	 */
	public int[] takeStillAsRGB(boolean keepPadding) throws IOException {
		return takeStillAsRGB(
				Integer.parseInt(options.get("width")[1]),
				Integer.parseInt(options.get("height")[1]),
				keepPadding);
	}
	
	/**
	 * Take a series of timelapsed photos for the specified time frame and save them under the 
	 * specified filename to the RPiCamera's save directory. Length of time to timelapse for may 
	 * be specified with the {@link #setTimeout(int)} method.
	 * <p>
	 * THIS METHOD WILL BLOCK THE THREAD UNTIL THE TIMELAPSE IS COMPLETE IF boolean wait ARGUMENT 
	 * IS SET TO true.
	 * <p>
	 * Most recent image in the series may be appended to the file specified in the 
	 * {@link #setLinkLatestImage(boolean, String)} method. If this property is set, timelapse() will return a File
	 * object representing the full path to the file the timelapsed photos are being linked to.
	 * Otherwise, will return null.
	 * <p>
	 * When passing in a String for the photo's name, it is important to include "%04d" in the name.
	 * This is where the frame count number will be included in the file name. If the name does not 
	 * contain these characters, the frame count will be appended to the beginning of each file name.
	 * <p>
	 * Usage Example:
	 * <pre>
	 * {@code
	 * // Create an RPiCamera instance using default constructor, which sets the 
	 * // save directory to "/home/pi/Pictures".
	 * RPiCamera piCamera = new RPiCamera()
     *     .setEncoding(Encoding.PNG) // Change encoding from JPEG to PNG
	 *     .setTimeout(10000)         // Set period for timelapse to run at 10 seconds
	 *     .setLinkLatestImage(true, "/home/pi/Pictures/linkfile.png"); // Set RPiCamera to link images to "linkFile.png"
	 * // Begin timelapse
	 * piCamera.timelapse(
	 * 	true, // Block thread until timelapse is completed
	 * 	"%04dTimelapsePics.png", // Save images under this name, with frame count added to beginning
	 * 	1000 // Wait 1 second between capturing each image
	 * );
	 * }
	 * </pre>
	 * 
	 * @param wait A boolean indicating whether to block the thread until timelapse is complete or not.
	 * @param pictureName A String containing name for each captured image.
	 * @param time Period of time in milliseconds to wait between taking each image.
	 * @return If the link latest image property is set, the File images are being linked to, 
	 * 		   otherwise returns null.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public File timelapse(boolean wait, String pictureName, int time) throws IOException, InterruptedException {
		if (!pictureName.contains("%04d"))
			pictureName = "%04d" + pictureName;
			
		List<String> command = new ArrayList<>();
		command.add("raspistill");
		command.add("-tl");
		command.add("" + time);
		command.add("-o");
		command.add(saveDir + File.separator + pictureName);
		for (Map.Entry<String, String[]> entry : options.entrySet()) {
			if (entry.getValue() != null)
                Collections.addAll(command, entry.getValue());
		}
		prevCommand = command.toString();
		pb = new ProcessBuilder(command);
		
// 		System.out.println("Executed this command:\n\t" + command.toString());	
// 		pb.redirectErrorStream(true);
// 		pb.redirectOutput(
// 				   new File(System.getProperty("user.home") + File.separator + 
// 		           "Desktop" + File.separator + "RPiCamera.out"));
		
		p = pb.start();
		if (wait)
			p.waitFor();
			
// 		if (!options.get("latest").equals(null)) {
		try {
			return new File(options.get("latest")[1]);
		} catch (NullPointerException e) {
			return null;
		}
// 		} else
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////// End of Image Taking Methods /////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Stops any raspistill processes being run by RPiCamera. 
	 */
	public void stop() {
		if (p != null)
			p.destroy();
	}
	
	/**
	 * Gets the raspistill command previously executed by the RPiCamera. If no commands have
	 * been executed, null will be returned.
	 * 
	 * @return String containing command previously executed.
	 */
	public String getPrevCommand() {
		return prevCommand.substring(1, prevCommand.lastIndexOf("]")).replaceAll(",", "");
	}
	
	/**
	 * Sets the RPiCamera's save directory.
	 * 
	 * @param saveDir String containing directory for RPiCamera to save images to.
	 */
	public RPiCamera setSaveDir(String saveDir) {
		this.saveDir = saveDir;
		return this;
	}
	
// 	public String getCameraSettings() {
// 		List<String> command = new ArrayList<String>();
// 		command.add("raspistill");
// 		command.add("-set");
// 		pb = new ProcessBuilder(command);
// 		prevCommand = command.toString();
// 		try {
// 			p = pb.start();
// 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
// 			String settings;
// 			while ((settings = br.readLine()) != null) {
// 			}
// 			br.close();
// 			return settings;
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 		// If this point is reached, IOException was thrown and failed to retrieve settings,
// 		// so return null.
// 		return null;
// 	}
	
	/**
	 * Sets all RPiCamera options to their default settings, overriding any previously
	 * set options.
	 */
	public RPiCamera setToDefaults() {
		saveDir = "/home/pi/Pictures";
		for (Map.Entry<String, String[]> entry : options.entrySet()) {
			entry.setValue(null);
		}
		return this;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// PREVIEW PARAMETER COMMANDS ///////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Turns off image preview.
	 */
	public RPiCamera turnOffPreview() {
		options.put("preview", new String[] { "-n" });
		return this;
	}
	
	/**
	 * Turns on image preview. Note the preview will be superimposed over the top of 
	 * any other windows/graphics.
	 */
	public RPiCamera turnOnPreview() {
		options.put("preview", null);
		return this;
	}
	
	/**
	 * Runs the preview windows using the full resolution capture mode. Maximum frames 
	 * per second in this mode is 15fps and the preview will have the same field of view as 
	 * the capture. Captures should happen more quickly as no mode change should be required. 
	 * This feature is currently under development.
	 */
	public RPiCamera setFullPreviewOn() {
		options.put("fullpreview", new String[] { "-fp" });
		return this;
	}
	
	/**
	 * Turns off fullpreview mode.
	 */
	public RPiCamera setFullPreviewOff() {
		options.put("fullpreview", null);
		return this;
	}
	
	/**
	 * Defines the size and location on the screen that the preview window will be placed. 
	 * Note the preview will be superimposed over the top of any other windows/graphics.
	 * 
	 * @param x int coordinate for upper right corner of preview window.
	 * @param y int coordinate for upper right corner of preview window. 
	 * @param w An int specifying width of preview window.
	 * @param h An int specifying height of preview window.
	 */
	public RPiCamera turnOnPreview(int x, int y, int w, int h) {
		options.put("preview", new String[] { "-p", "" + x + "," + y + "," + w + "," + h });
		return this;
	}
	
	/**
	 * Turn fullscreen preview on or off.
	 * 
	 * @param fullscreen turn on/off fullscreen.
	 */
	public RPiCamera setPreviewFullscreen(boolean fullscreen) {
		if (fullscreen)
			options.put("fullscreen", new String[] { "-f" });
		else
			options.put("fullscreen", null);
		return this;
	}
	
	/**
	 * Sets the preview window's opacity (0 to 255). Opacity values lower than 0
	 * will set opacity to 0, values greater than 255 will set opacity to 255.
	 * 
	 * @param opacity An integer specifying the opacity.
	 */
	public RPiCamera setPreviewOpacity(int opacity) {
		if (opacity > 255)
			opacity = 255;
		else if (opacity < 0)
			opacity = 0;
		options.put("opacity", new String[] { "-op", "" + opacity });
		return this;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// END OF PREVIEW PARAMETER COMMANDS ////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// IMAGE PARAMETER COMMANDS /////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets sharpness of image (-100 to 100), default value is 0. Sharpness values
	 * lower than -100 will set sharpness to -100, values greater than 100 will set
	 * sharpness to 100.
	 * 
	 * @param sharpness An int specifying the sharpness.
	 */
	public RPiCamera setSharpness(int sharpness) {
		if (sharpness > 100)
			sharpness = 100;
		else if (sharpness < -100)
			sharpness = -100;
		options.put("sharpness", new String[] { "-sh", "" + sharpness });
		return this;
	}
	
	/**
	 * Sets contrast of image (-100 to 100), default value is 0. Contrast values lower
	 * than -100 will set contrast to -100, values greater than 100 will set contrast
	 * to 100.
	 * 
	 * @param contrast An int specifying the contrast.
	 */
	public RPiCamera setContrast(int contrast) {
		if (contrast > 100)
			contrast = 100;
		else if (contrast < -100)
			contrast = -100;
		options.put("constast", new String[] { "-co", "" + contrast });
		return this;
	}
	
	/**
	 * Sets brightness of image (0 to 100), default value is 50. 0 is black, 100 is white.
	 * Brightness values lower than 0 will set brightness to 0, values greater than 100 will 
	 * set brightness to 100.
	 * 
	 * @param brightness An int specifying the brightness.
	 */
	public RPiCamera setBrightness(int brightness) {
		if (brightness > 100)
			brightness = 100;
		else if (brightness < 0)
			brightness = 0;
		options.put("brightness", new String[] { "-br", "" + brightness });
		return this;
	}
	
	/**
	 * Sets colour saturation of image (-100 to 100), default is 0. Saturation values lower
	 * than -100 will set saturation to -100, values greater than 100 will set saturation to
	 * 100.
	 * 
	 * @param saturation An int specifying the saturation.
	 */
	public RPiCamera setSaturation(int saturation) {
		if (saturation > 100)
			saturation = 100;
		else if (saturation < -100)
			saturation = -100;
		options.put("saturation", new String[] { "-sa", "" + saturation });
		return this;
	}
	
	/**
	 * WARNING: OPERATION NOT YET SUPPORTED BY raspistill SOFTWARE. OPTION MAY STILL BE SET, 
	 * BUT WILL HAVE NO EFFECT ON THE IMAGE UNTIL SUPPORT IS ADDED TO raspistill.<br>
	 * <br>
	 * Sets ISO of RPiCamera (100 to 800).
	 * 
	 * @param iso An int specifying the ISO.
	 */
	public RPiCamera setISO(int iso) {
		options.put("ISO", new String[] { "-ISO", "" + iso });
		return this;
	}
	
// 	// THIS IS ONLY USED FOR VIDEOS, NOT PICTURES
// 	private void turnOnVStab() {
// 		options.put("vstab", new String[] { "-vs" });
// 	}

// 	// THIS IS ONLY USED FOR VIDEOS, NOT PICTURES
// 	public void turnOffVStab() {
// 		options.put("vstab", null);
// 	}

// 	TODO: MAKE METHOD FOR EVCOMPENSATION (-25 - 25?), ONLY FOR VIDEO
	
	/**
	 * Sets exposure mode of RPiCamera. Certain modes may not be supported
	 * by raspistill software, depending on camera tuning.
	 * 
	 * @param exposure An Exposure enum specifying the desired mode.
	 */
	public RPiCamera setExposure(Exposure exposure) {
		options.put("exposure", new String[] { "-ex", exposure.toString() });
		return this;
	}
	
	/**
	 * Sets Automatic White Balance (AWB) mode. Certain modes may not
	 * be supported by raspistill software, depending on camera type.
	 * 
	 * @param awb An AWB enum specifying the desired AWB setting.
	 */
	public RPiCamera setAWB(AWB awb) {
		options.put("awb", new String[] { "-awb", awb.toString() });
		return this;
	}
	
	/**
	 * Sets an effect to be applied to image. Certain settings may not be 
	 * supported by raspistill software, depending on circumstances.
	 * 
	 * @param imageEffect An ImageEffect enum specifying the desired effect.
	 */
	public RPiCamera setImageEffect(ImageEffect imageEffect) {
		options.put("imxfx", new String[] { "-ifx", imageEffect.toString() });
		return this;
	}
	
	/**
	 * Sets colour effect of RPiCamera. The specified U and V parameters (0 to 255) 
	 * are applied to the U and Y channels of the image. 
	 * For example, setColourEffect(128, 128) should result in a monochrome image.
	 * 
	 * @param U
	 * @param V
	 */
	public RPiCamera setColourEffect(int U, int V) {
		if (U > 255)
			U = 255;
		else if (U < 0)
			U = 0;
		if (V > 255)
			V = 255;
		else if (V < 0)
			V = 0;
		options.put("colfx", new String[] { "-cfx", "" + U, ":", "" + V });
		return this;
	}
	
	/**
	 * Sets metering mode used for preview and capture.
	 * 
	 * @param meteringMode a MeteringMode enum specifying the desired mode.
	 */
	public RPiCamera setMeteringMode(MeteringMode meteringMode) {
		options.put("metering", new String[] { "-mm", meteringMode.toString() });
		return this;
	}
	
	/**
	 * Sets the rotation of the image in viewfinder and resulting image. This 
	 * can take any value from 0 upwards, but due to hardware constraints only 
	 * 0, 90, 180 and 270 degree rotations are supported.
	 * 
	 * @param rotation
	 */
	public RPiCamera setRotation(int rotation) {
		if (rotation > 359)
			while (rotation > 359)
				rotation = rotation - 360;
		else if (rotation < 0)
			while (rotation < 0)
				rotation = rotation + 360;
		options.put("rotation", new String[] { "-rot", "" + rotation });
		return this;
	}
	
	/**
	 * Flips the preview and saved image horizontally.
	 */
	public RPiCamera setHorizontalFlipOn() {
		options.put("hflip", new String[] { "-hf" });
		return this;
	}
	
	/**
	 * Turns off horizontal flip.
	 */
	public RPiCamera setHorizontalFlipOff() {
		options.put("hflip", null);
		return this;
	}
	
	/**
	 * Flips the preview and saved image vertically.
	 */
	public RPiCamera setVerticalFlipOn() {
		options.put("vflip", new String[] { "-vf" });
		return this;
	}
	
	/**
	 * Turns off vertical flip.
	 */
	public RPiCamera setVerticalFlipOff() {
		options.put("vflip", null);
		return this;
	}
	
	/**
	 * Allows the specification of the area of the sensor to be used as the source 
	 * for the preview and capture. This is defined as x,y for the top left corner, 
	 * and a width and height, all values in normalised coordinates (0.0-1.0). \
     * <p>
     * So to set a ROI at half way across and down the sensor, and a width and height of a
	 * quarter of the sensor use: {@code setRegionOfInterest(0.5, 0.5, 0.25, 0.25)}
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param d
	 */
	public RPiCamera setRegionOfInterest(double x, double y, double w, double d) {
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
		options.put("roi", new String[] { "-roi", "" + x, ",", "" + y, ",", "" + w, ",", "" + d });
		return this;
	}
	
	/**
	 * Set the shutter speed to the specified value (in microseconds). There 
	 * is currently an upper limit of approximately 6000000us (6000ms, 6s). 
	 * Shutter speed values passed in that are greater than 6000000 will be
	 * regarded as 6000000.
	 * 
	 * @param speed
	 */
	public RPiCamera setShutter(int speed) {
		if (speed > 6000000)
			speed = 6000000;
		if (speed < 0)
			speed = 0;
		options.put("shutter", new String[] { "-ss", "" + speed });
		return this;
	}
	
	// TODO: MAKE METHOD FOR AWBGAINS DISPLAY?
	
	/**
	 * Sets the Dynamic Range Compression of image. DRC changes the images by 
	 * increasing the range of dark areas of the image, and decreasing the 
	 * brighter areas. This can improve the image in low light areas. 
	 * 
	 * @param drc A DRC enum specifying the desired DRC level.
	 */
	public RPiCamera setDRC(DRC drc) {
		options.put("drc", new String[] { "-drc", drc.toString() });
		return this;
	}
	
	// TODO: MAKE METHOD FOR DISPLAYING STATS
	
	// TODO: MAKE METHOD FOR ANNOTATING PICTURE WITH FLAGS AND TEXT
	
	// TODO: MAKE METHOD FOR STEREOSCOPIC IMAGES
	
	// TODO: MAKE METHOD FOR PREVIEW PARAMETER COMMANDS (Bottom of raspistill -? output)
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////// ENG OF IMAGE PARAMETER COMMANDS ////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////// APPLICATION SPECIFIC SETTINGS //////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * Sets width of images taken by RPiCamera. Note that this setting
	 * can be overriden by the {@link #takeStill(String, int, int)}  and {@link #takeBufferedStill(int, int)} methods.
	 * 
	 * @param width An int specifying the width.
	 */
	public RPiCamera setWidth(int width) {
		options.put("width", new String[] { "-w", "" + width });
		return this;
	}
	
	/**
	 * Sets height of images taken by the RPiCamera. Note that this settings
     * can be overriden by the {@link #takeStill(String, int, int)}  and {@link #takeBufferedStill(int, int)} methods.
	 *
	 * @param height An int specifying the height.
	 */
	public RPiCamera setHeight(int height) {
		options.put("height", new String[] { "-h", "" + height });
		return this;
	}
	
	/**
	 * Sets quality of image (0 to 100), 75 is recommended for usual
	 * purposes. Quality values lower than 0 will set quality to 0, values
	 * greater than 100 will set quality to 100.
	 * 
	 * @param quality An int specifying the quality.
	 */
	public RPiCamera setQuality(int quality) {
		if (quality > 100)
			quality = 100;
		else if (quality < 0)
			quality = 0;
		options.put("quality", new String[] { "-q", "" + quality });
		return this;
	}
	
	/**
	 * Appends raw Bayer data from the RPiCamera to the image's JPEG metadata.
	 * 
	 * @param add turn on/off bayer data.
	 */
	public RPiCamera setAddRawBayer(boolean add) {
		if (add)
			options.put("raw", new String[] { "-r" });
		else
            options.put("raw", null);
		return this;
	}
	
	/**
	 * Links latest image to the specified file. Intended for use with timelapse
	 * photography. Allows the latest image in a series of time lapse photos to 
	 * be easily accessed while the lapse is still running at the specified file, 
	 * while still saving all photos with their unique file names.
	 * 
	 * @param link turn on/off linking.
	 * @param fileName fully qualified path to file to link images to.
	 */
	public RPiCamera setLinkLatestImage(boolean link, String fileName) {
		if (link)
			options.put("latest", new String[] { "-l", "" + fileName });
		else
			options.put("latest", null);
		return this;
	}
	
	/**
	 * Sets the RPiCamera's timeout. RPiCamera runs for the specified number of 
	 * milliseconds before taking the image. Default value is 5 seconds. If timeout 
	 * value is 0, then RPiCamera will continue to run indefinitely (but can be stopped
	 * by calling stop() on the instance). 
	 * 
	 * @param time An int specifying the timeout in milliseconds.
	 */
	public RPiCamera setTimeout(int time) {
		options.put("timeout", new String[] { "-t", "" + time });
		return this;
	}
	
	/**
	 * Allows specification of the thumbnail image inserted in to the image file. 
	 * If not specified, defaults are a size of 64x48 at quality 35.
	 * 
	 * @param x
	 * @param y
	 * @param quality
	 */
	public RPiCamera setThumbnailParams(int x, int y, int quality) {
		options.put("thumb", new String[] { "-th", "" + x, ":", "" + y, ":", "" + quality });
		return this;
	}
	
	/**
	 * Turns off image thumbnails. Reduces image's file size slightly.
	 */
	public RPiCamera turnOffThumbnail() {
		options.put("thumb", new String[] { "-th", "none" });
		return this;
	}
	
	// TODO: MAKE METHOD FOR DEMO MODE
	
	/**
	 * Sets the encoding for images. Default is JPG. Note that unaccelerated image 
	 * types (gif, png, bmp) will take much longer to save than JPG which is hardware 
	 * accelerated. Also note that the filename suffix is completely ignored when deciding 
	 * the encoding of a file.
	 * 
	 * @param encoding an Encoding enum specifying the desired image encoding.
	 */
	public RPiCamera setEncoding(Encoding encoding) {
		options.put("encoding", new String[] { "-e", encoding.toString() });
		return this;
	}
	
	// TODO: ADD EXIF DATA OPTION HERE, NEED TO TEST ON RPI TERMINAL TO KNOW HOW TO IMPLEMENT
	
	// TODO: LOOK INTO THE SIGUSR1 (-s) OPTION - DON'T KNOW WHAT DOES OR IS FOR?
	
	// TODO: ADD METHOD FOR -gl OPTION
	
	// TODO: ADD METHOD FOR -gc CAPTURE OPTION
	
	/**
	 * Selects which camera to use (on a multicamera system). 0 or 1. Default is 0.
	 * Values lower than 0 will select camera 0, values higher than 1 will select
	 * camera 1.
	 * 
	 * @param camNumber
	 */
	public RPiCamera selectCamera(int camNumber) {
		if (camNumber < 0)
			camNumber = 0;
		else if (camNumber > 1)
			camNumber = 1;
		options.put("camselect", new String[] { "-cs", "" + camNumber });
		return this;
	}
	
	/**
	 * Turns on burst mode for RPiCamera. Prevents raspistill from switching
	 * between preview and capture modes, saving a few 100ms of time for each 
	 * capture.
	 */
	public RPiCamera enableBurst() {
		options.put("burst", new String[] { "-bm" });
		return this;
	}
	
	/**
	 * Turns off burst mode for RPiCamera.
	 */
	public RPiCamera disableBurst() {
		options.put("burst", null);
		return this;
	}
	
	// TODO: ADD A METHOD SUPPORTING THE --mode OPTION, WILL NEED AN ENUM PROBLY

	/**
	 * Replaces output pattern (%d) in save file name with DateTime (MonthDayHourMinSec).
     * E.g.
     *<pre>{@code
     * RPiCamera camera = new RPiCamera()
     *    .setDateTimeOn();
     * camera.takeStill("APicture%d.jpg", 500, 500);
     *}</pre>
     * Would produce a file named "APicture1223140756" if executed on Dec 23rd, at 14:07:56.
     */
	public RPiCamera setDateTimeOn() {
		options.put("datetime", new String[] { "-dt" });
		return this;
	}

    /**
     * Disables effects of {@link #setDateTimeOn()}. This is default state of {@link RPiCamera}.
     */
	public RPiCamera setDateTimeOff() {
		options.put("datetime", null);
		return this;
	}

    /**
     * Replace output pattern (%d) with unix timestamp (seconds since 1970)
     * E.g.
     *<pre>{@code
     * RPiCamera camera = new RPiCamera()
     *     .setTimestampOn();
     * camera.takeStill("APicture%d.jpg", 500, 500);
     *}</pre>
     * Would produce a file named "APictureXXX", where XXX is the number of seconds since 1970.
     */
	public RPiCamera setTimestampOn() {
		options.put("timestamp", new String[] { "-ts" });
		return this;
	}

    /**
     * Disables effects of {@link #setTimestampOn()}. This is default state of {@link RPiCamera}.
     */
	public RPiCamera setTimestampOff() {
		options.put("timestamp", null);
		return this;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// END OF APPLICATION SPECIFIC SETTINGS /////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
}
