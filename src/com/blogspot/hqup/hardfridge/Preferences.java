package com.blogspot.hqup.hardfridge;



import com.hqup.hardfridge.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		addPreferencesFromResource(R.xml.preferences);

	}

}
