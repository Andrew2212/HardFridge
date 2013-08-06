package com.blogspot.hqup.hardfridge.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.WindowManager;

import com.blogspot.hqup.hardfridge.constants.Constants;
import com.hqup.hardfridge.R;

/**
 * 
 * @author
 */
public final class ImageManager {

	public static int getScale(Context context, BitmapFactory.Options options) {

		int widthDisplay = getDisplayWidth(context);
		int heightDisplay = getDisplayHeight(context);
		Logger.v("Display size = " + widthDisplay + " x " + heightDisplay);

		// Dimensions of the bitmap
		int widthBitmap = options.outWidth;
		int heightBitmap = options.outHeight;
		Logger.v("image size = " + widthBitmap + " x " + heightBitmap);

		// Index of 'scale'
		int scaleX = 1 + Math.round(widthBitmap / widthDisplay);
		int scaleY = 1 + Math.round(heightBitmap / heightDisplay);
		int scale = (scaleX + scaleY) / 2;

		Logger.v("scaleX = " + scaleX);
		Logger.v("scaleY = " + scaleY);
		Logger.v("scale = " + scale);

		return scale;
	}

	/**
	 * 
	 * @author bdiang
	 *         <p>
	 *         Crop image as require dimensions '_boxHeight' and
	 *         'IMG_DB_WIDTH'</br> http://habrahabr.ru/post/161027/
	 *         </p>
	 */
	public static Bitmap crop(Context context, Bitmap sourceBitmap) {

		if (sourceBitmap == null)
			return null;

		// initDimensions(context);

		int widthMax = Constants.IMG_DB_WIDTH;
		int heightMax = Constants.IMG_DB_HEIGHT;

		int srcWidth = sourceBitmap.getWidth();
		int srcHeight = sourceBitmap.getHeight();
		Logger.v("image size = " + srcWidth + " x " + srcHeight);

		int croppedX = 0;
		int croppedY = 0;

		if (srcWidth > Constants.IMG_DB_WIDTH) {
			croppedX = (srcWidth - Constants.IMG_DB_WIDTH) / 2;
		} else {
			croppedX = 0;
			widthMax = srcWidth;
		}

		if (srcHeight > Constants.IMG_DB_HEIGHT) {
			croppedY = (srcHeight - Constants.IMG_DB_HEIGHT) / 2;
		} else {
			croppedY = 0;
			heightMax = srcHeight;
		}

		if (croppedX == 0 && croppedY == 0)
			return sourceBitmap;

		Bitmap croppedBitmap = null;
		try {
			Logger.v("bitmap will be cropped");

			croppedBitmap = Bitmap.createBitmap(sourceBitmap, croppedX,
					croppedY, widthMax, heightMax);

			Logger.v("image size = " + widthMax + " x " + heightMax);
		} catch (Exception e) {
			Logger.v(e.getMessage());
		}

		return croppedBitmap;
	}

	/**
	 * @param context
	 * @param id
	 * @return baosImg.toByteArray()
	 *         <p>
	 *         Converts image from resources to byte[]
	 *         </p>
	 */
	public static byte[] createImgByteArrayFromResources(Context context, int id) {

		Logger.v();

		ByteArrayOutputStream baosImg = new ByteArrayOutputStream();
		Bitmap bitmap = ((BitmapDrawable) context.getResources()
				.getDrawable(id)).getBitmap();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */,
				baosImg);

		try {
			baosImg.close();
		} catch (IOException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			e.printStackTrace();
		}
		try {
			baosImg.flush();
		} catch (IOException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			e.printStackTrace();
		}

