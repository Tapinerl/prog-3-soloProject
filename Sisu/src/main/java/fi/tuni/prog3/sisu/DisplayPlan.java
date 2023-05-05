/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

/**
 * manages the content of the second tab in the UI,
 * which is responsible for displaying the study plan.
 */
public final class DisplayPlan {

    final SplitPane splitPane;
    private final TreeView<String> courseTree;
    private final VBox courseList;
    /**
    * Constructs a new DisplayPlan object and initializes the courseTree object with an empty root.
    */
    public DisplayPlan() {
        
        // Initialize courseTree with empty root
        courseTree = new TreeView<>(new TreeItem<>(""));
        // Initialize courseSelectionList
        courseList = new VBox();
        // Add courseTree and courseSelectionList to the splitPane
        splitPane = new SplitPane(courseTree, courseList);
    } 
    /**
    * Sets the cell factory for the TreeView object to display the course names.
    */
    public void initialize() {
        courseTree.setCellFactory(tree -> new TreeCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
    }
    /**
    * Updates the view of the course hierarchy based on the selected degree programme.
    * @param degreeProgramme The selected degree programme.
    */
    public void updateView(String degreeProgramme) {
        courseTree.getRoot().getChildren().clear();
        try {
            buildCourseTree(degreeProgramme);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
    * Finds a course object based on its name.
    * @param courseName The name of the course to find.
    * @return The DegreeModule object representing the course, or null if no such course exists.
    */
    private DegreeModule findCourse(String courseName) {
        for (DegreeModule course : DegreeModuleParser.getListOfCourses()) {
            if (course.getName().equals(courseName)) {
                return course;
            }
        }
        return null;
    }
    /**
    * Returns the number of credits required for a course based on its name.
    * @param courseName The name of the course.
    * @return The number of credits required for the course, or 0 if no such course exists.
    */
    private int requiredCredits(String courseName) {
        DegreeModule course = findCourse(courseName);
        return course == null ? 0 : course.getMinCredits();
    }
    /**
    * Builds the course hierarchy for the selected degree programme and populates the TreeView object with it.
    * @param degreeProgramme The selected degree programme.
    * @throws IOException if there was an error while fetching or parsing the course data.
    */
    private void buildCourseTree(String degreeProgramme) throws IOException {
        if (degreeProgramme == null || degreeProgramme.isEmpty()) {
            return;
        }
        DegreeModuleParser.fetchDegreeById(degreeProgramme);
        ArrayList<DegreeModule> courses = DegreeModuleParser.getListOfCourses();
        courseTree.getRoot().setValue(formatCourseItem(degreeProgramme, requiredCredits(courses.get(0).getName())));
        TreeItem<String> PriorGroupingModule = null;
        TreeItem<String> priorStudyModule = null;
        for (DegreeModule course : courses) {
            String formattedCourse = formatCourseItem(course.getName(), requiredCredits(course.getName()));
            switch (course.getId()) {
                case "GroupingModule":
                    PriorGroupingModule = new TreeItem<>(formattedCourse);
                    priorStudyModule = null;
                    courseTree.getRoot().getChildren().add(PriorGroupingModule);
                    break;
                case "StudyModule":
                    priorStudyModule = new TreeItem<>(formattedCourse);
                    if (PriorGroupingModule != null) {
                        PriorGroupingModule.getChildren().add(priorStudyModule);
                    } else {
                        courseTree.getRoot().getChildren().add(priorStudyModule);
                    }
                    break;
                case "CourseUnit":
                    TreeItem<String> courseItem = new TreeItem<>(formattedCourse);
                    if (priorStudyModule != null) {
                        priorStudyModule.getChildren().add(courseItem);
                    } else if (PriorGroupingModule != null) {
                        PriorGroupingModule.getChildren().add(courseItem);
                    } else {
                        courseTree.getRoot().getChildren().add(courseItem);
                    }
                    break;
            }
        }
    }
    private String formatCourseItem(String courseName, int credits) {
        return String.format("%s %d op", courseName, credits);
    }
    
}
