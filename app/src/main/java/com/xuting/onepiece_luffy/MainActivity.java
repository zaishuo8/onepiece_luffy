package com.xuting.onepiece_luffy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.idlefish.flutterboost.containers.BoostFlutterActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance = null;

    public static WeakReference<MainActivity> sRef;

    private TextView downloadProgress;

    public void changeDownloadProgress(String progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadProgress.setText(progress);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sRef = new WeakReference<>(this);

        setContentView(R.layout.native_page);

        instance = this;
        downloadProgress = findViewById(R.id.download_progress);

        Init.checkFlutter(this);

        // 打开 flutter activity
        findViewById(R.id.open_flutter_cus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        /*// 检查热更
        findViewById(R.id.hot_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUpdate();
            }
        });*/

        /*// 初始化引擎
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
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sRef.clear();
        sRef = null;
    }
}
