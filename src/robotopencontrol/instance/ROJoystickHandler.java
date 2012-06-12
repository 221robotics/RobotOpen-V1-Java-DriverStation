package robotopencontrol.instance;

import java.util.ArrayList;
import java.util.Observable;

import net.java.games.input.*;

/**
 * @author Eric Barch
 */
public class ROJoystickHandler extends Observable {
    // Stores all of the controllers currently connected
    private Controller[] controllers;
    // Stores all active joysticks
    ArrayList<JInputController> activeControllers;
   
    // Our constructor for the ROJoystickHandler object
    public ROJoystickHandler() {
        controllers = null;
        activeControllers = new ArrayList<JInputController>();
    }
    
    // Loop through controller array
    public String[] getControllers() {
    	controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        
        // An Array to store all of the controllers to be returned
        String[] controllersFound = new String[controllers.length+1];
        
        controllersFound[0] = "Disabled";
        
        // Return an ArrayList with the names of all of the connected devices
        for (int j=0;j<controllers.length;j++) {
            controllersFound[j+1] = controllers[j].getName();
        }
        
        return controllersFound;
    }
    
    // Set a controller as included in control packets
    public void activateController(int controllerIndex) {
        try {
        	if (!activeControllers.contains(controllers[controllerIndex]))
        		activeControllers.add(new JInputController(controllers[controllerIndex]));
        }
        catch (Exception e) {
        	System.exit(0);
        }
    }
    
    public void clearControllers() {
        // Reset the ArrayList
        activeControllers = new ArrayList<JInputController>();
    }
    
