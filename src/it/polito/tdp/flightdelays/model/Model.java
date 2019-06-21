package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private FlightDelaysDAO dao;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<String, Airport> airportIdMap;
	private List<Rotta> rotte;
	private Simulator sim;
	
	public Model() {
		this.dao = new FlightDelaysDAO();
		this.airportIdMap = new HashMap<String, Airport>();
		dao.loadAllAirports(airportIdMap);
		this.sim = new Simulator();
	}
	

	public List<Airline> getAllAirlines() {
		// TODO Auto-generated method stub
		return dao.loadAllAirlines();
	}


	public void creaGrafo(Airline line) {
		
		this.rotte = new LinkedList<>();
		//creo il grafo 
		this.grafo = new SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//carico i vertici (aereoporti della linea scelta)
		dao.loadAllGraph(grafo, airportIdMap, line, rotte);
		
	}


	public List<Rotta> getWorstRotte(int n) {
		
		if(rotte!=null) {
			
			List<Rotta> result  = new ArrayList<Rotta>();
			Collections.sort(rotte);
		    
			for(int i=0; i<rotte.size() && i<=n; i++ ) 
				result.add(rotte.get(i));
			
			return result;
		}
		
		return null;
	}
	
	public String simulate(Airline line, int num_pass, int num_voli) {
		
		if(grafo!=null) {
		
		String ris = "";
		
		sim.init(dao.getEventi(line, airportIdMap, grafo), grafo, num_pass, num_voli);
		sim.run();
		ris+=sim.findings();
		return ris;
		}
		return null;
	}

}
