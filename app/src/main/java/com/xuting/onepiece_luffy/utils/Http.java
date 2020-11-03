package com.xuting.onepiece_luffy.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {

    public static final String deviceId = "123456";
    public static final float appId = 1;

    public static final String host = "http://192.168.2.147:7001";

    public static final String latestFlutterVersionUrl = host + "/version/latest_flutter_version";
    public static final String checkUpdate = host + "/version/check";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static OkHttpClient client = new OkHttpClient();
    /**
     * 发 post 请求
     * */
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
