package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;


public class MainActivity extends AppCompatActivity {

	private Button mostraEquipsButton;
	private Button mostraPartitsButton;
	private Button mostraJornadesButton;
	private Button mostraClassificacioButton;

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

		mostraClassificacioButton = (Button)findViewById(R.id.button_mostra_classificacio);
		mostraClassificacioButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent mostraClassificacioIntent = new Intent(MainActivity.this,
					MostraClassificacioPuntsEquips.class);
				startActivity(mostraClassificacioIntent);
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
