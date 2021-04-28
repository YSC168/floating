package com.android.permission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.floatwindowpermission.R;
import android.widget.*;
import android.os.*;

public class BalloonAVCallFloatView extends FrameLayout {
	LinearLayout mFloatLayout;
	private BalloonRelativeLayout mBalloonRelativeLayout;

	private int TIME = 400;//这里默认每隔100毫秒添加一个气泡
    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                mHandler.postDelayed(this, TIME);

                mBalloonRelativeLayout.addBalloon();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
	
    private boolean isShowing = false;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    public BalloonAVCallFloatView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View floatView = inflater.inflate(R.layout.float_view_balloon, null);
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_view_balloon, null);
		mBalloonRelativeLayout = (BalloonRelativeLayout) floatView.findViewById(R.id.balloonRelativeLayout);
		mHandler.postDelayed(runnable, TIME);
        addView(floatView);
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    public void setIsShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }

}
