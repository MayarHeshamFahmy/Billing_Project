package com.localbrand.servlet;

import com.localbrand.model.Profile;
import com.localbrand.service.ProfileService;
import com.localbrand.service.impl.ProfileServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import org.json.JSONException;

@WebServlet("/api/profiles/*")
public class ProfileServlet extends HttpServlet {
    private ProfileService profileService;

    @Override
    public void init() throws ServletException {
        profileService = new ProfileServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<Profile> profiles = profileService.getAllProfiles();
            JSONArray profilesArray = new JSONArray();
            
            for (Profile profile : profiles) {
                JSONObject profileJson = new JSONObject();
                profileJson.put("id", profile.getId());
                profileJson.put("name", profile.getName());
                profileJson.put("description", profile.getDescription());
                profileJson.put("basePrice", profile.getBasePrice());
                profilesArray.put(profileJson);
            }
            
            JSONObject responseJson = new JSONObject();
            responseJson.put("profiles", profilesArray);
            out.print(responseJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            BufferedReader reader = request.getReader();
            JSONObject json = new JSONObject(reader.readLine());
            
            Profile profile = new Profile();
            profile.setName(json.getString("name"));
            profile.setDescription(json.getString("description"));
            profile.setBasePrice(BigDecimal.valueOf(json.getDouble("basePrice")));

            Profile savedProfile = profileService.createProfile(profile);
            out.print(new JSONObject(savedProfile).toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 