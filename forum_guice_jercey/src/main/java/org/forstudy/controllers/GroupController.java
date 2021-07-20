package org.forstudy.controllers;

import org.forstudy.servises.GroupService;
import org.forstudy.servises.TopicService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/group")
public class GroupController {

    private final GroupService groupService;
    private final TopicService topicService;

    @Inject
    public GroupController(GroupService groupService, TopicService topicService) {
        this.groupService = groupService;
        this.topicService = topicService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroups() {
        return groupService.findAll();
    }

    @GET
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupByID(@PathParam("groupId") String groupId) {
        return groupService.findById(groupId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createNewGroup(@QueryParam("newGroupTitle") String newGroupTitle) {
        return groupService.createNewGroup(newGroupTitle);
    }

    @GET
    @Path("/{groupId}/topics")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTopicsByGroupID(@PathParam("groupId") String groupId) {
        return topicService.findTopicByGroupId(groupId);
    }

    @PUT
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String  updateGroupTitleById(@PathParam("groupId") String groupId, @QueryParam("newGroupTitle") String newGroupTitle) {
        return groupService.updateGroupTitle(groupId, newGroupTitle);
    }

    @DELETE
    @Path("/{groupId}")
    public void deleteGroup(@PathParam("groupId") String groupId) {
        groupService.deleteGroupById(groupId);
    }
}
