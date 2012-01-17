package robotopencontrol;

import robotopencontrol.instance.RODashboardData;
import robotopencontrol.instance.ROJoystickHandler;
import robotopencontrol.instance.ROPacketTransmitter;

/**
 * @author ebarch
 */
public class RORobotInstance {
    private ROJoystickHandler joystickHandler;
    private RODashboardData dashboardData;
    private ROPacketTransmitter packetTransmitter;
    
    public RORobotInstance() {
        joystickHandler = new ROJoystickHandler();
        dashboardData = new RODashboardData();
        packetTransmitter = new ROPacketTransmitter(joystickHandler);
        packetTransmitter.setDashboardData(dashboardData);
    }
    
    public void reInitJoystickHandler() {
    	joystickHandler = new ROJoystickHandler();
    }
    
    public ROJoystickHandler getJoystickHandler() {
    	return joystickHandler;
    }
    
    public RODashboardData getDashboardData() {
    	return dashboardData;
    }
    
    public ROPacketTransmitter getPacketTransmitter() {
    	return packetTransmitter;
    }
}