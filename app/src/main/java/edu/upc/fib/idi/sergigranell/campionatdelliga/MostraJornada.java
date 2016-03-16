package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MostraJornada extends Activity {

	private ListView partitsJornadaListView;
	private ArrayAdapter<String> arrayAdapter;

	private DBManager dbmgr;

	private Jornada jornada;
	List<Partit> partits;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_jornada);

		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return;

		int numeroJornada = extras.getInt("Numero");

		dbmgr = new DBManager(this);

		jornada = dbmgr.queryJornada(numeroJornada);
		if (jornada == null)
			return;

		partits = jornada.getPartits();

		this.setTitle("Jornada " + numeroJornada);

		arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, partits) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
				TextView text2 = (TextView) view.findViewById(android.R.id.text2);

				Partit partit = partits.get(position);
				text1.setText(
					partit.getLocal().getNom() + " vs " +
						partit.getVisitant().getNom() + ": " +
						partit.getGolsLocal() + " - " +
						partit.getGolsVisitant()
				);
				text2.setText(Utils.dateToString(partit.getData()));
				return view;
			}
		};

		partitsJornadaListView = (ListView)findViewById(R.id.listview_partits_jornada);
		partitsJornadaListView.setAdapter(arrayAdapter);
		partitsJornadaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Partit partit = partits.get(position);

				Intent mostraPartitIntent = new Intent(MostraJornada.this,
					MostraPartit.class);

				mostraPartitIntent.putExtra("EquipLocal",
					partit.getLocal().getNom());
				mostraPartitIntent.putExtra("EquipVisitant",
					partit.getVisitant().getNom());
				mostraPartitIntent.putExtra("Data",
					Utils.dateToString(partit.getData()));

				startActivity(mostraPartitIntent);
			}
		});
	}

}
