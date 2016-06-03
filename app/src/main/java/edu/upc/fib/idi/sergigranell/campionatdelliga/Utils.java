package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by xerpi on 3/14/16.
 */
public final class Utils {

	public final static int NUM_EQUIPS = 10;
	public final static int NUM_PARTITS_PER_JORNADA = NUM_EQUIPS / 2;
	public final static int NUM_JUGADORS_EQUIP = 12;
	public final static int NUM_JUGADORS_TITULARS = 5;
	public final static int NUM_JORNADES_INICIALS = 2;
	public final static int NUM_JUGADORS_TOTALS = NUM_EQUIPS * NUM_JUGADORS_EQUIP;

	public final static int PICK_IMAGE_REQUEST = 1;

	public final static int ESCUT_IMAGE_MAX_SIZE = 256;

	public final static String DATE_FORMAT_STRING = "HH:mm:ss dd-MM-yyyy";

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

	public static String dateToString(Date date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
			DATE_FORMAT_STRING, Locale.getDefault());
		return dateFormat.format(date);
	}

	public static Date stringToDate(String dateString)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
			DATE_FORMAT_STRING, Locale.getDefault());
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class IntegerValueFormatter implements ValueFormatter {
		@Override
		public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler)
		{
			return String.valueOf((int)v);
		}
	};

	public static String getNextJugadorName(DBManager dbmgr, Context context)
	{
		List<Jugador> jugadors = dbmgr.queryAllJugadors();
		if (jugadors == null)
			return null;

		int numActual = 0;
		for (Jugador j: jugadors) {
			String nom = j.getNom();
			for (int i = nom.length() - 1; i >= 0; i--) {
				if (nom.charAt(i) == ' ') {
					numActual = Integer.valueOf(nom.substring(i + 1));
				}
			}
		}
		return new String("Jugador " + (numActual + 1));
	}

}
