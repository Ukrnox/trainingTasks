package org.forstudy.controllers;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.dto.AllPostVotesDTO;
import org.forstudy.entities.Post;
import org.forstudy.entities.Vote;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.goodresponse.GoodResponseMassage;
import org.forstudy.servises.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/posts")
public class PostController {

    private final PostService postService;
    private final VoteService voteService;
    private final ValidationService validationService;
    private final String link = "/posts/";

    @Inject
    public PostController(PostService postService,
                          VoteService voteService,
                          ValidationService validationService) {
        this.voteService = voteService;
        this.postService = postService;
        this.validationService = validationService;
    }

    @GET
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getPostByID(@PathParam("postId") String postId) throws AppException {
        validationService.idValidation(postId, link);
        return postService.findPostById(postId, link + postId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Post createNewPost(@QueryParam("userId") String userId, @QueryParam("topicId") String topicId, Post newPost) throws AppException {
        validationService.idValidation(userId + topicId, link);
        validationService.postValidation(newPost, link);
        return postService.save(userId, topicId, newPost, link);
    }

    @PUT
    @Path("/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Post updatePostById(@PathParam("postId") String postId, Post newPost) throws AppException {
        validationService.idValidation(postId, link);
        return postService.updatePostById(postId, newPost, link + postId);
    }

    @DELETE
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deletePost(@PathParam("postId") String postId) throws AppException {
        validationService.idValidation(postId, link);
        postService.removePostById(postId, link + postId);
    }

    @GET
    @Path("/{postId}/votes")
    @Produces(MediaType.APPLICATION_JSON)
    public AllPostVotesDTO getAllVotes(@PathParam("postId") String postId) throws AppException {
        validationService.idValidation(postId, link);
        return voteService.getAllVotes(postId, link);
    }

    @POST
    @Path("/{postId}/votes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Vote addVote(@PathParam("postId") String postId, JSONObject jsonObject) throws AppException {
        String userId = "";
        String vote = "";
        try {
            userId = jsonObject.getString("userId");
            vote = jsonObject.getString("vote");
            validationService.idValidation(postId, link);
            validationService.idValidation(userId, link);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return voteService.save(postId, userId, vote, link + postId + "/votes");
    }


    @PUT
    @Path("/{postId}/votes")
    @Produces(MediaType.APPLICATION_JSON)
    public Vote updateVote(@PathParam("postId") String postId, JSONObject jsonObject) throws AppException {
        String userId = "";
        String vote = "";
        try {
            userId = jsonObject.getString("userId");
            vote = jsonObject.getString("vote");
            validationService.idValidation(postId, link);
            validationService.idValidation(userId, link);
        }
        catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
        }

        return voteService.changeVoteByUserAndPostId(postId, userId, vote, link + postId + "/votes");
    }

    @DELETE
    @Path("/{postId}/votes")
    public Response deleteVoteByUserIdAndPostId(@PathParam("postId") String postId,
                                                @QueryParam("userId") String userId) throws AppException {
        validationService.idValidation(postId, link);
        validationService.idValidation(userId, link);
        voteService.removeVoteById(postId, userId, link + postId + "/votes");
        return Response.status(200)
                .entity(new GoodResponseMassage("Vote with postId " + postId + " and user id " + userId + " was deleted"))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }
}