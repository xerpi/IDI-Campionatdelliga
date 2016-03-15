package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by xerpi on 3/14/16.
 */
public final class Utils {

	public final static int PICK_IMAGE_REQUEST = 1;

	public final static int ESCUT_IMAGE_MAX_SIZE = 256;

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

	public static Bitmap loadEscutBitmap(Activity context, String escutFile)
	{
		Bitmap escutBitmap;
		if (escutFile == null)
			escutBitmap = BitmapFactory.decodeResource(context.getResources(),
				android.R.drawable.ic_menu_gallery);
		else
			escutBitmap = BitmapFactory.decodeFile(escutFile);
		return resizeBitmapIfNeeded(escutBitmap, ESCUT_IMAGE_MAX_SIZE);
	}

	public static Bitmap resizeBitmapIfNeeded(Bitmap original, int max_size)
	{
		int width = original.getWidth();
		int height = original.getHeight();

		if (width > height) {
			float ratio = (float) width / max_size;
			width = max_size;
			height = (int)(height / ratio);
		} else if (height > width) {
			float ratio = (float) height / max_size;
			height = max_size;
			width = (int)(width / ratio);
		} else {
			height = max_size;
			width = max_size;
		}

		return Bitmap.createScaledBitmap(original, width, height, true);
	}
}
