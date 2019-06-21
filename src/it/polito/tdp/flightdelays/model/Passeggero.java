package it.polito.tdp.flightdelays.model;

public class Passeggero {

	private int id;
	private int num_voli;
	private Airport aereoporto;
	private final Airport primo;
	private double delay;
	
	
	public Passeggero(int id, Airport aereoporto) {
		
		this.id = id;
		this.setAereoporto(aereoporto);
		this.primo = aereoporto;
		this.num_voli = 0;
	    this.delay=0;
	   
	}

	public int getId() {
		return id;
	}

	public int getNum_voli() {
		return num_voli;
	}
	
	public void aggiornaVoli() {
		this.num_voli++;
	}

	public void setNum_voli(int num_voli) {
		this.num_voli = num_voli;
	}

	public double getDelay() {
		return delay;
	}

	public Airport getAereoporto() {
		return aereoporto;
	}

	public void setAereoporto(Airport aereoporto) {
		this.aereoporto = aereoporto;
	}

	public void aggiornaDelay(double delay) {
		this.delay+=delay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Passeggero other = (Passeggero) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public String toString() {
		return id+" partito da "+primo+" ritardo totale: "+delay;
	}
	
	
}
