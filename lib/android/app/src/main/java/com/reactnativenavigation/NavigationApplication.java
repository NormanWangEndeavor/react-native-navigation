package com.reactnativenavigation;

import android.app.Application;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.soloader.OpenSourceMergedSoMapping;
import com.facebook.soloader.SoLoader;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator;

import java.util.Collections;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public abstract class NavigationApplication extends Application implements ReactApplication {

	public static NavigationApplication instance;

	private final Map<String, ExternalComponentCreator> externalComponents = new HashMap<>();
	private ReactGateway reactGateway;

    public NavigationApplication() {
        this(Collections.emptyMap());
    }

    public NavigationApplication(Map<RNNToggles, Boolean> featureToggleOverrides) {
        instance = this;
        RNNFeatureToggles.init(featureToggleOverrides);
    }

	@Override
	public void onCreate() {
		super.onCreate();
        try {
            SoLoader.init(this, OpenSourceMergedSoMapping.INSTANCE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DefaultNewArchitectureEntryPoint.load();

        reactGateway = createReactGateway();
	}

    /**
     * Subclasses of NavigationApplication may override this method to create the singleton instance
     * of {@link ReactGateway}. For example, subclasses may wish to provide a custom {@link ReactNativeHost}
     * with the ReactGateway. This method will be called exactly once, in the application's {@link #onCreate()} method.
     *
     * Custom {@link ReactNativeHost}s must be sure to include {@link com.reactnativenavigation.react.NavigationPackage}
     *
     * @return a singleton {@link ReactGateway}
     */
	protected ReactGateway createReactGateway() {
	    return new ReactGateway(getReactHost());
    }
    
	public ReactGateway getReactGateway() {
		return reactGateway;
	}

    /**
     * Generally no need to override this; override for custom permission handling.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    /**
     * Register a native View which can be displayed using the given {@code name}
     * @param name Unique name used to register the native view
     * @param creator Used to create the view at runtime
     */
    @SuppressWarnings("unused")
    public void registerExternalComponent(String name, ExternalComponentCreator creator) {
        if (externalComponents.containsKey(name)) {
            throw new RuntimeException("A component has already been registered with this name: " + name);
        }
        externalComponents.put(name, creator);
    }

    public final Map<String, ExternalComponentCreator> getExternalComponents() {
        return externalComponents;
    }
}
