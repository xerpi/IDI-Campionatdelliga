package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

	private Button mostraEquipsButton;
	private Button mostraPartitsButton;
	private Button mostraJornadesButton;
	private Button mostraClassificacioEquipsButton;
	private Button mostraClassificacioJugadorsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		mostraJornadesButton = (Button)findViewById(R.id.button_mostra_jornades);
		mostraJornadesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent mostraJornadesIntent = new Intent(MainActivity.this,
					MostraJornades.class);
				startActivity(mostraJornadesIntent);
			}
		});

		mostraClassificacioEquipsButton = (Button)findViewById(R.id.button_mostra_classificacio_equips);
		mostraClassificacioEquipsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent mostraClassificacioEquipsIntent = new Intent(MainActivity.this,
					MostraClassificacioPuntsEquips.class);
				startActivity(mostraClassificacioEquipsIntent);
			}
		});

		mostraClassificacioJugadorsButton = (Button)findViewById(R.id.button_mostra_classificacio_jugadors);
		mostraClassificacioJugadorsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent mostraClassificacioJugadorsIntent = new Intent(MainActivity.this,
					MostraClassificacioGolsJugadors.class);
				startActivity(mostraClassificacioJugadorsIntent);
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		if (id == R.id.main_activity_action_help) {
			return true;
		} else if (id == R.id.main_activity_action_about) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Campionat de lliga");

			// Setting Dialog Message
			alertDialog.setMessage(
				"Aplicació creada per Sergi Granell (sergi.granell@est.fib.upc.edu)\n" +
				"Codi font: https://github.com/xerpi/IDI-Campionatdelliga");

			// Showing Alert Message
			alertDialog.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