    public byte[] exportValues() {
    	try {
	        byte[] exportValues = new byte[getExportSize()];
	        int currentIndex = 0;
	        
	        for (int i = 0; i < activeControllers.size(); i++) {
	        	JInputController indexedController = activeControllers.get(i);
	            
	            boolean success = indexedController.poll();
	            
	            // The joystick is no longer available - reset everything
	            if (!success) {
	            	System.exit(0);
	            }
	            
	            // Set the length of the bundle
	            exportValues[currentIndex++] = (byte)18;
	            
	            // Set the bundleID
	        	exportValues[currentIndex++] = (byte)(48 + i);
	        	
	        	if (indexedController.getName().toLowerCase().indexOf("xbox") == -1 && !indexedController.getName().equalsIgnoreCase("controller")) {
	        		// Assume logitech style controller
	        		exportValues[currentIndex++] = (byte)indexedController.getXAxisValue();		// ANALOG_LEFTX
	        		exportValues[currentIndex++] = (byte)indexedController.getYAxisValue(); 	// ANALOG_LEFTY
	        		exportValues[currentIndex++] = (byte)indexedController.getZAxisValue();		// ANALOG_RIGHTX
	        		exportValues[currentIndex++] = (byte)indexedController.getRZAxisValue();	// ANALOG_RIGHTY
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(10);		// LEFT_ANALOG_BTN
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(11);		// RIGHT_ANALOG_BTN
	        		exportValues[currentIndex++] = (byte)indexedController.getPov();			// DPAD
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(0);		// BTN1
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(1);		// BTN2
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(2);		// BTN3
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(3);		// BTN4
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(4);		// BTN5
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(5);		// BTN6
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(6);		// BTN7
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(7);		// BTN8
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(8);		// BTN9
	        		exportValues[currentIndex++] = (byte)indexedController.getButton(9);		// BTN10
	        	}
	        	else {
	        		if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
	        			// os x implementation
	        			exportValues[currentIndex++] = (byte)indexedController.getXAxisValue();						// ANALOG_LEFTX
		        		exportValues[currentIndex++] = (byte)indexedController.getYAxisValue(); 					// ANALOG_LEFTY
		        		exportValues[currentIndex++] = (byte)indexedController.getRXAxisValue();					// ANALOG_RIGHTX
		        		exportValues[currentIndex++] = (byte)indexedController.getRYAxisValue();					// ANALOG_RIGHTY
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(6);						// LEFT_ANALOG_BTN
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(7);						// RIGHT_ANALOG_BTN
		        		if (indexedController.getButton(0) == 255 && indexedController.getButton(2) == 255)			// DPAD
		        			exportValues[currentIndex++] = (byte)31;
		        		else if (indexedController.getButton(1) == 255 && indexedController.getButton(2) == 255)
		        			exportValues[currentIndex++] = (byte)223;
		        		else if (indexedController.getButton(0) == 255 && indexedController.getButton(3) == 255)
		        			exportValues[currentIndex++] = (byte)95;
		        		else if (indexedController.getButton(1) == 255 && indexedController.getButton(3) == 255)
		        			exportValues[currentIndex++] = (byte)159;
		        		else if (indexedController.getButton(0) == 255)
		        			exportValues[currentIndex++] = (byte)63;
		        		else if (indexedController.getButton(1) == 255)
		        			exportValues[currentIndex++] = (byte)191;
		        		else if (indexedController.getButton(2) == 255)
		        			exportValues[currentIndex++] = (byte)255;
		        		else if (indexedController.getButton(3) == 255)
		        			exportValues[currentIndex++] = (byte)127;
		        		else
		        			exportValues[currentIndex++] = (byte)0;
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(11);		// BTN1
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(12);		// BTN2
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(13);		// BTN3
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(14);		// BTN4
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(8);		// BTN5
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(9);		// BTN6
		        		if (indexedController.getZAxisValue() > 127)								// BTN7
		        			exportValues[currentIndex++] = (byte)255;
		        		else
		        			exportValues[currentIndex++] = (byte)0;
		        		if (indexedController.getRZAxisValue() > 127)								// BTN8
		        			exportValues[currentIndex++] = (byte)255;
		        		else
		        			exportValues[currentIndex++] = (byte)0;
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(5);		// BTN9
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(4);		// BTN10
	        		} else {
	        			// windows implementation
	        			exportValues[currentIndex++] = (byte)indexedController.getXAxisValue();		// ANALOG_LEFTX
		        		exportValues[currentIndex++] = (byte)indexedController.getYAxisValue(); 	// ANALOG_LEFTY
		        		exportValues[currentIndex++] = (byte)indexedController.getRXAxisValue();	// ANALOG_RIGHTX
		        		exportValues[currentIndex++] = (byte)indexedController.getRYAxisValue();	// ANALOG_RIGHTY
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(8);		// LEFT_ANALOG_BTN
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(9);		// RIGHT_ANALOG_BTN
		        		exportValues[currentIndex++] = (byte)indexedController.getPov();			// DPAD
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(0);		// BTN1
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(1);		// BTN2
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(2);		// BTN3
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(3);		// BTN4
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(4);		// BTN5
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(5);		// BTN6
		        		if (indexedController.getZAxisValue() > 127)								// BTN7
		        			exportValues[currentIndex++] = (byte)255;
		        		else
		        			exportValues[currentIndex++] = (byte)0;
		        		if (indexedController.getZAxisValue() < 127)								// BTN8
		        			exportValues[currentIndex++] = (byte)255;
		        		else
		        			exportValues[currentIndex++] = (byte)0;
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(6);		// BTN9
		        		exportValues[currentIndex++] = (byte)indexedController.getButton(7);		// BTN10
	        		}
	        	}
	        }
	        
	        return exportValues;
    	}
    	catch (Exception e) {
    		System.exit(0);
    		return null;
    	}
    }
    
    private int getExportSize() {
        try {
	    	int sizeReturn = 0;
	       
	        for (int i = 0; i < activeControllers.size(); i++) {
	        	// The fixed component siez of 17, +2 is the length byte and bundle ID before each bundle
	            sizeReturn += 19;
	        }
	        
	        return sizeReturn;
        }
        catch (Exception e) {
        	System.exit(0);
        	return 0;
        }
    }
    
    
    
    public boolean controllersActive() {
    	try {
	    	if (activeControllers.size() > 0)
	    		return true;
	    	else
	    		return false;
    	}
    	catch (Exception e) {
    		System.exit(0);
    		return false;
    	}
    }
}