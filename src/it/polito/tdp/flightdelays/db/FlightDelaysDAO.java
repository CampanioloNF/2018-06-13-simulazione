package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Evento;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.Rotta;

public class FlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		
		String sql = "SELECT id, airline from airlines2";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getString("ID"), rs.getString("airline")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			return null;
		}
	}

	public void loadAllAirports(Map<String, Airport> airportIdMap) {

		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports2";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(!airportIdMap.containsKey(rs.getString("id"))) {
				
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				
				airportIdMap.put(airport.getId(), airport);
				
				}
			}
			
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void loadAllGraph(Graph<Airport, DefaultWeightedEdge> grafo, Map<String, Airport> airportIdMap,
			Airline line, List<Rotta> rotte) {
		
		String sql = "SELECT f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID, AVG(f.ARRIVAL_DELAY) AS ritardi " + 
				"FROM flights2 f " + 
				"WHERE f.AIRLINE = 'OO' " + 
				"GROUP BY f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID ";
		
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(airportIdMap.containsKey(rs.getString("f.ORIGIN_AIRPORT_ID")) 
						&& airportIdMap.containsKey(rs.getString("f.DESTINATION_AIRPORT_ID"))) {
				
					Airport origine = airportIdMap.get(rs.getString("f.ORIGIN_AIRPORT_ID"));
					Airport destinazione = airportIdMap.get(rs.getString("f.DESTINATION_AIRPORT_ID"));
						
				//come prima cosa aggiungo i vertici
					
					grafo.addVertex(origine);
					grafo.addVertex(destinazione);
					
			   //dunque aggiungo l'arco
					              
					double distanza = LatLngTool.distance(new LatLng(origine.getLatitude(), origine.getLongitude()), 
						     new LatLng(destinazione.getLatitude(), destinazione.getLongitude()), LengthUnit.KILOMETER);
					
					double peso = (double) rs.getDouble("ritardi")/(distanza);
					
					rotte.add(new Rotta(origine, destinazione, peso));
					
					Graphs.addEdge(grafo, origine, destinazione, peso);
				
				}
			}
			
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			
		}
	}

	public List<Evento> getEventi(Airline line, Map<String, Airport> airportIdMap, Graph<Airport, DefaultWeightedEdge> grafo) {
	
		String sql = "SELECT f.ORIGIN_AIRPORT_ID, f.DESTINATION_AIRPORT_ID, timestamp(f.SCHEDULED_DEP_DATE), timestamp(f.ARRIVAL_DATE), f.ARRIVAL_DELAY " + 
				"FROM flights2 f " + 
				"WHERE f.AIRLINE = ? AND YEAR(f.SCHEDULED_DEP_DATE)>=2015";
		
		List<Evento> result = new ArrayList<Evento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, line.getId());
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) { //controlli di routine
				
				if(airportIdMap.containsKey(rs.getString("f.ORIGIN_AIRPORT_ID")) 
						&& airportIdMap.containsKey(rs.getString("f.DESTINATION_AIRPORT_ID"))) {
				
					Airport origine = airportIdMap.get(rs.getString("f.ORIGIN_AIRPORT_ID"));
					Airport destinazione = airportIdMap.get(rs.getString("f.DESTINATION_AIRPORT_ID"));
					
					if(grafo.containsEdge(grafo.getEdge(origine, destinazione))) {
						
						//deve esistere la rotta
						result.add(new Evento(origine, destinazione, 
								rs.getTimestamp("timestamp(f.SCHEDULED_DEP_DATE)").toLocalDateTime(), 
								rs.getTimestamp("timestamp(f.ARRIVAL_DATE)").toLocalDateTime(),
								rs.getDouble("f.ARRIVAL_DELAY")));
				
					}
				
					
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	
	}
}
