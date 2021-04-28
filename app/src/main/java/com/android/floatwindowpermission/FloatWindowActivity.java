/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.android.floatwindowpermission;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.permission.*;

import android.view.*;
import android.content.*;
/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

public class FloatWindowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		DuckFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
		
		findViewById(R.id.black).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(FloatWindowActivity.this, Black.class); 

					startActivity(intent); 
				}
			});
		findViewById(R.id.white).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(FloatWindowActivity.this, White.class); 
         
					startActivity(intent); 
				}
			});
		findViewById(R.id.duck).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DuckFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.duckc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DuckFloatWindowManager.getInstance().dismissWindow();
				}
			});
		
		findViewById(R.id.jyduck).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					JyduckFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.jyduckc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					JyduckFloatWindowManager.getInstance().dismissWindow();
				}
			});
		
		findViewById(R.id.mood).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MoodFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.moodc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MoodFloatWindowManager.getInstance().dismissWindow();
				}
			});
		findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BearLikeFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.likec).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BearLikeFloatWindowManager.getInstance().dismissWindow();
				}
			});
		
		findViewById(R.id.snow).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SnowFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.snowc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SnowFloatWindowManager.getInstance().dismissWindow();
				}
			});
		findViewById(R.id.ball).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BallFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.ballc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BallFloatWindowManager.getInstance().dismissWindow();
				}
			});
		findViewById(R.id.balloon).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BalloonFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.balloonc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BalloonFloatWindowManager.getInstance().dismissWindow();
				}
			});
        findViewById(R.id.picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
            }
        });

        findViewById(R.id.picturec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.getInstance().dismissWindow();
            }
        });
		findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					HeartFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.heartc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					HeartFloatWindowManager.getInstance().dismissWindow();
				}
			});
		findViewById(R.id.bear).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BearFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
				}
			});

        findViewById(R.id.bearc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BearFloatWindowManager.getInstance().dismissWindow();
				}
			});
			
		findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DuckFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
					JyduckFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
					BearFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
					HeartFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
					FloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
					BalloonFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
							BallFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
							SnowFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
							MoodFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
							BearLikeFloatWindowManager.getInstance().applyOrShowFloatWindow(FloatWindowActivity.this);
					
					
					
					
					
					
				}
			});
			
		findViewById(R.id.remove_id).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SnowFloatWindowManager.getInstance().dismissWindow();
					BallFloatWindowManager.getInstance().dismissWindow();
					BalloonFloatWindowManager.getInstance().dismissWindow();
					HeartFloatWindowManager.getInstance().dismissWindow();
					BearFloatWindowManager.getInstance().dismissWindow();
					BearLikeFloatWindowManager.getInstance().dismissWindow();
					MoodFloatWindowManager.getInstance().dismissWindow();
					DuckFloatWindowManager.getInstance().dismissWindow();
					JyduckFloatWindowManager.getInstance().dismissWindow();
					FloatWindowManager.getInstance().dismissWindow();
				}
			});
    }

}
