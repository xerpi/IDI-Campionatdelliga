package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AfegirPartit extends Activity {

	private Spinner spinnerEquipLocal;
	private Spinner spinnerEquipVisitant;
	private Spinner spinnerJornada;

	private ListView listViewGols;

	private Button buttonAfegirGol;
	private Button buttonCancelar;
	private Button buttonAcceptar;

	private ArrayAdapter<String> arrayAdapterEquipLocal;
	private ArrayAdapter<String> arrayAdapterEquipVisitant;
	private ArrayAdapter<Integer> arrayAdapterJornada;
	private ArrayAdapter<String> arrayAdapterGolsListView;
	private ArrayList<String> arrayListEquipLocal;
	private ArrayList<String> arrayListEquipVisitant;
	private ArrayList<Integer> arrayListJornada;
	private ArrayList<String> arrayListGolsListView;

	private ArrayList<Partit.Gol> golsList;

	private DBManager dbmgr;

	int numeroJornada;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_afegir_partit);

		numeroJornada = 1;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			numeroJornada = extras.getInt("Jornada");
		}

		dbmgr = new DBManager(this);

		golsList = new ArrayList<Partit.Gol>();

		arrayListEquipLocal = new ArrayList<String>();
		arrayListEquipVisitant = new ArrayList<String>();
		arrayListJornada = new ArrayList<Integer>();
		arrayListGolsListView = new ArrayList<String>();

		arrayAdapterEquipLocal = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_dropdown_item, arrayListEquipLocal);

		arrayAdapterEquipVisitant = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_dropdown_item, arrayListEquipVisitant);

		arrayAdapterJornada = new ArrayAdapter<Integer>(this,
			android.R.layout.simple_spinner_dropdown_item, arrayListJornada);

		arrayAdapterGolsListView = new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, arrayListGolsListView);

		List<Equip> equips = dbmgr.queryAllEquips();
		for (Equip e: equips) {
			arrayListEquipLocal.add(e.getNom());
			arrayListEquipVisitant.add(e.getNom());
		}

		List<Jornada> jornades = dbmgr.queryAllJornades();
		for (Jornada j: jornades) {
			arrayListJornada.add(j.getNumero());
		}

		spinnerEquipLocal = (Spinner)findViewById(R.id.afegir_partit_spinner_equip_local);
		spinnerEquipVisitant = (Spinner)findViewById(R.id.afegir_partit_spinner_equip_visitant);
		spinnerJornada = (Spinner)findViewById(R.id.afegir_partit_spinner_jornada);
		listViewGols = (ListView)findViewById(R.id.afegir_partit_listview_gols);
		buttonAfegirGol = (Button)findViewById(R.id.afegir_partit_button_afegir_gol);
		buttonAcceptar = (Button)findViewById(R.id.afegir_partit_button_acceptar);
		buttonCancelar = (Button)findViewById(R.id.afegir_partit_button_cancelar);

		spinnerEquipLocal.setAdapter(arrayAdapterEquipLocal);
		spinnerEquipVisitant.setAdapter(arrayAdapterEquipVisitant);
		spinnerJornada.setAdapter(arrayAdapterJornada);

		listViewGols.setAdapter(arrayAdapterGolsListView);

		spinnerJornada.setSelection(arrayListJornada.indexOf(numeroJornada));

		buttonAfegirGol.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				final Dialog dialog = new Dialog(AfegirPartit.this);

				dialog.setContentView(R.layout.layout_afegir_gol);
				dialog.setTitle("Afegir gol");

				int equipLocalPos = spinnerEquipLocal.getSelectedItemPosition();
				int equipVisitantPos = spinnerEquipVisitant.getSelectedItemPosition();

				String equipLocalNom = arrayListEquipLocal.get(equipLocalPos);
				String equipVisitantNom = arrayListEquipVisitant.get(equipVisitantPos);

				Equip equipLocal = dbmgr.queryEquip(equipLocalNom);
				Equip equipVisitant = dbmgr.queryEquip(equipVisitantNom);

				List<Jugador> jugadorsEquipLocal = equipLocal.getJugadors();
				List<Jugador> jugadorsEquipVisitant = equipVisitant.getJugadors();

				final List<Jugador> jugadors = new ArrayList<Jugador>();
				jugadors.addAll(jugadorsEquipLocal);
				jugadors.addAll(jugadorsEquipVisitant);

				List<String> nomJugadors = new ArrayList<String>();
				for (Jugador j : jugadors) {
					nomJugadors.add(j.getNom());
				}

				ArrayAdapter<String> adp = new ArrayAdapter<String>(AfegirPartit.this,
					android.R.layout.simple_list_item_1, nomJugadors);

				final Spinner sp = (Spinner)dialog.findViewById(R.id.spinner_jugadors);
				sp.setAdapter(adp);

				final NumberPicker np = (NumberPicker)dialog.findViewById(R.id.numberpicker_minut_gol);
				np.setMinValue(0);
				np.setMaxValue(40);
				np.setValue(0);

				Button buttonAcceptarAfegirGol = (Button)dialog.findViewById(R.id.button_acceptar_afegir_gol);
				buttonAcceptarAfegirGol.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						int pos = sp.getSelectedItemPosition();
						int minut = np.getValue();

						Jugador j = jugadors.get(pos);

						golsList.add(new Partit.Gol(j, minut));

						arrayListGolsListView.add(j.getNom() + ": " + minut);
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

				if (equipLocalPos == equipVisitantPos) {
					Toast.makeText(AfegirPartit.this, "No poden ser el mateix equip!",
						Toast.LENGTH_SHORT).show();
					return;
				}

				String equipLocalNom = arrayListEquipLocal.get(equipLocalPos);
				String equipVisitantNom = arrayListEquipVisitant.get(equipVisitantPos);

				Equip equipLocal = dbmgr.queryEquip(equipLocalNom);
				Equip equipvisitant = dbmgr.queryEquip(equipVisitantNom);

				Partit nouPartit = new Partit(equipLocal, equipvisitant, new Date());
				nouPartit.addGolList(golsList);

				dbmgr.insertPartit(nouPartit);
				nouPartit.updatePuntsEquips(dbmgr);

				int numeroJornada = arrayListJornada.get(jornadaPos);

				Jornada jornada = dbmgr.queryJornada(numeroJornada);
				jornada.addPartit(nouPartit);
				dbmgr.updateJornada(jornada);

				finish();
			}
		});

		buttonCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}
}
