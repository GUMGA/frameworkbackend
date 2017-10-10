package io.gumga.domain.util;


import java.net.NetworkInterface;
import java.time.Instant;
import java.util.Enumeration;
import java.util.UUID;

public class UUIDUtil {

    private static String macAddress;

    protected UUIDUtil() {

    }

    public static String generate() {
        return Instant.now().toEpochMilli() + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase() + getMacValue();
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
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (toReturn.length() == 0) {
                 macAddress = "E9B85E1E6F3D";
                 return macAddress;
            }

            macAddress = toReturn;
            return toReturn;
        }

        return macAddress;
    }
}
