package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MostraPartit extends AppCompatActivity {

	private TextView equipLocalTextView;
	private TextView equipVisitantTextView;
	private TextView resultatTextView;
	private TextView dataTextView;

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

		equipLocalTextView = (TextView)findViewById(R.id.textview_equip_local);
		equipLocalTextView.setText(nomEquipLocal);

		equipVisitantTextView = (TextView)findViewById(R.id.textview_equip_visitant);
		equipVisitantTextView.setText(nomEquipVisitant);

		resultatTextView = (TextView)findViewById(R.id.textview_resultat);
		resultatTextView.setText(partit.getGolsLocal() + "-" + partit.getGolsVisitant());

		dataTextView = (TextView)findViewById(R.id.textview_data);
		dataTextView.setText(Integer.toString(partit.getJornada()));
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}
}
