# JRPiCam
JRPiCam is a Java API that allows Java applications running on a Raspberry Pi to access the Raspberry Pi Camera. JRPiCam
achieves this functionality by using the ProcessBuilder class to run the native raspistill program on the RPi. This means that 
JRPiCam has all the same functionality as raspistill, plus additional Java specific features.

Because JRPiCam works by invoking the raspistill software, it is important that your RPi be properly configured to run 
raspistill. The appropriate settings may be configured by running `raspi-config` in the terminal of your RPi. Further 
instructions can be found [here] (https://www.raspberrypi.org/documentation/configuration/camera.md).

# Using JRPiCam
To use JRPiCam in your project, just download and add the JRPiCam.jar file to your project's build path.

The core component of JRPiCam is the RPiCamera class, which can be instantiated as follows:
```
//Create a Camera that saves images to the Pi's Pictures directory.
RPiCamera piCamera = new RPiCamera("/home/pi/Pictures");
```
Various options can be set on the camera by calling the appropriate methods:
```
//Set Camera to produce 500x500 images.
piCamera.setWidth(500); 
piCamera.setHeight(500);

//Adjust Camera's brightness setting.
piCamera.setBrightness(75);

//Set Camera's exposure.
piCamera.setExposure(Exposure.AUTO);

//Set Camera's timeout.
piCamera.setTimeout(2);

//Add Raw Bayer data to image files created by Camera.
piCamera.setAddRawBayer(true);

//Sets all Camera options to their default settings, overriding any changes previously made.
piCamera.setToDefaults();
```
Once the RPiCamera has been created, and the desired options have been adjusted, an image may be captured from the RPi Camera
by called the takeStill() method:
```
piCamera.takeStill("An Awesome Pic.jpg");
```
takeStill() requires a String to be passed in to save the image under. In this case, once the Camera has captured the image, it will be saved as "/home/pi/Pictures/AnAwesomePic.jpg" (since we've set the Camera to save images to the "/home/pi/Pictures" 
directory).

takeStill() also returns a File object that can be used to easily obtain and load the image after it has been saved to the Pi's memory. The following code captures an image, saves it, and then loads it into a BufferedImage:
```
BufferedImage image = ImageIO.read(piCamera.take("An Awesome Pic.jpg")));
```
Images may also be loaded directly into a buffer within your application by calling the takeBufferedStill() method:
```
BufferedImage image = piCamera.takeBufferedStill();
```
Capturing images this way is much faster than saving them to memory and then loading them into your application (as you would
have to do with the takeStill() method), and is particularly useful if you don't want to save the photo in the RPi at all (perhaps you want to send it over a network and save it on a remote server).

Additional code examples and information can be found in the "examples" directory or the [JRPiCam wiki](https://github.com/Hopding/JRPiCam/wiki). All of the directories in "examples" contain source code as well as a .jar file that can be executed on the RPi's terminal. jrpicam_demo.jar runs a demo gui program that illustrates some functions of JRPiCam. The rest of the examples show how to do things like take a still image, save it, load it into a buffer, and take a series of images.
