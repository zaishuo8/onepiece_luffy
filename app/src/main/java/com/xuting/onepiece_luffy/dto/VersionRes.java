package com.xuting.onepiece_luffy.dto;

public class VersionRes {
    private int type;
    private String versionNumber;
    private String apkUrl;
    private boolean forceUpdate;
    private String description;
    private boolean tipUser;

    public VersionRes(int type, String versionNumber, String apkUrl, boolean forceUpdate, String description, boolean tipUser) {
        this.type = type;
        this.versionNumber = versionNumber;
        this.apkUrl = apkUrl;
        this.forceUpdate = forceUpdate;
        this.description = description;
        this.tipUser = tipUser;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTipUser() {
        return tipUser;
    }

    public void setTipUser(boolean tipUser) {
        this.tipUser = tipUser;
    }
}
