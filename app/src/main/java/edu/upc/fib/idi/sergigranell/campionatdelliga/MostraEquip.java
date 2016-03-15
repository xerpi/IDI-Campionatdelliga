package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MostraEquip extends Activity {

	private DBManager dbmgr;

	private Button mostraJugadorsButton;
	private ImageView escutEquipImageView;

	private Equip equip;
	private Bitmap escutBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_equip);

		Bundle extras = getIntent().getExtras();
		if (extras == null)
			return;

		String nomEquip = (String)extras.getString("Equip");
		if (nomEquip == null)
			return;

		dbmgr = new DBManager(MostraEquip.this);

		equip = dbmgr.queryEquip(nomEquip);
		if (equip == null)
			return;

		this.setTitle(equip.getNom());

		String escutFile = equip.getEscutFile();
		escutBitmap = Utils.loadEscutBitmap(this, escutFile);

		mostraJugadorsButton = (Button)findViewById(R.id.button_mostra_jugadors);
		escutEquipImageView = (ImageView)findViewById(R.id.imageview_escut_equip);

		escutEquipImageView.setImageBitmap(escutBitmap);
		escutEquipImageView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v)
			{
				Utils.startImagePickerActivity(MostraEquip.this);
				return true;
			}
		});

		mostraJugadorsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK) {
			if (requestCode == Utils.PICK_IMAGE_REQUEST) {
				Uri selectedImageUri = data.getData();
				String selectedImagePath = Utils.getFilePathFromURI(
					MostraEquip.this, selectedImageUri);

				equip.setEscutFile(selectedImagePath);
				dbmgr.updateEquip(equip);

				escutBitmap = Utils.loadEscutBitmap(this, selectedImagePath);
				escutEquipImageView.setImageBitmap(escutBitmap);
			}
		}
	}
}
