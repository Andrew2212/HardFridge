package com.blogspot.hqup.hardfridge;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.blogspot.hqup.hardfridge.constants.Constants;
import com.blogspot.hqup.hardfridge.constants.EnumCrudDao;
import com.blogspot.hqup.hardfridge.dal.AsyncTaskerCrud;
import com.blogspot.hqup.hardfridge.dbHandle.DbFeeder;
import com.blogspot.hqup.hardfridge.dbHandle.DbHelper;
import com.blogspot.hqup.hardfridge.entity.Item;
import com.blogspot.hqup.hardfridge.utils.AlertDialoger;
import com.blogspot.hqup.hardfridge.utils.ImageManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.blogspot.hqup.hardfridge.utils.Sounder;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

public class ItemOneActivity extends Activity implements
		android.view.View.OnClickListener {

	LinearLayout layoutFull;
	ImageView ivItemImg_Thumbnail;
	ImageView ivItemImg_FullScreen;
	TextView tvTitle;
	TextView tvToken1;
	TextView tvToken2;
	TextView tvToken3;
	RatingBar ratingBar;
	TextView tvDescription;
	Button btnNumber;
	Button btnDel;
	Button btnEdit;
	Button btnHelp;

	private byte[] imgByteArr;
	private Cursor cursor;
	private String itemID;
	private Context context;
	private Item item;
	/**
	 * Shows whether this Rating has been changed
	 */
	private boolean isRatingChanged = false;
	/**
	 * Shows whether Item need to refresh (false) or not (true)
	 */
	public static boolean isItemRefreshed = true;
	private String friendNumberPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_item_one);

		context = this;
		init();

		// Get Item, fill its fields and set that value into Views
		itemID = getIntent().getStringExtra("Item_id");
		item = new Item(itemID);
		createCursor();
		item.fillItemFieldsFromCursor(cursor);
		setValueOnView();

		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// Update Rating value into DB
				Logger.v("ratingBar.getRating() = " + ratingBar.getRating());
				fromUser = true;

				isRatingChanged = true;
				ListSelectedItemsActivity.isListViewRefreshed = false;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Logger.v();
		if (!isItemRefreshed) {
			Logger.v();
			createCursor();
			item.fillItemFieldsFromCursor(cursor);
			setValueOnView();
			isItemRefreshed = true;
		}
		if (cursor != null)
			cursor.close();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.v();

		if (isRatingChanged) {
			Logger.v();
			setRating();
			isRatingChanged = false;
		}

		if (cursor != null)
			cursor.close();
	}

	@Override
	public void onBackPressed() {

		Logger.v();

		if (View.VISIBLE == ivItemImg_FullScreen.getVisibility()) {

			ivItemImg_FullScreen.setVisibility(View.GONE);
			layoutFull.setVisibility(View.VISIBLE);

			Sounder.doSound(context, R.raw.beep);
			Logger.v();

		} else {
			super.onBackPressed();// finish();
			Logger.v();
		}
	}

	@Override
	public void onClick(View v) {
		Sounder.doSound(context, R.raw.beep);

		switch (v.getId()) {

		case R.id.btnItemOneDel:

			@SuppressWarnings("unused")
			AlertDialoger alertDialoger = new AlertDialoger(context,
					R.string.dialog_msg_item_delete) {

				@Override
				public void doThatIfYes() {

					Item item = new Item(itemID);// to wrap ID in Item
					new AsyncTaskerCrud(context, item, EnumCrudDao.DELETE_ITEM)
							.execute();

					ListSelectedItemsActivity.isListViewRefreshed = false;

					Toaster.doToastShort(context,
							R.string.toast_show_item_is_deleted);
				}
			};

			break;

		case R.id.btnItemOneEdit:

			Intent intent = new Intent(this, ItemOneEditActivity.class);
			intent.putExtra(ItemOneEditActivity.VALUE, itemID);
			intent.putExtra(ItemOneEditActivity.ORDER, ItemOneEditActivity.EDIT);
			startActivity(intent);
			break;

		case R.id.btnItemOneHelp:

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			friendNumberPhone = prefs.getString(
					getString(R.string.friend_phone_key),
					getString(R.string.friend_phone_name));

			alertDialoger = new AlertDialoger(context,
					R.string.dialog_msg_whose_help, R.string.btntext_friend,
					R.string.btntext_google) {

				@Override
				public void doThatIfYes() {
					Intent intent = new Intent(Intent.ACTION_DIAL,
							Uri.parse("tel:" + friendNumberPhone));
					startActivity(intent);
				}

				@Override
				public void doThatIfNo() {
					super.doThatIfNo();
					Intent intent = new Intent(Intent.ACTION_VIEW,
							Constants.HELP_FROM_WEB);
					startActivity(intent);
				}
			};
			break;

		case R.id.btnItemOneCallToNumber:

			String nameNumber = btnNumber.getText().toString();
			Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ nameNumber));
			startActivity(intentCall);
			break;

		case R.id.ivItemOneImg_Thumbnail:

			ivItemImg_FullScreen.setVisibility(View.VISIBLE);
			layoutFull.setVisibility(View.GONE);			
			break;

		case R.id.ivItemOneImg_FullScreen:

			onBackPressed();
			break;

		default:
			break;
		}

	}
	


