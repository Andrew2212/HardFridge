package com.blogspot.hqup.hardfridge.dbHandle;

import android.content.Context;

import com.blogspot.hqup.hardfridge.utils.ImageManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.hqup.hardfridge.R;

/**
 * @author Andrew
 *         <p>
 *         Initialize variables DefaultDB items for DbFeeder by data from the
 *         resources.
 *         <p>
 */
public class DbFields {

	// public static String emptyEmpty;
	public static String emptyName;
	public static byte[] byteImageEmpty;

	public static byte[] byteImageEgg;

	public static byte[] dbByteImage1;
	public static String dbName1;
	public static String dbTok1_1;
	public static String dbTok2_1;
	public static String dbTok3_1;
	public static String dbText1;

	public static byte[] dbByteImage2;
	public static String dbName2;
	public static String dbTok1_2;
	public static String dbTok2_2;
	public static String dbTok3_2;
	public static String dbText2;

	public static byte[] dbByteImage3;
	public static String dbName3;
	public static String dbTok1_3;
	public static String dbTok2_3;
	public static String dbTok3_3;
	public static String dbText3;

	public static byte[] dbByteImage4;
	public static String dbName4;
	public static String dbTok1_4;
	public static String dbTok2_4;
	public static String dbTok3_4;
	public static String dbText4;

	public static byte[] dbByteImage5;
	public static String dbName5;
	public static String dbTok1_5;
	public static String dbTok2_5;
	public static String dbTok3_5;
	public static String dbText5;

	public static byte[] dbByteImage6;
	public static String dbName6;
	public static String dbTok1_6;
	public static String dbTok2_6;
	public static String dbTok3_6;
	public static String dbText6;

	public static byte[] dbByteImage7;
	public static String dbName7;
	public static String dbTok1_7;
	public static String dbTok2_7;
	public static String dbTok3_7;
	public static String dbText7;

	public static byte[] dbByteImage8;
	public static String dbName8;
	public static String dbTok1_8;
	public static String dbTok2_8;
	public static String dbTok3_8;
	public static String dbText8;

	public static byte[] dbByteImage9;
	public static String dbName9;
	public static String dbTok1_9;
	public static String dbTok2_9;
	public static String dbTok3_9;
	public static String dbText9;

	public static byte[] dbByteImage10;
	public static String dbName10;
	public static String dbTok1_10;
	public static String dbTok2_10;
	public static String dbTok3_10;
	public static String dbText10;

	public static byte[] dbByteImage11;
	public static String dbName11;
	public static String dbTok1_11;
	public static String dbTok2_11;
	public static String dbTok3_11;
	public static String dbText11;

	public static byte[] dbByteImage12;
	public static String dbName12;
	public static String dbTok1_12;
	public static String dbTok2_12;
	public static String dbTok3_12;
	public static String dbText12;

	public static byte[] dbByteImage13;
	public static String dbName13;
	public static String dbTok1_13;
	public static String dbTok2_13;
	public static String dbTok3_13;
	public static String dbText13;

	public static byte[] dbByteImage14;
	public static String dbName14;
	public static String dbTok1_14;
	public static String dbTok2_14;
	public static String dbTok3_14;
	public static String dbText14;

	public static byte[] dbByteImage15;
	public static String dbName15;
	public static String dbTok1_15;
	public static String dbTok2_15;
	public static String dbTok3_15;
	public static String dbText15;

	public static String stringDescriptTest;

