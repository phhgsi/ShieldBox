package com.imuxuan.floatingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FloatingMagnetView extends FrameLayout {

    public static final int MARGIN_EDGE = 13;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;
    private MagnetViewListener mMagnetViewListener;
    private long mLastTouchDownTime;
    protected int mScreenWidth;
    private int mScreenHeight;
    private int mStatusBarHeight;
    private boolean mIsNearestEdge = true;
    private float mTouchSlop;

    public FloatingMagnetView(Context context) {
        this(context, null);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = getContext().getResources().getDimensionPixelSize(resourceId);
        }
    }

    public void setMagnetViewListener(MagnetViewListener magnetViewListener) {
        this.mMagnetViewListener = magnetViewListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeOriginalTouchParams(event);
                updateSize();
                mLastTouchDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition(event);
                break;
            case MotionEvent.ACTION_UP:
                clearPortraitY();
                if (isOnClickEvent()) {
                    dealClickEvent();
                }
                if (mIsNearestEdge) {
                    moveToEdge();
                }
                break;
        }
        return true;
    }

    protected void dealClickEvent() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onClick(this);
        }
    }

    protected boolean isOnClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime < 200
                && Math.abs(getX() - mOriginalX) < mTouchSlop
                && Math.abs(getY() - mOriginalY) < mTouchSlop;
    }

    private void updateViewPosition(MotionEvent event) {
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight;
        }
        if (desY > mScreenHeight - getHeight()) {
            desY = mScreenHeight - getHeight();
        }
        setY(desY);
    }

    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = getX();
        mOriginalY = getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
    }

    protected void updateSize() {
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public void moveToEdge() {
        float rawX = getX();
        float endX = (rawX + getWidth() / 2.0f < mScreenWidth / 2.0f) ? MARGIN_EDGE : (mScreenWidth - getWidth() - MARGIN_EDGE);
        animate().setDuration(200).x(endX).start();
    }

    private void clearPortraitY() {
        // bounds check
        if (getY() < mStatusBarHeight) {
            setY(mStatusBarHeight);
        } else if (getY() > mScreenHeight - getHeight()) {
            setY(mScreenHeight - getHeight());
        }
    }
}
