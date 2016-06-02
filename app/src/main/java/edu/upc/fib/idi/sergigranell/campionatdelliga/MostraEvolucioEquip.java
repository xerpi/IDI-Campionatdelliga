package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

public class MostraEvolucioEquip extends AppCompatActivity {

	public static class FragmentGols extends Fragment {

		private Context context;
		private DBManager dbmgr;
		private LineChart lineChart;
		private Equip equip;

		@Override
		public void onAttach(Activity activity)
		{
			super.onAttach(activity);
			context = activity.getApplicationContext();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			return inflater.inflate(R.layout.activity_mostra_evolucio_equip, container, false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState)
		{
			FrameLayout fl = (FrameLayout) view.findViewById(R.id.mostra_evolucio_equip_framelayout);
			fl.addView(lineChart);
		}

		@Override
		public void onDestroyView()
		{
			super.onDestroyView();
			FrameLayout fl = (FrameLayout) getView().findViewById(R.id.mostra_evolucio_equip_framelayout);
			fl.removeAllViews();
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			String nomEquip = getArguments().getString("Equip", null);

			dbmgr = new DBManager(context);

			equip = dbmgr.queryEquip(nomEquip);
			if (equip == null)
				return;

			ArrayList<String> jornadesValues = new ArrayList<>();
			ArrayList<Entry> golsMarcatsValues = new ArrayList<>();
			ArrayList<Entry> golsEncaixatsValues = new ArrayList<>();

			List<Jornada> jornades = dbmgr.queryAllJornades();

			int entryNum = 0;
			for (Jornada j: jornades) {
				List<Partit> partits = j.getPartits();
				int golsMarcats = 0;
				int golsEncaixats = 0;
				if (partits != null) {
					for (Partit p : partits) {
						if (p.getLocal().equals(equip)) {
							golsMarcats += p.getGolsLocal();
							golsEncaixats += p.getGolsVisitant();
						} else if (p.getVisitant().equals(equip)) {
							golsMarcats += p.getGolsVisitant();
							golsEncaixats += p.getGolsLocal();
						}
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

			lineChart = new LineChart(context);
			lineChart.setData(data);
			lineChart.setDescription("Gols de l'equip");
			lineChart.animateY(1000, Easing.EasingOption.EaseInSine);
			lineChart.getAxisLeft().setAxisMinValue(0);
			lineChart.getAxisRight().setAxisMinValue(0);
			lineChart.getXAxis().setAvoidFirstLastClipping(true);
		}
	}

	public static class FragmentPartits extends Fragment {

		private Context context;
		private DBManager dbmgr;
		private LineChart lineChart;
		private Equip equip;

		@Override
		public void onAttach(Activity activity)
		{
			super.onAttach(activity);
			context = activity.getApplicationContext();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			return inflater.inflate(R.layout.activity_mostra_evolucio_equip, container, false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState)
		{
			FrameLayout fl = (FrameLayout) view.findViewById(R.id.mostra_evolucio_equip_framelayout);
			fl.addView(lineChart);
		}

		@Override
		public void onDestroyView()
		{
			super.onDestroyView();
			FrameLayout fl = (FrameLayout) getView().findViewById(R.id.mostra_evolucio_equip_framelayout);
			fl.removeAllViews();
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			String nomEquip = getArguments().getString("Equip", null);

			dbmgr = new DBManager(context);

			equip = dbmgr.queryEquip(nomEquip);
			if (equip == null)
				return;

			ArrayList<String> jornadesValues = new ArrayList<>();
			ArrayList<Entry> partitsGuanyatsValues = new ArrayList<>();
			ArrayList<Entry> partitsPerdutsValues = new ArrayList<>();
			ArrayList<Entry> partitsEmpatatsValues = new ArrayList<>();

			List<Jornada> jornades = dbmgr.queryAllJornades();

			int entryNum = 0;
			for (Jornada j: jornades) {
				List<Partit> partits = j.getPartits();
				int partitsGuanyats = 0;
				int partitsPerduts = 0;
				int partitsEmpatats = 0;
				if (partits != null) {
					for (Partit p : partits) {
						if (p.getLocal().equals(equip)) {
							if (p.getGolsLocal() > p.getGolsVisitant()) {
								partitsGuanyats++;
							} else if (p.getGolsLocal() < p.getGolsVisitant()) {
								partitsPerduts++;
							} else {
								partitsEmpatats++;
							}
						} else if (p.getVisitant().equals(equip)) {
							if (p.getGolsVisitant() > p.getGolsLocal()) {
								partitsGuanyats++;
							} else if (p.getGolsVisitant() < p.getGolsLocal()) {
								partitsPerduts++;
							} else {
								partitsEmpatats++;
							}
						}
					}
				}
				partitsGuanyatsValues.add(new Entry(partitsGuanyats, entryNum));
				partitsPerdutsValues.add(new Entry(partitsPerduts, entryNum));
				partitsEmpatatsValues.add(new Entry(partitsEmpatats, entryNum));
				jornadesValues.add("Jornada " + j.getNumero());
				entryNum++;
			}

			LineDataSet dataSet1 = new LineDataSet(partitsGuanyatsValues, "Partits guanyats");
			dataSet1.setColor(Color.GREEN);
			dataSet1.setCircleColor(Color.GREEN);

			LineDataSet dataSet2 = new LineDataSet(partitsPerdutsValues, "Partits perduts");
			dataSet2.setColor(Color.RED);
			dataSet2.setCircleColor(Color.RED);

			LineDataSet dataSet3 = new LineDataSet(partitsEmpatatsValues, "Partits empatats");
			dataSet3.setColor(Color.BLUE);
			dataSet3.setCircleColor(Color.BLUE);

			ArrayList<ILineDataSet> dataSets = new ArrayList<>();
			dataSets.add(dataSet1);
			dataSets.add(dataSet2);
			dataSets.add(dataSet3);

			dataSet1.setValueFormatter(new Utils.IntegerValueFormatter());
			dataSet2.setValueFormatter(new Utils.IntegerValueFormatter());
			dataSet3.setValueFormatter(new Utils.IntegerValueFormatter());

			LineData data = new LineData(jornadesValues, dataSets);

			lineChart = new LineChart(context);
			lineChart.setData(data);
			lineChart.setDescription("Partits de l'equip");
			lineChart.animateY(1000, Easing.EasingOption.EaseInSine);
			lineChart.getAxisLeft().setAxisMinValue(0);
			lineChart.getAxisRight().setAxisMinValue(0);
			lineChart.getXAxis().setAvoidFirstLastClipping(true);
		}
	}

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

		this.setTitle("Evolució de " + nomEquip);

		setupTabs(nomEquip);
	}

	private void setupTabs(String nomEquip) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle bundleNomEquip = new Bundle();
		bundleNomEquip.putString("Equip", nomEquip);

		ActionBar.Tab tab1 = actionBar
			.newTab()
			.setText("Gols")
			.setTabListener(new SupportFragmentTabListener<FragmentGols>(
				R.id.mostra_evolucio_equip_framelayout,
				this, "gols", FragmentGols.class, bundleNomEquip));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		ActionBar.Tab tab2 = actionBar
			.newTab()
			.setText("Partits")
			.setTabListener(new SupportFragmentTabListener<FragmentPartits>(
				R.id.mostra_evolucio_equip_framelayout,
				this, "partits", FragmentPartits.class, bundleNomEquip));
		actionBar.addTab(tab2);
	}
}
