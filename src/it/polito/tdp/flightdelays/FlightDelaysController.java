package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Model;
import it.polito.tdp.flightdelays.model.Rotta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightDelaysController {

    private static final int N = 10;

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<Airline> cmbBoxLineaAerea;

    @FXML
    private Button caricaVoliBtn;

    @FXML
    private TextField numeroPasseggeriTxtInput;

    @FXML
    private TextField numeroVoliTxtInput;

	private Model model;

	private Airline line = null;
	
    @FXML
    void doCaricaVoli(ActionEvent event) {
    
    	txtResult.clear();
    	line = cmbBoxLineaAerea.getValue();
    	
    	if(line!=null) {
    		
    		model.creaGrafo(line);
    		txtResult.appendText("Le "+N+" rotte peggiori sono: \n");
    		for(Rotta rotta : model.getWorstRotte(N))
            		txtResult.appendText(""+rotta+"\n");
    	}
    	else {
    		txtResult.appendText("Si prega di scegliere una compagnia");
    		return;
    	}
    	
    	
    }

    @FXML
    void doSimula(ActionEvent event) {

    	txtResult.clear();
    	
    	if(line!=null && line.equals(cmbBoxLineaAerea.getValue())) {
    		
    		try {
    			
    			int num_voli = Integer.parseInt(this.numeroVoliTxtInput.getText());
    			int num_pass = Integer.parseInt(this.numeroPasseggeriTxtInput.getText());
    			
    			txtResult.appendText(model.simulate(line, num_pass, num_voli));
    			
    		}catch(NumberFormatException nfe) {
    			txtResult.appendText("Si prega di inserire solo numeri interi nei campi di testo");
    			return;
    		}
    	   
    	}
    	else {
         txtResult.appendText("Si prega di scegliere una compagnia.\n Nel caso si volesse cambiare compagnia premere nuovamente 'Carica Voli'.");
         return;    	
    	}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxLineaAerea != null : "fx:id=\"cmbBoxLineaAerea\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert caricaVoliBtn != null : "fx:id=\"caricaVoliBtn\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroPasseggeriTxtInput != null : "fx:id=\"numeroPasseggeriTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";

    }
    
	public void setModel(Model model) {
		this.model = model;
	}

	public void caricaBox() {
		
		cmbBoxLineaAerea.getItems().addAll(model.getAllAirlines());
	}
}
