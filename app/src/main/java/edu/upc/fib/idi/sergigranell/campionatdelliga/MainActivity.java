package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class MainActivity extends Activity {

	private Button mostraEquipsButton;
	private Equip e1, e2, e3;
	private Hashtable<String, Equip> equipsHashTable;

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

		DBManager dbmgr = new DBManager(this);
		//dbmgr.dropTables();

		dbmgr.insertJugador(j1);
		dbmgr.insertJugador(j2);
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

		dbmgr.insertEquip(e1);
		dbmgr.insertEquip(e2);
		dbmgr.insertEquip(e3);

		mostraEquipsButton = (Button)findViewById(R.id.button_mostra_equips);
		mostraEquipsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent mostraEquipsIntent = new Intent(MainActivity.this,
					MostraEquips.class);

				List<String> nomsEquips = new ArrayList<String>();
				nomsEquips.add(e1.getNom());
				nomsEquips.add(e2.getNom());
				nomsEquips.add(e3.getNom());

				/*List<Jugador> llistaJugadorsEquip = equip.getJugadors();
				List<String> nomsJugadorsEquip = new ArrayList<String>();

				for (Jugador j: llistaJugadorsEquip) {
					nomsJugadorsEquip.add(j.getNom());
				}*/

				mostraEquipsIntent.putStringArrayListExtra("Equips",
					(ArrayList<String>)nomsEquips);

				startActivity(mostraEquipsIntent);
				//startActivityForResult(mostraEquipsIntent, 1);
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
