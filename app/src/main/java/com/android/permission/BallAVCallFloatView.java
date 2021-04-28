package com.android.permission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.floatwindowpermission.R;

public class BallAVCallFloatView extends FrameLayout {
    private boolean isShowing = false;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    public BallAVCallFloatView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View floatView = inflater.inflate(R.layout.float_view_ball, null);
        addView(floatView);
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    public void setIsShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }

}
