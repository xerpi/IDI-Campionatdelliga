package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MostraJornada extends Activity {

	private ListView partitsJornadaListView;
	private ArrayAdapter<Partit> arrayAdapter;

	private DBManager dbmgr;

	private Jornada jornada;
	List<Partit> partits;

	int numeroJornada;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_jornada);

		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return;

		numeroJornada = extras.getInt("Numero");

		dbmgr = new DBManager(this);

		partits = new ArrayList<Partit>();

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

		refreshPartitsJornada();

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

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_mostra_jornada, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.action_afegir_partit) {
			Intent afegirPartitIntent = new Intent(MostraJornada.this,
				AfegirPartit.class);

			afegirPartitIntent.putExtra("Jornada", jornada.getNumero());

			startActivityForResult(afegirPartitIntent, 0);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		refreshPartitsJornada();
	}

	private void refreshPartitsJornada()
	{
		jornada = dbmgr.queryJornada(numeroJornada);
		partits = jornada.getPartits();

		arrayAdapter.clear();
		arrayAdapter.addAll(partits);
		arrayAdapter.notifyDataSetChanged();
	}
}
