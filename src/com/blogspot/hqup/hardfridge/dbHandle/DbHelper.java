package com.blogspot.hqup.hardfridge.dbHandle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.hqup.hardfridge.dal.CrudDaoBean;
import com.blogspot.hqup.hardfridge.utils.Logger;

/**
 * @author Andrew
 *         <p>
 *         Class can create and update our DB
 *         <p>
 */
public class DbHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "dbHardFridge";
	public static final String DB_TABLE = "tableHardFridge";
	private static final int DB_VERSION = 1;

	public static final String COLUMN_ID = "_id";
	public static final String IMAGE = "key_img";
	public static final String COLUMN_NAME = "key_name";
	public static final String TOKEN_ONE = "key_column_one";
	public static final String TOKEN_TWO = "key_column_two";
	public static final String TOKEN_THREE = "key_column_three";
	public static final String TOKEN_RATING = "key_rating";
	public static final String DESCRIPTION = "key_text";
	// Phone number of item
	public static final String PHONE_NUMBER = "key_number";
	// Idle field for correct choice (allows use empty tokens)
	public static final String COLUMN_CHOICE = "key_choice";
	// Spare column just in case
	public static final String IDLE = "key_idle";

	private static final String DB_CREATE = "create table " + DB_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + IMAGE
			+ " blob, " + COLUMN_NAME + " text," + TOKEN_ONE + " text,"
			+ TOKEN_TWO + " text," + TOKEN_THREE + " text," + TOKEN_RATING
			+ " float," + DESCRIPTION + " text," + PHONE_NUMBER + " text,"
			+ COLUMN_CHOICE + " text, " + IDLE + " text " + ");";

	private Context context;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		Logger.v();

		this.context = context;
	}

	/*
	 * (non-Javadoc) Called when the database is created for the first time
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
		// Create EmptyItem - it's always needed into DB for correct choice
		new CrudDaoBean(context).createEmptyItem(db);
		Logger.v();
	}

	/*
	 * (non-Javadoc)Called when the database needs to be upgraded. The
	 * implementation should use this method to drop tables, add tables, or do
	 * anything else it needs to upgrade to the new schema version
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
			onCreate(db);

		}

		Logger.v();
	}

}
