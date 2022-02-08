package com.tivio.bridge;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tivio.Tivio;

import java.util.Map;
import java.util.HashMap;

public class TivioBridge extends ReactContextBaseJavaModule{

    private static TivioBridge bridge;

    public TivioBridge(ReactApplicationContext context) {
        super(context);

        Log.d("TivioBridge", "Initialization of TivioBridge");

        bridge = this;
    }

    @NonNull
    @Override
    public String getName() {
        return "TivioManager";
    }

    @ReactMethod
    public void initTivio() {
        TivioBridge.initTivio(Tivio.instance.secret, Tivio.instance.deviceCapabilities);
    }

    public static void initTivio(String secret, String[] deviceCapabilities) {
        WritableMap initParams = Arguments.createMap();
        initParams.putString("secret", secret);
        initParams.putArray("deviceCapabilities", Arguments.fromJavaArgs(deviceCapabilities));

        if(bridge != null)
            bridge.sendEvent("init", initParams);
    }

    private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        this.getReactApplicationContext()
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
    }
}
