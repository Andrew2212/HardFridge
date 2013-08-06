package com.blogspot.hqup.hardfridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blogspot.hqup.hardfridge.constants.EnumCrudDao;
import com.blogspot.hqup.hardfridge.dal.AsyncTaskerCrud;
import com.blogspot.hqup.hardfridge.utils.AlertDialoger;
import com.blogspot.hqup.hardfridge.utils.JsonFileManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.blogspot.hqup.hardfridge.utils.Sounder;
import com.blogspot.hqup.hardfridge.utils.LoaderSpinner;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

/**
 * @This_is_activity there we can make our choice - i.e. select some token into
 *                   some spinners and press button "Search!". We can also press
 *                   "Menu", add new data, set application preferences and etc.
 */
public class FirstChoiceActivity extends Activity implements OnClickListener {

	Button btnAction;
	Button btnPref;
	Spinner spinner1;
	Spinner spinner2;
	Spinner spinner3;
	Spinner spinner4;
	TextView tvSpinner1;
	TextView tvSpinner2;
	TextView tvSpinner3;
	TextView tvSpinner4;

	ImageView ivAnimationTween;

	private Context context;
	private SharedPreferences prefs;
	private LoaderSpinner spinnerLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_first_choice);

		init();
		context = this;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		spinnerLoader = new LoaderSpinner(context);

		// Set onClick onto the Buttons
		btnPref.setOnClickListener(this);
		btnAction.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_choice, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.v();

		spinnerLoader.setSpinner();

		// Naming titles of the Spinners_1...3
		tvSpinner1.setText(getTokenTitle(R.string.tab_column1_key,
				R.string.tab_column1_name));
		tvSpinner2.setText(getTokenTitle(R.string.tab_column2_key,
				R.string.tab_column2_name));
		tvSpinner3.setText(getTokenTitle(R.string.tab_column3_key,
				R.string.tab_column3_name));

		setColorRatingTitle();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intentMenu;
		SharedPreferences.Editor prefsEditor = prefs.edit();

		switch (item.getItemId()) {

		case R.id.menuOptions_addNewData:
			Sounder.doSound(this, R.raw.beep_notify);
			Toaster.doToastShort(context, R.string.toast_show_go_add_new_item);
			intentMenu = new Intent(context, ItemOneEditActivity.class);
			intentMenu.putExtra(ItemOneEditActivity.ORDER,
					ItemOneEditActivity.ADD);
			startActivity(intentMenu);
			break;

		case R.id.menuOptions_import_json:
			Sounder.doSound(this, R.raw.beep);
			Toaster.doToastLong(context,
					getResources().getString(R.string.toast_show_allowed_files)
							+ JsonFileManager.NAME_SUFFIX);

			intentMenu = new Intent(context, ListJsonFilesActivity.class);
			startActivity(intentMenu);
			break;

		case R.id.menuOptions_showAll:
			Sounder.doSound(context, R.raw.beep);
			intentMenu = new Intent(context, ListSelectedItemsActivity.class);
			intentMenu.putExtra("ShowAll", "showAll");
			startActivity(intentMenu);
			break;

		case R.id.menuOptions_clearDB:
			if (!isClearDbAllowed()) {
				Toaster.doToastLong(context, R.string.toast_show_it_is_disabled);
				break;
			}
			Sounder.doSound(this, R.raw.beep);
			clickMenuOptionClearDB();
			prefsEditor.putBoolean(
					getResources().getString(R.string.clear_db_key), false);
			prefsEditor.apply();
			break;

		case R.id.menuOptions_addDB:

			if (!isCreateDbAllowed()) {
				Toaster.doToastLong(context, R.string.toast_show_it_is_disabled);
				break;
			}

			Sounder.doSound(this, R.raw.beep);
			clickMenuOptionAddDB();
			// Disable new creation Default DB
			prefsEditor.putBoolean(
					getResources().getString(R.string.create_db_key), false);
			prefsEditor.apply();
			break;

		case R.id.menuOptions_hello:
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

		case R.id.btnPref:
			Sounder.doSound(this, R.raw.beep_notify);
			intent = new Intent(this, Preferences.class);
			startActivity(intent);
			break;

		case R.id.btnSearch:
			Sounder.doSound(context, R.raw.beep);

			String columnOne = spinner1.getSelectedItem().toString();
			String columnTwo = spinner2.getSelectedItem().toString();
			String columnThree = spinner3.getSelectedItem().toString();
			String columnRating = spinner4.getSelectedItem().toString();

			intent = new Intent(context, ListSelectedItemsActivity.class);
			intent.putExtra("Column_one", columnOne);
			intent.putExtra("Column_two", columnTwo);
			intent.putExtra("Column_three", columnThree);
			intent.putExtra("Column_rating", columnRating);

			startActivity(intent);
			break;

		default:
			break;
		}

	}

	// -----------Private Methods and classes----------------------

	private void init() {

		ivAnimationTween = (ImageView) findViewById(R.id.imageFridge);

		btnAction = (Button) findViewById(R.id.btnSearch);
		btnPref = (Button) findViewById(R.id.btnPref);

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);
		spinner4 = (Spinner) findViewById(R.id.spinner4);

		tvSpinner1 = (TextView) findViewById(R.id.tvSpinner1);
		tvSpinner2 = (TextView) findViewById(R.id.tvSpinner2);
		tvSpinner3 = (TextView) findViewById(R.id.tvSpinner3);
		tvSpinner4 = (TextView) findViewById(R.id.tvSpinner4);

	}

	/**
	 * @param key
	 *            R.string.tab_column_N_key
	 * @param defValue
	 *            R.string.tab_column_N_name
	 * @return name = prefs.getString(String key, String defValue)
	 *         <p>
	 *         Gets name of the Spinner_1...3 Title from the Preferences
	 *         <p>
	 */
	private String getTokenTitle(int key, int defValue) {

		String name = null;
		name = prefs.getString(getString(key), getString(defValue));
		return name;
	}

	private void clickMenuOptionClearDB() {
		@SuppressWarnings("unused")
		AlertDialoger alertDialoger = new AlertDialoger(context,
				R.string.dialog_msg_clear_db) {

			@Override
			public void doThatIfYes() {
				new AsyncTaskerCrud(context, null, EnumCrudDao.CLEAR_DB)
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

	private void clickMenuOptionAddDB() {
		new AsyncTaskerCrud(context, null, EnumCrudDao.CREATE_DEFAULT_DB)
				.execute();
	}

	/**
	 * If Rating doesn't take part in choice (see Preferences) then its Title
	 * color will be gray
	 */
	private void setColorRatingTitle() {
		boolean isRatingRun = prefs.getBoolean(context.getResources()
				.getString(R.string.rating_key), false);

		if (!isRatingRun) {
			tvSpinner4.setTextColor(getResources().getColor(R.color.dimgray));
		} else {
			tvSpinner4.setTextColor(getResources().getColor(R.color.yellow));
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
	 * @return true if action 'ClearDB' is allowed into Preferenes
	 */
	private boolean isCreateDbAllowed() {
		boolean result = prefs.getBoolean(
				getResources().getString(R.string.create_db_key), true);
		return result;
	}

}
