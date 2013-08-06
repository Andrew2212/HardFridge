package com.blogspot.hqup.hardfridge;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;

import com.blogspot.hqup.hardfridge.utils.LoaderManagerCallback;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

/*
 * http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
 * http://www.codeproject.com/Articles/525313/Using-Cursor-Loader-in-Android
 */

/**
 * @author Andrew </br> List of the Contacts that have a phone number and whose
 *         IN_VISIBLE_GROUP == 1
 * 
 */
public class ListContactActivity extends ListActivity {

	// The callback through which we will interact with the LoaderManager.
	private LoaderManagerCallback lmCallback;

	// The adapter that binds our data to the ListView
	private SimpleCursorAdapter mAdapter;

	// Associated columns and View
	private String[] from;// from which Columns
	private int[] to;// to this Views

	// Parameters for LoaderManagerCallback's constructor
	private Context context;
	private Uri uri;
	private String[] projection;// A list of which columns to return
	private String selection;
	private String[] selectionArgs;
	private String sortOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		/*
		 * Initialize fields
		 */
		init();

		/*
		 * Initialize the adapter. Note that we pass a 'null' Cursor as the
		 * third argument. We will pass the adapter a Cursor only when the data
		 * has finished loading for the first time (i.e. when the LoaderManager
		 * delivers the data to onLoadFinished). Also note that we have passed
		 * the '0' flag as the last argument. This prevents the adapter from
		 * registering a ContentObserver for the Cursor (the CursorLoader will
		 * do this for us!).
		 */
		mAdapter = new SimpleCursorAdapter(this, R.layout.contact_row, null,
				from, to, 0);
		// Associate the (now empty) adapter with the ListView.
		setListAdapter(mAdapter);

		/*
		 * The Activity (which implements the LoaderCallbacks<Cursor> interface)
		 * is the callback object through which we will interact with the
		 * LoaderManager. The LoaderManager uses this object to instantiate the
		 * Loader and to notify the client when data is made
		 * available/unavailable.
		 * 
		 * Caution! Column _ID is essential condition into (projection)!
		 */

		lmCallback = new LoaderManagerCallback(context, uri, projection,
				selection, selectionArgs, sortOrder, mAdapter);

		/*
		 * Initialize the Loader with ID and callback. If the loader doesn't
		 * already exist, one is created. Otherwise, the already created Loader
		 * is reused. In either case, the LoaderManager will manage the Loader
		 * across the Activity/Fragment lifecycle, will receive any new loads
		 * once they have completed, and will report this new data back to the
		 * 'Callback' object
		 */
		LoaderManager lm = getLoaderManager();
		lm.initLoader(LoaderManagerCallback.LOADER_ID_CONTACT, null, lmCallback);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {

				// Get ContactName
				Cursor cursor = mAdapter.getCursor();
				cursor.moveToPosition(position);

				String contactName = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				Toaster.doToastLong(context, contactName);

				// Intent.putExtra(ContactName, value)
				// Go to the ItemOneEditActivity
				Intent intent = new Intent(context, ItemOneEditActivity.class);
				intent.putExtra(ItemOneEditActivity.ORDER,
						ItemOneEditActivity.CONTACT);
				intent.putExtra(ItemOneEditActivity.VALUE, contactName);
				startActivity(intent);

			}
		});

	}

	/**
	 * Initialize query parameters
	 */
	private void init() {

		// Forming associated columns (String[] from) and (int[] to)
		from = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
		to = new int[] { R.id.tv_contact };

		/*
		 * Initialize parameters for LoaderCallback's constructor Caution!
		 * Column _ID is essential condition into (projection)!
		 */
		context = this;
		uri = ContactsContract.Contacts.CONTENT_URI;
		projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.LOOKUP_KEY };
		selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " == 1"
				+ " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
				+ " != 0 ";
		selectionArgs = null;
		sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

	}

}
