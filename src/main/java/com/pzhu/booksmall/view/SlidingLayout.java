package com.pzhu.booksmall.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


public class SlidingLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;
    private View mFront;
    private View mBack;
    private int mWidth;
    private int mHeight;
    private int mRange;
    private boolean isOpen = false;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this,10, mCallback);
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mFront;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left > 0) {
                left = 0;
            } else if (left < -mRange) {
                left = -mRange;
            }
            return left;
        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //xvel 左正右负  mFront.getLeft()为负

            if (xvel < 0) {
                open();
            } else if (!isOpen && xvel == 0 && mFront.getLeft() < -((int) (mRange / 2.0f))) {
                open();
            } else {
                close();
            }
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void close() {
        isOpen = false;
        mDragHelper.smoothSlideViewTo(mFront, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void open() {
        isOpen = true;
        mDragHelper.smoothSlideViewTo(mFront, -mRange, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    protected void onFinishInflate() {
        mBack = getChildAt(0);
        mFront = getChildAt(1);
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRange = mBack.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBack.layout(mWidth - mRange, 0, mWidth, mHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }
}
