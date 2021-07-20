package org.forstudy.controllers;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.servises.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/posts")
public class PostController {

    private final PostService postService;
    private final VoteService voteService;

    @Inject
    public PostController(PostService postService,
                          VoteService voteService) {
        this.voteService = voteService;
        this.postService = postService;
    }

    @GET
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPostByID(@PathParam("postId") String postId) {
        String result;
        try {
            long id = Long.parseLong(postId);
            if ((result = postService.findPostById(Long.parseLong(postId))) == null) {
                result = "No post with id " + id;
            }
        }
        catch (NumberFormatException e) {
            result = "Wrong postId";
        }
        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createNewPost(@QueryParam("userId") String userId, @QueryParam("topicId") String topicId, JSONObject jsonObject) {
        String result;
        try {
            long longUserId = Long.parseLong(userId);
            long longTopicId = Long.parseLong(topicId);
            try {
                String newPostText = jsonObject.getString("newPostText");
                result = postService.save(longUserId, longTopicId, newPostText);
            }
            catch (JSONException e) {
                e.printStackTrace();
                result = "Wrong JSON in request";
            }
        }
        catch (NumberFormatException e) {
            result = "Wrong ID format";
        }
        return result;
    }

    @PUT
    @Path("/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePostById(@PathParam("postId") String postId, JSONObject jsonObject) {
        String result;
        try {
            long longPostId = Long.parseLong(postId);
            try {
                String newPostText = jsonObject.getString("newPostText");
                result = postService.updatePostById(longPostId, newPostText);
            }
            catch (JSONException e) {
                e.printStackTrace();
                result = "Wrong JSON in request";
            }

        }
        catch (NumberFormatException e) {
            result = "Wrong postId format";
        }
        return result;
    }

    @DELETE
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deletePost(@PathParam("postId") String postId) {
        try {
            long longPostId = Long.parseLong(postId);
            postService.removePostById(longPostId);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/{postId}/votes")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllVotes(@PathParam("postId") String postId) {
        String result;
        try {
            long longPostId = Long.parseLong(postId);
            result = voteService.getAllVotes(longPostId);
        }
        catch (NumberFormatException e) {
            result = "Wrong postId format";
        }
        return result;
    }

    @POST
    @Path("/{postId}/votes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addVote(@PathParam("postId") String postId, JSONObject jsonObject) {
        String result;
        try {
            long longPostID = Long.parseLong(postId);
            long longUserId = Long.parseLong(jsonObject.getString("userId"));
            String vote = jsonObject.getString("vote");
            result = voteService.save(longPostID, longUserId, vote);
        }
        catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
            result = "JSONException | NumberFormatException";
        }
        return result;
    }


    @PUT
    @Path("/{postId}/votes")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateVote(@PathParam("postId") String postId, JSONObject jsonObject) {
        String result;
        try {
            long longPostID = Long.parseLong(postId);
            long longUserId = Long.parseLong(jsonObject.getString("userId"));
            String vote = jsonObject.getString("vote");
            result = voteService.changeVoteByUserAndPostId(longPostID, longUserId, vote);
        }
        catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
            result = "JSONException | NumberFormatException";
        }

        return result;
    }

    @DELETE
    @Path("/{postId}/votes")
    public void deleteVoteByUserIdAndPostId(@PathParam("postId") String postId,
                                            @QueryParam("userId") String userId) {
        try {
            long longPostID = Long.parseLong(postId);
            long longUserId = Long.parseLong(userId);
            voteService.removeVoteById(longPostID, longUserId);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}