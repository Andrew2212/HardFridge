package com.blogspot.hqup.hardfridge;

import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.blogspot.hqup.hardfridge.adapters.AdapterListJsonFiles;
import com.blogspot.hqup.hardfridge.utils.AlertDialoger;
import com.blogspot.hqup.hardfridge.utils.JsonFileManager;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.blogspot.hqup.hardfridge.utils.Sounder;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

/**
 * @author http://www.techrepublic.com/blog/app-builder/android-coders-guide-to-
 *         browsing-external-storage/549
 * 
 */
public class ListJsonFilesActivity extends ListActivity {
	private File mCurrentNode = null;
	private File mLastNode = null;
	private File mRootNode = null;
	private ArrayList<File> mFiles = new ArrayList<File>();
	private AdapterListJsonFiles mAdapter = null;

	ListView listViewFiles;
	private final int IMPORT_FILE = 1;
	private final int DELETE_FILE = 2;

	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_list_json_files);
		context = this;

		mAdapter = new AdapterListJsonFiles(this, R.layout.file_list_row,
				mFiles);
		setListAdapter(mAdapter);
		if (savedInstanceState != null) {
			mRootNode = (File) savedInstanceState.getSerializable("root_node");
			mLastNode = (File) savedInstanceState.getSerializable("last_node");
			mCurrentNode = (File) savedInstanceState
					.getSerializable("current_node");
		}

		// Register ContextMenu on ListView
		listViewFiles = (ListView) findViewById(android.R.id.list);
		registerForContextMenu(listViewFiles);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshFileList();
	}

	private void refreshFileList() {
		if (mRootNode == null)
			mRootNode = new File(Environment.getExternalStorageDirectory()
					.toString());
		if (mCurrentNode == null)
			mCurrentNode = mRootNode;
		mLastNode = mCurrentNode;
		File[] files = mCurrentNode.listFiles();
		mFiles.clear();
		mFiles.add(mRootNode);
		mFiles.add(mLastNode);
		if (files != null) {
			for (int i = 0; i < files.length; i++)
				mFiles.add(files[i]);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("root_node", mRootNode);
		outState.putSerializable("current_node", mCurrentNode);
		outState.putSerializable("last_node", mLastNode);
		super.onSaveInstanceState(outState);
	}

	/**
	 * ListView on click handler.
	 */
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		File file = (File) parent.getItemAtPosition(position);
		String fileName = file.getName();

		if (position == 1) {
			if (mCurrentNode.compareTo(mRootNode) != 0) {
				mCurrentNode = file.getParentFile();
				refreshFileList();
			}
		} else if (file.isDirectory()) {
			mCurrentNode = file;
			refreshFileList();
		} else {
			Logger.v("Path: " + file.getPath());
			if (fileName.endsWith(JsonFileManager.NAME_SUFFIX)) {
				Toaster.doToastShort(this,
						R.string.toast_show_file_yes_imported);
			} else {
				Toaster.doToastShort(this,
						R.string.toast_show_file_not_imported);
			}
		}
	}

	// ------------ContextMenu----------------------
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, IMPORT_FILE, 0,
				getResources().getString(R.string.menu_ctx_import));
		menu.add(0, DELETE_FILE, 0,
				getResources().getString(R.string.menu_ctx_delete));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Sounder.doSound(this, R.raw.beep);

		AdapterContextMenuInfo adapterInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = adapterInfo.position;

		// File selectedFile = (File) mAdapter.getItem(position);
		final File selectedFile = (File) mAdapter.getItem(position);
		String fileName = selectedFile.getName();

		if (!fileName.endsWith(JsonFileManager.NAME_SUFFIX)) {
			Toaster.doToastShort(context, R.string.toast_show_nothing_happens);
			return true;
		}

		switch (item.getItemId()) {

		case IMPORT_FILE:
			Logger.v("Import file = " + fileName);
			if (fileName.endsWith(JsonFileManager.NAME_SUFFIX)) {

				// Go to ItemOneEditActivity and edit inserted item
				Intent intent = new Intent(this, ItemOneEditActivity.class);
				intent.putExtra(ItemOneEditActivity.VALUE,
						selectedFile.getPath());
				intent.putExtra(ItemOneEditActivity.ORDER,
						ItemOneEditActivity.IMPORT);
				startActivity(intent);
				break;
			}
			break;

		case DELETE_FILE:
			Logger.v("Delete file = " + fileName);
			@SuppressWarnings("unused")
			AlertDialoger alertDialoger = new AlertDialoger(context,
					R.string.dialog_msg_file_delete) {

				@Override
				public void doThatIfYes() {

					// Delete file
					if (selectedFile.exists()) {
						selectedFile.delete();
						mFiles.remove(selectedFile);
						mAdapter.notifyDataSetChanged();
					}

					Toaster.doToastShort(context,
							R.string.toast_show_file_is_deleted);
				}
			};
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);
	}
}
