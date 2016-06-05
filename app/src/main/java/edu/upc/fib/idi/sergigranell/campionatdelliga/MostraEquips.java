package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MostraEquips extends AppCompatActivity {

	private ListView equipsListView;
	private ArrayAdapter<Equip> arrayAdapterEquips;

	private DBManager dbmgr;

	private List<Equip> equips;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostra_equips);

		dbmgr = new DBManager(this);

		equips = dbmgr.queryAllEquips();

		this.setTitle("Llista d'equips");

		arrayAdapterEquips = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, equips) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView tv = (TextView) view.findViewById(android.R.id.text1);

				tv.setText(equips.get(position).getNom());
				return view;
			}
		};

		equipsListView = (ListView)findViewById(R.id.listview_equips);
		equipsListView.setAdapter(arrayAdapterEquips);
		equipsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Equip equip = equips.get(position);

				Intent mostraEquipIntent = new Intent(MostraEquips.this,
					MostraEquip.class);

				mostraEquipIntent.putExtra("Equip", equip.getNom());

				startActivity(mostraEquipIntent);
			}
		});
		equipsListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
			{
				if (view.getId() == R.id.listview_equips) {
					AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) contextMenuInfo;
					Equip equip = equips.get(info.position);

					MenuInflater inflater = getMenuInflater();
					inflater.inflate(R.menu.menu_mostra_equips, contextMenu);
				}
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		dbmgr.close();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		final Equip selectedEquip = equips.get(info.position);

		switch (item.getItemId()) {
		case R.id.mostra_equips_item_eliminar_equip:

			LayoutInflater inflater = this.getLayoutInflater();
			final View dialogView = inflater.inflate(R.layout.layout_afegir_equip, null);

			final AlertDialog alertDialog = new AlertDialog.Builder(MostraEquips.this)
				.setTitle("Afegir equip")
				.setView(dialogView)
				.setPositiveButton(android.R.string.ok, null)
				.setNegativeButton(android.R.string.cancel, null)
				.create();

			alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {

					Button buttonAccept = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
					buttonAccept.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view)
						{
							selectedEquip.setEliminat(true);

							EditText nomEquipEditText;
							EditText ciutatEquipEditText;

							nomEquipEditText = (EditText)dialogView.findViewById(R.id.edit_text_afegir_equip_nom);
							ciutatEquipEditText = (EditText)dialogView.findViewById(R.id.edit_text_afegir_equip_ciutat);

							String nomEquip = nomEquipEditText.getText().toString();
							String ciutatEquip =ciutatEquipEditText.getText().toString();

							if (TextUtils.isEmpty(nomEquip)) {
								nomEquipEditText.setError("El nom de l'equip no pot ser buit!");
								return;
							} else if (TextUtils.isEmpty(ciutatEquip)) {
								ciutatEquipEditText.setError("La ciutat de l'equip no pot ser buida!");
								return;
							}

							Equip nouEquip = new Equip(nomEquip, ciutatEquip);

							int nextJugadorNum = Utils.getNextJugadorNum(dbmgr);

							List <Jugador> jugadorsNous = new ArrayList<Jugador>();
							for (int i = 0; i < Utils.NUM_JUGADORS_EQUIP; i++) {
								Jugador j = new Jugador("Jugador " + (nextJugadorNum + i));
								if (i < Utils.NUM_JUGADORS_TITULARS)
									j.setTipus(Jugador.TipusJugador.TITULAR);
								else
									j.setTipus(Jugador.TipusJugador.RESERVA);
								jugadorsNous.add(j);
								nouEquip.addJugador(j);
							}

							dbmgr.insertJugadors(jugadorsNous);

							dbmgr.updateEquip(selectedEquip);
							dbmgr.insertEquip(nouEquip);

							equips.remove(selectedEquip);
							equips.add(nouEquip);

							arrayAdapterEquips.notifyDataSetChanged();
							alertDialog.dismiss();

							Toast.makeText(MostraEquips.this, selectedEquip.getNom() +
									" eliminat, " + nouEquip.getNom() + " ha entrat a la lliga.",
								Toast.LENGTH_LONG).show();
						}
					});
				}
			});
			alertDialog.show();
			return true;
		}
		return false;
	}

}