	/**
	 * @param context
	 *            <p>
	 *            Initialize variables for DbFeeder by EmptyItem data from the
	 *            resources. <br>
	 *            It's for EmptyItem
	 *            <p>
	 */
	public static void initEmptyFields(Context context) {

		Logger.v();

		emptyName = context.getResources().getString(R.string.db_name_empty);
		stringDescriptTest = context.getResources().getString(
				R.string.txt_item_description_test);
		byteImageEmpty = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.one_empty_thumbnail);
		// byteImageEgg = createByteImg(context, R.drawable.egg);

	}

	/**
	 * @param context
	 *            <p>
	 *            Initialize variable emptyView ('gold egg') for NewData without
	 *            setting image
	 *            <p>
	 */
	public static void initEmptyViewEgg(Context context) {
		byteImageEgg = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.egg);
	}

	/**
	 * @param context
	 *            <p>
	 *            Initialize variables for DbFeeder by DefaultDB data from the
	 *            resources. <br>
	 *            It's for all DefaultItems and EmptyItem
	 *            <p>
	 */
	public static void initAllFields(Context context) {
		Logger.v();
		// context = First_Activity.myContext;

		// take the String from Resources
		initEmptyFields(context);
		// emptyEmpty = " ";
		// emptyName = context.getResources().getString(R.string.db_name_empty);
		// stringDescriptTest = context.getResources().getString(
		// R.string.txt_item_description_test);

		dbName1 = context.getResources().getString(R.string.db_name_d1);
		dbTok1_1 = context.getResources().getString(R.string.db_tok_1_d1);
		dbTok2_1 = context.getResources().getString(R.string.db_tok_2_d1);
		dbTok3_1 = context.getResources().getString(R.string.db_tok_3_d1);
		dbText1 = context.getResources().getString(R.string.db_text_d1);

		dbName2 = context.getResources().getString(R.string.db_name_d2);
		dbTok1_2 = context.getResources().getString(R.string.db_tok_1_d2);
		dbTok2_2 = context.getResources().getString(R.string.db_tok_2_d2);
		dbTok3_2 = context.getResources().getString(R.string.db_tok_3_d2);
		dbText2 = context.getResources().getString(R.string.db_text_d2);

		dbName3 = context.getResources().getString(R.string.db_name_d3);
		dbTok1_3 = context.getResources().getString(R.string.db_tok_1_d3);
		dbTok2_3 = context.getResources().getString(R.string.db_tok_2_d3);
		dbTok3_3 = context.getResources().getString(R.string.db_tok_3_d3);
		dbText3 = context.getResources().getString(R.string.db_text_d3);

		dbName4 = context.getResources().getString(R.string.db_name_d4);
		dbTok1_4 = context.getResources().getString(R.string.db_tok_1_d4);
		dbTok2_4 = context.getResources().getString(R.string.db_tok_2_d4);
		dbTok3_4 = context.getResources().getString(R.string.db_tok_3_d4);
		dbText4 = context.getResources().getString(R.string.db_text_d4);

		dbName5 = context.getResources().getString(R.string.db_name_d5);
		dbTok1_5 = context.getResources().getString(R.string.db_tok_1_d5);
		dbTok2_5 = context.getResources().getString(R.string.db_tok_2_d5);
		dbTok3_5 = context.getResources().getString(R.string.db_tok_3_d5);
		dbText5 = context.getResources().getString(R.string.db_text_d5);

		dbName6 = context.getResources().getString(R.string.db_name_d6);
		dbTok1_6 = context.getResources().getString(R.string.db_tok_1_d6);
		dbTok2_6 = context.getResources().getString(R.string.db_tok_2_d6);
		dbTok3_6 = context.getResources().getString(R.string.db_tok_3_d6);
		dbText6 = context.getResources().getString(R.string.db_text_d6);

		dbName7 = context.getResources().getString(R.string.db_name_d7);
		dbTok1_7 = context.getResources().getString(R.string.db_tok_1_d7);
		dbTok2_7 = context.getResources().getString(R.string.db_tok_2_d7);
		dbTok3_7 = context.getResources().getString(R.string.db_tok_3_d7);
		dbText7 = context.getResources().getString(R.string.db_text_d7);

		dbName8 = context.getResources().getString(R.string.db_name_d8);
		dbTok1_8 = context.getResources().getString(R.string.db_tok_1_d8);
		dbTok2_8 = context.getResources().getString(R.string.db_tok_2_d8);
		dbTok3_8 = context.getResources().getString(R.string.db_tok_3_d8);
		dbText8 = context.getResources().getString(R.string.db_text_d8);

		dbName9 = context.getResources().getString(R.string.db_name_d9);
		dbTok1_9 = context.getResources().getString(R.string.db_tok_1_d9);
		dbTok2_9 = context.getResources().getString(R.string.db_tok_2_d9);
		dbTok3_9 = context.getResources().getString(R.string.db_tok_3_d9);
		dbText9 = context.getResources().getString(R.string.db_text_d9);

		dbName10 = context.getResources().getString(R.string.db_name_d10);
		dbTok1_10 = context.getResources().getString(R.string.db_tok_1_d10);
		dbTok2_10 = context.getResources().getString(R.string.db_tok_2_d10);
		dbTok3_10 = context.getResources().getString(R.string.db_tok_3_d10);
		dbText10 = context.getResources().getString(R.string.db_text_d10);

		dbName11 = context.getResources().getString(R.string.db_name_d11);
		dbTok1_11 = context.getResources().getString(R.string.db_tok_1_d11);
		dbTok2_11 = context.getResources().getString(R.string.db_tok_2_d11);
		dbTok3_11 = context.getResources().getString(R.string.db_tok_3_d11);
		dbText11 = context.getResources().getString(R.string.db_text_d11);

		dbName12 = context.getResources().getString(R.string.db_name_d12);
		dbTok1_12 = context.getResources().getString(R.string.db_tok_1_d12);
		dbTok2_12 = context.getResources().getString(R.string.db_tok_2_d12);
		dbTok3_12 = context.getResources().getString(R.string.db_tok_3_d12);
		dbText12 = context.getResources().getString(R.string.db_text_d12);

		dbName13 = context.getResources().getString(R.string.db_name_d13);
		dbTok1_13 = context.getResources().getString(R.string.db_tok_1_d13);
		dbTok2_13 = context.getResources().getString(R.string.db_tok_2_d13);
		dbTok3_13 = context.getResources().getString(R.string.db_tok_3_d13);
		dbText13 = context.getResources().getString(R.string.db_text_d13);

		dbName14 = context.getResources().getString(R.string.db_name_d14);
		dbTok1_14 = context.getResources().getString(R.string.db_tok_1_d14);
		dbTok2_14 = context.getResources().getString(R.string.db_tok_2_d14);
		dbTok3_14 = context.getResources().getString(R.string.db_tok_3_d14);
		dbText14 = context.getResources().getString(R.string.db_text_d14);

		dbName15 = context.getResources().getString(R.string.db_name_d15);
		dbTok1_15 = context.getResources().getString(R.string.db_tok_1_d15);
		dbTok2_15 = context.getResources().getString(R.string.db_tok_2_d15);
		dbTok3_15 = context.getResources().getString(R.string.db_tok_3_d15);
		dbText15 = context.getResources().getString(R.string.db_text_d15);

		// Convert image from resources to byte[] by own method createByteImg

		// byteImageEmpty = createByteImg(context,
		// R.drawable.one_empty_thumbnail);
		// byteImageEgg = createByteImg(context, R.drawable.egg);

		dbByteImage1 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_1);
		dbByteImage2 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_2);
		dbByteImage3 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_3);
		dbByteImage4 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_4);
		dbByteImage5 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_5);
		dbByteImage6 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_6);
		dbByteImage7 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_7);

		dbByteImage8 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_8);
		dbByteImage9 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_9);
		dbByteImage10 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_10);
		dbByteImage11 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_11);
		dbByteImage12 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_12);
		dbByteImage13 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_13);
		dbByteImage14 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_14);

		dbByteImage15 = ImageManager.createImgByteArrayFromResources(context,
				R.drawable.d_15);
	}
}
