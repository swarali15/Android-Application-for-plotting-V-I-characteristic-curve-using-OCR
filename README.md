# Android-Application-for-plotting-V-I-characteristic-curve-using-OCR
To detect the Voltage and current value from Battery using OCR and plot the Current Vs Voltage graph in an android application..
1.	Add dependency to include the play-services-vision dependency.		 “implementation 'com.google.android.gms:play-services-vision:18.0.0' “ and 
Add dependency to include the Graph View –
	“implementation 'com.jjoe64:graphview:4.2.2'”

2.	In createCameraSource function we declare object of TextRecognizer .TextRecognizer detector object processes images and determines what text appears within them. TextRecognizer can be used to detect text in all types of images. 
3.	Check if TextRecognizer is operational. The output of the TextRecognizer can be retrieved by using SparseArray and StringBuilder.
4.	Create a CameraSource, which is a camera manager pre-configured for Vision processing.
Set the resolution high and turn autofocus on, because that's a good match for recognizing small text. 		
setRequestedPreviewSize(1280, 1024) 					setRequestedFps(2.0f)
 setAutoFocusEnabled(true)

Implement surfaceChanged() and surfaceDestroyed() .
 surfaceChanged:This is called immediately after any structural changes (format or size) have been made to the surface. 
surfaceCreated:This is called immediately after the surface is first created. Implementations of this should start up whatever rendering code they desire. 
onResume()->onSurfaceCreated()->onSurfaceChanged()
onPause()->onSurfaceDestroyed()

5.	Create a Processor which will handle detections as often as they become available Detector.Processor<TextBlock>. We have used TextBlock to retrieve the paragraph from the image using OCR.
6.	Override receiveDetections() to detect and store it into StringBuilder using toString() print the text on the screen. 
7.	Store the detected data in string. If string has numbes it will parse the string into integer.Create a class DataSample to store values of current and voltage .This c;ass is used to initialise datapoint variable.
8.	Pass the integers to datapoint series using get and set functions in DataSample.java.
9.	Add series to graph view for ploting. mGraph.addSeries(series); 
