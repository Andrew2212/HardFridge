package com.blogspot.hqup.hardfridge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.blogspot.hqup.hardfridge.adapters.AdapterListSelectedItems;
import com.blogspot.hqup.hardfridge.constants.EnumCrudDao;
import com.blogspot.hqup.hardfridge.dal.AsyncTaskerCrud;
import com.blogspot.hqup.hardfridge.dbHandle.DbFeeder;
import com.blogspot.hqup.hardfridge.dbHandle.DbHelper;
import com.blogspot.hqup.hardfridge.entity.Item;
import com.blogspot.hqup.hardfridge.utils.JsonFileManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.blogspot.hqup.hardfridge.utils.ProgressDialoger;
import com.blogspot.hqup.hardfridge.utils.Sounder;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

/**
 * @author Andrew
 *         <p>
 *         Forms the columns of correspondence - data "from" set into views
 *         'to[]'.<br/>
 *         View 'to[]' = [ImageView, TextView, RatingBar].<br/>
 *         Sets String 'where' and String[] 'toTake[]' for the DB.query()<br/>
 *         Sets fetch from DB into the ListView
 *         </p>
 * 
 */
public class ListSelectedItemsActivity extends Activity {

	ListView listData;

	private String where;
	private String[] toTake;
	private String[] from;
	private int[] to;

	private String columnOne;
	private String columnTwo;
	private String columnThree;
	private String columnRating;
	private String showAll;

	private Context context;
	private SharedPreferences prefs;
	private AdapterListSelectedItems adapter;
	private Cursor cursor;
	private DbHelper dbHelper;
	private SQLiteDatabase db;

	private String itemName;
	private String emptyName;

	/**
	 * List of sent files paths
	 */
	private List<String> listOfPaths;

	/**
	 * Shows whether Rating takes part into the choice or not
	 */
	private boolean isRatingRun;
	/**
	 * Shows whether ListView need to refresh (false) or not (true)
	 */
	public static boolean isListViewRefreshed = false;

