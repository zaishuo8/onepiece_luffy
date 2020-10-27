package com.xuting.onepiece_luffy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

class Vr extends RelativeLayout {

    @SuppressLint("SetJavaScriptEnabled")
    public Vr(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.vr, this, true);

        TextView textView = findViewById(R.id.back);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("修改了内容");
            }
        });

        WebView wb = findViewById(R.id.vr_webView);
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


        wb.loadUrl("https://www.baidu.com/");
    }

}
