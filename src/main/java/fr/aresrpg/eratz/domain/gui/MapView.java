package fr.aresrpg.eratz.domain.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @since
 */
public class MapView extends Application {

	private static MapView INSTANCE = new MapView();
	private static Stage stage;

	public static void main(String[] args) {
		MapView.launch(args);
	}

	public MapView() {
		INSTANCE = this;
	}

	public static void setTitle(String name) {
		if (stage != null)
			Platform.runLater(() -> stage.setTitle(name));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public static void setStage(Stage stage) {
		MapView.stage = stage;
	}

	/**
	 * @return the iNSTANCE
	 */
	public static MapView getInstance() {
		return INSTANCE;
	}

	public void startView(Parent parent, String name) {
		Platform.runLater(() -> {
			Stage stage = new Stage();
			setStage(stage);
			stage.setTitle(name);
			stage.setScene(new Scene(parent));
			stage.show();
		});
	}

}
