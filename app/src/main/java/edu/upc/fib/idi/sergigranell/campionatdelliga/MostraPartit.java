package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MostraPartit extends AppCompatActivity {

	private TextView equipLocalTextView;
	private TextView equipVisitantTextView;
	private TextView resultatTextView;
	private TextView jornadaTextView;
	private ListView llistaGolsListView;

	private ArrayAdapter<Partit.Gol> arrayAdapterLlistaGols;
	private ArrayList<Partit.Gol> arrayListLlistaGols;

	private DBManager dbmgr;

	private Partit partit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_partit);

		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return;

		String nomEquipLocal = (String)extras.getString("EquipLocal");
		if (nomEquipLocal == null)
			return;

		String nomEquipVisitant = (String)extras.getString("EquipVisitant");
		if (nomEquipVisitant == null)
			return;

		int jornada = extras.getInt("Jornada");

		dbmgr = new DBManager(this);

		partit = dbmgr.queryPartit(nomEquipLocal, nomEquipVisitant, jornada);
		if (partit == null)
			return;

		this.setTitle(partit.getLocal().getNom() + " vs " +
			partit.getVisitant().getNom() + ": " +
			partit.getGolsLocal() + " - " +
			partit.getGolsVisitant());

		List<Partit.Gol> gols = partit.getGols();

		Collections.sort(gols, new Comparator<Partit.Gol>() {
			@Override
			public int compare(Partit.Gol g1, Partit.Gol g2) {
				return g1.getMinut() - g2.getMinut();
			}
		});

		arrayListLlistaGols = new ArrayList<Partit.Gol>();
		arrayListLlistaGols.addAll(partit.getGols());

		arrayAdapterLlistaGols = new ArrayAdapter<Partit.Gol>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arrayListLlistaGols) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(android.R.id.text1);

				Partit.Gol gol = arrayListLlistaGols.get(position);
				tv.setText(gol.getJugador().getNom() + ": Minut " + gol.getMinut());
				return view;
			}
		};

		llistaGolsListView = (ListView)findViewById(R.id.listview_llista_gols_partit);
		llistaGolsListView.setAdapter(arrayAdapterLlistaGols);

		equipLocalTextView = (TextView)findViewById(R.id.textview_equip_local);
		equipLocalTextView.setText(nomEquipLocal);

		equipVisitantTextView = (TextView)findViewById(R.id.textview_equip_visitant);
		equipVisitantTextView.setText(nomEquipVisitant);

		resultatTextView = (TextView)findViewById(R.id.textview_resultat);
		resultatTextView.setText(partit.getGolsLocal() + "-" + partit.getGolsVisitant());

		jornadaTextView = (TextView)findViewById(R.id.textview_jornada);
		jornadaTextView.setText(Integer.toString(partit.getJornada()));
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}
}
