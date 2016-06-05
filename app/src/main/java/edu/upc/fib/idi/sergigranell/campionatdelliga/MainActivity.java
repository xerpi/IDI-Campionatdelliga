package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

	private TextView jornadaActualTextView;
	private HorizontalBarChart top3EquipsBarChart;
	private HorizontalBarChart top3JugadorsBarChart;

	private ArrayList<BarEntry> top3EquipsPunts;
	private ArrayList<String> top3EquipsNoms;

	private BarDataSet barDataSetTop3Equips;
	private BarData barDataTop3Equips;

	private ArrayList<BarEntry> top3JugadorsGols;
	private ArrayList<String> top3JugadorsNoms;

	private BarDataSet barDataSetTop3Jugadors;
	private BarData barDataTop3Jugadors;

	private Button mostraEquipsButton;
	private Button mostraPartitsButton;
	private Button mostraJornadesButton;
	private Button mostraClassificacioEquipsButton;
	private Button mostraClassificacioJugadorsButton;

	private int numJornadaActual;
	private List<Equip> top3Equips;
	private List<Jugador> top3Jugadors;

	private DBManager dbmgr;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbmgr = new DBManager(this);

		top3Equips = new ArrayList<Equip>();
		top3EquipsPunts = new ArrayList<BarEntry>();
		top3EquipsNoms = new ArrayList<String>();

		barDataSetTop3Equips = new BarDataSet(top3EquipsPunts, null);
		barDataSetTop3Equips.setColors(ColorTemplate.COLORFUL_COLORS);
		barDataTop3Equips = new BarData(top3EquipsNoms, barDataSetTop3Equips);
		barDataSetTop3Equips.setValueFormatter(new Utils.IntegerValueFormatter());

		top3EquipsBarChart = (HorizontalBarChart)findViewById(R.id.horizontalbarchart_resum_equips);
		top3EquipsBarChart.setData(barDataTop3Equips);
		top3EquipsBarChart.setDescription("Top 3 classificació de la lliga");
		top3EquipsBarChart.getAxisLeft().setAxisMinValue(0);
		top3EquipsBarChart.getAxisRight().setAxisMinValue(0);
		top3EquipsBarChart.getXAxis().setAvoidFirstLastClipping(true);
		top3EquipsBarChart.setPinchZoom(false);
		top3EquipsBarChart.setHighlightPerTapEnabled(false);
		top3EquipsBarChart.setHighlightPerDragEnabled(false);
		top3EquipsBarChart.setDoubleTapToZoomEnabled(false);
		top3EquipsBarChart.getLegend().setEnabled(false);
		top3EquipsBarChart.getAxisLeft().setEnabled(false);

		top3Jugadors = new ArrayList<Jugador>();
		top3JugadorsGols = new ArrayList<BarEntry>();
		top3JugadorsNoms = new ArrayList<String>();

		barDataSetTop3Jugadors = new BarDataSet(top3JugadorsGols, null);
		barDataSetTop3Jugadors.setColors(ColorTemplate.COLORFUL_COLORS);
		barDataTop3Jugadors = new BarData(top3JugadorsNoms, barDataSetTop3Jugadors);
		barDataSetTop3Jugadors.setValueFormatter(new Utils.IntegerValueFormatter());

		top3JugadorsBarChart = (HorizontalBarChart)findViewById(R.id.horizontalbarchart_resum_jugadors);
		top3JugadorsBarChart.setData(barDataTop3Jugadors);
		top3JugadorsBarChart.setDescription("Top 3 jugadors de la lliga");
		top3JugadorsBarChart.getAxisLeft().setAxisMinValue(0);
		top3JugadorsBarChart.getAxisRight().setAxisMinValue(0);
		top3JugadorsBarChart.getXAxis().setAvoidFirstLastClipping(true);
		top3JugadorsBarChart.setPinchZoom(false);
		top3JugadorsBarChart.setHighlightPerTapEnabled(false);
		top3JugadorsBarChart.setHighlightPerDragEnabled(false);
		top3JugadorsBarChart.setDoubleTapToZoomEnabled(false);
		top3JugadorsBarChart.getLegend().setEnabled(false);
		top3JugadorsBarChart.getAxisLeft().setEnabled(false);

		jornadaActualTextView = (TextView)findViewById(R.id.textview_jornada_actual);

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
	protected void onResume()
	{
		super.onResume();

		numJornadaActual = dbmgr.getCurrentJornadaNum();
		jornadaActualTextView.setText("Jornada actual de lliga: " + numJornadaActual);

		top3Equips = dbmgr.queryTop3EquipsByPunts();
		top3EquipsPunts.clear();
		top3EquipsNoms.clear();

		int i = 0;
		for (Equip e: top3Equips) {
			top3EquipsPunts.add(0, new BarEntry(e.getPunts(), top3Equips.size()-(++i)));
			top3EquipsNoms.add(0, e.getNom());
		}

		barDataTop3Equips.notifyDataChanged();
		barDataTop3Equips.notifyDataChanged();
		top3EquipsBarChart.notifyDataSetChanged();
		top3EquipsBarChart.invalidate();

		top3Jugadors = dbmgr.queryTop3JugadorsByGols();
		top3JugadorsGols.clear();
		top3JugadorsNoms.clear();

		i = 0;
		for (Jugador j: top3Jugadors) {
			top3JugadorsGols.add(0, new BarEntry(j.getGolsMarcats(), top3Jugadors.size()-(++i)));
			top3JugadorsNoms.add(0, j.getNom());
		}

		barDataTop3Jugadors.notifyDataChanged();
		barDataTop3Jugadors.notifyDataChanged();
		top3JugadorsBarChart.notifyDataSetChanged();
		top3JugadorsBarChart.invalidate();
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
			final Dialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("Ajuda")
				.setMessage("S'obriarà un enllaç extern, estàs segur/a que vols continuar?")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse("https://github.com/xerpi/IDI-Campionatdelliga/blob/master/InformePractica.md"));
						startActivity(browserIntent);
					}
				})
				.setNegativeButton(android.R.string.no, null).show();
			return true;
		} else if (id == R.id.main_activity_action_about) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Campionat de lliga");
			alertDialog.setMessage(
				"Aplicació creada per Sergi Granell (sergi.granell@est.fib.upc.edu)\n" +
				"Codi font: https://github.com/xerpi/IDI-Campionatdelliga");
			alertDialog.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
