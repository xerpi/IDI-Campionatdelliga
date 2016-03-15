package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergi.granell on 3/14/16.
 */
public class DBManager extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "CampionatLliga.db";

	public static abstract class JugadorsEntry implements BaseColumns {
		public static final String TABLE_NAME = "jugadors";
		public static final String COLUMN_NAME_NOM = "nom";
		public static final String COLUMN_NAME_TIPUS = "tipus";
	}

	public static abstract class EquipsEntry implements BaseColumns {
		public static final String TABLE_NAME = "equips";
		public static final String COLUMN_NAME_NOM = "nom";
		public static final String COLUMN_NAME_CIUTAT = "ciutat";
		public static final String COLUMN_NAME_JUGADORS = "jugadors";
		public static final String COLUMN_NAME_ESCUTFILE = "escutfile";
	}

	private static final String SQL_CREATE_JUGADORS_ENTRIES =
		"CREATE TABLE " + JugadorsEntry.TABLE_NAME + " (" +
			JugadorsEntry._ID + " INTEGER PRIMARY KEY," +
			JugadorsEntry.COLUMN_NAME_NOM + " TEXT," +
			JugadorsEntry.COLUMN_NAME_TIPUS + " TEXT" +
			" )";

	private static final String SQL_CREATE_EQUIPS_ENTRIES =
		"CREATE TABLE " + EquipsEntry.TABLE_NAME + " (" +
			EquipsEntry._ID + " INTEGER PRIMARY KEY," +
			EquipsEntry.COLUMN_NAME_NOM + " TEXT," +
			EquipsEntry.COLUMN_NAME_CIUTAT + " TEXT," +
			EquipsEntry.COLUMN_NAME_ESCUTFILE + " TEXT," +
			EquipsEntry.COLUMN_NAME_JUGADORS + " TEXT" +
			" )";

	private static final String SQL_DELETE_JUGADORS_ENTRIES =
		"DROP TABLE IF EXISTS " + JugadorsEntry.TABLE_NAME;

	private static final String SQL_DELETE_EQUIPS_ENTRIES =
		"DROP TABLE IF EXISTS " + EquipsEntry.TABLE_NAME;

	public DBManager(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE_JUGADORS_ENTRIES);
		db.execSQL(SQL_CREATE_EQUIPS_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(SQL_DELETE_JUGADORS_ENTRIES);
		db.execSQL(SQL_DELETE_EQUIPS_ENTRIES);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		onUpgrade(db, oldVersion, newVersion);
	}

	public void dropTables()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(SQL_DELETE_JUGADORS_ENTRIES);
		db.execSQL(SQL_DELETE_EQUIPS_ENTRIES);
	}

	public void insertJugador(Jugador jugador)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(JugadorsEntry.COLUMN_NAME_NOM, jugador.getNom());
		contentValues.put(JugadorsEntry.COLUMN_NAME_TIPUS, jugador.getTipus().toString());
		db.insert(JugadorsEntry.TABLE_NAME, null, contentValues);
	}

	public Jugador queryJugador(String nom)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			JugadorsEntry.COLUMN_NAME_NOM,
			JugadorsEntry.COLUMN_NAME_TIPUS
		};

		String selection = JugadorsEntry.COLUMN_NAME_NOM + "=?";
		String[] selectionArgs = {
			nom
		};
		String sortOrder = JugadorsEntry.COLUMN_NAME_NOM + " DESC";

		Cursor cursor = db.query(
			JugadorsEntry.TABLE_NAME,  // The table to query
			projection,                // The columns to return
			selection,                 // The columns for the WHERE clause
			selectionArgs,             // The values for the WHERE clause
			null,                      // don't group the rows
			null,                      // don't filter by row groups
			sortOrder                  // The sort order
		);

		if (cursor == null)
			return null;

		cursor.moveToFirst();

		Jugador.TipusJugador tipus = Jugador.TipusJugador.valueOf(
			cursor.getString(cursor.getColumnIndexOrThrow(JugadorsEntry.COLUMN_NAME_TIPUS))
		);

		return new Jugador(nom, tipus);
	}

	public void updateJugador(Jugador jugador)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(JugadorsEntry.COLUMN_NAME_TIPUS, jugador.getTipus().toString());

		String selection = JugadorsEntry.COLUMN_NAME_NOM + " LIKE ?";
		String[] selectionArgs = {
			jugador.getNom()
		};

		int count = db.update(
			JugadorsEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);
	}

	private String getJugadorsEquipAsJSONString(Equip equip)
	{
		JSONObject jsonJugadors = new JSONObject();
		try {
			JSONArray array = new JSONArray();
			for (Jugador j: equip.getJugadors()) {
				array.put(j.getNom());
			}
			jsonJugadors.put("jugadors", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonJugadors.toString();
	}

	public void insertEquip(Equip equip)
	{
		String jsonJugadorsString = getJugadorsEquipAsJSONString(equip);

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(EquipsEntry.COLUMN_NAME_NOM, equip.getNom());
		contentValues.put(EquipsEntry.COLUMN_NAME_CIUTAT, equip.getCiutat());
		contentValues.put(EquipsEntry.COLUMN_NAME_ESCUTFILE, equip.getEscutFile());
		contentValues.put(EquipsEntry.COLUMN_NAME_JUGADORS, jsonJugadorsString);
		db.insert(EquipsEntry.TABLE_NAME, null, contentValues);
	}

	public Equip queryEquip(String nom)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			EquipsEntry.COLUMN_NAME_NOM,
			EquipsEntry.COLUMN_NAME_CIUTAT,
			EquipsEntry.COLUMN_NAME_ESCUTFILE,
			EquipsEntry.COLUMN_NAME_JUGADORS
		};

		String selection = EquipsEntry.COLUMN_NAME_NOM + "=?";
		String[] selectionArgs = {
			nom
		};
		String sortOrder = EquipsEntry.COLUMN_NAME_NOM + " DESC";

		Cursor cursor = db.query(
			EquipsEntry.TABLE_NAME,  // The table to query
			projection,                // The columns to return
			selection,                 // The columns for the WHERE clause
			selectionArgs,             // The values for the WHERE clause
			null,                      // don't group the rows
			null,                      // don't filter by row groups
			sortOrder                  // The sort order
		);

		if (cursor == null)
			return null;

		cursor.moveToFirst();

		String ciutat = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_CIUTAT)
		);

		String escutFile = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_ESCUTFILE)
		);

		String jsonJugadorsString = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_JUGADORS)
		);


		JSONObject jsonJugadors = null;
		try {
			jsonJugadors = new JSONObject(jsonJugadorsString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray jugadorsArray = null;
		try {
			jugadorsArray = jsonJugadors.getJSONArray("jugadors");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<Jugador> jugadors = new ArrayList<Jugador>();
		for (int i = 0; i < jugadorsArray.length(); i++) {
			try {
				String nomJugador = jugadorsArray.getString(i);
				jugadors.add(this.queryJugador(nomJugador));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		Equip equip = new Equip(nom, ciutat, jugadors);
		equip.setEscutFile(escutFile);

		return equip;
	}

	public void updateEquip(Equip equip)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(EquipsEntry.COLUMN_NAME_CIUTAT, equip.getCiutat());
		values.put(EquipsEntry.COLUMN_NAME_ESCUTFILE, equip.getEscutFile());
		values.put(EquipsEntry.COLUMN_NAME_JUGADORS, getJugadorsEquipAsJSONString(equip));

		String selection = EquipsEntry.COLUMN_NAME_NOM + " LIKE ?";
		String[] selectionArgs = {
			equip.getNom()
		};

		int count = db.update(
			EquipsEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);
	}
}