		return baosImg.toByteArray();
	}

	/**
	 * @param context
	 * @param imgBitmap
	 *            Bitmap
	 * @return imgByteArr = baosImg.toByteArray();
	 *         <p>
	 *         Converts image from imageView to byte[]
	 *         </p>
	 */
	public static byte[] createImgByteArrayFromView(Context context,
			Bitmap imgBitmap) {

		Logger.v();

		ByteArrayOutputStream baosImg = new ByteArrayOutputStream();

		imgBitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */,
				baosImg);

		try {
			baosImg.close();
		} catch (IOException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			e.printStackTrace();
		}
		try {
			baosImg.flush();
		} catch (IOException e) {
			Toaster.doToastLong(context, R.string.toast_show_try_catch);
			e.printStackTrace();
		}

		byte[] imgByteArr = baosImg.toByteArray();

		return imgByteArr;
	}

	/**
	 * @param imgByteArr
	 *            - byte[]
	 * @return imgBitmap - Bitmap from imgByteArr
	 */
	public static Bitmap createBitmapFromByteArr(byte[] imgByteArr) {
		ByteArrayInputStream imageStream = new ByteArrayInputStream(imgByteArr);
		Bitmap imgBitmap = BitmapFactory.decodeStream(imageStream, null, null);
		return imgBitmap;
	}


	public static Bitmap createBitmapThumbnailFromByteArr(byte[] imgByteArr) {

		ByteArrayInputStream imageStream = new ByteArrayInputStream(imgByteArr);
		BitmapFactory.Options options = new BitmapFactory.Options();
		int scale = (int) Math
				.floor((Constants.IMG_DB_WIDTH
						/ Constants.IMG_THUMBNAIL_LISTVIEW_WIDTH + Constants.IMG_DB_HEIGHT
						/ Constants.IMG_THUMBNAIL_LISTVIEW_HEIGHT) / 2);
		options.inSampleSize = scale;
		Logger.v("scale = " + scale);
		Bitmap thumbImgBitmap = BitmapFactory.decodeStream(imageStream, null,
				options);
		return thumbImgBitmap;
	}


	public static Bitmap scaleBitmapForListView(Bitmap imgBitmap) {
		Logger.v();

		int viewWidth = Constants.IMG_THUMBNAIL_LISTVIEW_WIDTH;
		int viewHeight = Constants.IMG_THUMBNAIL_LISTVIEW_HEIGHT;

		int imgWidth = imgBitmap.getWidth();
		int imgHeight = imgBitmap.getHeight();
		Logger.v("imgBitmap size = " + imgWidth + " x " + imgHeight);

		float scaleX = (float) viewWidth / (float) imgWidth;
		float scaleY = (float) viewHeight / (float) imgHeight;

		float scale = (scaleY <= scaleX) ? scaleY : scaleX;
		Logger.v("scale = " + scale);

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap imgBitmapScaled = Bitmap.createBitmap(imgBitmap, 0, 0, imgWidth,
				imgHeight, matrix, true);

		return imgBitmapScaled;
	}

	public static Bitmap rotateCW(Bitmap imgBitmap) {
		Logger.v();
		Matrix matrix = new Matrix();
		matrix.postRotate(90);

		Bitmap imgBitmapRotated = Bitmap.createBitmap(imgBitmap, 0, 0,
				imgBitmap.getWidth(), imgBitmap.getHeight(), matrix, true);

		return imgBitmapRotated;
	}

	public static Bitmap rotateCCW(Bitmap imgBitmap) {
		Logger.v();
		Matrix matrix = new Matrix();
		matrix.postRotate(270);

		Bitmap imgBitmapRotated = Bitmap.createBitmap(imgBitmap, 0, 0,
				imgBitmap.getWidth(), imgBitmap.getHeight(), matrix, true);

		return imgBitmapRotated;
	}

	// -----------Private Methods----------------------------

	private static int getDisplayWidth(Context context) {
		WindowManager win = ((Activity) context).getWindowManager();
		Display disp = win.getDefaultDisplay();
		int widthDisplay = disp.getWidth();

		return widthDisplay;
	}

	private static int getDisplayHeight(Context context) {
		WindowManager win = ((Activity) context).getWindowManager();
		Display disp = win.getDefaultDisplay();
		int heightDisplay = disp.getHeight();

		return heightDisplay;
	}

}
