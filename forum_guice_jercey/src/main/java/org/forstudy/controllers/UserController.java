package org.forstudy.controllers;

import com.google.inject.Inject;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.goodresponse.GoodResponseMassage;
import org.forstudy.servises.UserService;
import org.forstudy.servises.ValidationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Path("/users")
@XmlRootElement
public class UserController {

    private final UserService userService;
    private final ValidationService validationService;
    private final String link = "/users/";

    @Inject
    public UserController(UserService userService, ValidationService validationService) {
        this.userService = userService;
        this.validationService = validationService;
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
        validationService.idValidation(userId, link);
        return userService.findUserById(userId, link);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User save(User userFromJSON) throws AppException {
        validationService.userLoginAndPasswordValidation(userFromJSON, link);
        validationService.checkLogin(userFromJSON.getLogin(), link);
        return userService.save(userFromJSON, link);
    }

    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User updateUserById(@PathParam("userId") String userId, User userFromJSON) throws AppException {
        validationService.idValidation(userId, link);
        validationService.userLoginAndPasswordValidation(userFromJSON, link);
        validationService.checkLogin(userFromJSON.getLogin(), link);
        return userService.updateUserById(userId, userFromJSON, link + userId);
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) throws AppException {
        validationService.idValidation(userId, link);
        userService.deleteUserById(userId, link);
        return Response.status(200)
                .entity(new GoodResponseMassage("User with id " + userId + " was deleted"))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }
}
