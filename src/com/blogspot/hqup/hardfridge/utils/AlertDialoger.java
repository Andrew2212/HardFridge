package com.blogspot.hqup.hardfridge.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.hqup.hardfridge.R;

/**
 * @author Andrew
 *<p>Makes custom AlertDialog</p>
 */
public abstract class AlertDialoger {

	private Context context;

	private int message;
	private int yes;
	private int no;

	/**
	 * @param context
	 * @param message Dialog message
	 * @param yes  stringFromResources onto button 'Yes'
	 * @param no stringFromResources onto button 'Yes'
	 */
	public AlertDialoger(Context context, int message,
			int yes, int no) {
		this.context = context;
		this.message = message;
		this.yes = yes;
		this.no = no;
		buildDialog();
	}

	public AlertDialoger(Context context, int message) {
		this.context = context;
		this.context = context;
		this.message = message;
		yes = R.string.btntext_yes;
		no = R.string.btntext_no;
		buildDialog();
	}

	private void buildDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(
				((Activity) context).getResources().getString(message))
				.setPositiveButton(
						((Activity) context).getResources().getString(yes),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								Sounder.doSound(context, R.raw.beep);
								
								doThatIfYes();
								
							}
						})

				.setNegativeButton(
						((Activity) context).getResources().getString(no),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {

									doThatIfNo();
	
								dialog.cancel();
							}
						});
		@SuppressWarnings("unused")
		AlertDialog alert = builder.show();

	}

	/**
	 * Makes that is needed if we click "Yes" button </br>(i.e. what exists into
	 * onClick(DialogInterface dialog, int id))
	 */
	public abstract void doThatIfYes();

	/**
	 * Makes that is needed if we click "No" button </br>(i.e. what exists into
	 * onClick(DialogInterface dialog, int id))
	 */
	public void doThatIfNo() {
		Logger.v();

	}

}
