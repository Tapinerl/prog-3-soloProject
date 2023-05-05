/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
/*
    creates a string from JSON
*/
public class DegreeModuleParser {

    private static ArrayList<DegreeModule> courses = new ArrayList<>();
    private static ArrayList<String> namesOfProgramsList = new ArrayList<>();

    public DegreeModuleParser() {
    }

    public static ArrayList<DegreeModule> getListOfCourses() {
        return courses;
    }

    public static ArrayList<String> getListOfProgramNames() {
        return namesOfProgramsList;
    }

    public static void addCourseToList(DegreeModule info) {
        courses.add(info);
    }

    public static void addNamesOfProgramsToList(String program) {
        namesOfProgramsList.add(program);
    }

    public static void fetchAllDegreeNames()
            throws MalformedURLException, IOException {
        namesOfProgramsList = new ArrayList<>();
        String namesOfPrograms = "";

        String jsonStr = new SisuApiHandler().fetchDegrees();
        JsonObject degreeRoot = new Gson().fromJson(jsonStr, JsonObject.class);
        JsonArray array = degreeRoot.get("searchResults").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject programObj = array.get(i).getAsJsonObject();
            namesOfPrograms = programObj.get("name").getAsString();
            namesOfProgramsList.add(namesOfPrograms);
        }
    }

    public static void fetchDegreeById(String studyProgram)
            throws MalformedURLException, IOException {
        courses = new ArrayList<>();
        String groupId = "";

        String jsonStr = new SisuApiHandler().fetchDegrees();
        JsonObject root = new Gson().fromJson(jsonStr, JsonObject.class);
        JsonArray programArr = root.get("searchResults").getAsJsonArray();
        for (int i = 0; i < programArr.size(); i++) {
            JsonObject programObj = programArr.get(i).getAsJsonObject();
            if (programObj.get("name").getAsString().equals(studyProgram)) {
                groupId = programObj.get("groupId").getAsString();
            }
        }
        if (!groupId.equals("")) {
            createDegreeModuleFromJson(groupId);
        }
    }

    public static void getJsonCourseUnit(String groupId)
        throws MalformedURLException, IOException, JsonSyntaxException {
    // Get the course JSON as a string
    String courseJsonStr = new SisuApiHandler().fetchGroupIdCourse(groupId);
    
    // Parse the JSON
    JsonArray courseArray = new Gson().fromJson(courseJsonStr, JsonArray.class);
    
    // Check if there are any courses
    if (courseArray == null || courseArray.size() == 0) {
        throw new IllegalArgumentException("No courses found for group ID: " + groupId);
    }
    
    // Get the first course
    JsonObject course = courseArray.get(0).getAsJsonObject();
    
    // Get the course name
    String nameOfCourse = "";
    JsonObject nameObject = course.get("name").getAsJsonObject();
    if (nameObject.has("en")) {
        nameOfCourse = nameObject.get("en").getAsString();
    } else if (nameObject.has("fi")) {
        nameOfCourse = nameObject.get("fi").getAsString();
    } else {
        throw new IllegalArgumentException("Course name not found for group ID: " + groupId);
    }
    
    // Get the course credits
    int creditsForCourse = 0;
    JsonObject creditsObject = course.get("credits").getAsJsonObject();
    if (creditsObject.has("min")) {
        creditsForCourse = creditsObject.get("min").getAsInt();
    } else {
        throw new IllegalArgumentException("Course credits not found for group ID: " + groupId);
    }
    
    // Create a DegreeModule object
    String typeOfCourse = "CourseUnit";
    DegreeModule info = new DegreeModule(nameOfCourse, typeOfCourse, groupId, creditsForCourse) {
    };
    
    // Add the DegreeModule object to the courses list
    courses.add(info);
}
    
    public static void createDegreeModuleFromJson(String groupId) throws IOException {
        String jsonStr = new SisuApiHandler().fetchGroupIdModule(groupId);
        JsonObject root = new Gson().fromJson(jsonStr, JsonArray.class).get(0).getAsJsonObject();

        // Get course information from JSON
        String courseType = root.get("type").getAsString();
        String courseName = nameForCourse(root);
        int courseCredits = creditsForCOurse(root, courseType);

        // Create DegreeModule object and add it to courses list
        DegreeModule degreeModule = new DegreeModule(courseName, courseType, groupId, courseCredits) {};
        courses.add(degreeModule);

        // Process sub-rules in the rule object
        JsonObject objectRule = root.get("rule").getAsJsonObject();
        ArrayList<JsonObject> ruleList = collectRuleObjects(objectRule);
        for (JsonObject subRule : ruleList) {
            String subRuleType = subRule.get("type").getAsString();
            if (subRuleType.equals("ModuleRule")) {
                createDegreeModuleFromJson(subRule.get("moduleGroupId").getAsString());
            } else if (subRuleType.equals("CourseUnitRule")) {
                getJsonCourseUnit(subRule.get("courseUnitGroupId").getAsString());
            }
        }
    }

    private static String nameForCourse(JsonObject root) {
        String nameForCourse = "";
        if (root.get("name").getAsJsonObject().has("en")) {
            nameForCourse = root.get("name").getAsJsonObject().get("en").getAsString();
        } else {
            nameForCourse = root.get("name").getAsJsonObject().get("fi").getAsString();
        }
        return nameForCourse;
    }

    private static int creditsForCOurse(JsonObject root, String courseType) {
        int courseCredits = 0;
        if (!("GroupingModule".equals(courseType))) {
            courseCredits = root.get("targetCredits").getAsJsonObject().get("min").getAsInt();
        }
        return courseCredits;
    }

    private static ArrayList<JsonObject> collectRuleObjects(JsonObject ruleObj) {
        ArrayList<JsonObject> ruleList = new ArrayList<>();
        if (ruleObj.has("rules")) {
            JsonArray subRuleArray = ruleObj.getAsJsonArray("rules");
            for (JsonElement element : subRuleArray) {
                JsonObject subRuleObj = element.getAsJsonObject();
                if (subRuleObj.has("type")) {
                    String subRuleType = subRuleObj.get("type").getAsString();
                    if (subRuleType.equals("ModuleRule") || subRuleType.equals("CourseUnitRule")) {
                        ruleList.add(subRuleObj);
                    }
                }
                ruleList.addAll(collectRuleObjects(subRuleObj));
            }
        } else if (ruleObj.has("rule")) {
            JsonObject subRuleObj = ruleObj.getAsJsonObject("rule");
            if (subRuleObj.has("type")) {
                String subRuleType = subRuleObj.get("type").getAsString();
                if (subRuleType.equals("ModuleRule") || subRuleType.equals("CourseUnitRule")) {
                    ruleList.add(subRuleObj);
                }
            }
            ruleList.addAll(collectRuleObjects(subRuleObj));
        }
        return ruleList;
    }
}