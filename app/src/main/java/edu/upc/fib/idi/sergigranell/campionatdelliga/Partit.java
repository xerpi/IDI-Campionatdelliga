package edu.upc.fib.idi.sergigranell.campionatdelliga;

import java.util.Date;

/**
 * Created by xerpi on 3/14/16.
 */
public class Partit {
	private Equip local;
	private Equip visitant;
	private Date data;
	private int golsLocal;
	private int golsVisitant;

	public Partit(Equip local, Equip visitant, Date data, int golsLocal, int golsVisitant)
	{
		this.local = local;
		this.visitant = visitant;
		this.data = data;
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

	public Date getData()
	{
		return data;
	}

	public void setData(Date data)
	{
		this.data = data;
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
