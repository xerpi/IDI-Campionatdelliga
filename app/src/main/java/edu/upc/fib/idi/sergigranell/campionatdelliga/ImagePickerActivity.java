package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * Created by xerpi on 3/14/16.
 */
public class ImagePickerActivity extends Activity {

	private static final int PICK_IMAGE_REQUEST = 1;

	private String selectedImagePath;


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent imagePickerIntent = new Intent(Intent.ACTION_PICK,
			android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		imagePickerIntent.setType("image/*");
		this.startActivityForResult(imagePickerIntent, PICK_IMAGE_REQUEST);
	}

	/*public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK) {
			if (requestCode == PICK_IMAGE_REQUEST) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getFilePathFromURI(this, selectedImageUri);
			}
		}
	}*/

	public String getSelectedImagePath()
	{
		return selectedImagePath;
	}

	public String getFilePathFromURI(Activity context, Uri imageUri)
	{
		String filePath = null;
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(imageUri,
			filePathColumn, null, null, null);
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
