package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MostraPartit extends Activity {

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

		String dataString = (String)extras.getString("Data");
		if (dataString == null)
			return;

		dbmgr = new DBManager(this);

		partit = dbmgr.queryPartit(nomEquipLocal, nomEquipVisitant, dataString);
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
		dataTextView.setText(Utils.dateToString(partit.getData()));
	}
}
