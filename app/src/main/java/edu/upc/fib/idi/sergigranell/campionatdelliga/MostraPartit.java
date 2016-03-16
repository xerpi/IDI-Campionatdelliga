package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.os.Bundle;

public class MostraPartit extends Activity {

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
	}

}
