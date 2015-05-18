package it.polito.tdp.gtfs.main;
	
import it.polito.tdp.gtfs.db.GtfsDao;
import it.polito.tdp.gtfs.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		Model model = new Model();
		GtfsDao dao = new GtfsDao();
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Interfaccia.fxml"));
			BorderPane root = (BorderPane)loader.load();
			
			InterfacciaController controller = loader.getController();
			model.setDao(dao);
			controller.setModel(model);
		//	controller.riempi();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
