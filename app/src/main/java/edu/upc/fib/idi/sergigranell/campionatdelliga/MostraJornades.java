package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MostraJornades extends AppCompatActivity {

	private ListView jornadesListView;
	private ArrayAdapter<Jornada> arrayAdapterJornades;
	private ArrayList<Jornada> arrayListJornades;

	private DBManager dbmgr;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_jornades);

		dbmgr = new DBManager(this);

		arrayListJornades = new ArrayList<Jornada>();
		arrayAdapterJornades = new ArrayAdapter<Jornada>(this, android.R.layout.simple_list_item_1, arrayListJornades);

		arrayAdapterJornades = new ArrayAdapter<Jornada>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arrayListJornades) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(android.R.id.text1);

				Jornada jornada = arrayListJornades.get(position);
				tv.setText("Jornada " + jornada.getNumero());
				return view;
			}
		};

		this.setTitle("Llista de les jornades");

		jornadesListView = (ListView)findViewById(R.id.listview_jornades);
		jornadesListView.setAdapter(arrayAdapterJornades);
		jornadesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Jornada jornada = arrayListJornades.get(position);

				Intent mostraJornadaIntent = new Intent(MostraJornades.this,
					MostraJornada.class);

				mostraJornadaIntent.putExtra("Numero", jornada.getNumero());

				startActivity(mostraJornadaIntent);
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		List<Jornada> jornades = dbmgr.queryAllJornades();

		arrayAdapterJornades.clear();
		arrayAdapterJornades.addAll(jornades);
		arrayAdapterJornades.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}
}
