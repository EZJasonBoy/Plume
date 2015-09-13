package sausure.io.personallibrary.Utils;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "DEBUGING";
    public static final boolean IS_DEBUG = true;

    public static void v(String text) {
        if (IS_DEBUG) {
            if (StringUtil.isBlank(text)) {
                Log.v(TAG, "null");
            } else {
                Log.v(TAG, text);
            }
        }
    }

    public static void d(String text) {
        if (IS_DEBUG) {
            if (StringUtil.isBlank(text)) {
                Log.d(TAG, "null");
            } else {
                Log.d(TAG, text);
            }
        }
    }

    public static void i(String text) {
        if (IS_DEBUG) {
            if (StringUtil.isBlank(text)) {
                Log.i(TAG, "null");
            } else {
                Log.i(TAG, text);
            }
        }
    }

    public static void w(String text) {
        if (IS_DEBUG) {
            if (StringUtil.isBlank(text)) {
                Log.w(TAG, "null");
            } else {
                Log.w(TAG, text);
            }
        }
    }

    public static void w(String text, Throwable t) {
        if (IS_DEBUG) {
            if (StringUtil.isBlank(text)) {
                Log.w(TAG, "null", t);
            } else {
                Log.w(TAG, text, t);
            }
        }
    }

    public static void w(Throwable t) {
        if (IS_DEBUG) {
            Log.w(TAG, t);
        }
    }

    public static void e(String text) {
        if (IS_DEBUG) {
            if (StringUtil.isBlank(text)) {
                Log.e(TAG, "null");
            } else {
                Log.e(TAG, text);
            }
        }
    }
}
