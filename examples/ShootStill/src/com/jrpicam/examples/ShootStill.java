package com.jrpicam.examples;

import java.io.IOException;

import com.jrpicam.AWB;
import com.jrpicam.Camera;
import com.jrpicam.DRC;
import com.jrpicam.Exposure;

public class ShootStill {
	
	public static void main(String[] args) {
		Camera piCamera = new Camera("/home/pi/Pictures");
		piCamera.setWidth(500); //Change image width
		piCamera.setHeight(500); //Change image height
		piCamera.setAddRawBayer(true); //Add Bayer data to image file
		piCamera.setBrightness(75); //Set image brightness
		piCamera.setAWB(AWB.AUTO); //Set Camera's Automatic White Balance
		piCamera.setExposure(Exposure.AUTO); //Set Camera's exposure
		piCamera.setDRC(DRC.HIGH); //Set Dynamic Range Compression of Camera
		piCamera.setTimeout(2); //Set Camera's timeout/shutterspeed
		try {
			piCamera.take("A_Name_Without_Spaces.jpg"); //Taking and saving photo
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
