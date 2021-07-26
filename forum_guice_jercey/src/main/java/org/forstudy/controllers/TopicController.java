package org.forstudy.controllers;

import org.forstudy.entities.Post;
import org.forstudy.entities.Topic;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.goodresponse.GoodResponseMassage;
import org.forstudy.servises.PostService;
import org.forstudy.servises.TopicService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/topics")
public class TopicController {

    public final TopicService topicService;
    public final PostService postService;
    private final String link = "/topic/";

    @Inject
    public TopicController(TopicService topicService, PostService postService) {
        this.topicService = topicService;
        this.postService = postService;
    }


    @GET
    @Path("/{topicId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Topic getTopicByID(@PathParam("topicId") String topicId) throws AppException {
        return topicService.findById(topicId, link);
    }

    @GET
    @Path("/{topicId}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getAllPostsByTopicId(@PathParam("topicId") String topicId) throws AppException {
        return postService.findPostsByTopicId(topicId, link + topicId + "/posts");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Topic createNewTopic(@QueryParam("groupId") String groupId,
                                @QueryParam("userId") String userId,
                                Topic jsonNewTopic) throws AppException {
        return topicService.save(groupId, userId, jsonNewTopic, link);
    }

    @PUT
    @Path("/{topicId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Topic updateTopicById(@PathParam("topicId") String topicId,
                                 Topic jsonNewTopic) throws AppException {
        return topicService.changeTopicTitle(topicId, jsonNewTopic, link);
    }

    @DELETE
    @Path("/{topicId}")
    public Response deleteTopic(@PathParam("topicId") String topicId) throws AppException {
        topicService.removeTopicById(topicId, link);
        return Response.status(200)
                .entity(new GoodResponseMassage("Topic with id " + topicId + " was deleted"))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }
}
