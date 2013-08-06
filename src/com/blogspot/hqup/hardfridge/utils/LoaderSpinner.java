package com.blogspot.hqup.hardfridge.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blogspot.hqup.hardfridge.dbHandle.DbHelper;
import com.hqup.hardfridge.R;

/**
 * @author Andrew
 *         <p>
 *         Helper for FirstChoiceActivity</br> Writes from DB all values of
 *         TOKEN columns </br> (Token_1, Token_2, Token_3, Token_Rating)</br>
 *         and set they into the FirstChoiceActivity Spinners
 *         </p>
 *         <p>
 *         As well as it sets 'Greeting' into textView
 *         </p>
 */
public class LoaderSpinner {

	private Context context;

	Spinner spinner1;
	Spinner spinner2;
	Spinner spinner3;
	Spinner spinner4;
	TextView tvGreeting;
	ImageView ivFlyingMan;

	private SharedPreferences prefs;

	public LoaderSpinner(Context context) {
		this.context = context;
		init();

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	private void init() {

		spinner1 = (Spinner) ((Activity) context).findViewById(R.id.spinner1);
		spinner2 = (Spinner) ((Activity) context).findViewById(R.id.spinner2);
		spinner3 = (Spinner) ((Activity) context).findViewById(R.id.spinner3);
		spinner4 = (Spinner) ((Activity) context).findViewById(R.id.spinner4);

		tvGreeting = (TextView) ((Activity) context)
				.findViewById(R.id.tvGreetingFirstChoice);
		ivFlyingMan = (ImageView) ((Activity) context)
				.findViewById(R.id.ivFlyingMan);
	}

	/**
	 * Set List of String and List of float from DB columns into ALL Spinners
	 */
	public void setSpinner() {

		Logger.v();

		DbHelper dbHelper = new DbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// Set list of values into Spinners 1-3 (string)
		setListOfTokensToSpinner(DbHelper.TOKEN_ONE, spinner1, db);
		setListOfTokensToSpinner(DbHelper.TOKEN_TWO, spinner2, db);
		setListOfTokensToSpinner(DbHelper.TOKEN_THREE, spinner3, db);

		// Set list of values into Spinner 4 (rating)
		List<Float> ratingArrayList = new ArrayList<Float>();
		String[] columns = new String[] { DbHelper.TOKEN_RATING };
		Cursor cursorSpinner = db.query(true, DbHelper.DB_TABLE, columns, null,
				null, null, null, DbHelper.TOKEN_RATING, null);

		cursorSpinner.moveToFirst();

		while (!cursorSpinner.isAfterLast()) {
			ratingArrayList.add(cursorSpinner.getFloat(cursorSpinner
					.getColumnIndex(DbHelper.TOKEN_RATING)));
			cursorSpinner.moveToNext();
		}
		// Create adapter and build our list
		ArrayAdapter<Float> ratingArrayAdapter = new ArrayAdapter<Float>(
				context, R.layout.spinner_list, R.id.tv_spinner,
				ratingArrayList);
		spinner4.setAdapter(ratingArrayAdapter);

		cursorSpinner.close();
		db.close();

		setGreetingToUser();
		Logger.v();

	}

	/**
	 * @param token
	 *            is name of DB column i.e. which column to return by cursor
	 * @param spinner
	 *            Spinner that will be filled by List of value
	 * @param db
	 *            is current DB
	 *            <p>
	 *            Sets only String i.e. only into Spinner_1...3 (not into
	 *            Spinner_4 "Rating"/float/)
	 *            <p>
	 */
	private void setListOfTokensToSpinner(String token, Spinner spinner,
			SQLiteDatabase db) {
		Logger.v();
		// Get Cursor
		Cursor cursorSpinner;
		// A list of which columns to return by cursor
		String[] columns = new String[] { token };
		cursorSpinner = db.query(true, DbHelper.DB_TABLE, columns, null, null,
				null, null, token, null);
		// Create Adapter for value that the cursor retrieves
		ArrayAdapter<String> adapter = getAdapter(token, cursorSpinner);
		spinner.setAdapter(adapter);

		cursorSpinner.close();
	}

	/**
	 * @param token
	 *            is name of DB column
	 * @param cursor
	 *            is current Cursor
	 * @return ArrayAdapter/String/
	 *         <p>
	 *         Only for Spinner_1...3 (not for Spinner_4 "Rating"/float/)
	 */
	private ArrayAdapter<String> getAdapter(String token, Cursor cursor) {
		Logger.v();
		List<String> mArrayList = new ArrayList<String>();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			mArrayList.add(cursor.getString(cursor.getColumnIndex(token)));
			cursor.moveToNext();
		}

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
				R.layout.spinner_list, R.id.tv_spinner, mArrayList);
		return arrayAdapter;
	}

	// --------------Set 'Greeting' into the textView---------------
	/**
	 * Set String 'greeting' into textView
	 */
	private void setGreetingToUser() {

		String greeting = null;
		String userName = getUserName();

		if (isDbEmpty()) {
			greeting = userName
					+ "!\n"
					+ ((Activity) context).getResources().getString(
							R.string.txt_db_is_empty);
			tvGreeting.setTextColor(((Activity) context).getResources()
					.getColor(R.color.caution));
			ivFlyingMan.setVisibility(View.GONE);
		} else {
			greeting = userName
					+ "! "
					+ ((Activity) context).getResources().getString(
							R.string.txt_make_your_choice);

			tvGreeting.setTextColor(((Activity) context).getResources()
					.getColor(R.color.green));
			ivFlyingMan.setVisibility(View.VISIBLE);
		}

		tvGreeting.setText(greeting);
	}

	/**
	 * @return name = getResources().getString(R.string.user_name_name)</br> Get
	 *         'userName' from Preferences
	 */
	private String getUserName() {
		String name = prefs.getString(((Activity) context).getResources()
				.getString(R.string.user_name_key), ((Activity) context)
				.getResources().getString(R.string.user_name_name));

		if (name.isEmpty()) {
			name = ((Activity) context).getResources().getString(
					R.string.user_name_name);
		}
		return name;
	}

	/**
	 * @return true if DB is empty
	 */
	private boolean isDbEmpty() {

		boolean result = true;
		int numberOfItems = spinner1.getCount() + spinner2.getCount()
				+ spinner3.getCount();
		Logger.v("Spinners get count = " + numberOfItems);

		if (3 < numberOfItems)
			result = false;
		return result;
	}

}
