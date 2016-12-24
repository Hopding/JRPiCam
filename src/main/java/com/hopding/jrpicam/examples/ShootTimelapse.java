package com.hopding.jrpicam.examples;

import java.io.IOException;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.AWB;
import com.hopding.jrpicam.enums.DRC;
import com.hopding.jrpicam.enums.Encoding;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;

/**
 * ShootTimelapse is an example of how to take and save a 
 * series of images with JRPiCam.
 * 
 * @author Andrew Dillon
 */
public class ShootTimelapse {
	
	public static void main(String[] args) {
		RPiCamera piCamera = null;
		// Attempt to create an instance of RPiCamera, will fail
		// if raspistill is not properly installed
		try {
			String saveDir = "/home/pi/Desktop";
			piCamera = new RPiCamera(saveDir);
		} catch (FailedToRunRaspistillException e) {
			e.printStackTrace();
		}
		// Take a still image, buffer, and save it
		if (piCamera != null)
			shootTimelapse(piCamera);
	}
	
	public static void shootTimelapse(RPiCamera piCamera) {
		piCamera.setAWB(AWB.AUTO)       // Change Automatic White Balance setting to automatic 
			.setDRC(DRC.OFF)            // Turn off Dynamic Range Compression
			.setContrast(100)           // Set maximum contrast
			.setSharpness(100)          // Set maximum sharpness
			.setQuality(100)            // Set maximum quality
			.turnOnPreview()            // Turn on image preview
			.setEncoding(Encoding.PNG); // Change encoding of images to PNG
		// Take 10 650x650 still images, waiting 1 second between each capture.
		// Save images as "/home/pi/Desktop/<frame_count>Timelapse Image.png"
		try {
			piCamera.setWidth(650) // Set width of images to 650
				.setHeight(650)    // Set height of images to 650
				.setTimeout(10000) // Continue taking images for 10 seconds
				.timelapse(
					true, // Block thread until timelapse is complete
					"%04dTimelapse Image.png", // Image name, framecount will be placed in front of each image name
					1000); // Wait 1 second between taking each image
			System.out.println("New PNG images saved to:\n\t/home/pi/Desktop"); // Print out location of images
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
