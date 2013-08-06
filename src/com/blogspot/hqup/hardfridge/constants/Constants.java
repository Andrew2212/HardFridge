package com.blogspot.hqup.hardfridge.constants;

import android.net.Uri;

public class Constants {
	/**
	 * URI of the application inner created DB
	 */
	public static final Uri CONTENT_DB_URI = Uri
			.parse("databases://com.hqup.hardfridge/dbHardFridge");
	/**
	 * URI of resource 'about' = Uri
			.parse("http://hqup.blogspot.com");
	 */
	public static final Uri CONTENT_ABOUT_URI = Uri
			.parse("http://hqup.blogspot.com");

	/**
	 * URI of the web helper
	 */
	public static final Uri HELP_FROM_WEB = Uri.parse("http://google.com");

	/**
	 * Dimension (width) of image for the saving into DB
	 */
	public static final int IMG_DB_WIDTH = 400;
	/**
	 * Dimension (height) of image for the saving into DB
	 */
	public static final int IMG_DB_HEIGHT = 400;

	/**
	 * Dimension (width) of ListView thumbnails</br> see also dimen::
	 * name="itm_one_list_thumbnail_width" 72dp
	 */
	public static final int IMG_THUMBNAIL_LISTVIEW_WIDTH = 72;
	/**
	 * Dimension (height) of ListView thumbnails </br> see also dimen::
	 * name="itm_one_list_thumbnail_height" 72dp
	 */
	public static final int IMG_THUMBNAIL_LISTVIEW_HEIGHT = 72;

}
