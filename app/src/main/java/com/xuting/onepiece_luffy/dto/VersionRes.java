package com.xuting.onepiece_luffy.dto;

public class VersionRes {
    private int type;
    private String versionNumber;
    private String apkUrl;
    private boolean forceUpdate;
    private String description;
    private boolean tipUser;
    private String libappsoArmeabiV7aUrl;
    private String libappsoArm64V8aUrl;
    private String libappsoX8664Url;

    public VersionRes(
            int type, String versionNumber, String apkUrl, boolean forceUpdate, String description,
            boolean tipUser,
            String libappsoArmeabiV7aUrl,
            String libappsoArm64V8aUrl,
            String libappsoX8664Url
    ) {
        this.type = type;
        this.versionNumber = versionNumber;
        this.apkUrl = apkUrl;
        this.forceUpdate = forceUpdate;
        this.description = description;
        this.tipUser = tipUser;
        this.libappsoArmeabiV7aUrl = libappsoArmeabiV7aUrl;
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


    public String getLibappsoArmeabiV7aUrl() {
        return libappsoArmeabiV7aUrl;
    }

    public void setLibappsoArmeabiV7aUrl(String libappsoArmeabiV7aUrl) {
        this.libappsoArmeabiV7aUrl = libappsoArmeabiV7aUrl;
    }

    public String getLibappsoArm64V8aUrl() {
        return libappsoArm64V8aUrl;
    }

    public void setLibappsoArm64V8aUrl(String libappsoArm64V8aUrl) {
        this.libappsoArm64V8aUrl = libappsoArm64V8aUrl;
    }

    public String getLibappsoX8664Url() {
        return libappsoX8664Url;
    }

    public void setLibappsoX8664Url(String libappsoX8664Url) {
        this.libappsoX8664Url = libappsoX8664Url;
    }
}
