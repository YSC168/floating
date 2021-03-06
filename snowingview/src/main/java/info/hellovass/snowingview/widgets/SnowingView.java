package info.hellovass.snowingview.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import info.hellovass.snowingview.R;
import info.hellovass.snowingview.utils.DensityUtil;
import info.hellovass.snowingview.utils.RandomUtil;

/**
 * Created by hellovass on 16/9/26.
 *
 * winter is coming...
 */

public class SnowingView extends View implements SensorEventListener {

  private static final String TAG = SnowingView.class.getSimpleName();

  private final static long INVALID_TIME = -1;

  private final static int MSG_CALCULATE = 233;

  private final static int DEFAULT_SNOWFLAKE_BITMAP_VALUE = -1;

  private final static int DEFAULT_SNOWFLAKE_COUNT = 20;

  private final static int LOW_VELOCITY_Y = 80;

  private final static int HIGH_VELOCITY_Y = 2 * LOW_VELOCITY_Y;

  private final static float GRAVITATIONAL_ACCELERATION = 9.81F;

  private final static float MIN_OFFSET_X = 15.0F;

  private final static float MAX_OFFSET_X = 20.0F;

  private Context mContext;

  private int mWidth;

  private int mHeight;

  private float mSnowFlakeBitmapPivotX;

  private float mSnowFlakeBitmapPivotY;

  private Bitmap mSnowFlakeBitmap;

  private long mLastTimeMillis = INVALID_TIME;

  private Matrix mSnowFlakeMatrix;

  private Paint mSnowFlakePaint;

  private SnowFlake[] mSnowFlakes;

  private HandlerThread mCalculatePositionThread;

  private Handler mCalculateHandler;

  private boolean mIsSnowing = false;

  private SensorManager mSensorManager;

  private Sensor mAccelerometerSensor;

  private float mAccelerationXPercentage;

  public SnowingView(Context context) {
    super(context);
    init(context, null);
  }

