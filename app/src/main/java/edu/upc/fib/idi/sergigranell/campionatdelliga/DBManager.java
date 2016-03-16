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
import java.util.Date;
import java.util.List;

/**
 * Created by sergi.granell on 3/14/16.
 */
public class DBManager extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "CampionatLliga.db";

	public static abstract class JugadorsEntry {
		public static final String TABLE_NAME = "jugadors";
		public static final String COLUMN_NAME_NOM = "nom";
		public static final String COLUMN_NAME_TIPUS = "tipus";
	}

	public static abstract class EquipsEntry {
		public static final String TABLE_NAME = "equips";
		public static final String COLUMN_NAME_NOM = "nom";
		public static final String COLUMN_NAME_CIUTAT = "ciutat";
		public static final String COLUMN_NAME_JUGADORS = "jugadors";
		public static final String COLUMN_NAME_ESCUTFILE = "escutfile";
	}

	public static abstract class PartitsEntry {
		public static final String TABLE_NAME = "partits";
		public static final String COLUMN_NAME_EQUIP_LOCAL = "equip_local";
		public static final String COLUMN_NAME_EQUIP_VISITANT = "equip_visitant";
		public static final String COLUMN_NAME_DATA = "data";
		public static final String COLUMN_NAME_GOLS_LOCAL = "gols_local";
		public static final String COLUMN_NAME_GOLS_VISITANT = "gols_visitant";
	}

	private static final String SQL_CREATE_JUGADORS_ENTRIES =
		"CREATE TABLE " + JugadorsEntry.TABLE_NAME + " (" +
			JugadorsEntry.COLUMN_NAME_NOM + " TEXT PRIMARY KEY," +
			JugadorsEntry.COLUMN_NAME_TIPUS + " TEXT" +
		" )";

	private static final String SQL_CREATE_EQUIPS_ENTRIES =
		"CREATE TABLE " + EquipsEntry.TABLE_NAME + " (" +
			EquipsEntry.COLUMN_NAME_NOM + " TEXT PRIMARY KEY," +
			EquipsEntry.COLUMN_NAME_CIUTAT + " TEXT," +
			EquipsEntry.COLUMN_NAME_ESCUTFILE + " TEXT," +
			EquipsEntry.COLUMN_NAME_JUGADORS + " TEXT" +
		" )";

	private static final String SQL_CREATE_PARTITS_ENTRIES =
		"CREATE TABLE " + PartitsEntry.TABLE_NAME + " (" +
			PartitsEntry.COLUMN_NAME_EQUIP_LOCAL + " TEXT," +
			PartitsEntry.COLUMN_NAME_EQUIP_VISITANT + " TEXT," +
			PartitsEntry.COLUMN_NAME_DATA + " TEXT," +
			PartitsEntry.COLUMN_NAME_GOLS_LOCAL + " INTEGER," +
			PartitsEntry.COLUMN_NAME_GOLS_VISITANT + " INTEGER," +
			"PRIMARY KEY (" +
				PartitsEntry.COLUMN_NAME_EQUIP_LOCAL + "," +
				PartitsEntry.COLUMN_NAME_EQUIP_VISITANT + "," +
				PartitsEntry.COLUMN_NAME_DATA +
			" )" +
		" )";

	private static final String SQL_DELETE_JUGADORS_ENTRIES =
		"DROP TABLE IF EXISTS " + JugadorsEntry.TABLE_NAME;

	private static final String SQL_DELETE_EQUIPS_ENTRIES =
		"DROP TABLE IF EXISTS " + EquipsEntry.TABLE_NAME;

	private static final String SQL_DELETE_PARTITS_ENTRIES =
		"DROP TABLE IF EXISTS " + PartitsEntry.TABLE_NAME;

	public DBManager(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE_JUGADORS_ENTRIES);
		db.execSQL(SQL_CREATE_EQUIPS_ENTRIES);
		db.execSQL(SQL_CREATE_PARTITS_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(SQL_DELETE_JUGADORS_ENTRIES);
		db.execSQL(SQL_DELETE_EQUIPS_ENTRIES);
		db.execSQL(SQL_DELETE_PARTITS_ENTRIES);
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
		db.execSQL(SQL_DELETE_PARTITS_ENTRIES);
	}

	public void insertJugador(Jugador jugador)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(JugadorsEntry.COLUMN_NAME_NOM, jugador.getNom());
		contentValues.put(JugadorsEntry.COLUMN_NAME_TIPUS, jugador.getTipus().toString());
		db.insert(JugadorsEntry.TABLE_NAME, null, contentValues);
	}

	private Jugador getJugadorFromCursor(Cursor cursor)
	{
		String nom = cursor.getString(
			cursor.getColumnIndexOrThrow(JugadorsEntry.COLUMN_NAME_NOM)
		);
		Jugador.TipusJugador tipus = Jugador.TipusJugador.valueOf(
			cursor.getString(cursor.getColumnIndexOrThrow(JugadorsEntry.COLUMN_NAME_TIPUS))
		);

		Jugador jugador = new Jugador(nom, tipus);

		return jugador;
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

		return getJugadorFromCursor(cursor);
	}

	public List<Jugador> queryAllJugadors()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(JugadorsEntry.TABLE_NAME,
			null, null, null, null, null, null);

		if (cursor == null)
			return null;

		List<Jugador> llistaJugadors = new ArrayList<Jugador>();

		while (cursor.moveToNext()) {
			llistaJugadors.add(getJugadorFromCursor(cursor));
		}

		return llistaJugadors;
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

	public boolean existsJugador(String nomJugador)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			JugadorsEntry.COLUMN_NAME_NOM
		};

		String selection = JugadorsEntry.COLUMN_NAME_NOM + "=?";
		String[] selectionArgs = {
			nomJugador
		};

		Cursor cursor = db.query(
			JugadorsEntry.TABLE_NAME,  // The table to query
			projection,                // The columns to return
			selection,                 // The columns for the WHERE clause
			selectionArgs,             // The values for the WHERE clause
			null,                      // don't group the rows
			null,                      // don't filter by row groups
			null                  // The sort order
		);

		if (cursor == null)
			return false;

		return cursor.getCount() > 0;
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

	private List<Jugador> getJugadorsEquipFromJSONString(String jsonJugadorsString)
	{
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

		return jugadors;
	}

	private Equip getEquipFromCursor(Cursor cursor)
	{
		String nom = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_NOM)
		);
		String ciutat = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_CIUTAT)
		);
		String escutFile = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_ESCUTFILE)
		);
		String jsonJugadorsString = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_JUGADORS)
		);

		List<Jugador> jugadors = getJugadorsEquipFromJSONString(jsonJugadorsString);

		Equip equip = new Equip(nom, ciutat, jugadors);
		equip.setEscutFile(escutFile);

		return equip;
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

		return getEquipFromCursor(cursor);
	}

	public List<Equip> queryAllEquips()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(EquipsEntry.TABLE_NAME,
			null, null, null, null, null, null);

		if (cursor == null)
			return null;

		List<Equip> llistaEquips = new ArrayList<Equip>();

		while (cursor.moveToNext()) {
			llistaEquips.add(getEquipFromCursor(cursor));
		}

		return llistaEquips;
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

	public boolean existsEquip(String nomEquip)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			EquipsEntry.COLUMN_NAME_NOM
		};

		String selection = EquipsEntry.COLUMN_NAME_NOM + "=?";
		String[] selectionArgs = {
			nomEquip
		};

		Cursor cursor = db.query(
			EquipsEntry.TABLE_NAME,  // The table to query
			projection,                // The columns to return
			selection,                 // The columns for the WHERE clause
			selectionArgs,             // The values for the WHERE clause
			null,                      // don't group the rows
			null,                      // don't filter by row groups
			null                  // The sort order
		);

		if (cursor == null)
			return false;

		return cursor.getCount() > 0;
	}

	public void insertPartit(Partit partit)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PartitsEntry.COLUMN_NAME_EQUIP_LOCAL,
			partit.getLocal().getNom());
		contentValues.put(PartitsEntry.COLUMN_NAME_EQUIP_VISITANT,
			partit.getVisitant().getNom());
		contentValues.put(PartitsEntry.COLUMN_NAME_DATA,
			Utils.dateToString(partit.getData()));
		contentValues.put(PartitsEntry.COLUMN_NAME_GOLS_LOCAL,
			partit.getGolsLocal());
		contentValues.put(PartitsEntry.COLUMN_NAME_GOLS_VISITANT,
			partit.getGolsVisitant());
		db.insert(PartitsEntry.TABLE_NAME, null, contentValues);
	}

	private Partit getPartitFromCursor(Cursor cursor)
	{
		String nomEquipLocal = cursor.getString(
			cursor.getColumnIndexOrThrow(PartitsEntry.COLUMN_NAME_EQUIP_LOCAL)
		);
		String nomEquipVisitant = cursor.getString(
			cursor.getColumnIndexOrThrow(PartitsEntry.COLUMN_NAME_EQUIP_VISITANT)
		);
		String dataString = cursor.getString(
			cursor.getColumnIndexOrThrow(PartitsEntry.COLUMN_NAME_DATA)
		);
		int golsLocal = cursor.getInt(
			cursor.getColumnIndexOrThrow(PartitsEntry.COLUMN_NAME_GOLS_LOCAL)
		);
		int golsVisitant = cursor.getInt(
			cursor.getColumnIndexOrThrow(PartitsEntry.COLUMN_NAME_GOLS_VISITANT)
		);

		Equip local = this.queryEquip(nomEquipLocal);
		Equip visitant = this.queryEquip(nomEquipVisitant);
		Date data = Utils.stringToDate(dataString);

		Partit partit = new Partit(local, visitant, data, golsLocal, golsVisitant);

		return partit;
	}

	public Partit queryPartit(String equipLocal, String equipVisitant, String data)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			PartitsEntry.COLUMN_NAME_EQUIP_LOCAL,
			PartitsEntry.COLUMN_NAME_EQUIP_VISITANT,
			PartitsEntry.COLUMN_NAME_DATA,
			PartitsEntry.COLUMN_NAME_GOLS_LOCAL,
			PartitsEntry.COLUMN_NAME_GOLS_VISITANT
		};

		String selection = PartitsEntry.COLUMN_NAME_EQUIP_LOCAL + "=? and " +
			PartitsEntry.COLUMN_NAME_EQUIP_VISITANT + "=? and " +
			PartitsEntry.COLUMN_NAME_DATA + "=?";
		String[] selectionArgs = {
			equipLocal, equipVisitant, data
		};

		Cursor cursor = db.query(
			PartitsEntry.TABLE_NAME,   // The table to query
			projection,                // The columns to return
			selection,                 // The columns for the WHERE clause
			selectionArgs,             // The values for the WHERE clause
			null,                      // don't group the rows
			null,                      // don't filter by row groups
			null                       // The sort order
		);

		if (cursor == null)
			return null;

		cursor.moveToFirst();

		return getPartitFromCursor(cursor);
	}

	public List<Partit> queryAllPartits()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(PartitsEntry.TABLE_NAME,
			null, null, null, null, null, null);

		if (cursor == null)
			return null;

		List<Partit> llistaPartits = new ArrayList<Partit>();

		while (cursor.moveToNext()) {
			llistaPartits.add(getPartitFromCursor(cursor));
		}

		return llistaPartits;
	}

	public void updatePartit(Partit partit)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(PartitsEntry.COLUMN_NAME_GOLS_LOCAL,
			partit.getGolsLocal());
		values.put(PartitsEntry.COLUMN_NAME_GOLS_VISITANT,
			partit.getGolsVisitant());

		String selection = PartitsEntry.COLUMN_NAME_EQUIP_LOCAL + " LIKE ? and " +
			PartitsEntry.COLUMN_NAME_EQUIP_VISITANT + " LIKE ? and " +
			PartitsEntry.COLUMN_NAME_DATA + " LIKE ?";

		String[] selectionArgs = {
			partit.getLocal().getNom(), partit.getVisitant().getNom(),
			Utils.dateToString(partit.getData())
		};

		int count = db.update(
			PartitsEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);
	}

	public boolean existsPartit(String equipLocal, String equipVisitant, String data)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			PartitsEntry.COLUMN_NAME_EQUIP_LOCAL,
			PartitsEntry.COLUMN_NAME_EQUIP_VISITANT,
			PartitsEntry.COLUMN_NAME_DATA,
		};

		String selection = PartitsEntry.COLUMN_NAME_EQUIP_LOCAL + "=? and " +
			PartitsEntry.COLUMN_NAME_EQUIP_VISITANT + "=? and " +
			PartitsEntry.COLUMN_NAME_DATA + "=?";
		String[] selectionArgs = {
			equipLocal, equipVisitant, data
		};

		Cursor cursor = db.query(
			PartitsEntry.TABLE_NAME,  // The table to query
			projection,                // The columns to return
			selection,                 // The columns for the WHERE clause
			selectionArgs,             // The values for the WHERE clause
			null,                      // don't group the rows
			null,                      // don't filter by row groups
			null                  // The sort order
		);

		if (cursor == null)
			return false;

		return cursor.getCount() > 0;
	}
}