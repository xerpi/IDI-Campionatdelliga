package edu.upc.fib.idi.sergigranell.campionatdelliga;

/**
 * Created by xerpi on 3/14/16.
 */
public class Partit {
	private Equip local;
	private Equip visitant;

	private int golsLocal;
	private int golsVisitant;

	public Partit(Equip local, Equip visitant, int golsLocal, int golsVisitant)
	{
		this.local = local;
		this.visitant = visitant;
		this.golsLocal = golsLocal;
		this.golsVisitant = golsVisitant;
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
}