//	----------------Private Methods--------------------------------------
	
	private void init() {

		layoutFull = (LinearLayout) findViewById(R.id.layoutItemOneFull);

		ivItemImg_Thumbnail = (ImageView) findViewById(R.id.ivItemOneImg_Thumbnail);
		ivItemImg_FullScreen = (ImageView) findViewById(R.id.ivItemOneImg_FullScreen);

		tvTitle = (TextView) findViewById(R.id.tvItemOneTitle);
		tvToken1 = (TextView) findViewById(R.id.tvItemOneToken_1);
		tvToken2 = (TextView) findViewById(R.id.tvItemOneToken_2);
		tvToken3 = (TextView) findViewById(R.id.tvItemOneToken_3);
		ratingBar = (RatingBar) findViewById(R.id.ratingBarItemOne);
		tvDescription = (TextView) findViewById(R.id.tvItemFullText);
		btnNumber = (Button) findViewById(R.id.btnItemOneCallToNumber);
		btnDel = (Button) findViewById(R.id.btnItemOneDel);
		btnEdit = (Button) findViewById(R.id.btnItemOneEdit);
		btnHelp = (Button) findViewById(R.id.btnItemOneHelp);
		btnNumber.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnEdit.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		ivItemImg_Thumbnail.setOnClickListener(this);
		ivItemImg_FullScreen.setOnClickListener(this);

	}

	/**
	 * @return cursor </br>Creates cursor through AsyncTaskerCrud and
	 *         CrudDaoBean methods
	 */
	private Cursor createCursor() {
		AsyncTaskerCrud crud = new AsyncTaskerCrud(context, item,
				EnumCrudDao.GET_SLECTED_ITEM_CURSOR);
		crud.execute();

		try {
			cursor = crud.get();
		} catch (InterruptedException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			e.printStackTrace();
		}

		return cursor;
	}

	/**
	 * <p>
	 * Sets value from item fields onto the Views.
	 * </p>
	 */
	private void setValueOnView() {
		Logger.v();
		tvTitle.setText(item.getName());

		imgByteArr = item.getImg();
		Bitmap imgBitmap = ImageManager.createBitmapFromByteArr(imgByteArr);
		ivItemImg_Thumbnail.setImageBitmap(imgBitmap);
		ivItemImg_FullScreen.setImageBitmap(imgBitmap);

		tvToken1.setText(item.getToken_1());
		tvToken2.setText(item.getToken_2());
		tvToken3.setText(item.getToken_3());
		ratingBar.setRating(item.getRating());
		tvDescription.setText(item.getDescription());

		String phoneNum = item.getPhoneNum();
		// To see hint onto btnPhone
		if (phoneNum != null && !phoneNum.equals(DbFeeder.PHONE_DEFAULT)) {
			btnNumber.setText(phoneNum);
			Logger.v("btnNumber.getText = " + btnNumber.getText());
		}

	}

	/**
	 * Saves changed item Rating into DB
	 */
	private void setRating() {
		String where = " _id = ? ";
		String[] toTake = new String[] { itemID };
		ContentValues cv = new ContentValues();
		cv.put(DbHelper.TOKEN_RATING, ratingBar.getRating());
		DbHelper dbHelper = new DbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.update(DbHelper.DB_TABLE, cv, where, toTake);
		db.close();
	}
	
}
