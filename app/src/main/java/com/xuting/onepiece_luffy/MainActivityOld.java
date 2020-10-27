package com.xuting.onepiece_luffy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.flutter.embedding.android.FlutterActivity;

public class MainActivityOld extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView launchFlutterActivity = findViewById(R.id.launch_flutter_activity);
        launchFlutterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FlutterActivity.createDefaultIntent(MainActivityOld.this));
            }
        });
    }
}