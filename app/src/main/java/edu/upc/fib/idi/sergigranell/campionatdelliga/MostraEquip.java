package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MostraEquip extends Activity {

	private Button mostraJugadorsButton;
	private ImageView escutEquipImageView;
	private String nomEquip;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_equip);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			nomEquip = (String)extras.getString("Equip");
		}

		this.setTitle(nomEquip);

		mostraJugadorsButton = (Button)findViewById(R.id.button_mostra_jugadors);
		escutEquipImageView = (ImageView)findViewById(R.id.imageview_escut_equip);

		escutEquipImageView.setImageResource(android.R.drawable.btn_star_big_off);
		escutEquipImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent imagePickerIntent = new Intent(MostraEquip.this,
					ImagePickerActivity.class);

				ImagePickerActivity imagePicker = new ImagePickerActivity();
				startActivity(imagePickerIntent);

				Log.i("info", "picked: " + imagePicker.getSelectedImagePath());
			}
		});

		mostraJugadorsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				DBManager dbmgr = new DBManager(MostraEquip.this);

				Equip equip = dbmgr.queryEquip(nomEquip);
				if (equip == null)
					return;

				Intent mostraJugadorsEquipIntent = new Intent(MostraEquip.this,
					MostraJugadorsEquip.class);

				List<Jugador> llistaJugadorsEquip = equip.getJugadors();
				List<String> nomsJugadorsEquip = new ArrayList<String>();

				for (Jugador j: llistaJugadorsEquip) {
					nomsJugadorsEquip.add(j.getNom());
				}

				mostraJugadorsEquipIntent.putExtra("Equip", equip.getNom());
				mostraJugadorsEquipIntent.putStringArrayListExtra("JugadorsEquip",
					(ArrayList<String>)nomsJugadorsEquip);

				startActivity(mostraJugadorsEquipIntent);
			}
		});
	}

}
