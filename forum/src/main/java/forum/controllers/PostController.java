package forum.controllers;

import forum.entities.Post;
import forum.entities.Vote;
import forum.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final VoteService voteService;

    @Autowired
    public PostController(PostService postService,
                          VoteService voteService) {
        this.voteService = voteService;
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public Post getPostByID(@PathVariable("postId") Long postId) {
        return postService.findPostById(postId);
    }

    @PostMapping
    public Post createNewPost(@RequestParam Long userId, @RequestParam Long topicId, @RequestBody String newPostText) {
        return postService.save(userId, topicId, newPostText);
    }

    @PutMapping("/{postId}")
    public Post updatePostById(@PathVariable("postId") Long postId, @RequestBody String newText) {
        return postService.updatePostById(postId, newText);
    }

    @DeleteMapping("/{postId}")
    void deletePost(@PathVariable("postId") Long postId) {
        postService.removePostById(postId);
    }

    @GetMapping("/{postId}/votes")
    public Map<String, Long> getAllVotes(@PathVariable("postId") Long postId) {
        return voteService.getAllVotes(postId);
    }

    @PostMapping("/{postId}/votes")
    public Vote addVote(@PathVariable("postId") Long postId,
                        @RequestParam Long userId,
                        @RequestParam String vote) {
        return voteService.save(postId, userId, vote);
    }


    @PutMapping("/{postId}/votes")
    public Vote updateVote(@PathVariable("postId") Long postId,
                           @RequestParam Long userId,
                           @RequestParam String vote) {
        return voteService.changeVoteByUserAndPostId(postId, userId, vote);
    }

    @DeleteMapping("/{postId}/votes")
    public void deleteVoteByUserIdAndPostId(@PathVariable("postId") Long postId,
                                            @RequestParam Long userId) {
        voteService.removeVoteById(userId, postId);
    }
}