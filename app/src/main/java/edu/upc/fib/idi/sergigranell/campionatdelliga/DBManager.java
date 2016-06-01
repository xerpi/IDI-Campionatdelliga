package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
		public static final String COLUMN_NAME_PARTITS_GUANYATS = "partits_guanyats";
		public static final String COLUMN_NAME_PARTITS_EMPATATS = "partits_empatats";
		public static final String COLUMN_NAME_PARTITS_PERDUTS = "partits_perduts";
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
		public static final String COLUMN_NAME_LLISTA_GOLS = "llista_gols";
	}

	public static abstract class JornadesEntry {
		public static final String TABLE_NAME = "jornades";
		public static final String COLUMN_NAME_NUMERO = "numero";
		public static final String COLUMN_NAME_LLISTA_PARTITS = "llista_partits";
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
			EquipsEntry.COLUMN_NAME_PARTITS_GUANYATS + " INTEGER," +
			EquipsEntry.COLUMN_NAME_PARTITS_EMPATATS + " INTEGER," +
			EquipsEntry.COLUMN_NAME_PARTITS_PERDUTS + " INTEGER," +
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
			PartitsEntry.COLUMN_NAME_LLISTA_GOLS + " TEXT," +
			"PRIMARY KEY (" +
				PartitsEntry.COLUMN_NAME_EQUIP_LOCAL + "," +
				PartitsEntry.COLUMN_NAME_EQUIP_VISITANT + "," +
				PartitsEntry.COLUMN_NAME_DATA +
			" )" +
		" )";

	private static final String SQL_CREATE_JORNADES_ENTRIES =
		"CREATE TABLE " + JornadesEntry.TABLE_NAME + " (" +
			JornadesEntry.COLUMN_NAME_NUMERO + " INTEGER PRIMARY KEY," +
			JornadesEntry.COLUMN_NAME_LLISTA_PARTITS + " TEXT" +
			" )";

	private static final String SQL_DELETE_JUGADORS_ENTRIES =
		"DROP TABLE IF EXISTS " + JugadorsEntry.TABLE_NAME;

	private static final String SQL_DELETE_EQUIPS_ENTRIES =
		"DROP TABLE IF EXISTS " + EquipsEntry.TABLE_NAME;

	private static final String SQL_DELETE_PARTITS_ENTRIES =
		"DROP TABLE IF EXISTS " + PartitsEntry.TABLE_NAME;

	private static final String SQL_DELETE_JORNADES_ENTRIES =
		"DROP TABLE IF EXISTS " + JornadesEntry.TABLE_NAME;

	public DBManager(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private void insertInitialData(SQLiteDatabase db)
	{
		List <Jugador> jugadors = new ArrayList<Jugador>();
		for (int i = 1; i <= Utils.NUM_JUGADORS_TOTALS; i++) {
			Jugador j = new Jugador("Jugador " + i);
			int mod = (i - 1) % Utils.NUM_JUGADORS_EQUIP;
			if (mod < Utils.NUM_JUGADORS_TITULARS)
				j.setTipus(Jugador.TipusJugador.TITULAR);
			else
				j.setTipus(Jugador.TipusJugador.RESERVA);
			jugadors.add(j);
		}

		insertJugadors(db, jugadors);

		List <Equip> equips = new ArrayList<Equip>();
		for (int i = 1; i <= Utils.NUM_EQUIPS; i++) {
			Equip e = new Equip("Equip " + i, "Ciutat " + i);
			for (int j = (i - 1) * Utils.NUM_JUGADORS_EQUIP; j < i * Utils.NUM_JUGADORS_EQUIP; j++) {
				e.addJugador(jugadors.get(j));
			}
			equips.add(e);
		}

		insertEquips(db, equips);

		final GregorianCalendar gc = new GregorianCalendar(2016, 1, 1);
		final Date date = gc.getTime();

		List <Jornada> jornades = new ArrayList<Jornada>();
		for (int i = 1; i <= Utils.NUM_JORNADES_INICIALS; i++) {
			List <Partit> partits = new ArrayList<Partit>();
			for (int j = 0; j < Utils.NUM_EQUIPS; j+=2) {
				Partit p = new Partit(equips.get(j), equips.get(j+1), date, 0, 0);
				partits.add(p);
			}
			jornades.add(new Jornada(partits, i));
			insertPartits(db, partits);
		}

		insertJornades(db, jornades);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SQL_CREATE_JUGADORS_ENTRIES);
		db.execSQL(SQL_CREATE_EQUIPS_ENTRIES);
		db.execSQL(SQL_CREATE_PARTITS_ENTRIES);
		db.execSQL(SQL_CREATE_JORNADES_ENTRIES);

		insertInitialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL(SQL_DELETE_JUGADORS_ENTRIES);
		db.execSQL(SQL_DELETE_EQUIPS_ENTRIES);
		db.execSQL(SQL_DELETE_PARTITS_ENTRIES);
		db.execSQL(SQL_DELETE_JORNADES_ENTRIES);
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
		db.execSQL(SQL_DELETE_JORNADES_ENTRIES);
	}

	public void insertJugador(SQLiteDatabase db, Jugador jugador)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(JugadorsEntry.COLUMN_NAME_NOM, jugador.getNom());
		contentValues.put(JugadorsEntry.COLUMN_NAME_TIPUS, jugador.getTipus().toString());
		db.insert(JugadorsEntry.TABLE_NAME, null, contentValues);
	}

	public void insertJugador(Jugador jugador)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		insertJugador(db, jugador);
		db.close();
	}

	public void insertJugadors(SQLiteDatabase db, List<Jugador> jugadors)
	{
		for (Jugador j: jugadors) {
			insertJugador(db, j);
		}
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

		Jugador jugador = getJugadorFromCursor(cursor);

		cursor.close();
		db.close();

		return jugador;
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

		cursor.close();
		db.close();

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

		db.close();
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

		int count = cursor.getCount();

		cursor.close();
		db.close();

		return count > 0;
	}

	public boolean existsJugador(Jugador jugador)
	{
		return existsJugador(jugador.getNom());
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

	public void insertEquip(SQLiteDatabase db, Equip equip)
	{
		String jsonJugadorsString = getJugadorsEquipAsJSONString(equip);

		ContentValues contentValues = new ContentValues();
		contentValues.put(EquipsEntry.COLUMN_NAME_NOM, equip.getNom());
		contentValues.put(EquipsEntry.COLUMN_NAME_CIUTAT, equip.getCiutat());
		contentValues.put(EquipsEntry.COLUMN_NAME_PARTITS_GUANYATS, equip.getPartitsGuanyats());
		contentValues.put(EquipsEntry.COLUMN_NAME_PARTITS_EMPATATS, equip.getPartitsEmpatats());
		contentValues.put(EquipsEntry.COLUMN_NAME_PARTITS_PERDUTS, equip.getPartitsPerduts());
		contentValues.put(EquipsEntry.COLUMN_NAME_ESCUTFILE, equip.getEscutFile());
		contentValues.put(EquipsEntry.COLUMN_NAME_JUGADORS, jsonJugadorsString);
		db.insert(EquipsEntry.TABLE_NAME, null, contentValues);
	}

	public void insertEquip(Equip equip)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		insertEquip(db, equip);
		db.close();
	}

	public void insertEquips(SQLiteDatabase db, List<Equip> equips)
	{
		for (Equip e: equips) {
			insertEquip(db, e);
		}
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
		int partitsGuanyats = cursor.getInt(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_PARTITS_GUANYATS)
		);
		int partitsPerduts = cursor.getInt(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_PARTITS_PERDUTS)
		);
		int partitsEmpatats = cursor.getInt(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_PARTITS_EMPATATS)
		);
		String escutFile = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_ESCUTFILE)
		);
		String jsonJugadorsString = cursor.getString(
			cursor.getColumnIndexOrThrow(EquipsEntry.COLUMN_NAME_JUGADORS)
		);

		List<Jugador> jugadors = getJugadorsEquipFromJSONString(jsonJugadorsString);

		Equip equip = new Equip(nom, ciutat, jugadors);
		equip.setPartitsGuanyats(partitsGuanyats);
		equip.setPartitsEmpatats(partitsEmpatats);
		equip.setPartitsPerduts(partitsPerduts);
		equip.setEscutFile(escutFile);

		return equip;
	}

	public Equip queryEquip(String nom)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			EquipsEntry.COLUMN_NAME_NOM,
			EquipsEntry.COLUMN_NAME_CIUTAT,
			EquipsEntry.COLUMN_NAME_PARTITS_GUANYATS,
			EquipsEntry.COLUMN_NAME_PARTITS_EMPATATS,
			EquipsEntry.COLUMN_NAME_PARTITS_PERDUTS,
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

		Equip equip = getEquipFromCursor(cursor);

		cursor.close();
		db.close();

		return equip;
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

		cursor.close();
		db.close();

		return llistaEquips;
	}

	public void updateEquip(Equip equip)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(EquipsEntry.COLUMN_NAME_CIUTAT, equip.getCiutat());
		values.put(EquipsEntry.COLUMN_NAME_PARTITS_GUANYATS, equip.getPartitsGuanyats());
		values.put(EquipsEntry.COLUMN_NAME_PARTITS_EMPATATS, equip.getPartitsEmpatats());
		values.put(EquipsEntry.COLUMN_NAME_PARTITS_PERDUTS, equip.getPartitsPerduts());
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

		db.close();
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

		int count = cursor.getCount();

		cursor.close();
		db.close();

		return count > 0;
	}

	public boolean existsEquip(Equip equip)
	{
		return existsEquip(equip.getNom());
	}

	private String getGolsPartitAsJSONString(Partit partit)
	{
		JSONObject jsonJugadors = new JSONObject();
		try {
			JSONArray array = new JSONArray();
			for (Partit.Gol g: partit.getGols()) {
				JSONObject object = new JSONObject();
				object.put("jugador", g.getJugador().getNom());
				object.put("minut", g.getMinut());
				array.put(object);
			}
			jsonJugadors.put("llista_gols", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonJugadors.toString();
	}

	public void insertPartit(SQLiteDatabase db, Partit partit)
	{
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
		contentValues.put(PartitsEntry.COLUMN_NAME_LLISTA_GOLS,
			getGolsPartitAsJSONString(partit));
		db.insert(PartitsEntry.TABLE_NAME, null, contentValues);
	}

	public void insertPartit(Partit partit)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		insertPartit(db, partit);
		db.close();
	}

	public void insertPartits(SQLiteDatabase db, List<Partit> partits)
	{
		for (Partit p: partits) {
			insertPartit(db, p);
		}
	}

	private List<Partit.Gol> getGolsPartitFromJSONString(String llistaGolsString)
	{
		JSONObject jsonGols = null;
		try {
			jsonGols = new JSONObject(llistaGolsString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray golsArray = null;
		try {
			golsArray = jsonGols.getJSONArray("llista_gols");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<Partit.Gol> gols = new ArrayList<Partit.Gol>();
		for (int i = 0; i < golsArray.length(); i++) {
			try {
				JSONObject golObject = golsArray.getJSONObject(i);

				String nomJugador = golObject.getString("jugador");
				int minut = golObject.getInt("minut");

				Jugador jugador = this.queryJugador(nomJugador);

				gols.add(new Partit.Gol(jugador, minut));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return gols;
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
		String llistaGolsString = cursor.getString(
			cursor.getColumnIndexOrThrow(PartitsEntry.COLUMN_NAME_LLISTA_GOLS)
		);

		Equip local = this.queryEquip(nomEquipLocal);
		Equip visitant = this.queryEquip(nomEquipVisitant);
		Date data = Utils.stringToDate(dataString);
		List<Partit.Gol> gols = getGolsPartitFromJSONString(llistaGolsString);

		Partit partit = new Partit(local, visitant, data, golsLocal, golsVisitant);
		partit.setGols(gols);

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
			PartitsEntry.COLUMN_NAME_GOLS_VISITANT,
			PartitsEntry.COLUMN_NAME_LLISTA_GOLS
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

		Partit partit = getPartitFromCursor(cursor);

		cursor.close();
		db.close();

		return partit;
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

		cursor.close();
		db.close();

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
		values.put(PartitsEntry.COLUMN_NAME_LLISTA_GOLS,
			getGolsPartitAsJSONString(partit));

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

		db.close();
	}

	public boolean existsPartit(String equipLocal, String equipVisitant, String data)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			PartitsEntry.COLUMN_NAME_EQUIP_LOCAL,
			PartitsEntry.COLUMN_NAME_EQUIP_VISITANT,
			PartitsEntry.COLUMN_NAME_DATA
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

		int count = cursor.getCount();

		cursor.close();
		db.close();

		return count > 0;
	}

	public boolean existsPartit(Partit partit)
	{
		return existsPartit(partit.getLocal().getNom(), partit.getVisitant().getNom(),
			Utils.dateToString(partit.getData()));
	}

	private String getPartitsJornadaAsJSONString(Jornada jornada)
	{
		JSONObject jsonPartits = new JSONObject();
		try {
			JSONArray array = new JSONArray();
			for (Partit p: jornada.getPartits()) {
				JSONObject jsonPartit = new JSONObject();

				jsonPartit.put("equip_local", p.getLocal().getNom());
				jsonPartit.put("equip_visitant", p.getVisitant().getNom());
				jsonPartit.put("data", Utils.dateToString(p.getData()));

				array.put(jsonPartit);
			}
			jsonPartits.put("llista_partits", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonPartits.toString();
	}

	public void insertJornada(SQLiteDatabase db, Jornada jornada)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(JornadesEntry.COLUMN_NAME_NUMERO, jornada.getNumero());
		contentValues.put(JornadesEntry.COLUMN_NAME_LLISTA_PARTITS,
			getPartitsJornadaAsJSONString(jornada));
		db.insert(JornadesEntry.TABLE_NAME, null, contentValues);
	}

	public void insertJornada(Jornada jornada)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		insertJornada(db, jornada);
		db.close();
	}

	public void insertJornades(SQLiteDatabase db, List<Jornada> jornades)
	{
		for (Jornada j: jornades) {
			insertJornada(db, j);
		}
	}

	private List<Partit> getPartitsJornadaFromJSONString(String llistaPartitsString)
	{
		JSONObject jsonGols = null;
		try {
			jsonGols = new JSONObject(llistaPartitsString);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray partitsArray = null;
		try {
			partitsArray = jsonGols.getJSONArray("llista_partits");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<Partit> partits = new ArrayList<Partit>();
		for (int i = 0; i < partitsArray.length(); i++) {
			try {
				JSONObject golObject = partitsArray.getJSONObject(i);

				String nomEquipLocal = golObject.getString("equip_local");
				String nomEquipVisitant = golObject.getString("equip_visitant");
				String dataString = golObject.getString("data");

				Partit partit = this.queryPartit(nomEquipLocal,
					nomEquipVisitant, dataString);

				partits.add(partit);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return partits;
	}

	private Jornada getJornadaFromCursor(Cursor cursor)
	{
		int numero = cursor.getInt(
			cursor.getColumnIndexOrThrow(JornadesEntry.COLUMN_NAME_NUMERO)
		);
		String llistaPartitsString = cursor.getString(
			cursor.getColumnIndexOrThrow(JornadesEntry.COLUMN_NAME_LLISTA_PARTITS)
		);

		List<Partit> partits = getPartitsJornadaFromJSONString(llistaPartitsString);

		Jornada jornada = new Jornada(partits, numero);
		return jornada;
	}

	public Jornada queryJornada(int numero)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			JornadesEntry.COLUMN_NAME_NUMERO,
			JornadesEntry.COLUMN_NAME_LLISTA_PARTITS
		};

		String selection = JornadesEntry.COLUMN_NAME_NUMERO + "=?";
		String[] selectionArgs = {
			Integer.toString(numero)
		};
		String sortOrder = JornadesEntry.COLUMN_NAME_NUMERO + " DESC";

		Cursor cursor = db.query(
			JornadesEntry.TABLE_NAME,  // The table to query
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

		Jornada jornada = getJornadaFromCursor(cursor);

		cursor.close();
		db.close();

		return jornada;
	}

	public List<Jornada> queryAllJornades()
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(JornadesEntry.TABLE_NAME,
			null, null, null, null, null, null);

		if (cursor == null)
			return null;

		List<Jornada> llistaJornades = new ArrayList<Jornada>();

		while (cursor.moveToNext()) {
			llistaJornades.add(getJornadaFromCursor(cursor));
		}

		cursor.close();
		db.close();

		return llistaJornades;
	}

	public void updateJornada(Jornada jornada)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(JornadesEntry.COLUMN_NAME_LLISTA_PARTITS,
			getPartitsJornadaAsJSONString(jornada));

		String selection = JornadesEntry.COLUMN_NAME_NUMERO + " LIKE ?";
		String[] selectionArgs = {
			Integer.toString(jornada.getNumero())
		};

		int count = db.update(
			JornadesEntry.TABLE_NAME,
			values,
			selection,
			selectionArgs);

		db.close();
	}

	public boolean existsJornada(int numero)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = {
			JornadesEntry.COLUMN_NAME_NUMERO
		};

		String selection = JornadesEntry.COLUMN_NAME_NUMERO + "=?";
		String[] selectionArgs = {
			Integer.toString(numero)
		};

		Cursor cursor = db.query(
			JornadesEntry.TABLE_NAME,  // The table to query
			projection,                // The columns to return
			selection,                 // The columns for the WHERE clause
			selectionArgs,             // The values for the WHERE clause
			null,                      // don't group the rows
			null,                      // don't filter by row groups
			null                  // The sort order
		);

		if (cursor == null)
			return false;

		int count = cursor.getCount();

		cursor.close();
		db.close();

		return count > 0;
	}

	public boolean existsJornada(Jornada jornada)
	{
		return existsJornada(jornada.getNumero());
	}
}