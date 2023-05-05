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
/**
 * The SisuApiHandler class implements the iAPI interface and provides methods for
 * fetching JSON data from the Sisu API.
 */
public class SisuApiHandler implements iAPI {
    /**
    * Fetches a JSON string containing degree programmes from the Sisu API.
    * @return the JSON string containing degree programmes
    * @throws MalformedURLException if the URL for the API call is malformed
    * @throws IOException if an error occurs while reading from the URL
    */
    public String fetchDegrees() throws MalformedURLException, IOException {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";
        return fetchJson(urlStr);
    }
    /**
    * Fetches a JSON string containing modules for a specific group from the Sisu API.
    *
    * @param groupId the ID of the group for which to fetch modules
    * @return the JSON string containing modules for the specified group
    * @throws MalformedURLException if the URL for the API call is malformed
    * @throws IOException if an error occurs while reading from the URL
    */
    public String fetchGroupIdModule(String groupId) throws MalformedURLException, IOException {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=" + groupId + "&universityId=tuni-university-root-id";
        return fetchJson(urlStr);
    }
    /**
    * Fetches a JSON string containing course units for a specific group from the Sisu API.
    *
    * @param groupId the ID of the group for which to fetch course units
    * @return the JSON string containing course units for the specified group
    * @throws MalformedURLException if the URL for the API call is malformed
    * @throws IOException if an error occurs while reading from the URL
    */
    public String fetchGroupIdCourse(String groupId) throws MalformedURLException, IOException {
        String urlStr = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=" + groupId + "&universityId=tuni-university-root-id";
        return fetchJson(urlStr);
    }
    /**
    * Fetches a JSON string from a URL.
    *
    * @param urlStr the URL to fetch the JSON string from
    * @return the JSON string fetched from the URL
    * @throws MalformedURLException if the URL for the API call is malformed
    * @throws IOException if an error occurs while reading from the URL
    */
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
    /**
    * Retrieves a JsonObject from a specified URL using the Sisu API.
    *
    * @param urlString the URL from which to retrieve the JsonObject
    * @return the retrieved JsonObject
    */
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