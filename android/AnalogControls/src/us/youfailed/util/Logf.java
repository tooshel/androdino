package us.youfailed.util;

import android.util.Log;

public class Logf {
	public static void i(String tag, String format, Object... args) {
		Log.i(tag, String.format(format, args));
	}
	public static void i(String format, Object... args) {
		i("us.youfailed", format, args);
	}
}
