package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MostraEquip extends Activity {

	private DBManager dbmgr;

	private TextView partitsGuanyatsTextView;
	private TextView partitsEmpatatsTextView;
	private TextView partitsPerdutsTextView;
	private TextView puntsTextView;
	private Button mostraJugadorsButton;
	private Button mostraEvolucioButton;
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

		dbmgr = new DBManager(this);

		equip = dbmgr.queryEquip(nomEquip);
		if (equip == null)
			return;

		this.setTitle(equip.getNom());

		String escutFile = equip.getEscutFile();
		escutBitmap = Utils.loadEscutBitmap(this, escutFile);

		partitsGuanyatsTextView = (TextView)findViewById(
			R.id.mostra_equip_textview_partits_guanyats);
		partitsEmpatatsTextView = (TextView)findViewById(
			R.id.mostra_equip_textview_partits_empatats);
		partitsPerdutsTextView = (TextView)findViewById(
			R.id.mostra_equip_textview_partits_perduts);
		puntsTextView = (TextView)findViewById(R.id.mostra_equip_textview_punts);
		mostraJugadorsButton = (Button)findViewById(R.id.button_mostra_jugadors);
		mostraEvolucioButton = (Button)findViewById(R.id.button_mostra_evolucio);
		escutEquipImageView = (ImageView)findViewById(R.id.imageview_escut_equip);

		partitsGuanyatsTextView.setText(Integer.toString(equip.getPartitsGuanyats()));
		partitsEmpatatsTextView.setText(Integer.toString(equip.getPartitsEmpatats()));
		partitsPerdutsTextView.setText(Integer.toString(equip.getPartitsPerduts()));
		puntsTextView.setText(Integer.toString(equip.getPunts()));

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

		mostraEvolucioButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent mostraEvolucioEquipIntent = new Intent(MostraEquip.this,
					MostraEvolucioEquip.class);

				mostraEvolucioEquipIntent.putExtra("Equip", equip.getNom());

				startActivity(mostraEvolucioEquipIntent);
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

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}
}
