package com.blogspot.hqup.hardfridge.utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.SimpleCursorAdapter;

/*
 * http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
 * http://www.codeproject.com/Articles/525313/Using-Cursor-Loader-in-Android
 */

/**
 * @author Andrew </br> It can be used for other ListView into application but
 *         its realization will take some time
 */
public class LoaderManagerCallback implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mAdapter;
	private Context context;
	private Uri uri;
	private String[] projection;
	private String selection;
	private String[] selectionArgs;
	private String sortOrder;

	// The loader's unique id. Loader IDs are specific to the Activity or
	// Fragment in which they reside.
	public static final int LOADER_ID_CONTACT = 1;

	public LoaderManagerCallback(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder,
			SimpleCursorAdapter mAdapter) {

		this.context = context;
		this.uri = uri;
		this.projection = projection;
		this.selection = selection;
		this.selectionArgs = selectionArgs;
		this.sortOrder = sortOrder;
		this.mAdapter = mAdapter;

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Create a new CursorLoader with the following query parameters.
		Loader<Cursor> loader = new CursorLoader(context, uri, projection,
				selection, selectionArgs, sortOrder);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// A switch-case is useful when dealing with multiple Loaders/IDs

		switch (loader.getId()) {
		case LOADER_ID_CONTACT:
			/*
			 * The asynchronous load is complete and the data is now available
			 * for use. Only now can we associate the queried Cursor with the
			 * SimpleCursorAdapter.
			 */
			// ------------------Check out-----------------------
			String columnName = null;
			int columnCount = cursor.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				columnName = cursor.getColumnName(i);
				Logger.v("i = " + i + ", " + columnName);
			}

			cursor.moveToFirst();
			String contactName = null;
			while (!cursor.isAfterLast()) {
				contactName = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				Logger.v("contactName = " + contactName);
				cursor.moveToNext();
			}
			// -----------------------------------------------
			if (mAdapter != null && cursor != null) {
				mAdapter.swapCursor(cursor);
			} else {
				Logger.v("mAdapter or cursor is null");
			}
			break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		/*
		 * For whatever reason, the Loader's data is now unavailable. Remove any
		 * references to the old data by replacing it with a null Cursor.
		 */
		if (mAdapter != null) {
			mAdapter.swapCursor(null);
		} else {
			Logger.v("mAdapter is null");
		}

	}

}
