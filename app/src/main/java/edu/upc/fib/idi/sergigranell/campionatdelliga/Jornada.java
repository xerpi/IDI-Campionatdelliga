package edu.upc.fib.idi.sergigranell.campionatdelliga;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xerpi on 3/16/16.
 */
public class Jornada {

	private List<Partit> partits;
	private int numero;

	public Jornada(List<Partit> partits, int numero)
	{
		this.partits = partits;
		this.numero = numero;
	}

	public Jornada(int numero)
	{
		this(null, numero);
	}

	public List<Partit> getPartits()
	{
		return partits;
	}

	public void setPartits(List<Partit> partits)
	{
		this.partits = partits;
	}

	public int getNumero()
	{
		return numero;
	}

	public void setNumero(int numero)
	{
		this.numero = numero;
	}

	public void addPartit(Partit partit)
	{
		if (partits == null) {
			partits = new ArrayList<Partit>();
		}
		partits.add(partit);
	}

	public int getNumPartits()
	{
		if (partits == null)
			return 0;
		else
			return partits.size();
	}

}
