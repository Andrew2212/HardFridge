package com.blogspot.hqup.hardfridge.dal;

import static com.blogspot.hqup.hardfridge.dbHandle.DbFeeder.counter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blogspot.hqup.hardfridge.dbHandle.DbFeeder;
import com.blogspot.hqup.hardfridge.dbHandle.DbFields;
import com.blogspot.hqup.hardfridge.dbHandle.DbHelper;
import com.blogspot.hqup.hardfridge.entity.Item;
import com.blogspot.hqup.hardfridge.utils.Logger;

public class CrudDaoBean implements ICrudDao {

	private Context context;
	private String where;
	private String[] toTake;

	private Cursor cursor;
	private Item item;
	
	/**
	 * Disables action with EmptyItem
	 */
	public static long emptyItemID;

	public CrudDaoBean(Context context) {
		this.context = context;
		Logger.v();
	}

	/**
	 * @param db
	 *            is current SQLiteDatabase
	 */
	public void createEmptyItem(SQLiteDatabase db) {
		Logger.v();
		ContentValues cv = new ContentValues();
		DbFeeder.cvPutEmptyItem(context, cv);
		emptyItemID = db.insert(DbHelper.DB_TABLE, null, cv);
	}

	/**
	 * @param db
	 *            current SQLiteDatabase
	 *            <p>
	 *            Clears DB and creates EmptyItem into it
	 */
	public void clearDB(SQLiteDatabase db) {
		Logger.v();
		db.delete(DbHelper.DB_TABLE, null, null);
		createEmptyItem(db);
	}

	public void createDefaultDB(SQLiteDatabase db) {
		Logger.v();
		ContentValues cv = new ContentValues();
		DbFields.initAllFields(context);

		for (counter = 1; counter < DbFeeder.countMax; counter++) {
			DbFeeder.cvPutDefaultDB(context, cv);
			db.insert(DbHelper.DB_TABLE, null, cv);
		}
	}

	public void deleteItem(SQLiteDatabase db, long id) {
		Logger.v();

		String itemID = String.valueOf(id);
		where = " _id = ? ";
		toTake = new String[] { itemID };

		int result = db.delete(DbHelper.DB_TABLE, where, toTake);
		Logger.v("result = " + result);
	}

	public Cursor getSelectedItemCursor(SQLiteDatabase db, long id) {
		Logger.v();

		String itemID = String.valueOf(id);
		where = " _id = ? ";
		toTake = new String[] { itemID };

		cursor = db.query(DbHelper.DB_TABLE, null, where, toTake, null, null,
				null);
		return cursor;
	}

	public void updateItem(SQLiteDatabase db, Item item) {
		Logger.v(item.toString());
		this.item = item;
		String itemID = String.valueOf(item.get_id());
		where = " _id = ? ";
		toTake = new String[] { itemID };

		ContentValues cv = new ContentValues();
		cv.put(DbHelper.COLUMN_ID, item.get_id());
		putValues(cv);

		int result = db.update(DbHelper.DB_TABLE, cv, where, toTake);
		Logger.v("result = " + result);

	}

	public long insertItem(SQLiteDatabase db, Item item) {
		Logger.v(item.toString());
		this.item = item;
		String itemID = String.valueOf(item.get_id());
		where = " _id = ? ";
		toTake = new String[] { itemID };

		ContentValues cv = new ContentValues();
		putValues(cv);

		long resultID = db.insert(DbHelper.DB_TABLE, null, cv);
		Logger.v("resultID = " + resultID);

		return resultID;
	}

	// -------------------Private Methods-------------------------------

	private void putValues(ContentValues cv) {

		cv.put(DbHelper.IMAGE, item.getImg());
		cv.put(DbHelper.COLUMN_NAME, item.getName());
		cv.put(DbHelper.TOKEN_ONE, item.getToken_1());
		cv.put(DbHelper.TOKEN_TWO, item.getToken_2());
		cv.put(DbHelper.TOKEN_THREE, item.getToken_3());
		cv.put(DbHelper.TOKEN_RATING, item.getRating());
		cv.put(DbHelper.DESCRIPTION, item.getDescription());
		cv.put(DbHelper.PHONE_NUMBER, item.getPhoneNum());
		cv.put(DbHelper.COLUMN_CHOICE, DbFeeder.EMPTY_STRING);
	}

}
