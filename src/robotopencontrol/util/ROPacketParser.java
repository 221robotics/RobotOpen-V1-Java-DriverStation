package robotopencontrol.util;

import java.util.Arrays;
import robotopencontrol.instance.RODashboardData;

/* ---PACKET FORMAT---
 * Message Type (1 byte)
 * Protocol Version (1 byte)
 * Device ID (1 byte)
 * Firmware Version (1 byte)
 * Uptime (minutes) (1 byte)
 * Power Level (percent) (1 byte)
 * ...Bundle Payloads... (1 length byte + max of 255 payload bytes for each bundle)
 * CRC16 Checksum (2 bytes)
 */

/**
 * @author Eric Barch
 */
public class ROPacketParser {
    
    public static void parsePacket(byte[] packet, int length, RODashboardData dashboardData) {
        // Split up the packet
        byte[] packetPayload = Arrays.copyOfRange(packet, 0, length-2);
        byte[] feedbackData = Arrays.copyOfRange(packet, 6, length-2);
        byte[] packetChecksum = Arrays.copyOfRange(packet, length-2, length);

        // Calculate the CRC16
        byte[] checksum = CRC16.genCRC16(packetPayload);
        
        if (checksum[0] == packetChecksum[0] && checksum[1] == packetChecksum[1] && packetPayload[1] == ROParameters.PROTOCOL_VER) {
            dashboardData.incrementValidRxCount();
            if (packetPayload[0] == ROMessageTypes.HEARTBEAT_PACKET) {
                dashboardData.updateFirmwareVer(packetPayload[3]);
                dashboardData.updateRobotState(packetPayload[4]);
                dashboardData.updateUptime(packetPayload[5]);
                dashboardData.updateBundles(feedbackData);
                dashboardData.updatePacketTime((int)(System.currentTimeMillis() / 1000L));
            }
        }
        else {
            if (packetPayload[1] != ROParameters.PROTOCOL_VER)
                System.out.println("Incompatible protocol version packet received!");
            else {
                System.out.println("Invalid CRC!");
            }
            dashboardData.incrementInvalidRxCount();
        }
    }
}