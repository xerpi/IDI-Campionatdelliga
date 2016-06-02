package edu.upc.fib.idi.sergigranell.campionatdelliga;

/**
 * Created by sergi.granell on 3/14/16.
 */
public class Jugador {

	public enum TipusJugador {
		TITULAR,
		RESERVA,
		ELIMINAT
	}

	private String nom;
	private TipusJugador tipus;
	private int golsMarcats;

	public Jugador(String nom)
	{
		this.nom = nom;
		this.tipus = TipusJugador.TITULAR;
		this.golsMarcats = 0;
	}

	public Jugador(String nom, TipusJugador tipus)
	{
		this.nom = nom;
		this.tipus = tipus;
		this.golsMarcats = 0;
	}

	public Jugador(String nom, TipusJugador tipus, int golsMarcats)
	{
		this.nom = nom;
		this.tipus = tipus;
		this.golsMarcats = golsMarcats;
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

	public int getGolsMarcats()
	{
		return golsMarcats;
	}

	public void setGolsMarcats(int golsMarcats)
	{
		this.golsMarcats = golsMarcats;
	}

	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Jugador))
			return false;

		Jugador other = (Jugador)o;
		return this.nom.equals(other.nom) &&
			this.tipus == other.tipus;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + nom.hashCode();
		hash = hash * 31 + tipus.hashCode();
		return hash;
	}
}

