package com.blogspot.hqup.hardfridge.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.hqup.hardfridge.R;

/**
 * @author Andrew </br>Loads Contact information into the ItemOneEditActivity on
 *         request from ListContactActivity
 */
public class LoaderContactInfo {

	private Context context;
	private Cursor cursor;
	private Uri uri;

	// These are the Contacts rows that we will retrieve.
	private String[] contactsSummaryProjection;
	private String[] dataNames;

	// A filter declaring which rows to return. Passing null will return all
	// rows for the given URI.
	private String selection;// Same as 'String where'
	private String[] selectionArgs;// Same as 'String[] toTake'

	private String contactName;

	private CursorLoader cursorLoader;

	private List<String> listDescription = new ArrayList<String>();

	public LoaderContactInfo(Context context, String identifier) {

		this.context = context;
		contactName = identifier;

		dataNames = context.getResources().getStringArray(
				R.array.contact_data_names);

	}

	public String getContactName() {
		String result = null;

		cursor = getPhoneCursor();
		cursor.moveToFirst();

		int columnIndex = cursor
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		result = cursor.getString(columnIndex);

		listDescription.add(dataNames[1] + ": " + result + "\n");

		cursor.close();
		return result;
	}

	public String getPhoneNum() {
		String result = null;

		cursor = getPhoneCursor();
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			int columnIndex = cursor
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			result = cursor.getString(columnIndex);
			listDescription.add(dataNames[2] + ": " + result + "\n");
			cursor.moveToNext();
		}

		cursor.close();
		return result;
	}

	public String getDescription() {
		String result = "";
		getRestDescriptionList();

		for (String string : listDescription) {
			result += string;
		}

		return result;
	}

	private List<String> getRestDescriptionList() {
		String result = "";
		cursor = getEmailCursor();
		Logger.i("" + cursor.getCount());

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int columnIndex = cursor
					.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
			result = cursor.getString(columnIndex);
			listDescription.add(dataNames[3] + ": " + result + "\n");
			cursor.moveToNext();
		}
		cursor.close();
		return listDescription;
	}

	private Cursor getPhoneCursor() {

		uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		// These are the Contacts rows that we will retrieve.
		contactsSummaryProjection = new String[] {
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		selection = "(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " = ? )";
		selectionArgs = new String[] { contactName };

		cursorLoader = new CursorLoader(context, uri,
				contactsSummaryProjection, selection, selectionArgs, null);
		cursor = cursorLoader.loadInBackground();
		return cursor;
	}

	private Cursor getEmailCursor() {

		uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		// These are the Contacts rows that we will retrieve.
		contactsSummaryProjection = new String[] {
				ContactsContract.CommonDataKinds.Email._ID,
				ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY,
				ContactsContract.CommonDataKinds.Email.ADDRESS };

		selection = "("
				+ ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY
				+ " = ? )";
		selectionArgs = new String[] { contactName };

		cursorLoader = new CursorLoader(context, uri,
				contactsSummaryProjection, selection, selectionArgs, null);
		cursor = cursorLoader.loadInBackground();
		return cursor;
	}

}
