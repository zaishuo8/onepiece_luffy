package com.xuting.onepiece_luffy;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.idlefish.flutterboost.*;
import com.idlefish.flutterboost.containers.BoostFlutterActivity;
import com.idlefish.flutterboost.interfaces.IContainerRecord;
import com.idlefish.flutterboost.interfaces.IFlutterViewContainer;
import com.idlefish.flutterboost.interfaces.INativeRouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterView;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.StandardMessageCodec;

public class MyApplication extends Application {

    private HotUpdate hotUpdate;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // 热更新工具初始化
        hotUpdate = new HotUpdate(getApplicationContext());
    }

    public void initEngin() {
        INativeRouter router = new INativeRouter() {

            // flutter 端的 FlutterBoost.singleton.open() 会调到这个方法里面来
            // flutter 端传过来的参数 (url, urlParams) 和这里的方法中的参数对应
            @SuppressWarnings("unchecked")
            @Override
            public void openContainer(Context context, String url, Map<String, Object> urlParams, int requestCode, Map<String, Object> exts) {
                String assembleUrl = Utils.assembleUrl(url, urlParams);

                if (url != null && url.startsWith("flutter://")) {
                    // 路由 flutter 页面
                    Intent intent = BoostFlutterActivity.withNewEngine().url(url).params(urlParams)
                            .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                            .build(context);
                    Activity activity = (Activity) context;

                    // FlutterBoost.instance() 是个单例，通过它去找 FlutterBoost 里面的工具等
                    FlutterViewContainerManager mManager =
                            (FlutterViewContainerManager) FlutterBoost.instance().containerManager();

                    // replace 的时候，关闭当前页面
                    if (exts != null && "replace".equals(exts.get("navigatorType"))) {
                        // 这里不应该直接 finish
                        // activity.finish();
                        // mManager 里面还有一些东西要处理的，通过调用 mManager 的接口来关闭页面
                        IContainerRecord record = mManager.getCurrentTopRecord();
                        // String uniqueId = record.uniqueId();
                        // IFlutterViewContainer flutterViewContainer = record.getContainer();
                        // String containerUrl = flutterViewContainer.getContainerUrl();
                        // Activity containActivity = flutterViewContainer.getContextActivity();
                        FlutterBoost.instance().platform().closeContainer(record, null, null);
                    }

                    // reset 的时候，关闭当前所有页面
                    if (exts != null && "reset".equals(exts.get("navigatorType"))) {
                        List<IContainerRecord> mRecordList = mManager.getMRecordList();
                        for (IContainerRecord iContainerRecord : mRecordList) {
                            FlutterBoost.instance().platform().closeContainer(iContainerRecord, null, null);
                        }
                    }

                    // pushAndRemove 查找路由栈，关闭对应页面
                    if (exts != null && exts.get("removePageNames") != null) {
                        try {
                            List<String> removePages = (List<String>) exts.get("removePageNames");
                            if (removePages != null) {
                                // todo 第一次尝试失败
                                /*while (true) {
                                    IContainerRecord record = mManager.getCurrentTopRecord();
                                    IFlutterViewContainer flutterViewContainer = record.getContainer();
                                    String containerUrl = flutterViewContainer.getContainerUrl();
                                    if (removePages.contains(containerUrl)) {
                                        // 跟上面一样，不能用 activity 直接 finish
                                        // Activity contextActivity = flutterViewContainer.getContextActivity();
                                        // contextActivity.finish();
                                        // String uniqueId = record.uniqueId();
                                        // todo 这行代码执行后，mManager.getCurrentTopRecord() 并没有马上改变；要等到页面 destroy 后才会 removeRecord
                                        FlutterBoost.instance().platform().closeContainer(record, null, null);
                                    } else {
                                        break;
                                    }
                                }*/

                                // todo 第二次尝试失败
                                /*// todo 这里给 mManage 添加了 getMRecordStack 公有方法，把 mRecordStack 暴露出来了；不安全！！
                                // todo 这个 mRecordStack 没有同时维护所有页面
                                // todo 当 打开一个页面的时候，mRecordStack 会先把当前页面 pop 出来，再把新页面 push 进去
                                // todo 当 关闭一个页面的时候，mRecordStack 会先把当前页面 pop 出来，再把上一个页面 push 进去，再把刚刚 pop 出来的页面 remove 掉
                                // todo 说明这个 mRecordStack 只会维护一个页面，不知道为啥这么设计
                                Stack<IContainerRecord> mRecordStack = mManager.getMRecordStack();
                                List<IContainerRecord> needRemoveRecords = new ArrayList<>();
                                for (IContainerRecord iContainerRecord : mRecordStack) {
                                    IFlutterViewContainer flutterViewContainer = iContainerRecord.getContainer();
                                    String containerUrl = flutterViewContainer.getContainerUrl();
                                    if (removePages.contains(containerUrl)) {
                                        needRemoveRecords.add(iContainerRecord);
                                    } else {
                                        break;
                                    }
                                }
                                for (IContainerRecord iContainerRecord : needRemoveRecords) {
                                    FlutterBoost.instance().platform().closeContainer(iContainerRecord, null, null);
                                }*/

                                // todo 第三次尝试
                                // 在 mManage 中自己加了一个 mRecordList 用来自己维护路由
                                List<IContainerRecord> mRecordList = mManager.getMRecordList();
                                List<IContainerRecord> needRemoveRecords = new ArrayList<>();
                                for (int i = mRecordList.size() - 1; i >= 0; i--) {
                                    IContainerRecord iContainerRecord = mRecordList.get(i);
                                    IFlutterViewContainer flutterViewContainer = iContainerRecord.getContainer();
                                    String containerUrl = flutterViewContainer.getContainerUrl();
                                    if (removePages.contains(containerUrl)) {
                                        needRemoveRecords.add(iContainerRecord);
                                    } else {
                                        break;
                                    }
                                }
                                for (IContainerRecord iContainerRecord : needRemoveRecords) {
                                    FlutterBoost.instance().platform().closeContainer(iContainerRecord, null, null);
                                }
                            }
                        } catch (ClassCastException error) {
                            Log.w("MyApplication", error.getMessage());
                        }
                    }

                    activity.startActivityForResult(intent, 0);


                    return;
                }





                if ("firstCus".equals(url)) {
                    Map<String, Object> params = new HashMap<>();
                    Intent intent = BoostFlutterActivity.withNewEngine().url(url).params(params)
                            .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                            .build(context);
                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent, 0);

                    return;
                }

                PageRouter.openPageByUrl(context, assembleUrl, urlParams);
            }

        };

        FlutterBoost.BoostLifecycleListener boostLifecycleListener = new FlutterBoost.BoostLifecycleListener(){

            @Override
            public void beforeCreateEngine() {

            }

            @Override
            public void onEngineCreated() {

                // 注册 MethodChannel，监听 flutter 侧的 getPlatformVersion 调用
                MethodChannel methodChannel = new MethodChannel(FlutterBoost.instance().engineProvider().getDartExecutor(), "flutter_native_channel");
                methodChannel.setMethodCallHandler((call, result) -> {

                    if (call.method.equals("getPlatformVersion")) {
                        result.success(Build.VERSION.RELEASE);
                    } else if (call.method.equals("boolParam")) {
                        Log.d("MyApplication", "boolParam: " + call.arguments);
                        result.success(true);

                        // 同时 channel 主动发一个通知给 flutter，带上 bool 参数
                        Map<String, Object> arguments = new HashMap<>();
                        arguments.put("pageName", "flutter://zoro_flutter_page2");
                        arguments.put("uniqueId", "11443322");
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("name", "Jhon");
                        params.put("boolParams", true);
                        arguments.put("params", params);
                        methodChannel.invokeMethod("boolParamFromNative", arguments);
                    } else {
                        result.notImplemented();
                    }

                });

                // 注册 PlatformView viewTypeId 要和 flutter 中的 viewType 对应
                FlutterBoost
                        .instance()
                        .engineProvider()
                        .getPlatformViewsController()
                        .getRegistry()
                        .registerViewFactory(
                                "plugins.test/view",
                                new TextPlatformViewFactory(StandardMessageCodec.INSTANCE)
                        );

                FlutterBoost
                        .instance()
                        .engineProvider()
                        .getPlatformViewsController()
                        .getRegistry()
                        .registerViewFactory(
                                "plugins.vr/view",
                                new VrFactory()
                        );
            }

            @Override
            public void onPluginsRegistered() {

            }

            @Override
            public void onEngineDestroy() {

            }

        };


        // AndroidManifest.xml 中必须要添加 flutterEmbedding 版本设置
        //
        //   <meta-data android:name="flutterEmbedding"
        //               android:value="2">
        //    </meta-data>
        // GeneratedPluginRegistrant 会自动生成 新的插件方式　
        //
        // 插件注册方式请使用
        // FlutterBoost.instance().engineProvider().getPlugins().add(new FlutterPlugin());
        // GeneratedPluginRegistrant.registerWith()，是在engine 创建后马上执行，放射形式调用

        Platform platform = new FlutterBoost
                .ConfigBuilder(this, router)
                .isDebug(true)
                .whenEngineStart(FlutterBoost.ConfigBuilder.IMMEDIATELY)
                .renderMode(FlutterView.RenderMode.texture)
                .lifecycleListener(boostLifecycleListener)
                .build();

        FlutterBoost.instance().init(platform, HotUpdate.libappSoPath);
    }
}
