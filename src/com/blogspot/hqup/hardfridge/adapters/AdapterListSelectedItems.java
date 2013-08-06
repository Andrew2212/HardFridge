package com.blogspot.hqup.hardfridge.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.blogspot.hqup.hardfridge.dbHandle.DbHelper;
import com.blogspot.hqup.hardfridge.utils.ImageManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.hqup.hardfridge.R;

/**
 * @author Andrew
 *         <p>
 *         Realizes pattern ViewHolder and sets values into ListView of
 *         SelectedItems
 */
public class AdapterListSelectedItems extends SimpleCursorAdapter {

	private Cursor cursor;

	private LayoutInflater mLayoutInflater;
	private static int INDEX_COLUMN_NAME;
	private static int INDEX_COLUMN_IMAGE;
	private static int INDEX_COLUMN_RATING;

	public AdapterListSelectedItems(Context context, int layout, Cursor cursor,
			String[] from, int[] to) {
		super(context, layout, cursor, from, to);

		Logger.v();
		this.cursor = cursor;
		
		mLayoutInflater = LayoutInflater.from(context);
		INDEX_COLUMN_NAME = cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NAME);
		INDEX_COLUMN_IMAGE = cursor.getColumnIndexOrThrow(DbHelper.IMAGE);
		INDEX_COLUMN_RATING = cursor
				.getColumnIndexOrThrow(DbHelper.TOKEN_RATING);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Logger.v("******position = " + position);

		ViewHolder holder;
		View v = convertView;
		// Create View v for each position
		if (v == null) {
			v = mLayoutInflater.inflate(R.layout.item_list_row, null);
			holder = new ViewHolder();
			holder.itemImg = (ImageView) v.findViewById(R.id.ivItemOnListImg);
			holder.itemTxt = (TextView) v.findViewById(R.id.tvItemOnListText);
			holder.itemRating = (RatingBar) v
					.findViewById(R.id.ratingItemOnList);
			v.setTag(holder);
		} else { // Use form that was be earlier created by adapter
			holder = (ViewHolder) convertView.getTag();
		}

		// Set value into the View for each position
		if (cursor.moveToPosition(position)) {

			String name = cursor.getString(INDEX_COLUMN_NAME);
			holder.itemTxt.setText(name);

			byte[] imgByteArr = cursor.getBlob(INDEX_COLUMN_IMAGE);
			if (imgByteArr != null) {

				Bitmap imgBitmap = ImageManager
						.createBitmapFromByteArr(imgByteArr);
				// Scale bitmap to 80x80
				imgBitmap = ImageManager
						.scaleBitmapForListView( imgBitmap);

				
				Logger.i("imgBitmap size = " + imgBitmap.getWidth() + " x "
						+ imgBitmap.getHeight());

				holder.itemImg.setImageBitmap(imgBitmap);
			}

			float rating = cursor.getFloat(INDEX_COLUMN_RATING);
			holder.itemRating.setRating(rating);

		}

		return v;
	}

	/**
	 * Helps us to realize pattern ViewHolder
	 */
	static class ViewHolder {
		TextView itemTxt;
		ImageView itemImg;
		RatingBar itemRating;
	}

}
