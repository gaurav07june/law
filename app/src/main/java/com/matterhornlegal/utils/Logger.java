/**
 * class to manage logs.
 * @author Android developer, NMG
 */
package com.matterhornlegal.utils;

import android.util.Log;

public class Logger {

	public static boolean logstatus = true;
	public static String TAG = "MatterhornLaw";

	private Logger() {
	}

	public static void warn(String s) {
		if (logstatus)
			Log.w(TAG, s);
	}

	public static void warn(String s, Throwable throwable) {
		if (logstatus)
			Log.w(TAG, s, throwable);
	}

	public static void warn(Throwable throwable) {
		if (logstatus)
			Log.w(TAG, throwable);
	}

	public static void verbose(String s) {
		if (logstatus)
			Log.v(TAG, s);
	}

	public static void debug(String s) {
		if (logstatus)
			Log.d(TAG, s);
	}

	public static void info(String s) {
		if (logstatus)
			Log.i(TAG, s);
	}

	public static void info(String s, Throwable throwable) {
		if (logstatus)
			Log.i(TAG, s, throwable);
	}

	public static void error(String s) {
		if (logstatus)
			Log.e(TAG, s);
	}

	public static void error(Throwable throwable) {
		if (logstatus)
			Log.e(TAG, null, throwable);
	}

	public static void error(String s, Throwable throwable) {
		if (logstatus)
			Log.e(TAG, s, throwable);
	}
}
