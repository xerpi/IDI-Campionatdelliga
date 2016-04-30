package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MostraClassificacioPuntsEquips extends Activity {

	private BarChart barChart;
	private DBManager dbmgr;
	private List<Equip> equips;

	private ArrayList<BarEntry> puntsEquips;
	private ArrayList<String> nomsEquips;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_classificacio_punts_equips);

		this.setTitle("Classificació");

		dbmgr = new DBManager(this);
		equips = dbmgr.queryAllEquips();

		Collections.sort(equips, new Comparator<Equip>() {
			@Override
			public int compare(Equip p1, Equip p2) {
				return p2.getPunts() - p1.getPunts();
			}
		});

		puntsEquips = new ArrayList<>();
		nomsEquips = new ArrayList<String>();

		int i = 0;
		for (Equip e: equips) {
			puntsEquips.add(new BarEntry(e.getPunts(), i++));
			nomsEquips.add(e.getNom());
		}

		BarDataSet dataSetPuntsEquips = new BarDataSet(puntsEquips, null);
		BarData data = new BarData(nomsEquips, dataSetPuntsEquips);

		dataSetPuntsEquips.setValueFormatter(new ValueFormatter() {
			@Override
			public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler)
			{
				return String.valueOf((int)v);
			}
		});

		barChart = (BarChart)findViewById(R.id.barchart_classificacio);
		barChart.setData(data);
		barChart.setDescription("Punts dels equips");
		barChart.animateY(1000, Easing.EasingOption.EaseInSine);
	}
}
