package com.blogspot.hqup.hardfridge.utils;

import com.hqup.hardfridge.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author Andrew
 *         <p>
 *         Launches Toast if that allowed into Preferences
 *         </p>
 */
public class Toaster {

	/**
	 * Y-Offset for 'Toast.setGravity (int gravity, int xOffset, int yOffset) '
	 */
	private static final int OFFSET_Y = 300;

	/**
	 * @param context
	 * @param id
	 *            - message from res/string
	 *            <p>
	 *            Show toast if that allowed; duration s = Toast.LENGTH_LONG;
	 *            </p>
	 */
	public static void doToastLong(Context context, int id) {

		boolean chbToastPref;
		int s = Toast.LENGTH_LONG;
		Toast t = Toast.makeText(context, id, s);

		// Create Preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		// read value Toast CheckBox from CheckBoxPreference
		chbToastPref = prefs.getBoolean(
				context.getResources().getString(R.string.toast_key), true);
		if (chbToastPref) {
			t.setGravity(Gravity.BOTTOM, 0, OFFSET_Y);
			t.show();
		}
		return;
	}

	/**
	 * @param context
	 * @param id
	 *            - message from res/string
	 *            <p>
	 *            Show toast if that allowed; duration s = Toast.LENGTH_SHORT;
	 *            </p>
	 */
	public static void doToastShort(Context context, int id) {

		boolean chbToastPref;
		int s = Toast.LENGTH_SHORT;
		Toast t = Toast.makeText(context, id, s);
		// Create Preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		// read value Toast CheckBox from CheckBoxPreference
		chbToastPref = prefs.getBoolean(
				context.getResources().getString(R.string.toast_key), true);
		if (chbToastPref) {
			t.setGravity(Gravity.BOTTOM, 0, OFFSET_Y);
			t.show();
		}
		return;
	}

	/**
	 * @param context
	 * @param string
	 *            - message from res/string or hardcode
	 *            <p>
	 *            Show toast if that allowed; duration s = Toast.LENGTH_SHORT;
	 *            </p>
	 */
	public static void doToastLong(Context context, String string) {

		boolean chbToastPref;
		int s = Toast.LENGTH_LONG;
		Toast t = Toast.makeText(context, string, s);
		// Create Preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		// read value Toast CheckBox from CheckBoxPreference
		chbToastPref = prefs.getBoolean(
				context.getResources().getString(R.string.toast_key), true);
		if (chbToastPref) {
			t.setGravity(Gravity.BOTTOM, 0, OFFSET_Y);
			t.show();
		}
		return;
	}

	/**
	 * @param context
	 * @param id
	 *            - message from res/string or hardcode
	 *            <p>
	 *            Show toast if that allowed; duration s = Toast.LENGTH_SHORT;
	 *            </p>
	 */
	public static void doToastShort(Context context, String string) {

		boolean chbToastPref;
		int s = Toast.LENGTH_SHORT;
		Toast t = Toast.makeText(context, string, s);
		// Create Preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		// read value Toast CheckBox from CheckBoxPreference
		chbToastPref = prefs.getBoolean(
				context.getResources().getString(R.string.toast_key), true);
		if (chbToastPref) {
			t.setGravity(Gravity.BOTTOM, 0, OFFSET_Y);
			t.show();
		}
		return;
	}
}
