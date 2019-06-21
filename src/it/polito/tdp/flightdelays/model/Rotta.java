package it.polito.tdp.flightdelays.model;

public class Rotta implements Comparable<Rotta>{
	

	private Airport origine;
	private Airport destinazione;
	private double peso;
	
	public Rotta(Airport origine, Airport destinazione, double peso) {
		super();
		this.origine = origine;
		this.destinazione = destinazione;
		this.peso = peso;
	}

	public Airport getOrigine() {
		return origine;
	}

	public Airport getDestinazione() {
		return destinazione;
	}

	public double getPeso() {
		return peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destinazione == null) ? 0 : destinazione.hashCode());
		result = prime * result + ((origine == null) ? 0 : origine.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		if (destinazione == null) {
			if (other.destinazione != null)
				return false;
		} else if (!destinazione.equals(other.destinazione))
			return false;
		if (origine == null) {
			if (other.origine != null)
				return false;
		} else if (!origine.equals(other.origine))
			return false;
		return true;
	}
	
	
	public String toString(){
		return "Da "+origine.getName()+" a "+destinazione.getName()+" con peso: "+peso;
	}

	@Override
	public int compareTo(Rotta r) {
		// TODO Auto-generated method stub
	    if(r.peso-this.peso<0)
	    	return -1;
	    else if(r.peso-this.peso>0)
	    	return 1;
	    else
	    	return 0;
	}
	
}
