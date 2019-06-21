package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	public enum TipoEvento{	
		ARRIVO,
		VOLO
	}
	
	private Airport origine;
	private Airport destinazione;
	private Passeggero pass;
	private LocalDateTime istante;
	private LocalDateTime dataArrivo;
	private TipoEvento tipo;
	private double delay;
	
	//Costruttore di volo
	public Evento(Airport origine, Airport destinazione, LocalDateTime dataPartenza, LocalDateTime dataArrivo, double delay) {
		
		this.tipo = TipoEvento.VOLO;
		
		this.origine = origine;
		this.destinazione = destinazione;
		this.pass = null;
		this.istante = dataPartenza;
		this.dataArrivo = dataArrivo;
		this.delay = delay;
		
		
		//scandisco i voli non appena c'è un passeggero libero creo un evento di arrivo (dal momento che faccio partire il passegero)
		
	}
  
	//quando un passeggero trova un volo libero parte ed arriva dopo il tempo di viaggio in un nuovo aereoporto
	
    public Evento(Airport destinazione, LocalDateTime dataArrivo, Passeggero pass) {
		
		this.tipo = TipoEvento.ARRIVO;
		
		//una volta arrivato torna tra i passeggeri disponibili.
	
		this.origine = null;
		this.dataArrivo = null;
		this.destinazione = destinazione;
		this.pass = pass;
		this.istante = dataArrivo;
		this.delay=0;
		
	}

	public Airport getOrigine() {
		return origine;
	}

	public Airport getDestinazione() {
		return destinazione;
	}

	public Passeggero getPass() {
		return pass;
	}

	public LocalDateTime getIstante() {
		return istante;
	}

	public LocalDateTime getDataArrivo() {
		return dataArrivo;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	public double getDelay() {
		return delay;
	}

	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return this.istante.compareTo(o.istante);
	}
    
    
	
	
	}
