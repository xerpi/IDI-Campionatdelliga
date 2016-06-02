package edu.upc.fib.idi.sergigranell.campionatdelliga;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xerpi on 3/14/16.
 */
public class Partit {
	private Equip local;
	private Equip visitant;
	int jornada;
	private int golsLocal;
	private int golsVisitant;

	static public class Gol {
		private Jugador jugador;
		private int minut;

		public Gol(Jugador jugador, int minut)
		{
			this.jugador = jugador;
			this.minut = minut;
		}

		public Jugador getJugador()
		{
			return jugador;
		}

		public void setJugador(Jugador jugador)
		{
			this.jugador = jugador;
		}

		public int getMinut()
		{
			return minut;
		}

		public void setMinut(int minut)
		{
			this.minut = minut;
		}

		@Override
		public boolean equals(Object o)
		{
			if (o == null || !(o instanceof Gol))
				return false;

			Gol other = (Gol)o;
			return this.jugador.equals(other.jugador) &&
				this.minut == other.minut;
		}

		@Override
		public int hashCode() {
			int hash = 1;
			hash = hash * 31 + (jugador == null ? 0 : jugador.hashCode());
			hash = hash * 31 + minut;
			return hash;
		}
	}

	List<Gol> gols;

	public Partit(Equip local, Equip visitant, int jornada, int golsLocal, int golsVisitant)
	{
		this.local = local;
		this.visitant = visitant;
		this.jornada = jornada;
		this.golsLocal = golsLocal;
		this.golsVisitant = golsVisitant;
		this.gols = new ArrayList<Gol>();
	}

	public Partit(Equip local, Equip visitant, int jornada)
	{
		this(local, visitant, jornada, 0, 0);
	}

	public Equip getLocal()
	{
		return local;
	}

	public void setLocal(Equip local)
	{
		this.local = local;
	}

	public Equip getVisitant()
	{
		return visitant;
	}

	public void setVisitant(Equip visitant)
	{
		this.visitant = visitant;
	}

	public int getJornada()
	{
		return jornada;
	}

	public void setJornada(int jornada)
	{
		this.jornada = jornada;
	}

	public int getGolsVisitant()
	{
		return golsVisitant;
	}

	public void setGolsVisitant(int golsVisitant)
	{
		this.golsVisitant = golsVisitant;
	}

	public int getGolsLocal()
	{
		return golsLocal;
	}

	public void setGolsLocal(int golsLocal)
	{
		this.golsLocal = golsLocal;
	}

	public List<Gol> getGols()
	{
		return gols;
	}

	public void setGols(List<Gol> gols)
	{
		this.gols = gols;
	}

	public void addGol(Jugador jugador, int minut)
	{
		addGol(new Gol(jugador, minut));
	}

	public void addGol(Gol gol)
	{
		if (gols.contains(gol))
			return;

		if (getLocal().getTitulars().contains(gol.getJugador()))
			golsLocal++;
		else if (getVisitant().getTitulars().contains(gol.getJugador()))
			golsVisitant++;
		else
			return;

		gols.add(gol);
	}

	public void addGolList(List<Gol> gols)
	{
		for (Gol g: gols)
			addGol(g);
	}

	public void updatePuntsEquips(DBManager dbmgr)
	{
		if (golsLocal > golsVisitant) {
			local.setPartitsGuanyats(local.getPartitsGuanyats() + 1);
			visitant.setPartitsPerduts(visitant.getPartitsPerduts() + 1);
		} else if (golsLocal < golsVisitant) {
			local.setPartitsPerduts(local.getPartitsPerduts() + 1);
			visitant.setPartitsGuanyats(visitant.getPartitsGuanyats() + 1);
		} else {
			local.setPartitsEmpatats(local.getPartitsEmpatats() + 1);
			visitant.setPartitsEmpatats(visitant.getPartitsEmpatats() + 1);
		}

		dbmgr.updateEquip(local);
		dbmgr.updateEquip(visitant);
	}
}
