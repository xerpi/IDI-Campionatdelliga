package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MostraJornades extends AppCompatActivity {

	private ListView jornadesListView;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> arrayList;

	private DBManager dbmgr;

	private List<Jornada> jornades;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_jornades);

		arrayList = new ArrayList<String>();
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

		dbmgr = new DBManager(this);

		jornades = dbmgr.queryAllJornades();
		for (Jornada j: jornades) {
			arrayList.add("Jornada " + j.getNumero());
		}

		this.setTitle("Llista de les jornades");

		jornadesListView = (ListView)findViewById(R.id.listview_jornades);
		jornadesListView.setAdapter(arrayAdapter);
		jornadesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Jornada jornada = jornades.get(position);

				Intent mostraJornadaIntent = new Intent(MostraJornades.this,
					MostraJornada.class);

				mostraJornadaIntent.putExtra("Numero", jornada.getNumero());

				startActivity(mostraJornadaIntent);
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
