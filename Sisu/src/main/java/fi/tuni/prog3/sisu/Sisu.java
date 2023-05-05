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

    public static void main(String[] args) {
        launch();  
    }

    public static DisplayPlan getDisplayPlan() {
        return displayPlan;
    }

    public static void setDisplayPlan(DisplayPlan displayplan) {
        displayPlan = displayplan;
    }
    
    public static PrimaryView getPrimaryView() {
        return primaryView;
    }
}