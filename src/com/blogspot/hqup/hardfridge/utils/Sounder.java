package com.blogspot.hqup.hardfridge.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.hqup.hardfridge.R;

/**
 * @author Andrew
 *         <p>
 *         Launches MediaPlayer if that allowed into Preferences
 *         </p>
 */
public class Sounder {

	private static MediaPlayer mp;

	/**
	 * @param context
	 * @param id
	 *            some sound from res/raw
	 *            <p>
	 *            Launches MediaPlayer into the new Thread (AsyncTask) if that
	 *            allowed into Preferences
	 *            <p>
	 */
	public static void doSound(Context context, int id) {
		Logger.v();
		boolean chbSound;
//		try{
//		mp = new MediaPlayer();
		mp = MediaPlayer.create(context, id);
		Sounder.SounderAsyncTask sounderAsyncTask = new Sounder.SounderAsyncTask();

		// Get preferences from Preferences.java
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		// read value from Sound-CheckBox into CheckBoxPreference
		chbSound = prefs.getBoolean(
				context.getResources().getString(R.string.sound_key), true);

		if (chbSound) {
			sounderAsyncTask.execute();
		}

		return;
	}

	/**
	 * @author Andrew
	 *         <p>
	 *         Plays some sound into the new Thread
	 *         <p>
	 */
	private static class SounderAsyncTask extends AsyncTask<Void, Void, Void> {


		@Override
		protected Void doInBackground(Void... params) {
			Logger.v();
			if(mp != null)
			mp.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (mp != null && !mp.isPlaying()) {
				mp.reset();
				mp.release();
				mp = null;
			}
		}

	}

}