package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;

/**
 * JavaFX Sisu 
 */
public class Sisu extends Application {

    private static DisplayPlan displayPlan;
    private static PrimaryView primaryView;
    /**
    * The start method is called when the application is launched.
    * It creates the UI and sets up the stage. 
    * @param stage the primary stage for this application
    * @throws IOException if there is an error initializing the PrimaryView or DisplayPlan objects
    */
    @Override
    public void start(Stage stage) throws IOException {

        // Create UI
        primaryView = new PrimaryView();
        Scene scene = new Scene(primaryView.getTabPane(), 800, 600);
        setDisplayPlan(primaryView.getTabDisplayPlan());

        stage.setTitle("Sisu");
        stage.setScene(scene);
        stage.show();
    }
    /**
    * The main method launches the application.
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        launch();  
    }
    /**
    * Returns the DisplayPlan object associated with the Sisu application.
    * @return the DisplayPlan object associated with the Sisu application
    */
    public static DisplayPlan getDisplayPlan() {
        return displayPlan;
    }
    /**
    * Sets the DisplayPlan object associated with the Sisu application. 
    * @param displayplan the new DisplayPlan object to associate with the Sisu application
    */
    public static void setDisplayPlan(DisplayPlan displayplan) {
        displayPlan = displayplan;
    }
    /**
    * Returns the PrimaryView object associated with the Sisu application. 
    * @return the PrimaryView object associated with the Sisu application
    */
    public static PrimaryView getPrimaryView() {
        return primaryView;
    }
}