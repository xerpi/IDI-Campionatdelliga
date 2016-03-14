package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by xerpi on 3/14/16.
 */
public class Escut {

	public static final int BITMAP_MAX_SIZE = 256;

	private String filePath;
	private Bitmap bitmap;

	public Escut(String filePath)
	{
		this.filePath = filePath;
		if (filePath != null) {
			this.bitmap = BitmapFactory.decodeFile(filePath);
			if (this.bitmap == null) {
				loadDefaultEscutBitmap();
			}
		} else {
			loadDefaultEscutBitmap();
		}
	}

	private void loadDefaultEscutBitmap()
	{
		//this.bitmap = new Bitmap();
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public Bitmap getBitmap()
	{
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}
}
