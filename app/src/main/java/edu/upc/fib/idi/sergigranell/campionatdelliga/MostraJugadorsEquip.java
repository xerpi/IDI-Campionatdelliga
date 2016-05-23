package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class MostraJugadorsEquip extends AppCompatActivity {

	private DBManager dbmgr;
	private Equip equip;
	private ListView jugadorsEquipListView;
	private ArrayAdapter<Jugador> arrayAdapter;
	private List<Jugador> jugadors;
	private Jugador selectedJugador;
	private List<Jugador> selectedListReserves;
	private List<Jugador> selectedListTitulars;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_jugadors_equip);

		dbmgr = new DBManager(this);

		Bundle extras = getIntent().getExtras();

		String nomEquip = extras.getString("Equip");
		equip = dbmgr.queryEquip(nomEquip);
		jugadors = equip.getJugadors();

		this.setTitle("Jugadors de " + nomEquip);

		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, jugadors) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				TextView text2 = (TextView) view.findViewById(android.R.id.text2);

				text1.setText(jugadors.get(position).getNom());
				text2.setText(jugadors.get(position).getTipus().toString());
				return view;
			}
		};

		jugadorsEquipListView = (ListView)findViewById(R.id.listview_jugadors_equip);
		jugadorsEquipListView.setAdapter(arrayAdapter);
		jugadorsEquipListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
			{
				if (view.getId() == R.id.listview_jugadors_equip) {
					AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
					Jugador jugador = jugadors.get(info.position);

					MenuInflater inflater = getMenuInflater();
					if (jugador.getTipus() == Jugador.TipusJugador.TITULAR)
						inflater.inflate(R.menu.menu_mostra_jugadors_titular, contextMenu);
					else
						inflater.inflate(R.menu.menu_mostra_jugadors_reserva, contextMenu);
				}
			}
		});

		selectedListReserves = new ArrayList<Jugador>();
		selectedListTitulars = new ArrayList<Jugador>();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		selectedJugador = jugadors.get(info.position);

		switch (item.getItemId()) {
		case R.id.passar_a_reserva:
			selectedListReserves.clear();

			List<String> nomReserves = new ArrayList<String>();
			for (Jugador j: jugadors) {
				if (j.getTipus() == Jugador.TipusJugador.RESERVA) {
					nomReserves.add(j.getNom());
					selectedListReserves.add(j);
					Log.i("lel", "Reserva: " + j.getNom());
				}
			}

			CharSequence[] csReserves = nomReserves.toArray(new CharSequence[nomReserves.size()]);

			AlertDialog.Builder dialogReserves = new AlertDialog.Builder(this);
			dialogReserves.setTitle("Reemplaçar per...");

			dialogReserves.setItems(csReserves, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int position) {
					selectedJugador.setTipus(Jugador.TipusJugador.RESERVA);
					Jugador selectedReserva = selectedListReserves.get(position);
					selectedReserva.setTipus(Jugador.TipusJugador.TITULAR);

					dbmgr.updateJugador(selectedJugador);
					dbmgr.updateJugador(selectedReserva);

					arrayAdapter.notifyDataSetChanged();

					mostraToastReemplac(selectedJugador, selectedReserva);
				}
			});

			AlertDialog alertReserves = dialogReserves.create();
			alertReserves.show();
			return true;
		case R.id.passar_a_titlar:
			selectedListTitulars.clear();

			List<String> nomTitulars = new ArrayList<String>();
			for (Jugador j: jugadors) {
				if (j.getTipus() == Jugador.TipusJugador.TITULAR) {
					nomTitulars.add(j.getNom());
					selectedListTitulars.add(j);
					Log.i("lel", "Titular: " + j.getNom());
				}
			}

			CharSequence[] csTitulars = nomTitulars.toArray(new CharSequence[nomTitulars.size()]);

			AlertDialog.Builder dialogTitulars = new AlertDialog.Builder(this);
			dialogTitulars.setTitle("Reemplaçar per...");

			dialogTitulars.setItems(csTitulars, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int position) {
					selectedJugador.setTipus(Jugador.TipusJugador.TITULAR);
					Jugador selectedTitular = selectedListTitulars.get(position);
					selectedTitular.setTipus(Jugador.TipusJugador.RESERVA);

					dbmgr.updateJugador(selectedJugador);
					dbmgr.updateJugador(selectedTitular);

					arrayAdapter.notifyDataSetChanged();

					mostraToastReemplac(selectedJugador, selectedTitular);
				}
			});

			AlertDialog alertTitulars = dialogTitulars.create();
			alertTitulars.show();
			return true;
		}
		return false;
	}

	private void mostraToastReemplac(Jugador j1, Jugador j2)
	{
		Toast.makeText(this, j1.getNom() + " reemplaçat per " + j2.getNom(),
			Toast.LENGTH_SHORT).show();
	}
}

