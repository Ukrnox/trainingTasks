package org.forstudy.controllers;

import org.forstudy.entities.Group;
import org.forstudy.entities.Topic;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.goodresponse.GoodResponseMassage;
import org.forstudy.servises.GroupService;
import org.forstudy.servises.TopicService;
import org.forstudy.servises.ValidationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/groups")
public class GroupController {

    private final GroupService groupService;
    private final TopicService topicService;
    private final String link = "/groups/";
    private final ValidationService validationService;

    @Inject
    public GroupController(GroupService groupService, TopicService topicService, ValidationService validationService) {
        this.groupService = groupService;
        this.topicService = topicService;
        this.validationService = validationService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> getGroups() {
        return groupService.findAll();
    }

    @GET
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group getGroupByID(@PathParam("groupId") String groupId) throws AppException {
        validationService.idValidation(groupId, link);
        return groupService.findById(groupId, link + groupId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Group createNewGroup(@QueryParam("newGroupTitle") String newGroupTitle) {
        return groupService.createNewGroup(newGroupTitle, link);
    }

    @GET
    @Path("/{groupId}/topics")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Topic> getAllTopicsByGroupID(@PathParam("groupId") String groupId) throws AppException {
        validationService.idValidation(groupId, link);
        return topicService.findTopicsByGroupId(groupId, link + groupId + "/topic");
    }

    @PUT
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group updateGroupTitleById(@PathParam("groupId") String groupId, @QueryParam("newGroupTitle") String newGroupTitle) throws AppException {
        validationService.idValidation(groupId, link);
        return groupService.updateGroupTitle(groupId, newGroupTitle, link + groupId);
    }

    @DELETE
    @Path("/{groupId}")
    public Response deleteGroup(@PathParam("groupId") String groupId) throws AppException {
        validationService.idValidation(groupId, link);
        groupService.deleteGroupById(groupId, link);
        return Response.status(200)
                .entity(new GoodResponseMassage("Group with id " + groupId + " was deleted"))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }
}
