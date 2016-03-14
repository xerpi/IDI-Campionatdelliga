package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by xerpi on 3/14/16.
 */
public final class Utils {

	public final static int PICK_IMAGE_REQUEST = 1;

	public static void startImagePickerActivity(Activity context)
	{
		Intent intent = new Intent(Intent.ACTION_PICK,
			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		context.startActivityForResult(intent, PICK_IMAGE_REQUEST);
	}

	public static String getFilePathFromURI(Activity context, Uri imageUri)
	{
		String filePath = null;
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(imageUri, filePathColumn,
			null, null, null);
		if (cursor != null) {
			try {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				filePath = cursor.getString(columnIndex);
			} finally {
				cursor.close();
			}
		}
		return filePath;
	}
}
