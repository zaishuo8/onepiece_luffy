package com.xuting.onepiece_luffy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.xuting.onepiece_luffy.dto.VersionReq;
import com.xuting.onepiece_luffy.dto.VersionRes;
import com.xuting.onepiece_luffy.utils.Http;
import com.xuting.onepiece_luffy.utils.SharePrefUtil;

import java.io.IOException;

public class Init {

    private static final String Tag = "InitInit";

    private static void toast(String message) {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 是否已经加载了 libapp.so
     * */
    private static boolean isLoadFlutter() {
        return !(getFlutterVersionNumber().equals("0.0.0"));
    }

    /**
     * 获取 flutter 版本号
     * */
    private static String getFlutterVersionNumber() {
        return SharePrefUtil.getFlutterVersion();
    }

    /**
     * 设置 flutter 版本号
     * */
    private static void setFlutterVersionNumber(String flutterVersionNumber) {
        SharePrefUtil.setFlutterVersion(flutterVersionNumber);
    }

    /**
     * 获取原生版本号
     */
    private static String getNativeVersionNumber() {
        String appVersionName = "";
        Context context = MyApplication.getContext();
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Tag, e.getMessage());
        }
        return appVersionName;
    }

    /**
     * 获取当前原生版本最新的 flutter
     * */
    private static VersionRes getLatestFlutterVersion() {
        try {
            VersionReq versionReq = new VersionReq(
                    Http.deviceId, Http.appId, getNativeVersionNumber(), ""
            );
            Gson gson = new Gson();
            String versionRes = Http.post(Http.latestFlutterVersionUrl, gson.toJson(versionReq));
            return gson.fromJson(versionRes, VersionRes.class);
        } catch (IOException e) {
            Log.e(Tag, e.getMessage());
        }
        return null;
    }

    /**
     * 初始化引擎
     * */
    private static void initEngine() {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                ((MyApplication) MyApplication.getContext()).initEngin();
            }
        });
    }

    /**
     * 原生更新
     * */
    private static void nativeUpdate(VersionRes versionRes, Context context) {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("原生更新版本：" + versionRes.getVersionNumber())
                        .setMessage(versionRes.getDescription())
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 启动系统浏览器打开下载 apk 地址
                                Uri uri = Uri.parse(versionRes.getApkUrl());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                initEngine();
                            }
                        })
                        .create().show();
            }
        });
    }

    /**
     * flutter 更新
     * */
    private static void flutterUpdate(VersionRes versionRes, Context context) {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Flutter 更新版本：" + versionRes.getVersionNumber())
                        .setMessage(versionRes.getDescription())
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        HotUpdate.check(versionRes.getLibappsoArm64V8aUrl());
                                        setFlutterVersionNumber(versionRes.getVersionNumber());
                                        initEngine();
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                initEngine();
                            }
                        })
                        .create().show();
            }
        });
    }

    /**
     * 检测更新
     * */
    private static void checkUpdate(Context context) {
        try {
            String nativeVersion = getNativeVersionNumber();
            String flutterVersion = getFlutterVersionNumber();
            toast("native 版本号：" + nativeVersion + "\n" + "flutter 版本号：" + flutterVersion);
            Log.d(Tag, "native 版本号：" + nativeVersion);
            Log.d(Tag, "flutter 版本号：" + flutterVersion);
            VersionReq req = new VersionReq(Http.deviceId, Http.appId, nativeVersion, flutterVersion);
            Gson gson = new Gson();
            String resJson = Http.post(Http.checkUpdate, gson.toJson(req));
            VersionRes versionRes = gson.fromJson(resJson, VersionRes.class);
            int type = versionRes.getType();
            if (type == 0) {
                // 不需要更新
                toast("已经是最新版本了");
                Log.d(Tag, "已经是最新版本了");
                initEngine();
            } else if (type == 1) {
                // 原生更新
                nativeUpdate(versionRes, context);
            } else if (type == 2) {
                // flutter 更新
                flutterUpdate(versionRes, context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否已经有 flutter
     * 有 - 执行更新检测
     * 无 - 下载，下载完成后设置 flutter 版本号，执行更新检测
     * 没有的话下载，下载完成后设置 flutter 版本号
     * */
    public static void checkFlutter(Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isLoadFlutter()) {
                    VersionRes versionRes = getLatestFlutterVersion();
                    HotUpdate.check(versionRes.getLibappsoArm64V8aUrl());
                    setFlutterVersionNumber(versionRes.getVersionNumber());
                }
                checkUpdate(context);
            }
        }).start();
    }
}