	private final int EDIT_ITEM = 1;
	private final int SEND_ITEM = 2;
	private final int SAVE_AS_FILE_ITEM = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_list_selected_items);

		Logger.v();

		init();
		// Get name of EmptyItem for disable its editing
		emptyName = getResources().getString(R.string.db_name_empty);
		// Forming columns of correspondence Data "from" set into views "to"
		from = new String[] { DbHelper.IMAGE, DbHelper.COLUMN_NAME,
				DbHelper.TOKEN_RATING };
		to = new int[] { R.id.ivItemOnListImg, R.id.tvItemOnListText,
				R.id.ratingItemOnList };
		getWhere();
		getToTake();

		registerForContextMenu(listData);

		listData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long id) {

				Logger.v("long id =  " + id);
				cursor.moveToPosition(position);

				// Go to ItemOneActivity with selected item ID
				showSelectedItemFullScreen(position, id);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		Logger.v();
		if (!isListViewRefreshed) {

			new AsyncTaskerListViewLoader().execute();

			isListViewRefreshed = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.v();

		if (isFinishing()) {
			Logger.v("this.finish();");

			isListViewRefreshed = false;

			if (cursor != null) {
				cursor.close();
				Logger.v();
			}
			db.close();
			dbHelper.close();
			
			// Remove template files for sending
			for (String filePath : listOfPaths) {
				 // Remove template JSON file
				 File file = new File(filePath);
				 if (file != null)
				 file.delete();
				 listOfPaths.remove(filePath);
			}
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, SAVE_AS_FILE_ITEM, 0,
				getResources().getString(R.string.menu_ctx_save_as_file));
		menu.add(0, EDIT_ITEM, 0,
				getResources().getString(R.string.menu_ctx_edit));
		menu.add(0, SEND_ITEM, 0,
				getResources().getString(R.string.menu_ctx_send));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo adapterInfo = null;
		adapterInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		long _id = adapterInfo.id;
		final String itemId = String.valueOf(_id);
		// itemId = String.valueOf(_id);
		int position = adapterInfo.position;

		Sounder.doSound(this, R.raw.beep);

		// Disable action with EmptyItem
		if (!isItemNotEmpty(position)) {
			return true;
		}

		switch (item.getItemId()) {

		case SAVE_AS_FILE_ITEM:
			Logger.v("Save as file item ID = " + itemId);
			// Save item as JSON file
			JsonFileManager.saveItemAsJsonFileIntoExternalStorageDir(context,
					getSelectedItem(itemId));

			Toaster.doToastLong(
					context,
					getResources().getString(
							R.string.toast_show_path_for_saving)
							+ " "
							+ JsonFileManager.getPathExternalStorage(context)
									.toString());
			break;

		case EDIT_ITEM:
			Logger.v("Edit item ID = " + itemId);
			// Go to ItemOneEditAcivity
			Intent intent = new Intent(this, ItemOneEditActivity.class);
			intent.putExtra(ItemOneEditActivity.VALUE, itemId);
			intent.putExtra(ItemOneEditActivity.ORDER, ItemOneEditActivity.EDIT);
			startActivity(intent);
			break;

		case SEND_ITEM:
			Logger.v("Send item ID = " + itemId);
			// Send item
			String filePath = JsonFileManager.sendItem(context, getSelectedItem(itemId));
//			Add filePath to listOfPaths
			listOfPaths.add(filePath);
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}

	// -----------Private Methods-------------------

	private void init() {

		listData = (ListView) findViewById(R.id.lvData);
		context = this;

		columnOne = getIntent().getStringExtra("Column_one");
		columnTwo = getIntent().getStringExtra("Column_two");
		columnThree = getIntent().getStringExtra("Column_three");
		columnRating = getIntent().getStringExtra("Column_rating");
		showAll = getIntent().getStringExtra("ShowAll");

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		isRatingRun = prefs.getBoolean(
				context.getResources().getString(R.string.rating_key), false);

		listOfPaths = new ArrayList<String>();
	}

	/**
	 * @param itemId
	 *            String item ID
	 * @return item with all its filled fields from DB
	 */
	private Item getSelectedItem(String itemId) {
		Logger.v();
		Item item = new Item(itemId);
		Cursor cursorItem = null;
		// Get item with all its filled fields
		AsyncTaskerCrud crud = new AsyncTaskerCrud(context, item,
				EnumCrudDao.GET_SLECTED_ITEM_CURSOR);
		crud.execute();

		try {
			cursorItem = crud.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		item.fillItemFieldsFromCursor(cursorItem);
		cursorItem.close();
		Logger.v(item.toString());
		return item;
	}

	/**
	 * @param id
	 *            selected item ID
	 *            <p>
	 *            Goes to ItemOneActivity and shows selected item there
	 *            </p>
	 */
	private void showSelectedItemFullScreen(int position, long id) {

		Logger.v("*** item name = " + itemName);
		// Disable edit EmptyItem
		if (!isItemNotEmpty(position))
			return;

		Sounder.doSound(context, R.raw.rimshot);
		Toaster.doToastShort(context, R.string.toast_show_good_choice);

		Intent intentItemFull = new Intent();
		intentItemFull.setClass(context, ItemOneActivity.class);

		// Add clicked item ID to the Intent
		String itemID = String.valueOf((long) id);
		intentItemFull.putExtra("Item_id", itemID);
		startActivity(intentItemFull);

	}

	/**
	 * @param position
	 *            - position into ListData
	 * @return true if current item is NOT EmptyItem
	 */
	private boolean isItemNotEmpty(int position) {
		boolean result = true;
		// Get item name
		itemName = cursor
				.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME));
		Logger.v("position = " + position + ":: itemName = " + itemName);
		// Disable edit EmptyItem
		if (itemName.equals(emptyName)) {
			Sounder.doSound(context, R.raw.beep);
			Toaster.doToastShort(context, R.string.toast_show_nothing);
			result = false;
		}
		return result;
	}

	/**
	 * @return String 'where' for DB query
	 */
	private String getWhere() {

		String where_1 = "";
		String where_2 = "";
		String where_3 = "";
		String where_4 = "";

		if (null != showAll) {
			// If transition from click button 'ShowAll'
			where = null;
		} else {
			// If transition from click button 'Search'
			if (DbFeeder.EMPTY_STRING.equals(columnOne)) {
				where_1 = "key_choice = ?";
			} else {
				where_1 = "key_column_one = ?";
			}
			if (DbFeeder.EMPTY_STRING.equals(columnTwo)) {
				where_2 = "key_choice = ?";
			} else {
				where_2 = "key_column_two = ?";
			}
			if (DbFeeder.EMPTY_STRING.equals(columnThree)) {
				where_3 = "key_choice = ?";
			} else {
				where_3 = "key_column_three = ?";
			}
			if (DbFeeder.EMPTY_STRING.equals(columnRating)) {
				where_4 = "key_choice = ?";
			} else {
				where_4 = "key_rating = ?";
			}
			// Check out whether Rating takes part into the set 'where'&'toTake'
			if (isRatingRun) {
				where = (where_1 + " AND " + where_2 + " AND " + where_3
						+ " AND " + where_4);
			} else {
				where = (where_1 + " AND " + where_2 + " AND " + where_3);
			}
		}

		Logger.v("where = " + where);
		return where;
	}

	/**
	 * @return String[] 'toTake' for DB query
	 */
	private String[] getToTake() {

		if (null != showAll) {
			// If transition from click button 'ShowAll'
			toTake = null;
		} else {
			// If transition from click button 'Search'
			if (isRatingRun) {

				toTake = new String[] { columnOne, columnTwo, columnThree,
						columnRating };
			} else {

				toTake = new String[] { columnOne, columnTwo, columnThree };
			}

			Logger.v("toTake.length = " + toTake.length);
			if (null != toTake) {
				for (int i = 0; i < toTake.length; i++) {
					Logger.v("toTake[i] = " + toTake[i]);
				}
			}
		}

		return toTake;
	}

	/**
	 * @author Andrew
	 *         <p>
	 *         Opens and DON'T closes DB and initializes cursor by DB.query into
	 *         new Thread (i.e. in doInBackground())</br>Creates and dismisses
	 *         ProgressDialog</br>ListWiew set 'adapter' = new
	 *         AdapterListSelectedItems(...);
	 *         </p>
	 */
	private class AsyncTaskerListViewLoader extends
			AsyncTask<Void, Void, Integer> {
		ProgressDialoger pd = new ProgressDialoger(context);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.showPD();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			cursor = getCursorShowItIsFoundInList();
			return (Integer) cursor.getCount();
		}

		@Override
		protected void onPostExecute(Integer result) {

			if (result.equals(0)) {
				((Activity) context).finish();
				Sounder.doSound(context, R.raw.beep_notify);
				Toaster.doToastShort(context, R.string.toast_show_not_found);
			} else {
				adapter = new AdapterListSelectedItems(context,
						R.layout.item_list_row, cursor, from, to);
				listData.setAdapter(adapter);
			}
			pd.dismissPD();

		}
	}

	/**
	 * @param db
	 *            current SQLiteDatabase
	 * @return cursor = db.query(DbHelper.DB_TABLE, null,
	 *         ListSelectedItemsActivity.where,
	 *         ListSelectedItemsActivity.toTake, null, null,
	 *         DbHelper.COLUMN_NAME);
	 *         <p>
	 *         DB is closed into 'Pause()' metod. If DB is closed here it'll be
	 *         CRASH
	 *         </p>
	 */
	private Cursor getCursorShowItIsFoundInList() {
		if (dbHelper != null)
			dbHelper.close();
		Logger.v();
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(DbHelper.DB_TABLE, null, where, toTake, null,
				null, DbHelper.COLUMN_NAME);
		Logger.v("cursor.getCount() = " + cursor.getCount());

		return cursor;
	}

}
