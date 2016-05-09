package edu.upc.fib.idi.sergigranell.campionatdelliga;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergi.granell on 3/14/16.
 */
public class Equip {

	private String nom;
	private String ciutat;

	private int partitsGuanyats;
	private int partitsPerduts;
	private int partitsEmpatats;

	private String escutFile;

	private List<Jugador> jugadors;
	private List<Jugador> titulars;
	private List<Jugador> reserves;

	public Equip(String nom, String ciutat)
	{
		this.nom = nom;
		this.ciutat = ciutat;
		this.partitsGuanyats = 0;
		this.partitsEmpatats = 0;
		this.partitsPerduts = 0;
		this.escutFile = null;
		jugadors = new ArrayList<Jugador>();
		titulars = new ArrayList<Jugador>();
		reserves = new ArrayList<Jugador>();
	}

	public Equip(String nom, String ciutat, List<Jugador> jugadors)
	{
		this(nom, ciutat);

		for (Jugador j: jugadors) {
			this.jugadors.add(j);
			if (j.getTipus() == Jugador.TipusJugador.TITULAR)
				titulars.add(j);
			else if (j.getTipus() == Jugador.TipusJugador.RESERVA)
				reserves.add(j);
		}
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getCiutat()
	{
		return ciutat;
	}

	public void setCiutat(String ciutat)
	{
		this.ciutat = ciutat;
	}

	public int getPartitsGuanyats()
	{
		return partitsGuanyats;
	}

	public void setPartitsGuanyats(int partitsGuanyats)
	{
		this.partitsGuanyats = partitsGuanyats;
	}

	public int getPartitsPerduts()
	{
		return partitsPerduts;
	}

	public void setPartitsPerduts(int partitsPerduts)
	{
		this.partitsPerduts = partitsPerduts;
	}

	public int getPartitsEmpatats()
	{
		return partitsEmpatats;
	}

	public void setPartitsEmpatats(int partitsEmpatats)
	{
		this.partitsEmpatats = partitsEmpatats;
	}

	public String getEscutFile()
	{
		return escutFile;
	}

	public void setEscutFile(String escutFile)
	{
		this.escutFile = escutFile;
	}

	public List<Jugador> getJugadors()
	{
		return jugadors;
	}

	public List<Jugador> getTitulars()
	{
		return titulars;
	}

	public List<Jugador> getReserves()
	{
		return reserves;
	}

	public int getPunts()
	{
		return 3 * partitsGuanyats + 1 * partitsEmpatats;
	}

	public void addJugador(Jugador jugador)
	{
		jugadors.add(jugador);

		if (jugador.getTipus() == Jugador.TipusJugador.TITULAR)
			titulars.add(jugador);
		else if (jugador.getTipus() == Jugador.TipusJugador.RESERVA)
			reserves.add(jugador);
		else
			Log.i("info", "addJugador: Tipus invalid de jugador");

	}

	public void removeJugador(Jugador jugador)
	{
		jugadors.remove(jugador);

		if (jugador.getTipus() == Jugador.TipusJugador.TITULAR)
			titulars.remove(jugador);
		else if (jugador.getTipus() == Jugador.TipusJugador.RESERVA)
			reserves.remove(jugador);
		else
			Log.i("info", "removeJugador: Tipus invalid de jugador");
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Equip))
			return false;

		Equip other = (Equip)o;
		return this.nom.equals(other.nom);
	}
}

