/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
/**
* Class representing the UI component for managing student information
* manages content on the first tab
*/
public class DisplayStudent {

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField studentNumberField;
    private ComboBox<String> studyProgrammeComboBox;
    private GridPane formGrid;
    private Button submitButton;
    private VBox studentInfoDisplay;
    private BorderPane mainPane;
    private boolean isFirstNameFilled = false;
    private boolean isLastNameFilled = false;
    private boolean isStudentNumberFilled = false;
    private boolean isStudyProgrammeSelected = false;
    
    public DisplayStudent() {
        // Create UI elements
        formGrid = new GridPane();
        firstNameField = new TextField();
        lastNameField = new TextField();
        studentNumberField = new TextField();
        studyProgrammeComboBox = new ComboBox<>();
        submitButton = new Button("Submit");
        studentInfoDisplay = new VBox();
        mainPane = new BorderPane();
        mainPane.setCenter(formGrid);
        mainPane.setRight(studentInfoDisplay);
        // Add labels and fields to the form grid
        formGrid.add(new Label("Nimi:"), 0, 1);
        formGrid.add(firstNameField, 1, 1);
        formGrid.add(new Label("Sukunimi:"), 0, 2);
        formGrid.add(lastNameField, 1, 2);
        formGrid.add(new Label("Opiskelijanumero:"), 0, 3);
        formGrid.add(studentNumberField, 1, 3);
        formGrid.add(new Label("Tutkinto:"), 0, 4);
        formGrid.add(studyProgrammeComboBox, 1, 4);
    } 
    
    /**
     * Initializes the DisplayStudent UI component.
    */
    public void initialize() {
        initProgramList();
        initListeners();
        styleUI();
        updateSubmitButtonState();
        formGrid.add(submitButton, 1, 5);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        submitButton.setOnAction(event -> displayStudentInfo());
    }
    
    /**
    * Applies styling to the UI elements in the component.
    */
    private void styleUI() {
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20, 20, 20, 20));
        formGrid.setAlignment(Pos.TOP_LEFT);
        firstNameField.setMaxWidth(200);
        lastNameField.setMaxWidth(200);
        studentNumberField.setMaxWidth(200);
        studyProgrammeComboBox.setMaxWidth(200);
    }
    /**
    * Initializes the list of study programmes in the ComboBox.
    */
    private void initProgramList() {
        try {
            DegreeModuleParser.fetchAllDegreeNames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String program : DegreeModuleParser.getListOfProgramNames()) {
            studyProgrammeComboBox.getItems().add(program);
        }
    }
    /**
    * Initializes the listeners for the UI elements in the component.
    */
    private void initListeners() {
        studyProgrammeComboBox.setOnAction(event -> {
            Sisu.getDisplayPlan().updateView(studyProgrammeComboBox.getValue());
            isStudyProgrammeSelected = studyProgrammeComboBox.getValue() != null;
            updateSubmitButtonState();
        });
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            isFirstNameFilled = !newValue.trim().isEmpty();
            updateSubmitButtonState();
        });
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            isLastNameFilled = !newValue.trim().isEmpty();
            updateSubmitButtonState();
        });
        studentNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            isStudentNumberFilled = !newValue.trim().isEmpty();
            updateSubmitButtonState();
        });
    }
    /**
    * Updates the state of the submit button.
    */
    private void updateSubmitButtonState() {
        submitButton.setDisable(!(isFirstNameFilled && isLastNameFilled && isStudentNumberFilled && isStudyProgrammeSelected));
    }
    /**
    * Displays the student information after the form is submitted.
    */
    private void displayStudentInfo() {
        Sisu.getPrimaryView().getTabPlan().setDisable(false);
        studentInfoDisplay.getChildren().clear();
        Label nameLabel = new Label("Nimi: " + firstNameField.getText() + " " + lastNameField.getText());
        Label studentNumberLabel = new Label("Opiskelijanumero: " + studentNumberField.getText());
        Label studyProgrammeLabel = new Label("Tutkinto: " + studyProgrammeComboBox.getValue());
        studentInfoDisplay.getChildren().addAll(nameLabel, studentNumberLabel, studyProgrammeLabel);
        studentInfoDisplay.setSpacing(10);
        studentInfoDisplay.setPadding(new Insets(10, 10, 10, 10));
    }
    /**
    * Returns the main BorderPane containing the UI elements of the component.
     * @return the main BorderPane of the component
    */
    public BorderPane getView() {
        return mainPane;
    }
    /**
    * Sets the main BorderPane containing the UI elements of the component.
     * @param mainPane the main BorderPane to be set
    */
    public void setView(BorderPane mainPane) {
        this.mainPane = mainPane;
    }
    
}
