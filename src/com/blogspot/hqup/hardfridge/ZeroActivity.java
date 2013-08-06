package com.blogspot.hqup.hardfridge;

import com.hqup.hardfridge.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

/**
 * @author Andrew
 *         <p>
 *         Chooses which of Activities will start</br> either
 *         SplashScreenActivity or FirstChoiceActivity
 *         </p>
 */
public class ZeroActivity extends Activity {

	private boolean isSplashScreenAllowed;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// Get value from CheckBoxPreference 'SplashScreen'
		isSplashScreenAllowed = prefs.getBoolean(
				getString(R.string.splash_screen_key), true);

		if (isSplashScreenAllowed) {

			Intent intent = new Intent(this, SplashScreenActivity.class);
			startActivity(intent);
			finish();
		} else {

			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
