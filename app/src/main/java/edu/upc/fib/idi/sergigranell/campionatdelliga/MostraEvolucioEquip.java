package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class MostraEvolucioEquip extends Activity {

	private DBManager dbmgr;
	private LineChart lineChart;
	private Equip equip;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_evolucio_equip);

		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return;

		String nomEquip = (String)extras.getString("Equip");
		if (nomEquip == null)
			return;

		dbmgr = new DBManager(this);

		equip = dbmgr.queryEquip(nomEquip);
		if (equip == null)
			return;

		this.setTitle("Evolució de " + equip.getNom());

		ArrayList<String> jornadesValues = new ArrayList<>();
		ArrayList<Entry> golsMarcatsValues = new ArrayList<>();
		ArrayList<Entry> golsEncaixatsValues = new ArrayList<>();

		List<Jornada> jornades = dbmgr.queryAllJornades();

		int entryNum = 0;
		for (Jornada j: jornades) {
			List<Partit> partits = j.getPartits();
			int golsMarcats = 0;
			int golsEncaixats = 0;
			for (Partit p: partits) {
				if (p.getLocal().equals(equip)) {
					golsMarcats += p.getGolsLocal();
					golsEncaixats += p.getGolsVisitant();
				} else if (p.getVisitant().equals(equip)) {
					golsMarcats += p.getGolsVisitant();
					golsEncaixats += p.getGolsLocal();
				}
			}
			golsMarcatsValues.add(new Entry(golsMarcats, entryNum));
			golsEncaixatsValues.add(new Entry(golsEncaixats, entryNum));
			jornadesValues.add("Jornada " + j.getNumero());
			entryNum++;
		}

		LineDataSet dataSet1 = new LineDataSet(golsMarcatsValues, "Gols marcats");
		dataSet1.setColor(Color.GREEN);
		dataSet1.setCircleColor(Color.GREEN);

		LineDataSet dataSet2 = new LineDataSet(golsEncaixatsValues, "Gols encaixats");
		dataSet2.setColor(Color.RED);
		dataSet2.setCircleColor(Color.RED);

		ArrayList<ILineDataSet> dataSets = new ArrayList<>();
		dataSets.add(dataSet1);
		dataSets.add(dataSet2);

		dataSet1.setValueFormatter(new Utils.IntegerValueFormatter());
		dataSet2.setValueFormatter(new Utils.IntegerValueFormatter());

		LineData data = new LineData(jornadesValues, dataSets);

		lineChart = (LineChart) findViewById(R.id.linechart_evolucio);
		lineChart.setData(data);
		lineChart.setDescription("Evolució de l'equip");
		lineChart.animateY(1000, Easing.EasingOption.EaseInSine);
		lineChart.getAxisLeft().setAxisMinValue(0);
		lineChart.getAxisRight().setAxisMinValue(0);
		lineChart.getXAxis().setAvoidFirstLastClipping(true);
	}
}
