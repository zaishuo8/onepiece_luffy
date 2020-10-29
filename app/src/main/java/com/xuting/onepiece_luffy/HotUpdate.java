package com.xuting.onepiece_luffy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

class HotUpdate {

    static String libappSoFileName = "libapp.so";
    static boolean downloadDone = true;
    static boolean downSuccess = false;

    static ApplicationInfo applicationInfo;
    static String libappSoPath;

    HotUpdate(@NonNull Context applicationContext) {
        applicationInfo = getApplicationInfo(applicationContext);
        libappSoPath = applicationInfo.dataDir + File.separator + libappSoFileName;
    }

    @NonNull
    private ApplicationInfo getApplicationInfo(@NonNull Context applicationContext) {
        try {
            return applicationContext
                    .getPackageManager()
                    .getApplicationInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void check() {
        new Thread(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        File destFile = new File(libappSoPath);
                        boolean fileExists = destFile.exists();
                        if (fileExists) {
                            // todo 不能直接删除，改个文件名 libapp_backup.so 用来下载更新失败回滚用
                            destFile.delete();
                        }

                        if (!destFile.exists()){
                            HotUpdate.downloadDone = false;
                            HotUpdate.downSuccess = false;
                            MainActivity.instance.changeDownloadProgress("开始下载...");

                            boolean res = destFile.createNewFile();
                            if (res) {
                                String newLibappSoUrl = "https://zaishuo8.github.io/ssq/libapp_2.so";
                                URL url = new URL(newLibappSoUrl);
                                trustAllHosts();
                                URLConnection connection = url.openConnection();
                                connection.connect();
                                InputStream inputStream = connection.getInputStream();
                                int fileSize = connection.getContentLength();
                                if (fileSize <= 0) {
                                    throw new RuntimeException("无法获知文件大小");
                                }
                                if (inputStream == null) {
                                    throw new  RuntimeException("stream is null");
                                }

                                FileOutputStream fileOutputStream = new FileOutputStream(destFile);
                                byte[] buffer = new byte[1024];
                                double downLoadFileSize = 0;
                                do{
                                    //循环读取
                                    int numread = inputStream.read(buffer);
                                    if (numread == -1)
                                    {
                                        break;
                                    }
                                    fileOutputStream.write(buffer, 0, numread);
                                    downLoadFileSize += numread;
                                    // 更新进度条
                                    double progress = downLoadFileSize / fileSize * 100;
                                    DecimalFormat df = new DecimalFormat("#.00");
                                    String str = df.format(progress);
                                    String progressStr = str + "%";
                                    MainActivity.instance.changeDownloadProgress(progressStr);
                                } while (true);

                                inputStream.close();
                                fileOutputStream.close();

                                HotUpdate.downloadDone = true;
                                HotUpdate.downSuccess = true;

                                Log.d("--- HotUpdate ---", "下载成功");
                            }
                        }
                    } catch (IOException e){
                        HotUpdate.downloadDone = true;
                        HotUpdate.downSuccess = false;
                        e.printStackTrace();
                        Log.d("--- HotUpdate ---", "下载失败");
                    }
                }
            }
        ).start();
    }

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                    }

                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        Log.i(TAG, "checkClientTrusted");
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        Log.i(TAG, "checkServerTrusted");
                    }
                }

        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CheckDto {
        private long id;
        private String version;   // 版本号 "1.0.0" 格式
        private boolean needTip;  // 是否需要提醒用户
        private String tipMessage;  // 提醒文案
        private boolean forceUpdate;  // 提醒用户更新的时候，能不能不更新

        private String downUrl;  // 更新包下载地址

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public boolean isNeedTip() {
            return needTip;
        }

        public void setNeedTip(boolean needTip) {
            this.needTip = needTip;
        }

        public String getTipMessage() {
            return tipMessage;
        }

        public void setTipMessage(String tipMessage) {
            this.tipMessage = tipMessage;
        }

        public boolean isForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }
    }
}
