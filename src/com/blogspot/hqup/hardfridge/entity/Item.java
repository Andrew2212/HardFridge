package com.blogspot.hqup.hardfridge.entity;

import android.database.Cursor;

import com.blogspot.hqup.hardfridge.dbHandle.DbHelper;

public class Item {

	private long _id;
	private byte[] img;
	private String name;
	private String token_1;
	private String token_2;
	private String token_3;
	private float rating;
	private String description;
	private String phoneNum;
	private String choice;

	public Item() {

	}

	/**
	 * @param itemID
	 *            </br>To wrap ID in Item for AsyncTaskerCrud
	 */
	public Item(String itemID) {
		long _id = Long.parseLong(itemID);
		set_id(_id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken_1() {
		return token_1;
	}

	public void setToken_1(String token_1) {
		this.token_1 = token_1;
	}

	public String getToken_2() {
		return token_2;
	}

	public void setToken_2(String token_2) {
		this.token_2 = token_2;
	}

	public String getToken_3() {
		return token_3;
	}

	public void setToken_3(String token_3) {
		this.token_3 = token_3;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((token_1 == null) ? 0 : token_1.hashCode());
		result = prime * result + ((token_2 == null) ? 0 : token_2.hashCode());
		result = prime * result + ((token_3 == null) ? 0 : token_3.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (token_1 == null) {
			if (other.token_1 != null)
				return false;
		} else if (!token_1.equals(other.token_1))
			return false;
		if (token_2 == null) {
			if (other.token_2 != null)
				return false;
		} else if (!token_2.equals(other.token_2))
			return false;
		if (token_3 == null) {
			if (other.token_3 != null)
				return false;
		} else if (!token_3.equals(other.token_3))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item, ID = " + get_id() + " [name=" + name + ", token_1="
				+ token_1 + ", token_2=" + token_2 + ", token_3=" + token_3
				+ ", rating=" + rating + ", phoneNum=" + phoneNum + "]";
	}

	/**
	 * @param cursor current Cursor
	 * <p>Fills item fields by cursor data</p>
	 */
	public void fillItemFieldsFromCursor(Cursor cursor) {

		if (cursor.moveToFirst()) {

			set_id(cursor.getLong(cursor.getColumnIndex(DbHelper.COLUMN_ID)));

			setName(cursor.getString(cursor
					.getColumnIndex(DbHelper.COLUMN_NAME)));

			setImg(cursor.getBlob(cursor.getColumnIndex(DbHelper.IMAGE)));

			setToken_1(cursor.getString(cursor
					.getColumnIndex(DbHelper.TOKEN_ONE)));

			setToken_2(cursor.getString(cursor
					.getColumnIndex(DbHelper.TOKEN_TWO)));

			setToken_3(cursor.getString(cursor
					.getColumnIndex(DbHelper.TOKEN_THREE)));

			setRating(cursor.getFloat(cursor
					.getColumnIndex(DbHelper.TOKEN_RATING)));

			setDescription(cursor.getString(cursor
					.getColumnIndex(DbHelper.DESCRIPTION)));

			String phoneNum = cursor.getString(cursor
					.getColumnIndex(DbHelper.PHONE_NUMBER));
			if ((phoneNum != null) && (2 < phoneNum.length())) {
				setPhoneNum(phoneNum);
			}

		}
	}

}
