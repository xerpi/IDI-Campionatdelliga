package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;


public class MainActivity extends Activity {

	private Button mostraEquipsButton;
	private Button mostraPartitsButton;
	private Equip e1, e2, e3;
	private Partit p1, p2, p3;
	private Hashtable<String, Equip> equipsHashTable;
	private DBManager dbmgr;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		e1 = new Equip("Equip 1", "A");
		e2 = new Equip("Equip 2", "B");
		e3 = new Equip("Equip 3", "C");

		equipsHashTable = new Hashtable<String, Equip>();
		equipsHashTable.put(e1.getNom(), e1);
		equipsHashTable.put(e2.getNom(), e2);
		equipsHashTable.put(e3.getNom(), e3);

		Jugador j1, j2, j3;
		j1 = new Jugador("Jugador 1");
		j2 = new Jugador("Jugador 2");
		j3 = new Jugador("Jugador 3");
		j3.setTipus(Jugador.TipusJugador.RESERVA);

		dbmgr = new DBManager(this);
		//dbmgr.dropTables();

		if (!dbmgr.existsJugador(j1.getNom()))
			dbmgr.insertJugador(j1);
		if (!dbmgr.existsJugador(j2.getNom()))
			dbmgr.insertJugador(j2);
		if (!dbmgr.existsJugador(j3.getNom()))
			dbmgr.insertJugador(j3);

		e1.addJugador(j1);
		e1.addJugador(j2);
		e1.addJugador(j3);
		e2.addJugador(j1);
		e2.addJugador(j2);
		e2.addJugador(j3);
		e3.addJugador(j1);
		e3.addJugador(j2);
		e3.addJugador(j3);

		if (!dbmgr.existsEquip(e1.getNom()))
			dbmgr.insertEquip(e1);
		if (!dbmgr.existsEquip(e2.getNom()))
			dbmgr.insertEquip(e2);
		if (!dbmgr.existsEquip(e3.getNom()))
			dbmgr.insertEquip(e3);

		p1 = new Partit(e1, e2, new Date(), 0, 2);
		p2 = new Partit(e1, e3, new Date(), 1, 2);
		p3 = new Partit(e2, e3, new Date(), 0, 1);

		p1.addGol(j1, 10);
		p1.addGol(j3, 18);

		dbmgr.insertPartit(p1);
		dbmgr.insertPartit(p2);
		dbmgr.insertPartit(p3);

		mostraEquipsButton = (Button)findViewById(R.id.button_mostra_equips);
		mostraEquipsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent mostraEquipsIntent = new Intent(MainActivity.this,
					MostraEquips.class);
				startActivity(mostraEquipsIntent);
			}
		});

		mostraPartitsButton = (Button)findViewById(R.id.button_mostra_partits);
		mostraPartitsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent mostraPartitsIntent = new Intent(MainActivity.this,
					MostraPartits.class);
				startActivity(mostraPartitsIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