  public SnowingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public SnowingView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public SnowingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mSensorManager.unregisterListener(this);
    notifyCalculateThreadStop();
    mCalculatePositionThread.quit();
  }

  /**
   * ??????????????????
   */
  public void startFall() {
    mIsSnowing = true;
    setVisibility(VISIBLE);
    mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
  }

  /**
   * ??????????????????
   */
  public void stopFall() {
    mIsSnowing = false;
    setVisibility(GONE);
    notifyCalculateThreadStop();
    mSensorManager.unregisterListener(this);
  }

  /**
   * ??????????????????
   *
   * @return true??????????????????
   */
  public boolean isSnowing() {
    return mIsSnowing;
  }

  /**
   * ???????????????????????????????????????
   */
  @Override public void onSensorChanged(SensorEvent event) {
    float accelerationX = event.values[SensorManager.DATA_X];
    mAccelerationXPercentage = accelerationX / GRAVITATIONAL_ACCELERATION;
  }

  @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  private void init(Context context, AttributeSet attrs) {

    TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SnowingView);
    applyAttrsFromXML(array);
    array.recycle();

    mContext = context;
    initSensorManager();
    initCalculateThread();
    initCalculateHandler();
    initSnowFlakeMatrix();
    initSnowFlakePaint();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    mWidth = getMeasuredWidth();
    mHeight = getMeasuredHeight();

    createSnowFlakes();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    for (SnowFlake snowFlake : mSnowFlakes) {
      mSnowFlakeMatrix.setTranslate(0, 0);
      mSnowFlakeMatrix.postScale(snowFlake.getScale(), snowFlake.getScale(), mSnowFlakeBitmapPivotX,
          mSnowFlakeBitmapPivotY);
      mSnowFlakeMatrix.postTranslate(snowFlake.getPositionX(), snowFlake.getPositionY());
      mSnowFlakePaint.setColor(snowFlake.getTransparency());
      canvas.drawBitmap(mSnowFlakeBitmap, mSnowFlakeMatrix, mSnowFlakePaint);
    }

    mCalculateHandler.sendEmptyMessage(MSG_CALCULATE);
  }

  /**
   * ???XML???????????????????????????????????????????????????
   *
   * @param array TypedArray
   */
  private void applyAttrsFromXML(TypedArray array) {
    mSnowFlakeBitmap = BitmapFactory.decodeResource(getResources(),
        array.getResourceId(R.styleable.SnowingView_src, DEFAULT_SNOWFLAKE_BITMAP_VALUE));
    mSnowFlakeBitmapPivotX = mSnowFlakeBitmap.getWidth() / 2.0F;
    mSnowFlakeBitmapPivotY = mSnowFlakeBitmap.getHeight() / 2.0F;
  }

  /**
   * ??????????????????
   */
  private void initSensorManager() {

    if (isInEditMode()) {
      return;
    }

    mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
  }

  /**
   * ?????????????????????
   */
  private void initCalculateThread() {
    mCalculatePositionThread = new HandlerThread("calculate_thread");
    mCalculatePositionThread.start();
  }

  /**
   * ?????????Handler
   */
  private void initCalculateHandler() {

    mCalculateHandler = new Handler(mCalculatePositionThread.getLooper()) {

      @Override public void handleMessage(Message msg) {
        super.handleMessage(msg);

        long currentTimeMillis = System.currentTimeMillis();

        if (mLastTimeMillis != INVALID_TIME) {

          float deltaTime = (currentTimeMillis - mLastTimeMillis) / 1000.0F;

          for (SnowFlake snowFlake : mSnowFlakes) {

            float x = snowFlake.getPositionX() + randomOffsetX();
            float y = snowFlake.getPositionY() + snowFlake.getVelocityY() * deltaTime;

            snowFlake.setPositionX(x);
            snowFlake.setPositionY(y);

            if (outOfRange(x, y)) {
              snowFlake.setPositionX(randomPositionX());
              snowFlake.setPositionY(resetPositionY());
            }
          }
        }

        mLastTimeMillis = currentTimeMillis;
        postInvalidate();
      }
    };
  }

  /**
   * ??????HandlerThread????????????
   */
  private void notifyCalculateThreadStop() {
    mCalculateHandler.removeMessages(MSG_CALCULATE);
  }

  /**
   * ?????????????????????
   */
  private void initSnowFlakeMatrix() {
    mSnowFlakeMatrix = new Matrix();
  }

  /**
   * ?????????????????????
   */
  private void initSnowFlakePaint() {
    mSnowFlakePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  }

  /**
   * ??????????????????
   */
  private void createSnowFlakes() {

    mSnowFlakes = new SnowFlake[DEFAULT_SNOWFLAKE_COUNT];

    for (int index = 0; index < mSnowFlakes.length; index++) {

      SnowFlake snowFlake = new SnowFlake.Builder().setPositionX(randomPositionX())
          .setPositionY(randomPositionY())
          .setVelocityY(randomVelocityY())
          .setTransparency(randomTransparency())
          .setScale(randomScale())
          .create();

      mSnowFlakes[index] = snowFlake;
    }
  }

  /**
   * ???????????????X??????
   *
   * @return ?????????X??????
   */
  private float randomPositionX() {
    return RandomUtil.nextFloat(mWidth + 2 * mSnowFlakeBitmap.getWidth())
        - mSnowFlakeBitmap.getWidth();
  }

  /**
   * ???????????????Y??????
   *
   * @return ?????????Y??????
   */
  private float randomPositionY() {
    return RandomUtil.nextFloat(mHeight + 2 * mSnowFlakeBitmap.getHeight())
        - mSnowFlakeBitmap.getHeight();
  }

  /**
   * ????????????Y????????????
   *
   * @return ?????????Y??????
   */
  private float resetPositionY() {
    return -mSnowFlakeBitmap.getHeight();
  }

  /**
   * ???????????????Y?????????????????????(2dp/s-4dp/s)
   *
   * @return y?????????????????????
   */
  private float randomVelocityY() {

    return RandomUtil.nextFloat(DensityUtil.dip2px(mContext, LOW_VELOCITY_Y),
        DensityUtil.dip2px(mContext, HIGH_VELOCITY_Y));
  }

  /**
   * ????????????????????????
   *
   * @return ??????????????????
   */
  private int randomTransparency() {
    return RandomUtil.nextInt(10, 255) << 24;
  }

  /**
   * ???????????????????????????
   *
   * @return ??????????????????
   */
  private float randomScale() {
    return RandomUtil.nextFloat(0.5F, 2.0F);
  }

  /**
   * ??????X???????????????
   *
   * @return x??????????????????
   */
  private float randomOffsetX() {
    return RandomUtil.nextFloat(DensityUtil.dip2px(mContext, MIN_OFFSET_X),
        DensityUtil.dip2px(mContext, MAX_OFFSET_X)) * -mAccelerationXPercentage;
  }

  /**
   * ????????????View?????????
   *
   * @return true??????????????????
   */
  private boolean outOfRange(float x, float y) {

    if (x < -mSnowFlakeBitmap.getWidth() || x > mWidth + mSnowFlakeBitmap.getWidth()) {
      return true;
    }

    if (y > mHeight + mSnowFlakeBitmap.getHeight()) {
      return true;
    }

    return false;
  }
}
