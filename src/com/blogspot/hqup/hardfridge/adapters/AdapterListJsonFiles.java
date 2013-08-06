package com.blogspot.hqup.hardfridge.adapters;

import java.io.File;
import java.util.ArrayList;

import com.blogspot.hqup.hardfridge.utils.JsonFileManager;
import com.hqup.hardfridge.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author http://www.techrepublic.com/blog/app-builder/android-coders-guide-to-
 *         browsing-external-storage/549
 * 
 */
public class AdapterListJsonFiles extends ArrayAdapter<File> {

	private ArrayList<File> listFiles;
	private Context context = null;
	private String folderName;

	public AdapterListJsonFiles(Context context, int textViewResourceId,
			ArrayList<File> items) {
		super(context, textViewResourceId, items);
		this.listFiles = items;
		this.context = context;

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		folderName = prefs.getString(
				context.getResources().getString(R.string.folder_save_key),
				context.getResources().getString(R.string.folder_save_name));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.file_list_row, null);
		}
		TextView filename = null;
		ImageView fileicon = null;
		File file = listFiles.get(position);
		if (file != null) {
			filename = (TextView) v.findViewById(R.id.filename);
			fileicon = (ImageView) v.findViewById(R.id.fileicon);
		}
		if (filename != null) {
			if (position == 0) {
				filename.setText(file.getAbsolutePath());
			} else if (position == 1) {
				filename.setText(file.getAbsolutePath());
			} else {
				filename.setText(file.getName());
			}			

		}
		if (fileicon != null) {
			if (position == 0) {
				fileicon.setImageResource(R.drawable.fm_home);
			} else if (position == 1) {
				fileicon.setImageResource(R.drawable.fm_arrow_up);
			} else if (file.isDirectory()) {

				if (folderName.matches("[a-zA-Z_0-9]*")
						&& String.valueOf(filename.getText()).endsWith(
								folderName)) {
					fileicon.setImageResource(R.drawable.fm_folder_egg);
				} else {
					fileicon.setImageResource(R.drawable.fm_folder);
				}

			} else {
				fileicon.setImageResource(R.drawable.fm_file_lock);

				if (String.valueOf(filename.getText()).endsWith(
						JsonFileManager.NAME_SUFFIX)) {
					fileicon.setImageResource(R.drawable.egg_icon);
				}
			}
		}
		return v;
	}
}
