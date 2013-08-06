package com.blogspot.hqup.hardfridge.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.blogspot.hqup.hardfridge.entity.Item;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.hqup.hardfridge.R;

/**
 * 
 * @author Andrew</br> http://code.google.com/p/json-io/
 */
public class JsonFileManager {

	/**
	 * It's path where we will write and read our *hf.json files</br> It's
	 * 'public' because is needed for Toast after the saving into
	 * ListSelectedActivity
	 */
	public static File pathExternalStorage;

	/**
	 * Suffix (extension) for saved JSON files
	 */
	public static final String NAME_SUFFIX = ".hf";

	// ----------Public Methods---------------------------

	/**
	 * @param item
	 * @param context
	 * @return String filePath = file.getPath()
	 *         <p>
	 *         Saves Item as JSON file onto sdcard and sends this file
	 *         </p>
	 */
	public static String sendItem(Context context, Item item) {
		Logger.v();

		// Addresses that should be delivered to.
		String yourEmail = null;
		/*
		 * Save Item as template JSON file onto sdcard
		 */
		String filePath = saveItemAsJsonFileIntoExternalStorageDir(context,
				item);
		String uriString = "file://" + filePath;
		Logger.v("uriString = " + uriString);

		Intent intentEmail = new Intent(Intent.ACTION_SEND);
		intentEmail.putExtra(Intent.EXTRA_EMAIL, yourEmail);
		intentEmail.setType("application/hf");
		intentEmail.putExtra(Intent.EXTRA_STREAM, Uri.parse(uriString));

		((Activity) context).startActivity(Intent.createChooser(intentEmail,
				context.getResources().getString(R.string.email_choose)));

		return filePath;
	}

	/**
	 * @param context
	 * @param filePath
	 *            - path to read file
	 * @return item = (Item) new JsonReader(inputStream).readObject();
	 *         </br>InputStream to Java object</br> InputStream (could be from a
	 *         File, etc.) is supplying an unknown amount of JSON.
	 */
	public static Item readJsonFile(Context context, String filePath) {
		Logger.v();
		Item item = null;

		Logger.v("filePath = " + filePath);
		File fileJson = new File(filePath);

		try {
			// Create InputStream
			InputStream inputStream = new FileInputStream(fileJson);
			// The JsonReader returns the Java object graph it represents.
			JsonReader jsonReader = new JsonReader(inputStream);
			item = (Item) jsonReader.readObject();

			jsonReader.close();
			inputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.v(fileJson.toString());

		// Check - whether the imported files should be removed
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean isShouldBeDeleted = prefs.getBoolean(context.getResources()
				.getString(R.string.del_import_file_key), true);

		if (isShouldBeDeleted && fileJson.exists()) {
			Logger.v("file.delete()");
			fileJson.delete();
		}
		Logger.v(item.toString());
		return item;
	}

	/**
	 * @param context
	 *            Context
	 * @param item
	 *            - Item that will be saved into specify directory as File
	 * 
	 * @return String filePath = file.getPath()
	 */
	public static String saveItemAsJsonFileIntoExternalStorageDir(
			Context context, Item item) {

		Logger.v();
		item.set_id(0);// In order to avoid Exception by insert(?)

		// Get fileName
		String fileName = getNameOfFileForSaving(item);

		// Get file directory
		File dir = getPathExternalStorage(context);

		// Create file with 'fileName' into 'fileDir'
		File file = createFileForSavingItem(dir, fileName);

		// Write file as JSON in ExternalStorage
		writeItemToFile(item, file);

		String filePath = file.getPath();
		Logger.v("fileUri = " + filePath);

		return filePath;
	}

	/**
	 * @param context
	 * @return dir = Environment
	 *         .getExternalStoragePublicDirectory(folderName);</br> Here
	 *         'folderName' is string from Preferences (if it exists) or
	 *         DIRECTORY_DOWNLOADS
	 */
	public static File getPathExternalStorage(Context context) {
		/*
		 * Create a path where we will place our JSON in the user's public SOME
		 * directory. Note that you should be careful about what you place here,
		 * since the user often manages these files.
		 */

		// Get 'folderName' from Preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String folderName = prefs.getString(
				context.getResources().getString(R.string.folder_save_key),
				context.getResources().getString(R.string.folder_save_name));
		Logger.v("folderName = " + folderName);

		if (folderName.matches("[a-zA-Z_0-9]*")) {
			Logger.v();
			pathExternalStorage = Environment
					.getExternalStoragePublicDirectory(folderName);
		} else {
			Logger.v();
			pathExternalStorage = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		}
		// Creates the directory named by this file
		pathExternalStorage.mkdirs();

		Logger.v("pathExternalStorage = " + pathExternalStorage.toString());
		return pathExternalStorage;
	}

	// ------------------Private Methods--------------------------------

	/**
	 * @param dir
	 * @param fileName
	 * @return File file = new File(dir, fileName);
	 */
	private static File createFileForSavingItem(File dir, String fileName) {
		Logger.v();

		File file = new File(dir, fileName);

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.v(file.getPath());
		Logger.i("file.getTotalSpace() = " + file.getTotalSpace());
		return file;
	}

	/**
	 * @param item
	 * @return String fileName = item.getName() + "_" + strFormat +
	 *         NAME_SUFFICS;
	 */
	private static String getNameOfFileForSaving(Item item) {
		// Create file name
		SimpleDateFormat s = new SimpleDateFormat("hhmmss", Locale.US);
		String strFormat = s.format(new Date());
		String fileName = item.getName() + "_" + strFormat + NAME_SUFFIX;

		return fileName;
	}

	/**
	 * @param item
	 *            - what will be saved
	 * @param file
	 *            - where it'll be saved
	 */
	private static void writeItemToFile(Item item, File file) {
		Logger.v();
		try {
			// Create OutputStream
			OutputStream outStream = null;
			outStream = new FileOutputStream(file);

			// Create JsonWriter
			JsonWriter jsonWriter = new JsonWriter(outStream);

			// Java object is written to an output stream in JSON format.
			jsonWriter.write(item);
			Logger.v(file.toString());

			jsonWriter.close();
			outStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Logger.v("fileName = " + file.getName());
	}

}
