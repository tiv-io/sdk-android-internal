package com.tivio.bridge;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tivio.Tivio;
import com.tivio.TivioPlayerSource;
import com.tivio.TivioPlayerWrapper;

public class TivioPlayerWrapperBridge extends ReactContextBaseJavaModule {

    private static TivioPlayerWrapperBridge bridge;

    public TivioPlayerWrapperBridge(ReactApplicationContext context) {
        super(context);

        bridge = this;
    }

    @NonNull
    @Override
    public String getName() {
        return "TivioPlayerWrapper";
    }

    @ReactMethod
    public void setSource(ReadableMap data) {
        TivioPlayerWrapper playerWrapper = Tivio.instance.playerWrappers.get(data.getInt("playerWrapperId"));

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                playerWrapper.delegate.setSource(new TivioPlayerSource(data));
            }
        });
    }

    public static void reportPlaybackEnded(int playerWrapperId) {
        WritableMap params = new WritableNativeMap();
        params.putInt("playerWrapperId", playerWrapperId);

        bridge.sendEvent("reportPlaybackEnded", params);
    }

    public static void reportTimeProgress(int miliseconds, int playerWrapperId) {
        WritableMap params = new WritableNativeMap();
        params.putInt("miliseconds", miliseconds);
        params.putInt("playerWrapperId", playerWrapperId);

        bridge.sendEvent("reportTimeProgress", params);
    }

    public static void seekTo(int miliseconds, int playerWrapperId) {
        WritableMap params = new WritableNativeMap();
        params.putInt("miliseconds", miliseconds);
        params.putInt("playerWrapperId", playerWrapperId);

        bridge.sendEvent("seekTo", params);
    }

    public static void setSource(TivioPlayerSource source, int playerWrapperId) {
        WritableMap params = new WritableNativeMap();
        params.putInt("playerWrapperId", playerWrapperId);

        if(source.channelName != null) params.putString("channelName", source.channelName);
        if(source.continueFromPosition > -1) params.putInt("continueFromPosition", source.continueFromPosition);
        if(source.epgFrom > -1) params.putDouble("epgFrom", source.epgFrom);
        if(source.epgTo > -1) params.putDouble("epgTo", source.epgTo);
        if(source.mode != null) params.putString("mode", source.mode);
        if(source.type != null) params.putString("type", source.type);
        if(source.startFromPosition > -1) params.putInt("startFromPosition", source.startFromPosition);
        if(source.streamStart > -1) params.putDouble("streamStart", source.streamStart);
        if(source.uri != null) params.putString("uri", source.uri);

        bridge.sendEvent("setSource", params);
    }

    private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        this.getReactApplicationContext()
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }
}
