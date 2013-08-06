package com.blogspot.hqup.hardfridge.utils;

import com.hqup.hardfridge.R;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author Andrew
 *<p>Makes custom ProgressDialog simply</p>
 */
public class ProgressDialoger {
	
	private ProgressDialog pd;
	private Context context;
	
	public ProgressDialoger(Context context) {
		this.context = context;
	}

	public void showPD(){
		pd = new ProgressDialog(context);
		pd.setMessage(context.getText(R.string.dialog_msg_progress_bar_message));
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.show();
	}
	
	public void dismissPD(){
		pd.dismiss();
	}

}
