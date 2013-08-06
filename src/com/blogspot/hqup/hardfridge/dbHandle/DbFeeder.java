package com.blogspot.hqup.hardfridge.dbHandle;

import com.blogspot.hqup.hardfridge.utils.Logger;

import android.content.ContentValues;
import android.content.Context;

/**
 * @author Andrew
 *         <p>
 *         Puts the data to DefaultDB (for DefaultItems and EmptyItem)
 *         <p>
 */
public class DbFeeder {

	/**
	 * Counts default items to create them into cycle by CrudDaoBean.java
	 */
	public static int counter = 1;
	public static int countMax = 16;
	public static final float RATING_DEFAULT = (float) 0.0;
	public static final String PHONE_DEFAULT = "";
	public static final String EMPTY_STRING = " ";

	/**
	 * @param context
	 * @param cv
	 *            ContentValues
	 *            <p>
	 *            Puts EmptyItem into DB</br> Without this item application will
	 *            have CRASH!</br> This item take part in logical choice
	 *            <p>
	 */
	public static void cvPutEmptyItem(Context context, ContentValues cv) {

		Logger.v();

		// Initialize fields of DbFields.java
		DbFields.initEmptyFields(context);

		//
		cv.put(DbHelper.IMAGE, DbFields.byteImageEmpty);
		cv.put(DbHelper.COLUMN_NAME, DbFields.emptyName);
		cv.put(DbHelper.TOKEN_ONE, EMPTY_STRING);
		cv.put(DbHelper.TOKEN_TWO, EMPTY_STRING);
		cv.put(DbHelper.TOKEN_THREE, EMPTY_STRING);
		cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
		cv.put(DbHelper.DESCRIPTION, EMPTY_STRING);
		cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
		cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);

	}

	/**
	 * @param context
	 * @param cv
	 *            ContentValues
	 *            <p>
	 *            Puts DefaultItems into ContentValues<br/>
	 *            (need manage 15 cycles to put all fields for 15 default items
	 *            into the same columns<br/>
	 *            and need initialize its fields i.e. call
	 *            DbFields.initAllFields(context))
	 *            <p>
	 */
	public static void cvPutDefaultDB(Context context, ContentValues cv) {

		Logger.v();

		if (1 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage1);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName1);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_1);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_1);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_1);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText1);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}
		if (2 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage2);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName2);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_2);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_2);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_2);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText2);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}
		if (3 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage3);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName3);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_3);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_3);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_3);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText3);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}
		if (4 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage4);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName4);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_4);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_4);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_4);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText4);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (5 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage5);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName5);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_5);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_5);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_5);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText5);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (6 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage6);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName6);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_6);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_6);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_6);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText6);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (7 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage7);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName7);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_7);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_7);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_7);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText7);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (8 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage8);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName8);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_8);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_8);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_8);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText8);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (9 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage9);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName9);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_9);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_9);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_9);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText9);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (10 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage10);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName10);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_10);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_10);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_10);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText10);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (11 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage11);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName11);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_11);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_11);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_11);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText11);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (12 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage12);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName12);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_12);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_12);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_12);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText12);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (13 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage13);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName13);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_13);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_13);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_13);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText13);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (14 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage14);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName14);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_14);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_14);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_14);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText14);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		if (15 == counter) {
			cv.put(DbHelper.IMAGE, DbFields.dbByteImage15);
			cv.put(DbHelper.COLUMN_NAME, DbFields.dbName15);
			cv.put(DbHelper.TOKEN_ONE, DbFields.dbTok1_15);
			cv.put(DbHelper.TOKEN_TWO, DbFields.dbTok2_15);
			cv.put(DbHelper.TOKEN_THREE, DbFields.dbTok3_15);
			cv.put(DbHelper.TOKEN_RATING, RATING_DEFAULT);
			cv.put(DbHelper.DESCRIPTION, DbFields.dbText15);
			cv.put(DbHelper.PHONE_NUMBER, PHONE_DEFAULT);
			cv.put(DbHelper.COLUMN_CHOICE, EMPTY_STRING);
		}

		Logger.v("counter = " + counter + "; Data are put into ContentValues");
	}

}
