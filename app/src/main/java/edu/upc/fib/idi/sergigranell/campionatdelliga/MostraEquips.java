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

	private List<Equip> equips;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_equips);

		arrayList = new ArrayList<String>();
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

		dbmgr = new DBManager(this);

		equips = dbmgr.queryAllEquips();
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
				Equip equip = equips.get(position);

				Intent mostraEquipIntent = new Intent(MostraEquips.this,
					MostraEquip.class);

				mostraEquipIntent.putExtra("Equip", equip.getNom());

				startActivity(mostraEquipIntent);
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

