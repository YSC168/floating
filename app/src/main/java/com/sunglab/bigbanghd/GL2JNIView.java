package com.sunglab.bigbanghd;



import android.content.*;
import android.graphics.*;
import android.graphics.Bitmap.*;
import android.net.*;
import android.opengl.*;
import android.os.*;
import android.provider.MediaStore.Images.*;
import android.util.*;
import android.widget.*;
import com.sunglab.bigbanghd.*;

import java.nio.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

class GL2JNIView extends GLSurfaceView {
	private static final boolean DEBUG = false;
	private static String TAG;
	private static Bitmap bitmapWip;
	static Context m_Context;

	private static class ConfigChooser implements EGLConfigChooser {
		private static int EGL_OPENGL_ES2_BIT;
		private static int[] s_configAttribs2;
		protected int mAlphaSize;
		protected int mBlueSize;
		protected int mDepthSize;
		protected int mGreenSize;
		protected int mRedSize;
		protected int mStencilSize;
		private int[] mValue;

		static {
			int r3_int = 4;
			EGL_OPENGL_ES2_BIT = r3_int;
			int[] r0_int_A = new int[9];
			r0_int_A[0] = 12324;
			r0_int_A[1] = r3_int;
			r0_int_A[2] = 12323;
			r0_int_A[3] = r3_int;
			r0_int_A[r3_int] = 12322;
			r0_int_A[5] = r3_int;
			r0_int_A[6] = 12352;
			r0_int_A[7] = EGL_OPENGL_ES2_BIT;
			r0_int_A[8] = 12344;
			s_configAttribs2 = r0_int_A;
		}

		public ConfigChooser(int r, int g, int b, int a, int depth, int stencil) {
			this.mValue = new int[1];
			this.mRedSize = r;
			this.mGreenSize = g;
			this.mBlueSize = b;
			this.mAlphaSize = a;
			this.mDepthSize = depth;
			this.mStencilSize = stencil;
		}

