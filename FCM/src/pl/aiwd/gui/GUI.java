package pl.aiwd.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		MainScene ms = new MainScene(primaryStage);
		 primaryStage.setScene(ms.scene);
		 primaryStage.centerOnScreen();
	     primaryStage.show();
		
	}

	public static void main(String[] args) {
        launch(args);
    }	

}
