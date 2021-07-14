package forum.controllers;

import forum.entities.Post;
import forum.entities.Topic;
import forum.entities.User;
import forum.entities.Vote;
import forum.services.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    PostService postService;
    VoteService voteService;
    GroupService groupService;
    UserService userService;
    TopicService topicService;

    public PostController(PostService postService,
                          VoteService voteService,
                          GroupService groupService,
                          UserService userService,
                          TopicService topicService) {
        this.voteService = voteService;
        this.postService = postService;
        this.groupService = groupService;
        this.userService = userService;
        this.topicService = topicService;
    }

    @GetMapping("{postId}")
    public @ResponseBody
    Post getPostByID(@PathVariable("postId") Long postId) {
        return postService.findPostById(postId);
    }

    @PostMapping
    public @ResponseBody
    Post createNewPost(@RequestParam Long topicId, @RequestParam Long userId, @RequestBody String newPostText) {
        Post newPostCreator = null;
        User user = userService.findUserById(userId);
        Topic topic = topicService.findById(topicId);
        if (user != null && topic != null) {
            newPostCreator = new Post();
            newPostCreator.setAuthor(user);
            newPostCreator.setTopic(topic);
            newPostCreator.setText(newPostText);
            newPostCreator.setDateCreated(LocalDateTime.now());
            postService.save(newPostCreator);
        }
        return newPostCreator;
    }

    @PutMapping("/{postId}")
    public @ResponseBody
    Post updatePostById(@PathVariable("postId") Long postId, @RequestBody String newText) {
        postService.updatePostById(postId, newText);
        return postService.findPostById(postId);
    }

    @DeleteMapping("{postId}")
    void deletePost(@PathVariable("postId") Long postId) {
        postService.removePostById(postId);
    }

    @GetMapping("{postId}/votes")
    public @ResponseBody
    Map<String, Long> getAllVotes(@PathVariable("postId") Long postId) {
        return voteService.findVotesByPostId(postId).stream()
                .collect(Collectors.toMap(x -> x.getUpVotes() == 1 ? "upVotes" : "downVotes", n -> 1L, Long::sum));
    }

    @PostMapping("/{postId}/votes")
    public @ResponseBody
    Vote addVote(@PathVariable("postId") Long postId,
                 @RequestParam Long userId,
                 @RequestParam String vote) {
        Post postById = postService.findPostById(postId);
        User userById = userService.findUserById(userId);
        Vote votesByUserAndPostId = voteService.findVotesByUserAndPostId(userId, postId);
        Vote newVote = null;
        if (postById != null &&
                userById != null &&
                votesByUserAndPostId == null &&
                (vote.equals("upVote") || vote.equals("downVote"))) {
            newVote = new Vote();
            newVote.setUpVotes(vote.equals("upVote") ? 1 : 0);
            newVote.setDownVotes(vote.equals("downVote") ? 1 : 0);
            newVote.setAuthor(userById);
            newVote.setPost(postById);
            voteService.save(newVote);
        }
        return newVote;
    }


    @PutMapping("/{postId}/votes")
    public @ResponseBody
    Vote updateVote(@PathVariable("postId") Long postId,
                    @RequestParam Long userId,
                    @RequestParam String vote) {
        Vote votesByUserAndPostId = voteService.findVotesByUserAndPostId(userId, postId);
        if (votesByUserAndPostId != null &&
                (vote.equals("upVote") || vote.equals("downVote"))) {
            int upVote = vote.equals("upVote") ? 1 : 0;
            int downVote = vote.equals("downVote") ? 1 : 0;
            long voteId = votesByUserAndPostId.getId();
            voteService.changeVoteById(downVote, upVote, voteId);
        }
        return voteService.findVotesByUserAndPostId(userId, postId);
    }

    @DeleteMapping("/{postId}/votes")
    public void deleteVoteByUserIdAndPostId(@PathVariable("postId") Long postId,
                           @RequestParam Long userId) {
        Vote votesByUserAndPostId = voteService.findVotesByUserAndPostId(userId, postId);
        if (votesByUserAndPostId != null) {
            voteService.removeVoteById(votesByUserAndPostId.getId());
        }
    }
}