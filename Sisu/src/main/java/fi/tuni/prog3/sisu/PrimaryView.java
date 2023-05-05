/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
/* 
manages primary UI
*/
class PrimaryView {

    private Tab tab_student;
    private Tab tab_plan;
    private TabPane tabPane;
    private DisplayStudent displayStudent;
    private DisplayPlan tabDisplayPlan;

    public PrimaryView() throws IOException {
        // Create UI elements
        tabPane = new TabPane();
        // Create tabs for TabPane
        tab_student = new Tab("Opiskelijan tiedot");
        tab_plan = new Tab("Opintojen rakenne");
        tab_plan.setDisable(true);
        
        displayStudent = new DisplayStudent();
        displayStudent.initialize();
        
        tab_student.setContent(displayStudent.getView());
        
        tabDisplayPlan = new DisplayPlan();
        tabDisplayPlan.initialize();
        
        tab_plan.setContent(tabDisplayPlan.splitPane);
        // Add tabs to TabPane
        tabPane.getTabs().addAll(tab_student, tab_plan);
    }
    public Tab getTab_student() {
        return tab_student;
    }

    public void setTab_student(Tab tab_student) {
        this.tab_student = tab_student;
    }

    public Tab getTab_plan() {
        return tab_plan;
    }

    public void setTab_plan(Tab tab_plan) {
        this.tab_plan = tab_plan;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public DisplayStudent getDisplayStudent() {
        return displayStudent;
    }

    public void setDisplayStudent(DisplayStudent displayStudent) {
        this.displayStudent = displayStudent;
    }

    public DisplayPlan getTabDisplayPlan() {
        return tabDisplayPlan;
    }

    public void setTabDisplayPlan(DisplayPlan tabDisplayPlan) {
        this.tabDisplayPlan = tabDisplayPlan;
    }

    public Tab getTabPlan() {
        return tab_plan;
    }    
}
