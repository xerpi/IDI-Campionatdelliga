package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MostraClassificacioGolsJugadors extends AppCompatActivity {

	private DBManager dbmgr;
	private BarChart barChart;
	private List<Jugador> jugadors;

	private ArrayList<BarEntry> golsJugadors;
	private ArrayList<String> nomsJugadors;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_classificacio_gols_jugadors);

		this.setTitle("Classificació de gols");

		dbmgr = new DBManager(this);
		jugadors = dbmgr.queryAllJugadors();

		Collections.sort(jugadors, new Comparator<Jugador>() {
			@Override
			public int compare(Jugador j1, Jugador j2) {
				return j1.getGolsMarcats() - j1.getGolsMarcats();
			}
		});

		golsJugadors = new ArrayList<>();
		nomsJugadors = new ArrayList<String>();

		int i = 0;
		for (Jugador j: jugadors) {
			golsJugadors.add(new BarEntry(j.getGolsMarcats(), i++));
			nomsJugadors.add(j.getNom());
		}

		BarDataSet dataSetPuntsEquips = new BarDataSet(golsJugadors, null);
		BarData data = new BarData(nomsJugadors, dataSetPuntsEquips);

		dataSetPuntsEquips.setValueFormatter(new Utils.IntegerValueFormatter());

		barChart = (BarChart)findViewById(R.id.barchart_classificacio_jugadors);
		barChart.setData(data);
		barChart.setDescription("Gols marcats");
		barChart.animateY(1000, Easing.EasingOption.EaseInSine);
		barChart.getXAxis().setAvoidFirstLastClipping(true);
	}
}
