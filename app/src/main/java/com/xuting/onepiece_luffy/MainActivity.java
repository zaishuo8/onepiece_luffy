package com.xuting.onepiece_luffy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.idlefish.flutterboost.containers.BoostFlutterActivity;
import com.xuting.onepiece_luffy.dto.VersionReq;
import com.xuting.onepiece_luffy.dto.VersionRes;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static MainActivity instance = null;

    public static WeakReference<MainActivity> sRef;

    private TextView mOpenNative;
    private TextView mOpenFlutter;
    private TextView mOpenFlutterFragment;

    private TextView downloadProgress;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    /**
     * 发 post 请求
     * */
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * 获取原生版本号
     */
    public static String getNativeVersionNumber(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }

    /**
     * 获取 flutter 版本号
     * */
    public static String getFlutterVersionNumber() {
        return "1.0.0";
    }

    /**
     * 检测更新
     * */
    public void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String nativeVersion = getNativeVersionNumber(MainActivity.this);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.info)).setText("当前版本号：" + nativeVersion);
                        }
                    });
                    String flutterVersion = getFlutterVersionNumber();
                    VersionReq req = new VersionReq(
                            123456, 1,
                            nativeVersion, flutterVersion
                    );
                    Gson gson = new Gson();
                    String resJson = post("http://192.168.0.100:7001/version/check", gson.toJson(req));
                    VersionRes versionRes = gson.fromJson(resJson, VersionRes.class);
                    if (versionRes.getType() == 0) {
                        // 不需要更新
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        MainActivity.this,
                                        "已经是最新版本",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });
                        return;
                    }
                    if (versionRes.getType() == 1) {
                        // 原生更新
                        nativeUpdate(versionRes);
                        return;
                    }
                    if (versionRes.getType() == 2) {
                        // flutter 更新
                        flutterUpdate(versionRes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 原生更新
     * */
    public void nativeUpdate(VersionRes versionRes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("原生更新版本：" + versionRes.getVersionNumber())
                        .setMessage(versionRes.getDescription())
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 启动系统浏览器打开下载 apk 地址
                                Uri uri = Uri.parse(versionRes.getApkUrl());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("否", null)
                        .create().show();
            }
        });
    }

    /**
     * flutter 更新
     * */
    public void flutterUpdate(VersionRes versionRes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Flutter 更新版本：" + versionRes.getVersionNumber())
                        .setMessage(versionRes.getDescription())
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HotUpdate.check(versionRes.getLibappsoArmeabiV7aUrl());
                            }
                        })
                        .setNegativeButton("否", null)
                        .create().show();
    }

    public void changeDownloadProgress(String progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadProgress.setText(progress);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sRef = new WeakReference<>(this);

        setContentView(R.layout.native_page);

        instance = this;
        downloadProgress = findViewById(R.id.download_progress);

        /*mOpenNative = findViewById(R.id.open_native);
        mOpenFlutter = findViewById(R.id.open_flutter);
        mOpenFlutterFragment = findViewById(R.id.open_flutter_fragment);

        mOpenNative.setOnClickListener(this);
        mOpenFlutter.setOnClickListener(this);
        mOpenFlutterFragment.setOnClickListener(this);*/

        // 打开 flutter activity
        findViewById(R.id.open_flutter_cus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // test();

                // 这个 url 是在 flutter 端注册好的页面对应的字符串
                String url = "flutter://zoro_flutter_page";
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("p1","v1");
                params.put("p2","v2");
                Intent intent = BoostFlutterActivity.withNewEngine().url(url).params(params)
                        .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                        .build(MainActivity.this);
                MainActivity.this.startActivityForResult(intent, 0);
            }
        });

        // 检查热更
        findViewById(R.id.hot_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdate();
            }
        });

        // 初始化引擎
        findViewById(R.id.init_engine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!HotUpdate.downloadDone) {
                        Toast.makeText(getApplicationContext(), "还没有下载好", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!HotUpdate.downSuccess) {
                        Toast.makeText(getApplicationContext(), "下载失败了，重试一下", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ((MyApplication) getApplicationContext()).initEngin();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        // 打开 flutter 错误捕获页面
        findViewById(R.id.open_flutter_catch_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "flutter://error_catch_page";
                Intent intent = BoostFlutterActivity.withNewEngine().url(url).params(new HashMap<>())
                        .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                        .build(MainActivity.this);
                MainActivity.this.startActivityForResult(intent, 0);
            }
        });

        // 打开 flutter 帧率监测页面
        findViewById(R.id.open_flutter_fps_watch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "flutter://fsp_watch_page";
                Intent intent = BoostFlutterActivity.withNewEngine().url(url).params(new HashMap<>())
                        .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                        .build(MainActivity.this);
                MainActivity.this.startActivityForResult(intent, 0);
            }
        });

        // 打开 flutter 业务版本号页面
        findViewById(R.id.open_flutter_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "flutter://flutter_version_page";
                Intent intent = BoostFlutterActivity.withNewEngine().url(url).params(new HashMap<>())
                        .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                        .build(MainActivity.this);
                MainActivity.this.startActivityForResult(intent, 0);
            }
        });

        /*WebView wb = findViewById(R.id.vr_webView_in_main);
        WebSettings s = wb.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);     // enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        wb.requestFocus();
        wb.loadUrl("https://www.baidu.com/");*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sRef.clear();
        sRef = null;
    }

    @Override
    public void onClick(View v) {
        Map params = new HashMap();
        params.put("test1","v_test1");
        params.put("test2","v_test2");
        //Add some params if needed.
        if (v == mOpenNative) {
            PageRouter.openPageByUrl(this, PageRouter.NATIVE_PAGE_URL , params);
        } else if (v == mOpenFlutter) {
            PageRouter.openPageByUrl(this, PageRouter.FLUTTER_PAGE_URL, params);
        } else if (v == mOpenFlutterFragment) {
            PageRouter.openPageByUrl(this, PageRouter.FLUTTER_FRAGMENT_PAGE_URL, params);
        }
    }

    public void test() {
        Object value = null;
        Map<String, Object> map = new HashMap<>();
        map.put("key", new Boolean(true));
        value = map.get("key");
        Log.d("MainMain", String.valueOf(value == Boolean.TRUE));
    }
}
