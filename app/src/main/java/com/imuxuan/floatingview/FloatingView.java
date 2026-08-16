package com.imuxuan.floatingview;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FloatingView {

    private static volatile FloatingView sInstance;
    private FloatingMagnetView mFloatingMagnetView;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    private FloatingView() {
    }

    public static FloatingView get() {
        if (sInstance == null) {
            synchronized (FloatingView.class) {
                if (sInstance == null) {
                    sInstance = new FloatingView();
                }
            }
        }
        return sInstance;
    }

    public FloatingView customView(FloatingMagnetView view) {
        mFloatingMagnetView = view;
        return this;
    }

    public FloatingMagnetView getView() {
        return mFloatingMagnetView;
    }

    public FloatingView attach(Activity activity) {
        if (mFloatingMagnetView == null || activity == null || activity.isFinishing()) {
            return this;
        }
        attach(getActivityRoot(activity));
        return this;
    }

    public FloatingView attach(FrameLayout container) {
        if (container == null || mFloatingMagnetView == null) {
            return this;
        }
        if (mFloatingMagnetView.getParent() == container) {
            return this;
        }
        detach(mFloatingMagnetView);
        container.addView(mFloatingMagnetView);
        return this;
    }

    public FloatingView detach(Activity activity) {
        if (mFloatingMagnetView == null || activity == null) {
            return this;
        }
        detach(getActivityRoot(activity));
        return this;
    }

    public FloatingView detach(FrameLayout container) {
        if (container == null || mFloatingMagnetView == null) {
            return this;
        }
        if (mFloatingMagnetView.getParent() == container) {
            container.removeView(mFloatingMagnetView);
        }
        return this;
    }

    private void detach(View view) {
        if (view != null && view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            return null;
        }
    }
}
