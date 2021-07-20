package org.forstudy.controllers;

import com.google.inject.Inject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.servises.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserController {

    private final UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listOfAllUsers() {
        System.out.println("GuiceController : list");
        return userService.listOfAllUsers();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserById(@PathParam("userId") String userId) {
        return userService.findUserById(Long.parseLong(userId));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String save(JSONObject jsonObject) {
        System.out.println("GuiceController : save");
        String userJSON;
        try {
            userJSON = userService.save(jsonObject.getString("login"), jsonObject.getString("password"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            userJSON = "Wrong JSON in request";
        }
        return userJSON;
    }


    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUserById(@PathParam("userId") String userId, JSONObject jsonObject) {
        String userJSON;
        try {
            userJSON = userService.updateUserById(userId, jsonObject.getString("login"), jsonObject.getString("password"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            userJSON = "Wrong JSON in request";
        }
        return userJSON;
    }

    @DELETE
    @Path("/{userId}")
    public void deleteUser(@PathParam("userId") String userId) {
        userService.deleteUserById(userId);
    }
}
