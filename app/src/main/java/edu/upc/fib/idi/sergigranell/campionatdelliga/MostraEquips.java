package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MostraEquips extends Activity {

	private ListView equipsListView;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> arrayList;
	private DBManager dbmgr;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_equips);

		arrayList = new ArrayList<String>();
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

		dbmgr = new DBManager(MostraEquips.this);

		List <Equip> equips = dbmgr.queryAllEquips();
		for (Equip e: equips) {
			arrayList.add(e.getNom());
		}

		this.setTitle("Llista d'equips");

		equipsListView = (ListView)findViewById(R.id.listview_equips);
		equipsListView.setAdapter(arrayAdapter);
		equipsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String nomEquip = (String)parent.getItemAtPosition(position);

				Equip equip = dbmgr.queryEquip(nomEquip);
				if (equip == null)
					return;

				Intent mostraEquipIntent = new Intent(MostraEquips.this,
					MostraEquip.class);

				List<Jugador> llistaJugadorsEquip = equip.getJugadors();
				List<String> nomsJugadorsEquip = new ArrayList<String>();

				for (Jugador j: llistaJugadorsEquip) {
					nomsJugadorsEquip.add(j.getNom());
				}

				mostraEquipIntent.putExtra("Equip", nomEquip);

				startActivity(mostraEquipIntent);
			}
		});
	}

}

