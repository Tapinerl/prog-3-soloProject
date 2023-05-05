/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
/*
    methods for fetching JSON data from API
*/
public class SisuApiHandler implements iAPI {

    public String fetchDegrees() throws MalformedURLException, IOException {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";
        return fetchJson(urlStr);
    }

    public String fetchGroupIdModule(String groupId) throws MalformedURLException, IOException {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=" + groupId + "&universityId=tuni-university-root-id";
        return fetchJson(urlStr);
    }

    public String fetchGroupIdCourse(String groupId) throws MalformedURLException, IOException {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=" + groupId + "&universityId=tuni-university-root-id";
        return fetchJson(urlStr);
    }

    private String fetchJson(String urlStr) throws MalformedURLException, IOException {
        URL url = new URL(urlStr);
        try {
            String jsonStr = new String(url.openStream().readAllBytes());
            return jsonStr;
        } catch (MalformedURLException ex) {
            System.err.println("Malformed URL: " + ex.getMessage());
            throw ex;
        } catch (IOException ex) {
            System.err.println("Error reading from URL: " + ex.getMessage());
            throw ex;
        }
    }
    @Override
    public JsonObject getJsonObjectFromApi(String urlString) {
        try {
            String jsonStr = fetchJson(urlString);
            return new Gson().fromJson(jsonStr, JsonObject.class);
        } catch (IOException ex) {
            System.err.println("Error fetching JSON object: " + ex.getMessage());
            return null;
        }
    }
}