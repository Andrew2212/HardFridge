package com.blogspot.hqup.hardfridge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blogspot.hqup.hardfridge.constants.EnumCrudDao;
import com.blogspot.hqup.hardfridge.dal.AsyncTaskerCrud;
import com.blogspot.hqup.hardfridge.dbHandle.DbFeeder;
import com.blogspot.hqup.hardfridge.entity.Item;
import com.blogspot.hqup.hardfridge.utils.AlertDialoger;
import com.blogspot.hqup.hardfridge.utils.LoaderContactInfo;
import com.blogspot.hqup.hardfridge.utils.ImageManager;
import com.blogspot.hqup.hardfridge.utils.JsonFileManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.blogspot.hqup.hardfridge.utils.ProgressDialoger;
import com.blogspot.hqup.hardfridge.utils.Sounder;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

public class ItemOneEditActivity extends Activity {

	EditText edtNameTitle;
	ImageView ivImage;
	EditText edtToken1;
	EditText edtToken2;
	EditText edtToken3;
	EditText edtNumber;
	Button btnSave;
	EditText edtDescription;

	private Context context;
	private String itemID;
	private String order;
	private Item item;
	private String name;
	private Cursor cursor;

	private static final int PICK_GALLERY_IMG = 1;
	private static final int PICK_CAMERA_IMG = 2;

	private static final int ROTATE_CW = 101;
	private static final int ROTATE_CCW = 102;

	private File cameraDestinationFile;
	private Intent data;

	public static final String ORDER = "Order";
	public static final String VALUE = "Value";
	public static final String ADD = "Add";
	public static final String EDIT = "Edit";
	public static final String IMPORT = "Import";
	public static final String CONTACT = "Contact";

