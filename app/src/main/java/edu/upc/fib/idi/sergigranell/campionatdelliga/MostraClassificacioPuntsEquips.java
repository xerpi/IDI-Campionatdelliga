package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MostraClassificacioPuntsEquips extends AppCompatActivity {

	private DBManager dbmgr;
	private BarChart barChart;
	private List<Equip> equips;

	private ArrayList<BarEntry> puntsEquips;
	private ArrayList<String> nomsEquips;

	private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_classificacio_punts_equips);

		this.setTitle("Classificació");

		toast = new Toast(this);

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

		dataSetPuntsEquips.setValueFormatter(new Utils.IntegerValueFormatter());

		barChart = (BarChart)findViewById(R.id.barchart_classificacio_equips);
		barChart.setData(data);
		barChart.setDescription("Punts dels equips");
		barChart.animateY(1000, Easing.EasingOption.EaseInSine);
		barChart.getXAxis().setAvoidFirstLastClipping(true);
		barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
			@Override
			public void onValueSelected(Entry entry, int i, Highlight highlight)
			{
				Equip equip = equips.get(entry.getXIndex());
				toast.cancel();
				toast = Toast.makeText(MostraClassificacioPuntsEquips.this,
					equip.getNom() + ": " + equip.getPunts() + " punts.",
					Toast.LENGTH_SHORT);
				toast.show();
			}

			@Override
			public void onNothingSelected()
			{

			}
		});
	}
}
