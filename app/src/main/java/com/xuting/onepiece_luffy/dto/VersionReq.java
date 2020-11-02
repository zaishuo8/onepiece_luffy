package com.xuting.onepiece_luffy.dto;

public class VersionReq {
    private float deviceId;
    private float appId;
    private String nativeVersionNumber;
    private String hotUpdateVersionNumber;

    public float getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(float deviceId) {
        this.deviceId = deviceId;
    }

    public float getAppId() {
        return appId;
    }

    public void setAppId(float appId) {
        this.appId = appId;
    }

    public String getNativeVersionNumber() {
        return nativeVersionNumber;
    }

    public void setNativeVersionNumber(String nativeVersionNumber) {
        this.nativeVersionNumber = nativeVersionNumber;
    }

    public String getHotUpdateVersionNumber() {
        return hotUpdateVersionNumber;
    }

    public void setHotUpdateVersionNumber(String hotUpdateVersionNumber) {
        this.hotUpdateVersionNumber = hotUpdateVersionNumber;
    }

    public VersionReq(float deviceId, float appId, String nativeVersionNumber, String hotUpdateVersionNumber) {
        this.deviceId = deviceId;
        this.appId = appId;
        this.nativeVersionNumber = nativeVersionNumber;
        this.hotUpdateVersionNumber = hotUpdateVersionNumber;
    }
}
