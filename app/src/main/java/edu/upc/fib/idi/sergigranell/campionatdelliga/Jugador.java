package edu.upc.fib.idi.sergigranell.campionatdelliga;

/**
 * Created by sergi.granell on 3/14/16.
 */
public class Jugador {

	public enum TipusJugador {
		TITULAR,
		RESERVA
	}

	private String nom;
	private TipusJugador tipus;

	public Jugador(String nom)
	{
		this.nom = nom;
		this.tipus = TipusJugador.TITULAR;
	}

	public Jugador(String nom, TipusJugador tipus)
	{
		this.nom = nom;
		this.tipus = tipus;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public TipusJugador getTipus()
	{
		return tipus;
	}

	public void setTipus(TipusJugador tipus)
	{
		this.tipus = tipus;
	}
}