	private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_item_one_edit);

		init();
		context = this;
		order = getIntent().getStringExtra(ORDER);

		// Create Item
		createCurrentItem();
		// Fill Views
		fillViewsFromItemFields();
		if (item != null)
			Logger.v("order = " + order + "; " + item.toString());

		// Register ContextMenu on ImageView
		registerForContextMenu(ivImage);

		btnSave.setOnClickListener(new OnClickListener() {
			/*
			 * Save edited item or insert new one
			 */
			@Override
			public void onClick(View v) {
				Logger.v(item.toString());
				Sounder.doSound(context, R.raw.beep);

				// Set flags to refresh Activities
				ListSelectedItemsActivity.isListViewRefreshed = false;
				ItemOneActivity.isItemRefreshed = false;

				// Check whether name is valid
				name = edtNameTitle.getText().toString();
				if (!isNewItemNameValid(name)) {
					return;
				}
				// Refresh item fields by values from Views
				fillItemFieldsFromViews();
				/*
				 * Execute INSERT or UPDATE. Activity.finish() is called into
				 * AsyncTaskerCrud.java
				 */
				AsyncTaskerCrud crud = null;

				if (order.equals(EDIT)) {// Edit existing item
					Logger.v("Update item");
					crud = new AsyncTaskerCrud(context, item,
							EnumCrudDao.UPDATE_ITEM);
					Toaster.doToastShort(context,
							R.string.toast_show_item_is_edited);

				}

				if (order.equals(ADD) || order.equals(IMPORT)
						|| order.equals(CONTACT)) {
					// Create OR Import and insert new Item
					Logger.i(order + " item");
					crud = new AsyncTaskerCrud(context, item,
							EnumCrudDao.INSERT_ITEM);
					Toaster.doToastShort(context,
							R.string.toast_show_item_is_added);
				}

				crud.execute();
			}
		});

		ivImage.setOnClickListener(new OnClickListener() {
			/*
			 * StartActivity to take photo from SDcard Or by Camera
			 */
			@Override
			public void onClick(View v) {
				Sounder.doSound(context, R.raw.beep);

				new AlertDialoger(context, R.string.dialog_msg_where_take_img,
						R.string.btntext_gallery, R.string.btntext_camera) {

					@Override
					public void doThatIfYes() {
						// Take photo from Gallery
						takeImgFromGallery();
					}

					@Override
					public void doThatIfNo() {
						// Take photo from Camera
						takeImgFromCamera();
					}
				};
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (cursor != null)
			cursor.close();
	}

	// ContextMenu for rotating Image if it is needed
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, ROTATE_CW, 0,
				getResources().getString(R.string.menu_ctx_rotate_cw));
		menu.add(0, ROTATE_CCW, 0,
				getResources().getString(R.string.menu_ctx_rotate_ccw));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		Bitmap imgBitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();

		switch (item.getItemId()) {

		case ROTATE_CW:
			imgBitmap = ImageManager.rotateCW(imgBitmap);
			ivImage.setImageBitmap(imgBitmap);
			break;

		case ROTATE_CCW:
			imgBitmap = ImageManager.rotateCCW(imgBitmap);
			ivImage.setImageBitmap(imgBitmap);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	// ---------------Private Methods---------------------

	private void init() {

		edtNameTitle = (EditText) findViewById(R.id.edtItemEdit_Title);
		ivImage = (ImageView) findViewById(R.id.ivItemEditThumbnail);
		edtToken1 = (EditText) findViewById(R.id.edtxtItemEdit_Token_1);
		edtToken2 = (EditText) findViewById(R.id.edtxtItemEdit_Token_2);
		edtToken3 = (EditText) findViewById(R.id.edtxtItemEdit_Token_3);
		edtDescription = (EditText) findViewById(R.id.edtxtItemEditDescripton);
		edtNumber = (EditText) findViewById(R.id.edtxtItemEdit_PhoneNumber);
		btnSave = (Button) findViewById(R.id.btnItemEdit_Save);
	}

	private void createCurrentItem() {
		if (order.equals(ADD) || order.equals(CONTACT)) {
			// If we want to add new item (maybe from Contacts)
			item = new Item();
		}
		if (order.equals(EDIT)) {
			// If we want to edit existing Item
			itemID = getIntent().getStringExtra(VALUE);
			item = new Item(itemID);
			createCursor();
			item.fillItemFieldsFromCursor(cursor);
		}
		if (order.equals(IMPORT)) {
			// If we want to import item from JSON file
			String path = getIntent().getStringExtra(VALUE);
			item = JsonFileManager.readJsonFile(context, path);
		}

		if (item != null)
			Logger.v(order + " :" + item.toString());
	}

	private void fillViewsFromItemFields() {
		if (item == null) {
			Toaster.doToastLong(context, R.string.toast_show_wrong_file);
			finish();
			Logger.v();
			return;
		}
		Logger.v(item.toString());

		if (order.equals(EDIT) || order.equals(IMPORT)) {
			// Set item fields values onto the Views
			edtNameTitle.setText(item.getName());
			edtToken1.setText(item.getToken_1());
			edtToken2.setText(item.getToken_2());
			edtToken3.setText(item.getToken_3());
			edtDescription.setText(item.getDescription());

			String phoneNum = item.getPhoneNum();
			if (phoneNum != null && !phoneNum.equals(DbFeeder.PHONE_DEFAULT))
				edtNumber.setText(item.getPhoneNum());
			Logger.v("edtText.setPhoneNum");
		}

		if (order.equals(CONTACT)) {

			String identifier = getIntent().getStringExtra(VALUE);
			LoaderContactInfo cursorLoader = new LoaderContactInfo(
					context, identifier);
			// Get 'contactName', 'phoneNum' and 'description'
			String contactName = cursorLoader.getContactName();
			String phoneNum = cursorLoader.getPhoneNum();
			String description = cursorLoader.getDescription();

			edtNameTitle.setText(contactName);
			edtDescription.setText(description);
			edtNumber.setText(phoneNum);

		}

		// Set image into imageView
		byte[] imgByteArr = item.getImg();
		if (imgByteArr == null) {
			imgByteArr = ImageManager.createImgByteArrayFromResources(context,
					R.drawable.egg);
		}
		Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgByteArr, 0,
				imgByteArr.length);
		ivImage.setImageBitmap(imgBitmap);
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
	 * @param string
	 *            - new Item name that is set into field 'Name'
	 * @return true if name is valid false in other way
	 */
	private boolean isNewItemNameValid(String string) {
		Logger.v();
		boolean result = true;

		if (string.equalsIgnoreCase(getResources().getString(
				R.string.db_name_empty))) {
			Toaster.doToastLong(context,
					R.string.toast_show_nothing_already_exist);
			result = false;
		}
		if (TextUtils.isEmpty(edtNameTitle.getText().toString())
				|| edtNameTitle.getText().toString()
						.equals(getString(R.string.txt_name_empty))) {
			Toaster.doToastShort(context, R.string.toast_show_field_name_empty);
			result = false;
		}

		return result;
	}

	/**
	 * @return Item item with filling fields
	 *         <p>
	 *         Fills the item fields by values from Views</br> Fills all fields
	 *         except ID !!!
	 *         </p>
	 */
	private Item fillItemFieldsFromViews() {
		Logger.v();

		String token_1 = edtToken1.getText().toString();
		if (token_1.isEmpty() || token_1.length() < 3)
			token_1 = DbFeeder.EMPTY_STRING;
		String token_2 = edtToken2.getText().toString();
		if (token_2.isEmpty() || token_2.length() < 3)
			token_2 = DbFeeder.EMPTY_STRING;
		String token_3 = edtToken3.getText().toString();
		if (token_3.isEmpty() || token_3.length() < 3)
			token_3 = DbFeeder.EMPTY_STRING;
		String phoneNum = edtNumber.getText().toString();
		if (phoneNum == null || phoneNum.isEmpty() || phoneNum.length() < 3)
			phoneNum = DbFeeder.PHONE_DEFAULT;

		Bitmap imgBitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
		byte[] imgByteArr = ImageManager.createImgByteArrayFromView(context,
				imgBitmap);

		String description = edtDescription.getText().toString();
		if (description.equals(""))
			description = DbFeeder.EMPTY_STRING;

		String choice = DbFeeder.EMPTY_STRING;

		// Set all fields
		item.setName(name);
		item.setImg(imgByteArr);
		item.setToken_1(token_1);
		item.setToken_2(token_2);
		item.setToken_3(token_3);
		item.setDescription(description);
		item.setPhoneNum(phoneNum);
		item.setChoice(choice);

		Logger.v(item.toString());
		return item;
	}

	// --------Get Image from either Gallery or Camera------------

	/**
	 * Starts ActivityForResult with according Intent
	 */
	private void takeImgFromGallery() {
		Logger.v();
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		intent.setType("image/*");// choice among pictures any format
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PICK_GALLERY_IMG);
	}

	/**
	 * Starts ActivityForResult with according Intent
	 */
	private void takeImgFromCamera() {
		Logger.v();

		cameraDestinationFile = new File(
				Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Put data to Uri.fromFile()
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(cameraDestinationFile));
		startActivityForResult(intent, PICK_CAMERA_IMG);

		Logger.v(Uri.fromFile(cameraDestinationFile).toString());
	}

	/**
	 * Gets photo and set it onto ImageView
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		this.data = data;
		if (resultCode != RESULT_OK)
			return;

		// Get imgBitmap by AsyncLoaderBitmap
		AsyncBitmapLoader bitmapLoader = new AsyncBitmapLoader();
		bitmapLoader.execute(requestCode);
		Bitmap imgBitmap = null;

		try {
			imgBitmap = bitmapLoader.get();
		} catch (InterruptedException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			Logger.v(e.getMessage());
		} catch (ExecutionException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			Logger.v(e.getMessage());
		}

		// Set image into ImageView
		ivImage.setImageBitmap(imgBitmap);
	}

	private class AsyncBitmapLoader extends AsyncTask<Integer, Void, Bitmap> {
		ProgressDialoger pd = new ProgressDialoger(context);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Logger.v();
			pd.showPD();
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {

			Logger.v();
			int action = params[0];
			Bitmap imgBitmap = null;

			if (action == PICK_GALLERY_IMG) {
				imgBitmap = loadImgForViewFromGallery(data);
			}
			if (action == PICK_CAMERA_IMG) {
				imgBitmap = loadImgForViewFromCamera();
			}
			return imgBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			Logger.v();
			pd.dismissPD();
		}

	}

	/**
	 * @param data
	 *            An Intent, which can return result data to the caller (various
	 *            data can be attached to Intent "extras").
	 * @return imgBitmap = createBitmapForView(inStream, scale);
	 *         <p>
	 *         Gets image from Gallery, scales, crops and return it
	 *         </p>
	 */
	private Bitmap loadImgForViewFromGallery(Intent data) {
		this.data = data;
		Logger.v(" data = " + data);
		if (null == data)
			return null;
		InputStream inStream = null;
		Uri uriImage = data.getData();
		Logger.v("uriImage = " + uriImage);

		// Open InputStream
		try {
			inStream = getContentResolver().openInputStream(uriImage);
		} catch (FileNotFoundException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			Logger.v(e.getMessage());
		}

		// Get raw options
		BitmapFactory.Options options = getOptionsBitmapRaw(inStream);

		// Close InputStream
		try {
			inStream.close();
		} catch (IOException e) {
			Logger.v(e.getMessage());
		}

		// Receive required scale by raw options
		int scale = ImageManager.getScale(context, options);

		// Open new InputStream
		try {
			inStream = getContentResolver().openInputStream(uriImage);
		} catch (FileNotFoundException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			Logger.v(e.getMessage());
		}

		// Get Bitmap in accordance to scale
		Bitmap imgBitmap = createBitmapForView(inStream, scale);

		return imgBitmap;
	}

	/**
	 * <p>
	 * Gets image from Camera, scales, crops and return it
	 * </p>
	 */
	private Bitmap loadImgForViewFromCamera() {
		Logger.v();

		InputStream inStream = null;

		// Open InputStream
		try {
			inStream = new FileInputStream(cameraDestinationFile);
		} catch (FileNotFoundException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			Logger.v(e.getMessage());
		}

		// Get raw options
		BitmapFactory.Options options = getOptionsBitmapRaw(inStream);

		// Close InputStream
		try {
			inStream.close();
		} catch (IOException e) {
			Logger.v(e.getMessage());
		}

		// Receive and set required scale
		int scale = ImageManager.getScale(context, options);

		// Open new InputStream
		try {
			inStream = new FileInputStream(cameraDestinationFile);
		} catch (FileNotFoundException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			Logger.v(e.getMessage());
		}

		// Get Bitmap in accordance to scale
		Bitmap imgBitmap = createBitmapForView(inStream, scale);

		// Delete temporary file
		if (cameraDestinationFile.exists())
			cameraDestinationFile.delete();

		return imgBitmap;
	}

	/**
	 * @param inStream
	 *            InputStream
	 * @return options of raw Bitmap
	 */
	private BitmapFactory.Options getOptionsBitmapRaw(InputStream inStream) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// Only options without Bitmap
		@SuppressWarnings("unused")
		Bitmap imgBitmap = BitmapFactory.decodeStream(inStream, null, options);
		return options;
	}

	/**
	 * @param inStream
	 *            InputStream
	 * @param scale
	 * @return Bitmap imgBitmap
	 * 
	 *         <p>
	 *         Gets bitmap by 'decodeStream' in accordance with 'scale'</br>
	 *         Crops image by ImageManager and its parameters
	 *         </p>
	 */
	private Bitmap createBitmapForView(InputStream inStream, int scale) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = scale;

		Bitmap imgBitmap = BitmapFactory.decodeStream(inStream, null, options);
		Logger.v("image size = " + options.outWidth + " x " + options.outHeight);

		// Crop bitmap
		imgBitmap = ImageManager.crop(context, imgBitmap);
		Logger.v("image size = " + imgBitmap.getWidth() + " x "
				+ imgBitmap.getHeight());

		return imgBitmap;
	}

}
