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

/*
    manages stuff for second tab (study plan)
*/
public final class DisplayPlan {

    final SplitPane splitPane;
    private final TreeView<String> courseTree;
    private final VBox courseSelectionList;

    public DisplayPlan() {
        
        // Initialize courseTree with empty root
        courseTree = new TreeView<>(new TreeItem<>(""));
        // Initialize courseSelectionList
        courseSelectionList = new VBox();
        // Add courseTree and courseSelectionList to the splitPane
        splitPane = new SplitPane(courseTree, courseSelectionList);
    } 
    
    public void initialize() {
        courseTree.setCellFactory(tree -> new TreeCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
    }

    public void updateView(String degreeProgramme) {
        courseTree.getRoot().getChildren().clear();
        try {
            buildTree(degreeProgramme);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DegreeModule findCourse(String courseName) {
        for (DegreeModule course : DegreeModuleParser.getListOfCourses()) {
            if (course.getName().equals(courseName)) {
                return course;
            }
        }
        return null;
    }

    private int creditsRequired(String courseName) {
        DegreeModule course = findCourse(courseName);
        return course == null ? 0 : course.getMinCredits();
    }

    private void buildTree(String degreeProgramme) throws IOException {
        if (degreeProgramme == null || degreeProgramme.isEmpty()) {
            return;
        }
        DegreeModuleParser.fetchDegreeById(degreeProgramme);
        ArrayList<DegreeModule> courses = DegreeModuleParser.getListOfCourses();
        courseTree.getRoot().setValue(formatCourseItem(degreeProgramme, creditsRequired(courses.get(0).getName())));
        TreeItem<String> previousGroupingModule = null;
        TreeItem<String> previousStudyModule = null;
        for (DegreeModule course : courses) {
            String textValue = formatCourseItem(course.getName(), creditsRequired(course.getName()));
            switch (course.getId()) {
                case "GroupingModule":
                    previousGroupingModule = new TreeItem<>(textValue);
                    previousStudyModule = null;
                    courseTree.getRoot().getChildren().add(previousGroupingModule);
                    break;
                case "StudyModule":
                    previousStudyModule = new TreeItem<>(textValue);
                    if (previousGroupingModule != null) {
                        previousGroupingModule.getChildren().add(previousStudyModule);
                    } else {
                        courseTree.getRoot().getChildren().add(previousStudyModule);
                    }
                    break;
                case "CourseUnit":
                    TreeItem<String> courseItem = new TreeItem<>(textValue);
                    if (previousStudyModule != null) {
                        previousStudyModule.getChildren().add(courseItem);
                    } else if (previousGroupingModule != null) {
                        previousGroupingModule.getChildren().add(courseItem);
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
