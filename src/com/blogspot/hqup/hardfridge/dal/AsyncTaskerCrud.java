package com.blogspot.hqup.hardfridge.dal;

import static com.blogspot.hqup.hardfridge.constants.EnumCrudDao.CLEAR_DB;
import static com.blogspot.hqup.hardfridge.constants.EnumCrudDao.CREATE_DEFAULT_DB;
import static com.blogspot.hqup.hardfridge.constants.EnumCrudDao.DELETE_ITEM;
import static com.blogspot.hqup.hardfridge.constants.EnumCrudDao.INSERT_ITEM;
import static com.blogspot.hqup.hardfridge.constants.EnumCrudDao.UPDATE_ITEM;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.blogspot.hqup.hardfridge.constants.EnumCrudDao;
import com.blogspot.hqup.hardfridge.dbHandle.DbHelper;
import com.blogspot.hqup.hardfridge.entity.Item;
import com.blogspot.hqup.hardfridge.utils.Logger;
import com.blogspot.hqup.hardfridge.utils.ProgressDialoger;
import com.blogspot.hqup.hardfridge.utils.Sounder;
import com.blogspot.hqup.hardfridge.utils.LoaderSpinner;
import com.blogspot.hqup.hardfridge.utils.Toaster;
import com.hqup.hardfridge.R;

/**
 * @author Andrew
 *         <p>
 *         Provides the access into DB in new Thread by AsyncTask to </br>Clear
 *         DB, Create default DB, Delete Item, Update or Insert Item
 *         </p>
 */
public class AsyncTaskerCrud extends AsyncTask<Void, Void, Cursor> {

	private ProgressDialoger pd;
	private Context context;
	private EnumCrudDao doing;
	private CrudDaoBean crudDaoBean;

	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Item item;
	private long _id;

	/**
	 * @param context
	 *            Context
	 * @param item
	 *            specific Item for 'doing' (if it's needed)
	 * @param doing
	 *            EnumCrudDao - is required action
	 */
	public AsyncTaskerCrud(Context context, Item item, EnumCrudDao doing) {

		this.context = context;
		this.doing = doing;
		this.item = item;
		if (item != null)
			_id = item.get_id();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		Logger.v();
		crudDaoBean = new CrudDaoBean(context);
		pd = new ProgressDialoger(context);

		// Get writable DB
		dbHelper = new DbHelper(context);
		try {
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			Toaster.doToastLong(context, R.string.toast_show_db_is_not_opened);
			Logger.v("Catch " + e.getMessage());
		}

		pd.showPD();
	}

	@Override
	protected Cursor doInBackground(Void... params) {

		Logger.v();

		switch (doing) {
		case CREATE_DEFAULT_DB:
			Logger.v();
			crudDaoBean.createDefaultDB(db);
			Sounder.doSound(context, R.raw.joke_sting);
			break;
			
		case CREATE_DEFAULT_DB_HOME:
			Logger.v();
			crudDaoBean.createDefaultDB(db);
			Sounder.doSound(context, R.raw.joke_sting);
			break;

		case CLEAR_DB:
			Logger.v();
			crudDaoBean.clearDB(db);
			Sounder.doSound(context, R.raw.wilhelm_scream);
			break;
			
		case CLEAR_DB_HOME:
			Logger.v();
			crudDaoBean.clearDB(db);
			Sounder.doSound(context, R.raw.wilhelm_scream);
			break;

		case DELETE_ITEM:
			Logger.v("delte item ID = " + _id);
			crudDaoBean.deleteItem(db, _id);
			Sounder.doSound(context, R.raw.wilhelm_scream);
			break;

		case GET_SLECTED_ITEM_CURSOR:
			Logger.v();

			return crudDaoBean.getSelectedItemCursor(db, _id);

		case UPDATE_ITEM:
			Logger.v();
			crudDaoBean.updateItem(db, item);
			break;

		case INSERT_ITEM:
			Logger.v();
			crudDaoBean.insertItem(db, item);
			break;

		default:
			break;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Cursor cursorResult) {

		Logger.v();
		if (doing == CREATE_DEFAULT_DB || doing == CLEAR_DB) {
			new LoaderSpinner(context).setSpinner();
		}

		db.close();
		pd.dismissPD();

		if ((doing == DELETE_ITEM) || (doing == UPDATE_ITEM)
				|| (doing == INSERT_ITEM)) {
			((Activity) context).finish();
			Logger.i("Activity.finish()");
		}

	}

}