		private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
			if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
				return this.mValue[0];
			} else {
				return defaultValue;
			}
		}

		private void printConfig(EGL10 egl, EGLDisplay display, EGLConfig config) {
			int[] attributes = new int[] { 12320, 12321, 12322, 12323, 12324, 12325, 12326, 12327, 12328, 12329, 12330, 12331, 12332, 12333, 12334, 12335, 12336, 12337, 12338, 12339, 12340, 12343, 12342, 12341, 12345, 12346, 12347, 12348, 12349, 12350, 12351, 12352, 12354 };
			String[] names = new String[33];
			names[0] = "EGL_BUFFER_SIZE";
			names[1] = "EGL_ALPHA_SIZE";
			names[2] = "EGL_BLUE_SIZE";
			names[3] = "EGL_GREEN_SIZE";
			names[4] = "EGL_RED_SIZE";
			names[5] = "EGL_DEPTH_SIZE";
			names[6] = "EGL_STENCIL_SIZE";
			names[7] = "EGL_CONFIG_CAVEAT";
			names[8] = "EGL_CONFIG_ID";
			names[9] = "EGL_LEVEL";
			names[10] = "EGL_MAX_PBUFFER_HEIGHT";
			names[11] = "EGL_MAX_PBUFFER_PIXELS";
			names[12] = "EGL_MAX_PBUFFER_WIDTH";
			names[13] = "EGL_NATIVE_RENDERABLE";
			names[14] = "EGL_NATIVE_VISUAL_ID";
			names[15] = "EGL_NATIVE_VISUAL_TYPE";
			names[16] = "EGL_PRESERVED_RESOURCES";
			names[17] = "EGL_SAMPLES";
			names[18] = "EGL_SAMPLE_BUFFERS";
			names[19] = "EGL_SURFACE_TYPE";
			names[20] = "EGL_TRANSPARENT_TYPE";
			names[21] = "EGL_TRANSPARENT_RED_VALUE";
			names[22] = "EGL_TRANSPARENT_GREEN_VALUE";
			names[23] = "EGL_TRANSPARENT_BLUE_VALUE";
			names[24] = "EGL_BIND_TO_TEXTURE_RGB";
			names[25] = "EGL_BIND_TO_TEXTURE_RGBA";
			names[26] = "EGL_MIN_SWAP_INTERVAL";
			names[27] = "EGL_MAX_SWAP_INTERVAL";
			names[28] = "EGL_LUMINANCE_SIZE";
			names[29] = "EGL_ALPHA_MASK_SIZE";
			names[30] = "EGL_COLOR_BUFFER_TYPE";
			names[31] = "EGL_RENDERABLE_TYPE";
			names[32] = "EGL_CONFORMANT";
			int[] value = new int[1];
			int i = 0;
			while (i < attributes.length) {
				String name = names[i];
				if (egl.eglGetConfigAttrib(display, config, attributes[i], value)) {
					Object[] r8_Object_A = new Object[2];
					r8_Object_A[0] = name;
					r8_Object_A[1] = Integer.valueOf(value[0]);
					Log.w(GL2JNIView.TAG, String.format("  %s: %d\n", r8_Object_A));
					i += 1;
				}
				do {
				} while (egl.eglGetError() != 12288);
				i += 1;
			}
		}

		private void printConfigs(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
			int numConfigs = configs.length;
			Object[] r4_Object_A = new Object[1];
			r4_Object_A[0] = Integer.valueOf(numConfigs);
			Log.w(GL2JNIView.TAG, String.format("%d configurations", r4_Object_A));
			int i = 0;
			while (i < numConfigs) {
				r4_Object_A = new Object[1];
				r4_Object_A[0] = Integer.valueOf(i);
				Log.w(GL2JNIView.TAG, String.format("Configuration %d:\n", r4_Object_A));
				this.printConfig(egl, display, configs[i]);
				i += 1;
			}
		}

		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
			int[] num_config = new int[1];
			egl.eglChooseConfig(display, s_configAttribs2, null, 0, num_config);
			int numConfigs = num_config[0];
			if (numConfigs <= 0) {
				throw new IllegalArgumentException("No configs match configSpec");
			} else {
				EGLConfig[] configs = new EGLConfig[numConfigs];
				egl.eglChooseConfig(display, s_configAttribs2, configs, numConfigs, num_config);
				return this.chooseConfig(egl, display, configs);
			}
		}

		public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
			int r13_int = 0;
			while (r13_int < configs.length) {
				EGLConfig config = configs[r13_int];
				int s = this.findConfigAttrib(egl, display, config, 12326, 0);
				if (this.findConfigAttrib(egl, display, config, 12325, 0) >= this.mDepthSize) {
					if (s < this.mStencilSize) {
						r13_int = (r13_int + 1);
					} else {
						int r10_int = this.findConfigAttrib(egl, display, config, 12323, 0);
						int r8_int = this.findConfigAttrib(egl, display, config, 12322, 0);
						int r7_int = this.findConfigAttrib(egl, display, config, 12321, 0);
						if (this.findConfigAttrib(egl, display, config, 12324, 0) == this.mRedSize) {
							if (r10_int == this.mGreenSize) {
								if (r8_int == this.mBlueSize) {
									if (r7_int == this.mAlphaSize) {
										return config;
									}
								}
							}
						}
					}
				}
				r13_int = (r13_int + 1);
			}
			return null;
		}
	}

	private static class ContextFactory implements EGLContextFactory {
		private static int EGL_CONTEXT_CLIENT_VERSION;

		static {
			EGL_CONTEXT_CLIENT_VERSION = 12440;
		}

		private ContextFactory() {
		}

		/* synthetic */ ContextFactory(ContextFactory r1_ContextFactory) {
			this();
		}

		public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
			int r4_int = 2;
			Log.w(GL2JNIView.TAG, "creating OpenGL ES 2.0 context");
			GL2JNIView.checkEglError("Before eglCreateContext", egl);
			int[] attrib_list = new int[3];
			attrib_list[0] = EGL_CONTEXT_CLIENT_VERSION;
			attrib_list[1] = r4_int;
			attrib_list[r4_int] = 12344;
			GL2JNIView.checkEglError("After eglCreateContext", egl);
			return egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
		}

		public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
			egl.eglDestroyContext(display, context);
		}
	}

	public static class Renderer implements GLSurfaceView. Renderer {
		public static int DefinallyROTATION;
		public static s activity;
		public static Bitmap bmp;
		static boolean capture;
		public static int colors;
		//9private static final BroadcastReceiver mReceiver;
		public static int number;
		public static String path;
		static boolean share;
		public static float tail;
		public static float thick;
		public static int windowHEIGHT;
		public static int windowWIDTH;

		class InnerClass_1 extends BroadcastReceiver {
			InnerClass_1() {
				super();
			}

			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("android.intent.action.MEDIA_SCANNER_STARTED")) {
					Log.e("haha", "start???");
					return;
				} else if (intent.getAction().equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
					Log.e("haha", "finish???");
					return;
				} else if (intent.getAction().equals("android.intent.action.CALL")) {
					Log.e("haha", "call1???");
					return;
				} else if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
					Log.e("haha", "call2???");
					Toast.makeText(GL2JNIView.m_Context, "Saving to PhotoAlbum", 1).show();
					return;
				} else if (intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED")) {
					Log.e("haha", "call3???");
					Toast.makeText(GL2JNIView.m_Context, "Saving Error ! There is no", 1).show();
					return;
				} else {
					return;
				}
			}
		}

		static {
			capture = false;
			share = false;
			windowWIDTH = 0;
			windowHEIGHT = 0;
			DefinallyROTATION = 0;
			tail = 0.0f;
			thick = 0.0f;
			number = 0;
			colors = 0;
			//mReceiver = new InnerClass_1();
		}

		public static void SavePic() {
			capture = true;
		}

		public static Bitmap SavePixels(int x, int y, int w, int h, GL10 gl) {
			int[] b = new int[((y + h) * w)];
			int[] bt = new int[(w * h)];
			IntBuffer ib = IntBuffer.wrap(b);
			ib.position(0);
			gl.glReadPixels(x, 0, w, (y + h), 6408, 5121, ib);
			int i = 0;
			int k = 0;
			while (i < h) {
				int j = 0;
				while (j < w) {
					int pix = b[((i * w) + j)];
					bt[((((h - k) - 1) * w) + j)] = ((((-16711936) & pix) | ((pix << 16) & (-65536))) | ((pix >> 16) & 255));
					j += 1;
				}
				i += 1;
				k += 1;
			}
			return Bitmap.createBitmap(bt, w, h, Config.ARGB_8888);
		}

		public static void SharePic() {
			share = true;
		}

		public static Bitmap rotate(Bitmap b, int degrees) {
			float r3_float = 2.0f;
			if (degrees != 0) {
				if (b != null) {
					Bitmap b2;
					Matrix m = new Matrix();
					m.setRotate(((float) (degrees)), (((float) (b.getWidth())) / r3_float), (((float) (b.getHeight())) / r3_float));
					int r1_int = 0;
					int r2_int = 0;
					try {
						b2 = Bitmap.createBitmap(b, r1_int, r2_int, b.getWidth(), b.getHeight(), m, true);
						if (b != b2) {
							b.recycle();
							return b2;
						} else {
							return b;
						}
					} catch (OutOfMemoryError r0_OutOfMemoryError) {
						return b;
					}
				} else {
					return b;
				}
			} else {
				return b;
			}
		}

		public static void saveAlbum(GL10 gl) {
			bmp = SavePixels(0, 0, windowWIDTH, windowHEIGHT, gl);
			bmp = rotate(bmp, DefinallyROTATION);
			path = Media.insertImage(GL2JNIView.m_Context.getContentResolver(), bmp, "hi", null);
			IntentFilter intentFilter = new IntentFilter("android.intent.action.MEDIA_SCANNER_STARTED");
			intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
			intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
			intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
			intentFilter.addDataScheme("file");
//			GL2JNIView.m_Context.registerReceiver(mReceiver, intentFilter);
//			GL2JNIView.m_Context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(new StringBuilder("file://").append(Environment.getExternalStorageDirectory()).toString())));
		}

		public void onDrawFrame(GL10 gl) {
			if (capture) {
				saveAlbum(gl);
				capture = false;
			}
			GL2JNIView.UpdateStarEngine();
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			Log.e("omg", "onSurfaceChanged -> CALL ALL JNI FUNC");
			windowWIDTH = width;
			windowHEIGHT = height;
			GL2JNIView.TurnOnStarEngine(width, height);
			Log.e("haha", new StringBuilder("number:").append(number).append("tail:").append(tail).append("thick:").append(thick).append("colors:").append(colors).toString());
			if (number != 0) {
				GL2JNIView.JNINumber(number);
			}
			if (tail != 0.0f) {
				GL2JNIView.JNITail(tail);
			}
			if (thick != 0.0f) {
				GL2JNIView.JNIThick(thick);
			}
			if (colors != 0) {
				GL2JNIView.JNIColor(colors);
				return;
			} else {
				return;
			}
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		}

		public void saveandShare(GL10 gl) {
			bmp = SavePixels(0, 0, windowWIDTH, windowHEIGHT, gl);
			bmp = rotate(bmp, DefinallyROTATION);
			path = Media.insertImage(GL2JNIView.m_Context.getContentResolver(), bmp, "hi", null);
			IntentFilter intentFilter = new IntentFilter("android.intent.action.MEDIA_SCANNER_STARTED");
			intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
			intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
			intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
			intentFilter.addDataScheme("file");
//			GL2JNIView.m_Context.registerReceiver(mReceiver, intentFilter);
//			GL2JNIView.m_Context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(new StringBuilder("file://").append(Environment.getExternalStorageDirectory()).toString())));
//			Log.e("omg", "Save Ok");
		}
	}

	static {
		TAG = "GL2JNIView";
		System.loadLibrary("StarEngine");
	}

	public GL2JNIView(Context context) {
		super(context);
		m_Context = context;
		this.init(false, 0, 0);
	}

	public GL2JNIView(Context context, boolean translucent, int depth, int stencil) {
		super(context);
		this.init(translucent, depth, stencil);
	}

	public static native void JNIColor(int r1_int);

	public static native void JNINumber(int r1_int);

	public static native void JNITail(float r1_float);

	public static native void JNIThick(float r1_float);

	public static native void ReRunStarEngine();

	public static native void SetupTexture(int[] r1_int_A, int r2_int, int r3_int, int r4_int);

	public static native void TouchDownNumber();

	public static native void TouchMoveNumber(float r1_float, float r2_float, int r3_int, int r4_int);

	public static native void TouchUpNumber();

	public static native void TurnOffStarEngine();

	public static native void TurnOnStarEngine(int r1_int, int r2_int);

	public static native void UpdateStarEngine();

	private static void checkEglError(String prompt, EGL10 egl) {
		while (true) {
			int error = egl.eglGetError();
			if (error == 12288) {
				return;
			} else {
				Object[] r3_Object_A = new Object[2];
				r3_Object_A[0] = prompt;
				r3_Object_A[1] = Integer.valueOf(error);
				Log.e(TAG, String.format("%s: EGL error: 0x%x", r3_Object_A));
			}
		}
	}

	private void init(boolean translucent, int depth, int stencil) {
		ConfigChooser r0_ConfigChooser;
		int r5_int = 5;
		int r1_int = 8;
		if (translucent) {
			this.getHolder().setFormat((-3));
		}
		this.setEGLContextFactory(new ContextFactory(null));
		if (translucent) {
			r0_ConfigChooser = new ConfigChooser(r1_int, r1_int, r1_int, r1_int, depth, stencil);
		} else {
			r0_ConfigChooser = new ConfigChooser(r5_int, 6, r5_int, 0, depth, stencil);
		}
		this.setEGLConfigChooser(r0_ConfigChooser);
		this.setRenderer(new Renderer());
	}
}

