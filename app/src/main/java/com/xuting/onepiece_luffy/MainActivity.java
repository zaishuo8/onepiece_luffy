package com.xuting.onepiece_luffy;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.idlefish.flutterboost.containers.BoostFlutterActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static WeakReference<MainActivity> sRef;

    private TextView mOpenNative;
    private TextView mOpenFlutter;
    private TextView mOpenFlutterFragment;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sRef = new WeakReference<>(this);

        setContentView(R.layout.native_page);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否替换尝试 lib.so")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HotUpdate.check();
                            }
                        })
                        .setNegativeButton("否", null)
                        .create().show();
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
