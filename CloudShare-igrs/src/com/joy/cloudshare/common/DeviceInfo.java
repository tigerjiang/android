package com.joy.cloudshare.common;

public class DeviceInfo {
    private int    deviceId;
    private String deviceName;
    private int    deviceLogoId;
    
    public DeviceInfo(int id, String deviceName, int deviceLogoId) {
        super();
        this.deviceId = id;
        this.deviceName = deviceName;
        this.deviceLogoId = deviceLogoId;
    }
    
 
    public int getDeviceId() {
        return deviceId;
    }


    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }


    public String getDeviceName() {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public int getDeviceLogoId() {
        return deviceLogoId;
    }
    
    public void setDeviceLogoId(int deviceLogoId) {
        this.deviceLogoId = deviceLogoId;
    }
    
}
