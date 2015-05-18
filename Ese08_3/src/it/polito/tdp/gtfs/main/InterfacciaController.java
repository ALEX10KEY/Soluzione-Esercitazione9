

package it.polito.tdp.gtfs.main;

import it.polito.tdp.gtfs.model.Model;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.onebusaway.gtfs.model.ComparatoreDiStops;
import org.onebusaway.gtfs.model.Stop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class InterfacciaController {
	
	private Model model;
	//private GtfsDao dao;
	
    public void setModel(Model m){
    	this.model=m;
    	
    	ComparatoreDiStops comp = new ComparatoreDiStops();
    	List<Stop> stops = new LinkedList<Stop>();
    	
    	for(Stop s : m.getAllStops()){
    		if(!s.getId().toString().endsWith("_B")){
    			stops.add(s);
    		}
    	}
    	Collections.sort(stops,comp);
    	         
         stazionePartenza.getItems().addAll(stops);
     	stazioneArrivo.getItems().addAll(stops);    	
    }
    

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtresult"
    private TextArea txtresult; // Value injected by FXMLLoader

    @FXML // fx:id="cercabtn"
    private Button cercabtn; // Value injected by FXMLLoader

    @FXML // fx:id="stazionePartenza"
    private ChoiceBox<Stop> stazionePartenza; // Value injected by FXMLLoader

    @FXML // fx:id="stazioneArrivo"
    private ChoiceBox<Stop> stazioneArrivo; // Value injected by FXMLLoader

    @FXML
    void doCerca(ActionEvent event) {
    	txtresult.clear();
    	
    	model.buildGraph();
    	
    	Stop partenza = stazionePartenza.getValue();
    	Stop arrivo = stazioneArrivo.getValue();
    	
    	if(!partenza.equals(arrivo)){
		    	List<Stop> sequenza = model.cercaPercorso(partenza, arrivo);
		    	if(sequenza!=null){
		    	txtresult.appendText(model.stampaSequenza(sequenza));
		    	}else{
		    		txtresult.appendText(String.format("Spiacente, non vi sono percorsi possibili da %s a %s", partenza.getName(), arrivo.getName()));
		    	}
    	}else{
    		txtresult.appendText("Spiacente, le stazioni devono essere differenti per calcolare un percorso");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtresult != null : "fx:id=\"txtresult\" was not injected: check your FXML file 'Interfaccia.fxml'.";
        assert cercabtn != null : "fx:id=\"cercabtn\" was not injected: check your FXML file 'Interfaccia.fxml'.";
        assert stazionePartenza != null : "fx:id=\"stazionePartenza\" was not injected: check your FXML file 'Interfaccia.fxml'.";
        assert stazioneArrivo != null : "fx:id=\"stazioneArrivo\" was not injected: check your FXML file 'Interfaccia.fxml'.";

      
    }
    


    
}

