package org.forstudy.controllers;

import com.google.inject.Inject;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.goodresponse.GoodResponseMassage;
import org.forstudy.servises.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Path("/users")
@XmlRootElement
public class UserController {

    private final UserService userService;
    private final String link = "/users/";

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listOfAllUsers() {
        return userService.listOfAllUsers();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("userId") String userId) throws AppException {
        String link = this.link + userId;
        return userService.findUserById(userId, link);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User save(User userJSON) throws AppException {
        return userService.save(userJSON, link);
    }

    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUserById(@PathParam("userId") String userId, User userFromJSON) throws AppException {
        return userService.updateUserById(userId, userFromJSON, link + userId);
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) throws AppException {
        userService.deleteUserById(userId, link);
        return Response.status(200)
                .entity(new GoodResponseMassage("User with id " + userId + " was deleted"))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }
}
