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

import java.util.ArrayList;
import java.util.List;

public class MostraPartits extends Activity {

	private ListView equipsListView;
	private ArrayAdapter<String> arrayAdapter;

	private List<Partit> partits;
	private DBManager dbmgr;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_partits);

		dbmgr = new DBManager(MostraPartits.this);

		partits = dbmgr.queryAllPartits();

		this.setTitle("Llista de partits");

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

		equipsListView = (ListView)findViewById(R.id.listview_partits);
		equipsListView.setAdapter(arrayAdapter);
		equipsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Partit partit = partits.get(position);

				Intent mostraPartitIntent = new Intent(MostraPartits.this,
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
