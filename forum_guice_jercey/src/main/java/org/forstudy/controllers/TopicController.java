package org.forstudy.controllers;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.servises.PostService;
import org.forstudy.servises.TopicService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/topic")
public class TopicController {

    public final TopicService topicService;
    public final PostService postService;

    @Inject
    public TopicController(TopicService topicService, PostService postService) {
        this.topicService = topicService;
        this.postService = postService;
    }


    @GET
    @Path("/{topicId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTopicByID(@PathParam("topicId") String topicId) {
        return topicService.findById(topicId);
    }

    @GET
    @Path("/{topicId}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPostsByTopicId(@PathParam("topicId") String topicId) {
        String result;
        try {
            long longTopicId = Long.parseLong(topicId);
            result = postService.findPostsByTopicId(longTopicId);
        }
        catch (NumberFormatException e) {
            result = "Wrong topicId format";
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createNewTopic(@QueryParam("groupId") String groupId,
                                 @QueryParam("userId") String userId,
                                 JSONObject jsonNewTopicTitle) {
        String topicJSON;
        try {
            topicJSON = topicService.save(groupId, userId, jsonNewTopicTitle.getString("newTopicTitle"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            topicJSON = "Wrong JSON in request";
        }
        return topicJSON;
    }

    @PUT
    @Path("/{topicId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTopicById(@PathParam("topicId") String topicId,
                                  JSONObject jsonNewTopicTitle) {
        String topicJSON;
        try {
            topicJSON = topicService.changeTopicTitle(topicId, jsonNewTopicTitle.getString("newTopicTitle"));
        }
        catch (JSONException e) {
            e.printStackTrace();
            topicJSON = "Wrong JSON in request";
        }
        return topicJSON;
    }

    @DELETE
    @Path("/{topicId}")
    public void deleteTopic(@PathParam("topicId") String topicId) {
        topicService.removeTopicById(topicId);
    }
}
