package com.sunglab.bigbanghd;


import android.app.*;
import android.content.*;
import android.graphics.*;
import android.hardware.*;
import android.media.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.view.WindowManager.*;
import android.widget.*;
import com.sunglab.bigbanghd.GL2JNIView.*;
import com.android.floatwindowpermission.*;
import java.io.*;
import java.net.*;
import java.util.*;

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;


public class s extends Activity implements SensorEventListener 
{

    @Override
    public void onAccuracyChanged ( Sensor p1, int p2 )
	{
	}
	MediaPlayer mediaPlayer = new MediaPlayer();

    public static final int Amazon = 1;
    public static final int Google = 0;
    public static boolean IsthatPlaying = false;
    public static final int Samsung = 3;
    @Override
    protected void onCreate ( Bundle savedInstanceState )
	{
        super.onCreate ( savedInstanceState );
	
		requestWindowFeature ( Window.FEATURE_NO_TITLE );//取消标题

		getWindow ( ).setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, 
								WindowManager.LayoutParams.FLAG_FULLSCREEN );//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
							 WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//设置长亮

		


			GL2JNIView mView = new GL2JNIView ( this );
			setContentView(mView);
			mView.setOnTouchListener ( new OnTouchListener ( ){

					@Override
					public boolean onTouch ( View p1, MotionEvent p2 )
					{
						int pointerCount = p2.getPointerCount ( );
						for ( int i = 0; i < pointerCount; i++ )
						{
							GL2JNIView.TouchMoveNumber ( p2.getX ( i ), p2.getY ( i ), i, p2.getPointerCount ( ) );
						}
						switch ( p2.getAction ( ) )
						{
							case Google /*0*/:
								GL2JNIView.TouchDownNumber ( );
								break;
							case Amazon /*1*/:
								GL2JNIView.TouchUpNumber ( );
								break;
							case 5:
								GL2JNIView.TouchDownNumber ( );
								break;
						}
						return true;
					}
				} );

		}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)  {
			finish();
			mediaPlayer.stop();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


    public void onSensorChanged ( SensorEvent event )
	{
        int r2_int = 2;
        if ( event.sensor.getType ( ) == 3 )
		{
            if ( ( (int) ( event.values [ r2_int ] ) ) > 45 )
			{
                Renderer.DefinallyROTATION = ( -90 );
                return;
			}
            else if ( ( (int) ( event.values [ r2_int ] ) ) < ( -45 ) )
			{
                Renderer.DefinallyROTATION = 90;
                return;
			}
            else
			{
                Renderer.DefinallyROTATION = 0;
                return;
			}
		}
        else
		{
            return;
		}
	}


}

