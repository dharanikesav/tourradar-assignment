package com.assessment;

import com.google.gson.JsonObject;

import java.net.InetAddress;

public interface ISystemProperties {
    String machineName = getMachineName();
    String OS = System.getProperty("os.name");
    String currentDir = System.getProperty("user.dir");
    String pathSeperator = OS.contains("Mac") ? "/" : "\\";

    private static String getMachineName() {
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            return addr.getHostName().toUpperCase();
        }
        catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

}
