package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {

	/*
	 * Si potrebbe intedere in tal senso:
	 * 
	 *  Inizialmente ho una coda di voli.
	 *  Se vi sono sono dei passeggeri disponibili controllo se c'è un volo per loro altrimenti salto il volo
	 *  Se c'è un volo disponibile li mando 
	 *   (la disponibilità del volo è data dal grafo)
	 */
	
	//variabili interne
	
	private PriorityQueue<Evento> queue;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private List<Passeggero> passeggeri;
	private Set<Passeggero> disponibili; //passeggeri che non sono in volo

	
    //parametri di simulazione
	

	private int num_voli;
	
	
	public void init (List<Evento> list, Graph<Airport, DefaultWeightedEdge> grafo, int num_pass, int num_voli) {
		
		
		
		//mi sistemo gli eventi
		this.queue = new PriorityQueue<Evento>(list);
		
		this.grafo = grafo;
		
		List<Airport> vertici = new ArrayList<Airport>(grafo.vertexSet());
		
		this.disponibili = new HashSet<Passeggero>();
		
		for(int i=0; i<num_pass; i++) {            
			//dispongo in maniera casuale i passeggeri
			
			int caso = (int)(Math.random()*vertici.size());
			Airport partenza = vertici.get(caso);
			
			Passeggero pass = new Passeggero(i, partenza);
			disponibili.add(pass);
			
		}
		
		this.passeggeri = new ArrayList<>(disponibili);
		
		this.num_voli = num_voli;
		
		
	}
	
	public void run() {
		
		
		while(!queue.isEmpty()) {
			
			
			Evento ev = queue.poll();
			
			switch(ev.getTipo()) {
			
			case VOLO:
				
				if(!disponibili.isEmpty()) {
				
				//se c'è un volo vado a vedere se in quell'aereoporto sono presenti dei passeggeri disponibili
				for(Passeggero pass : new ArrayList<Passeggero>(disponibili)) {
					//se ho un passaggero in quell'aereoporto lo mando nel volo (il primo)
					if(pass.getAereoporto().equals(ev.getOrigine())) {
						
						//ne aggiorno i voli e i ritardi
						pass.aggiornaVoli();
						pass.aggiornaDelay(ev.getDelay());
						//aggiungo l'evento di arrivo
						queue.add(new Evento(ev.getDestinazione(), ev.getDataArrivo(), pass));
						//ne tolgo la disponibilita
						disponibili.remove(pass);
					}
				}
			}
				break;
			
			case ARRIVO:
				
				//nel caso in cui un passeggero arrivi
	            Passeggero arrivato = ev.getPass();
	            
	            //torna disponibile
	            if(arrivato.getNum_voli()<num_voli)
	            	disponibili.add(arrivato);
	                                    
				
				break;
			
			}
			
			
		}
		
	}

	public String findings() {
		
		if(passeggeri!=null) {
			
			String ris  = "La lista dei passeggeri e dei ritardi \n";
			
			for(Passeggero pass : passeggeri)
				ris+=pass+"\n";
			
			return ris;
			
		}
		
		return "";
	}
	
	
}
