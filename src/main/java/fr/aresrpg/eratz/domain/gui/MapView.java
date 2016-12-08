package fr.aresrpg.eratz.domain.gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @since
 */
public class MapView extends Application {

	private static MapView INSTANCE = new MapView();

	public static void main(String[] args) {
		MapView.launch(args);
	}

	public MapView() {
		INSTANCE = this;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	}

	/**
	 * @return the iNSTANCE
	 */
	public static MapView getInstance() {
		return INSTANCE;
	}

	public void startView(Parent parent) {
		Stage stage = new Stage();
		stage.setTitle("Dofus Map");
		stage.setScene(new Scene(parent));
		stage.show();
	}

}
