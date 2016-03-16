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

