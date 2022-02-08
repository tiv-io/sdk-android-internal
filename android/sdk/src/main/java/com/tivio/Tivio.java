package com.tivio;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactBridge;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.uimanager.RootView;
import com.tivio.bridge.TivioBridge;

import java.util.ArrayList;
import java.util.List;

public class Tivio {

    public static Tivio instance = null;

    public String secret;
    public String[] deviceCapabilities;

    ReactInstanceManager mReaactInstanceManager;

    public ArrayList<TivioPlayerWrapper> playerWrappers = new ArrayList<>();

    private Tivio(Activity activity, String secret, String[] deviceCapabilities) {
        this.secret = secret;
        this.deviceCapabilities = deviceCapabilities;

        List<ReactPackage> packages = new PackageList(activity.getApplication()).getPackages();
        packages.add(new TivioPackage());

        this.mReaactInstanceManager = ReactInstanceManager.builder()
            .setApplication(activity.getApplication())
            .setCurrentActivity(activity)
            .setBundleAssetName("index.android.bundle")
            .setJSMainModulePath("index")
            .addPackages(packages)
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build();

        ReactRootView mReactRootView = new ReactRootView(activity.getApplicationContext());
        mReactRootView.startReactApplication(this.mReaactInstanceManager, "tivio", null);

        TivioBridge.initTivio(this.secret, this.deviceCapabilities);
    }

    public static void init(Activity activity, String secret, String[] deviceCapabilities) {
        if(instance != null) {
            throw new Error("Tivio is already initialized");
        }

        instance = new Tivio(activity, secret, deviceCapabilities);
    }

    public static TivioPlayerWrapper getPlayerWrapper() {
        TivioPlayerWrapper playerWrapper = new TivioPlayerWrapper(instance.playerWrappers.size());

        instance.playerWrappers.add(playerWrapper);

        return playerWrapper;
    }

}
