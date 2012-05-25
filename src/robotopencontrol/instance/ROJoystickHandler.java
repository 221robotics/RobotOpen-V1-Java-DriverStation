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
    ArrayList<Controller> activeControllers;
   
    // Our constructor for the ROJoystickHandler object
    public ROJoystickHandler() {
        controllers = null;
        activeControllers = new ArrayList<Controller>();
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
        		activeControllers.add(controllers[controllerIndex]);
        }
        catch (Exception e) {
        	System.exit(0);
        }
    }
    
    public void clearControllers() {
        // Reset the ArrayList
        activeControllers = new ArrayList<Controller>();
    }
    
    public byte[] exportValues() {
    	try {
	        byte[] exportValues = new byte[getExportSize()];
	        int currentIndex = 0;
	        
	        for (int i = 0; i < activeControllers.size(); i++) {
	            Controller indexedController = activeControllers.get(i);
	            
	            boolean success = indexedController.poll();
	            
	            // The joystick is no longer available - reset everything
	            if (!success) {
	            	System.exit(0);
	            }
	            
	            Component[] components = indexedController.getComponents();
	            
	            // Set the length of the bundle
	            exportValues[currentIndex++] = (byte)(int)(components.length + 1);
	            
	            // Set the bundleID
	        	exportValues[currentIndex++] = (byte)(48 + i);
	            
	            // Return an ArrayList with the values from the joystick
	            for (int j=0;j<components.length;j++) {
	                if (components[j].isAnalog()) {
	                    // We need to convert this value to something between 0 and 255
	                    float analogVal = mapValue(components[j].getPollData(), -1, 1, 0, 255);
	                    
	                    // Return as byte
	                    exportValues[currentIndex++] = (byte)(int)analogVal;
	                    
	                    System.out.print("C" + j + ":" + (int)mapValue(components[j].getPollData(), -1, 1, 0, 255) + " ");
	                }
	                else {
	                    // Return as byte
	                    exportValues[currentIndex++] = (byte)(int)(components[j].getPollData() * 255);
	                    
	                    System.out.print("C" + j + ":" + (int)(components[j].getPollData() * 255) + " ");
	                }
	            }
	            System.out.println("");
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
	        	// The +2 is the length byte and bundle ID before each bundle
	            sizeReturn += activeControllers.get(i).getComponents().length + 2;
	        }
	        
	        return sizeReturn;
        }
        catch (Exception e) {
        	System.exit(0);
        	return 0;
        }
    }
    
    private float mapValue(float input, float inMin, float inMax, float outMin, float outMax) {
        return (input - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
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