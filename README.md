# JRPiCam
JRPiCam is a simple Java API that allows Java applications running on a Raspberry Pi to access the Raspberry Pi Camera. JRPiCam
achieves this functionality by using the ProcessBuilder class to run the native raspistill program on the RPi. This means that 
JRPiCam has the same functionality as raspistill - no more, no less. 

Because JRPiCam works by invoking the raspistill software, it is important that your RPi be properly configured to run 
raspistill. The appropriate settings may be configured by running `raspi-config` in the terminal of your RPi. Further 
instructions can be found [here] (https://www.raspberrypi.org/documentation/configuration/camera.md).

The core component of JRPiCam is the Camera class, which can be instantiated as follows:
```
//Create a Camera that saves images to the Pi's Pictures directory.
Camera piCamera = new Camera("/home/pi/Pictures");
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

//Sets all Camera options to their default settings, removing any changes previously made.
piCamera.setToDefaults();
```
Once the Camera has been created, and the desired options have been adjusted, an image may be captured from the RPi Camera
by called the take() method:
```
piCamera.take("AnAwesomePic.jpg");
```
take() requires a String to be passed in to save the image under. In this case, once the Camera has captured the image, it will
be saved as "/home/pi/Pictures/AnAwesomePic.jpg" (since we've set the Camera to save images to the "/home/pi/Pictures" 
directory). It is important that image names do NOT CONTAIN SPACES. If they do, JRPiCam may not properly take, save, or load 
the image.

take() also returns a File object that can be used to easily obtain and load the image after it has been saved to the Pi's 
memory. The following code captures an image and loads it into a BufferedInputStream:
```
BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(piCamera.take("AnAwesomePic.jpg")));
```
Additional code examples can be found in the "examples" directory. Both the "/examples/JRPiCam_Demo" and "/examples/ShootStill"
directories contain source code as well as a .jar file that can be executed on the RPi's terminal. JRPiCam_Demo.jar runs a demo
program that illustrates some functions of JRPiCam. ShootStill.jar captures an image and saves it as "/home/pi/Pictures/A_Name_Without_Spaces.jpg".
