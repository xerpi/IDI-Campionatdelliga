package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AfegirPartit extends AppCompatActivity {

	private Spinner spinnerEquipLocal;
	private Spinner spinnerEquipVisitant;
	private Spinner spinnerJornada;

	private ListView listViewGols;

	private Button buttonAfegirGol;
	private Button buttonCancelar;
	private Button buttonAcceptar;

	private ArrayAdapter<Equip> arrayAdapterEquipLocal;
	private ArrayAdapter<Equip> arrayAdapterEquipVisitant;
	private ArrayAdapter<Integer> arrayAdapterJornada;
	private ArrayAdapter<Partit.Gol> arrayAdapterGolsListView;
	private ArrayList<Equip> arrayListEquipLocal;
	private ArrayList<Equip> arrayListEquipVisitant;
	private ArrayList<Integer> arrayListJornada;
	private ArrayList<Partit.Gol> arrayListGols;

	private DBManager dbmgr;

	private List<Equip> equips;

	private int numeroJornada;

	private int minuteOfLastGol;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_afegir_partit);

		this.setTitle("Afegir nou partit");

		numeroJornada = 1;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			numeroJornada = extras.getInt("Jornada");
		}

		dbmgr = new DBManager(this);

		equips = dbmgr.queryAllEquips();

		arrayListEquipLocal = new ArrayList<Equip>();
		arrayListEquipVisitant = new ArrayList<Equip>();
		arrayListJornada = new ArrayList<Integer>();
		arrayListGols = new ArrayList<Partit.Gol>();

		arrayAdapterEquipLocal = new ArrayAdapter<Equip>(this,
			android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
			arrayListEquipLocal) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);
				equipsSpinnerView(view, position);
				return view;
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);
				equipsSpinnerView(view, position);
				return view;
			}
		};

		arrayAdapterEquipVisitant = new ArrayAdapter<Equip>(this,
			android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
			arrayListEquipVisitant) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);
				equipsSpinnerView(view, position);
				return view;
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);
				equipsSpinnerView(view, position);
				return view;
			}
		};

		arrayAdapterJornada = new ArrayAdapter<Integer>(this,
			android.R.layout.simple_spinner_dropdown_item, arrayListJornada);

		arrayAdapterGolsListView = new ArrayAdapter<Partit.Gol>(this,
			android.R.layout.simple_list_item_1, arrayListGols);

		arrayAdapterGolsListView = new ArrayAdapter<Partit.Gol>(this,
			android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
			arrayListGols) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
				Partit.Gol gol = arrayListGols.get(position);
				tv.setText(gol.getJugador().getNom() + ": " + gol.getMinut());
				return view;
			}
		};

		for (Equip e: equips) {
			arrayListEquipLocal.add(e);
			arrayListEquipVisitant.add(e);
		}

		List<Jornada> jornades = dbmgr.queryAllJornades();
		for (Jornada j: jornades) {
			arrayListJornada.add(j.getNumero());
		}

		spinnerEquipLocal = (Spinner)findViewById(R.id.afegir_partit_spinner_equip_local);
		spinnerEquipLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				spinnerEquipsChangeUpdateGolList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});

		spinnerEquipVisitant = (Spinner)findViewById(R.id.afegir_partit_spinner_equip_visitant);
		spinnerEquipVisitant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
			{
				spinnerEquipsChangeUpdateGolList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView)
			{

			}
		});

		spinnerJornada = (Spinner)findViewById(R.id.afegir_partit_spinner_jornada);
		listViewGols = (ListView)findViewById(R.id.afegir_partit_listview_gols);
		buttonAfegirGol = (Button)findViewById(R.id.afegir_partit_button_afegir_gol);
		buttonAcceptar = (Button)findViewById(R.id.afegir_partit_button_acceptar);
		buttonCancelar = (Button)findViewById(R.id.afegir_partit_button_cancelar);

		spinnerEquipLocal.setAdapter(arrayAdapterEquipLocal);
		spinnerEquipVisitant.setAdapter(arrayAdapterEquipVisitant);
		spinnerJornada.setAdapter(arrayAdapterJornada);

		listViewGols.setAdapter(arrayAdapterGolsListView);
		listViewGols.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
			{
				if (view.getId() == R.id.afegir_partit_listview_gols) {
					AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
					Partit.Gol gol = arrayListGols.get(info.position);

					MenuInflater inflater = getMenuInflater();
					inflater.inflate(R.menu.menu_llista_gols_seleccionat, contextMenu);
				}
			}
		});
		buttonAfegirGol.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				int equipLocalPos = spinnerEquipLocal.getSelectedItemPosition();
				int equipVisitantPos = spinnerEquipVisitant.getSelectedItemPosition();

				Equip equipLocal = arrayListEquipLocal.get(equipLocalPos);
				Equip equipVisitant = arrayListEquipVisitant.get(equipVisitantPos);

				if (equipLocal.equals(equipVisitant)) {
					Toast.makeText(AfegirPartit.this, "No poden ser el mateix equip!",
						Toast.LENGTH_SHORT).show();
					return;
				}

				final Dialog dialog = new Dialog(AfegirPartit.this);

				dialog.setContentView(R.layout.layout_afegir_gol);
				dialog.setTitle("Afegir gol");

				List<Jugador> jugadorsTitularsEquipLocal = equipLocal.getTitulars();
				List<Jugador> jugadorsTitularsEquipVisitant = equipVisitant.getTitulars();

				final List<Jugador> jugadors = new ArrayList<Jugador>();
				jugadors.addAll(jugadorsTitularsEquipLocal);
				jugadors.addAll(jugadorsTitularsEquipVisitant);

				List<String> nomJugadors = new ArrayList<String>();
				for (Jugador j : jugadorsTitularsEquipLocal) {
					nomJugadors.add(equipLocal.getNom() + ": " + j.getNom());
				}
				for (Jugador j : jugadorsTitularsEquipVisitant) {
					nomJugadors.add(equipVisitant.getNom() + ": " + j.getNom());
				}

				ArrayAdapter<String> adp = new ArrayAdapter<String>(AfegirPartit.this,
					android.R.layout.simple_list_item_1, nomJugadors);

				final Spinner sp = (Spinner)dialog.findViewById(R.id.spinner_jugadors);
				sp.setAdapter(adp);

				final NumberPicker np = (NumberPicker)dialog.findViewById(R.id.numberpicker_minut_gol);
				np.setMinValue(0);
				np.setMaxValue(Utils.NUM_MINUTS_PARTIT);
				np.setValue(minuteOfLastGol);

				Button buttonAcceptarAfegirGol = (Button)dialog.findViewById(R.id.button_acceptar_afegir_gol);
				buttonAcceptarAfegirGol.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						int pos = sp.getSelectedItemPosition();
						int minut = np.getValue();

						Jugador j = jugadors.get(pos);

						arrayListGols.add(new Partit.Gol(j, minut));
						minuteOfLastGol = minut;

						arrayAdapterGolsListView.notifyDataSetChanged();
						dialog.cancel();
					}
				});

				Button buttonCancelarAfegirGol = (Button)dialog.findViewById(R.id.button_cancelar_afegir_gol);
				buttonCancelarAfegirGol.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						dialog.cancel();
					}
				});

				dialog.show();
			}
		});

		buttonAcceptar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				int equipLocalPos = spinnerEquipLocal.getSelectedItemPosition();
				int equipVisitantPos = spinnerEquipVisitant.getSelectedItemPosition();
				int jornadaPos = spinnerJornada.getSelectedItemPosition();

				int numeroJornada = arrayListJornada.get(jornadaPos);
				Jornada jornada = dbmgr.queryJornada(numeroJornada);

				Equip equipLocal = arrayListEquipLocal.get(equipLocalPos);
				Equip equipVisitant = arrayListEquipVisitant.get(equipVisitantPos);

				if (equipLocal.equals(equipVisitant)) {
					Toast.makeText(AfegirPartit.this, "No poden ser el mateix equip!",
						Toast.LENGTH_SHORT).show();
					return;
				} else if (jornada.getNumPartits() == Utils.NUM_PARTITS_PER_JORNADA) {
					Toast.makeText(AfegirPartit.this,
						"Ja s'han jugat tots els partits de la jornada " +
							numeroJornada + "!",
						Toast.LENGTH_SHORT).show();
					return;
				} else if (dbmgr.existsPartit(equipLocal.getNom(), equipVisitant.getNom(), numeroJornada)
					|| dbmgr.existsPartit(equipVisitant.getNom(), equipLocal.getNom(), numeroJornada)) {
					Toast.makeText(AfegirPartit.this,
						"Aquests dos partits ja han jugat en la jornada " +
							numeroJornada + "!",
						Toast.LENGTH_SHORT).show();
					return;
				}

				Partit nouPartit = new Partit(equipLocal, equipVisitant, numeroJornada);
				nouPartit.addGolList(arrayListGols);
				for (Partit.Gol gol: arrayListGols) {
					Jugador jugador = gol.getJugador();
					jugador.setGolsMarcats(jugador.getGolsMarcats() + 1);
					dbmgr.updateJugador(jugador);
				}

				dbmgr.insertPartit(nouPartit);
				nouPartit.updatePuntsEquips(dbmgr);

				jornada.addPartit(nouPartit);
				dbmgr.updateJornada(jornada);

				if (jornada.getNumPartits() == Utils.NUM_PARTITS_PER_JORNADA) {
					dbmgr.insertJornada(new Jornada(null, numeroJornada + 1));
					Toast.makeText(AfegirPartit.this,
						"S'ha iniciat la jornada " +
							(numeroJornada + 1) + "!",
						Toast.LENGTH_LONG).show();
				}

				finish();
			}
		});

		buttonCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				if (arrayListGols.size() > 0) {
					final Dialog dialog = new AlertDialog.Builder(AfegirPartit.this)
						.setTitle("Canvis no desats...")
						.setMessage("Estàs segur/a que vols tirar enrere?")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton)
							{
								finish();
							}
						})
						.setNegativeButton(android.R.string.no, null).show();
				} else {
					finish();
				}
			}
		});

		spinnerJornada.setSelection(arrayListJornada.indexOf(numeroJornada));
		minuteOfLastGol = 0;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		Partit.Gol gol = arrayListGols.get(info.position);

		switch (item.getItemId()) {
			case R.id.eliminar_gol:
				arrayListGols.remove(gol);
				arrayAdapterGolsListView.notifyDataSetChanged();
				return true;
		}
		return false;
	}

	private void equipsSpinnerView(View view, int position)
	{
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		Equip equip = equips.get(position);
		tv.setText(equip.getNom());
	}

	private boolean jugadorListContainsJugador(List<Jugador> jugadorList, Jugador jugador)
	{
		for (Jugador j: jugadorList) {
			if (j.getNom().equals(jugador.getNom())) {
				return true;
			}
		}
		return false;
	}

	private void spinnerEquipsChangeUpdateGolList()
	{
		int equipLocalPos = spinnerEquipLocal.getSelectedItemPosition();
		int equipVisitantPos = spinnerEquipVisitant.getSelectedItemPosition();

		Equip equipLocal = arrayListEquipLocal.get(equipLocalPos);
		Equip equipVisitant = arrayListEquipVisitant.get(equipVisitantPos);

		Iterator<Partit.Gol> it = arrayListGols.iterator();
		while (it.hasNext()) {
			Partit.Gol gol = it.next();
			if (!jugadorListContainsJugador(equipLocal.getTitulars(), gol.getJugador())
				|| !jugadorListContainsJugador(equipLocal.getTitulars(), gol.getJugador())) {
				it.remove();
			}
		}
	}
}
