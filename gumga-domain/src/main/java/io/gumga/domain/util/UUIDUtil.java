package io.gumga.domain.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class UUIDUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UUIDUtil.class);

    private static String macAddress;
    private static long lastLocalId;

    protected UUIDUtil() {

    }

    public static synchronized String generate() {
        long localId = System.currentTimeMillis()*10000;

        while (localId <= lastLocalId) {
            localId++;
        }

        lastLocalId = localId;

        return "" + Long.toHexString(localId).toUpperCase() + String.format("%06X", ((long)( Math.random() * 0x1000000))) + getMacValue();
    }

    private static String getMacValue() {
        if(macAddress == null) {
            String toReturn = "";
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface network = networkInterfaces.nextElement();
                    byte[] mac = network.getHardwareAddress();
                    if (mac == null) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X", mac[i]));
                    }
                    toReturn += sb.toString();
                    break;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (toReturn.length() == 0) {
                macAddress = "6D756E696666";
                return macAddress;
            }
            macAddress = toReturn;
            return macAddress;
        }

        return macAddress;
    }
}
