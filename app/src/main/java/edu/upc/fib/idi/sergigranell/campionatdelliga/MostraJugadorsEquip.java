package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MostraJugadorsEquip extends Activity {

	private ListView jugadorsEquipListView;
	private ArrayAdapter<Jugador> arrayAdapter;

	private String nomEquip;
	private List<Jugador> jugadors;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_jugadors_equip);

		jugadors = new ArrayList<Jugador>();
		DBManager db = new DBManager(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			List<String> nomJugadors = (ArrayList<String>)extras.getStringArrayList("JugadorsEquip");

			for (String nom: nomJugadors) {
				jugadors.add(db.queryJugador(nom));
			}

			nomEquip = extras.getString("Equip");
		}

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
	}

}

