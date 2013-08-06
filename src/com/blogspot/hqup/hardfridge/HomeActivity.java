package com.blogspot.hqup.hardfridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.hqup.hardfridge.constants.Constants;
import com.blogspot.hqup.hardfridge.constants.EnumCrudDao;
import com.blogspot.hqup.hardfridge.dal.AsyncTaskerCrud;
import com.blogspot.hqup.hardfridge.utils.AlertDialoger;
import com.blogspot.hqup.hardfridge.utils.JsonFileManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.blogspot.hqup.hardfridge.utils.Sounder;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

public class HomeActivity extends Activity implements OnClickListener {

	Button btnSearch;
	Button btnAddNew;
	Button btnImport;
	Button btnPref;
	Button btnManual;
	Button btnContact;
	TextView tvHiUser;
	ImageView ivAnimationTween;

	private Context context;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);

		init();
		context = this;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Sounder.doSound(this, R.raw.beep_notify);

		// Animation is been executed
		ivAnimationTween.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.project_title_2w));
		new Thread(new AnimatorTween()).start();

		// Set onClick onto the Buttons
		btnSearch.setOnClickListener(this);
		btnAddNew.setOnClickListener(this);
		btnImport.setOnClickListener(this);
		btnContact.setOnClickListener(this);
		btnPref.setOnClickListener(this);
		btnManual.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.v();
		// Set 'userName' into textView
		tvHiUser.setText(getGreetingToUser());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intentMenu;

		switch (item.getItemId()) {

		case R.id.menuHomeOptions_addDB:

			if (!isCreateDbAllowed()) {
				Toaster.doToastLong(context, R.string.toast_show_it_is_disabled);
				break;
			}

			Sounder.doSound(this, R.raw.beep);
			clickMenuOptionAddDB();
			// Disable new creation Default DB
			SharedPreferences.Editor prefsEditor = prefs.edit();
			prefsEditor.putBoolean(
					getResources().getString(R.string.create_db_key), false);
			prefsEditor.apply();
			break;

		case R.id.menuHomeOptions_clearDB:
			if (!isClearDbAllowed()) {
				Toaster.doToastLong(context, R.string.toast_show_it_is_disabled);
				break;
			}
			Sounder.doSound(this, R.raw.beep);
			clickMenuOptionClearDB();
			break;

		case R.id.menuHomeOptions_about:
			Sounder.doSound(this, R.raw.beep);
			intentMenu = new Intent(Intent.ACTION_VIEW, getUriAbout());
			startActivity(intentMenu);
			break;

		case R.id.menuHomeOptions_hello:
			Sounder.doSound(this, R.raw.effroi_scream);
			Toaster.doToastLong(this, R.string.toast_show_hello);
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		Logger.v();

		switch (v.getId()) {

		case R.id.btnHome_Search:
			Sounder.doSound(context, R.raw.beep);
			// Go to the FirstChoiceActivity
			intent = new Intent(context, FirstChoiceActivity.class);
			startActivity(intent);
			break;

		case R.id.btnHome_AddNewData:
			Sounder.doSound(context, R.raw.beep);
			Toaster.doToastShort(context, R.string.toast_show_go_add_new_item);

			intent = new Intent(context, ItemOneEditActivity.class);
			intent.putExtra(ItemOneEditActivity.ORDER, ItemOneEditActivity.ADD);
			startActivity(intent);
			break;

		case R.id.btnHome_Import:
			Sounder.doSound(this, R.raw.beep);
			Toaster.doToastLong(context,
					getResources().getString(R.string.toast_show_allowed_files)
							+ JsonFileManager.NAME_SUFFIX);

			intent = new Intent(context, ListJsonFilesActivity.class);
			startActivity(intent);
			break;

		case R.id.btnHome_Contact:
			// =====================================
			Sounder.doSound(context, R.raw.beep);
			Toaster.doToastShort(context, R.string.toast_show_get_from_contacts);

			intent = new Intent(context, ListContactActivity.class);
			startActivity(intent);

			// =====================================
			break;

		case R.id.btnHome_Preferences:
			Sounder.doSound(context, R.raw.beep_notify);
			intent = new Intent(this, Preferences.class);
			startActivity(intent);
			break;

		case R.id.btnHome_Manual:
			Sounder.doSound(this, R.raw.beep);
			intent = new Intent(this, ManualActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	// ----------Private Methods------------------------

	private void init() {

		ivAnimationTween = (ImageView) findViewById(R.id.imageHome_Fridge);

		btnSearch = (Button) findViewById(R.id.btnHome_Search);
		btnAddNew = (Button) findViewById(R.id.btnHome_AddNewData);
		btnImport = (Button) findViewById(R.id.btnHome_Import);
		btnContact = (Button) findViewById(R.id.btnHome_Contact);
		btnPref = (Button) findViewById(R.id.btnHome_Preferences);
		btnManual = (Button) findViewById(R.id.btnHome_Manual);

		tvHiUser = (TextView) findViewById(R.id.tvHomeTop);
	}

	private void clickMenuOptionAddDB() {
		new AsyncTaskerCrud(context, null, EnumCrudDao.CREATE_DEFAULT_DB_HOME)
				.execute();
	}

	private void clickMenuOptionClearDB() {
		@SuppressWarnings("unused")
		AlertDialoger alertDialoger = new AlertDialoger(context,
				R.string.dialog_msg_clear_db) {

			@Override
			public void doThatIfYes() {
				new AsyncTaskerCrud(context, null, EnumCrudDao.CLEAR_DB_HOME)
						.execute();
				Toaster.doToastShort(context,
						R.string.toast_show_all_data_deleted);

				// Disable clear Default DB
				SharedPreferences.Editor prefsEditor = prefs.edit();
				prefsEditor.putBoolean(
						getResources().getString(R.string.clear_db_key), false);
				prefsEditor.apply();
			}
		};
	}

	private class AnimatorTween implements Runnable {
		public void run() {
			Logger.v();
			Animation animationTween = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.icon_first);
			ivAnimationTween.startAnimation(animationTween);
			animationTween.setFillAfter(true);

		}
	}

	/**
	 * @return true if action 'ClearDB' is allowed into Preferenes
	 */
	private boolean isClearDbAllowed() {
		boolean result = prefs.getBoolean(
				getResources().getString(R.string.clear_db_key), true);
		return result;
	}

	/**
	 * @return true if action 'ClearDB' is allowed into Preferences
	 */
	private boolean isCreateDbAllowed() {
		boolean result = prefs.getBoolean(
				getResources().getString(R.string.create_db_key), true);
		return result;
	}

	/**
	 * @return uriAbout - Uri .parse(uriString)</br> 'uriString' is either
	 *         'http://hqup.blogspot.com' (default) or String from Preferences
	 */
	private Uri getUriAbout() {
		Uri uriAbout = Constants.CONTENT_ABOUT_URI;
		String about = prefs.getString(
				getResources().getString(R.string.uri_about_key),
				getResources().getString(R.string.uri_about_name));
		String uriString = "http://" + about;

		if (!about.isEmpty() || 5 < about.length()) {
			uriAbout = Uri.parse(uriString);
		}
		return uriAbout;
	}

	private String getGreetingToUser() {

		String name = prefs.getString(
				getResources().getString(R.string.user_name_key),
				getResources().getString(R.string.user_name_name));

		if (name.isEmpty()) {
			name = getResources().getString(R.string.user_name_name);
		}
		String greeting = getResources().getString(R.string.txt_hi_user) + " "
				+ name + "!";

		return greeting;
	}

}
