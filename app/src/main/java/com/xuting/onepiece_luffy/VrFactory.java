package com.xuting.onepiece_luffy;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

class VrFactory extends PlatformViewFactory {

    public VrFactory() {
        super(StandardMessageCodec.INSTANCE);
    }


    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        return new VrPlatViewView(context);
    }

    private static class VrPlatViewView implements PlatformView {

//        private final Context context;
        private TextView textView;
        private Vr vr;

        VrPlatViewView(Context context) {
            // this.context = context;

            /*textView = new TextView(context);

            textView.setText("1");
            textView.setTextSize(100f);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView.setText("2");
                }
            });*/

            vr = new Vr(context);
        }

        @Override
        public View getView() {
            // return new Vr(this.context);

            // return textView;

            return vr;
        }

        @Override
        public void dispose() {

        }
    }
}
