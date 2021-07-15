package forum.controllers;

import forum.entities.Post;
import forum.entities.Topic;
import forum.services.GroupService;
import forum.services.PostService;
import forum.services.TopicService;
import forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    public final TopicService topicService;
    public final GroupService groupService;
    public final UserService userService;
    public final PostService postService;

    @Autowired
    public TopicController(TopicService topicService,
                           GroupService groupService,
                           UserService userService,
                           PostService postService) {
        this.topicService = topicService;
        this.groupService = groupService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/{topicId}")
    public Topic getTopicByID(@PathVariable("topicId") Long topicId) {
        return topicService.findById(topicId);
    }

    @GetMapping("/{topicId}/posts")
    public List<Post> getAllPostsByTopicId(@PathVariable("topicId") Long topicId) {
        return postService.findPostsByTopicId(topicId);
    }

    @PostMapping
    public Topic createNewTopic(@RequestParam Long groupId,
                                @RequestParam Long userId,
                                @RequestBody String newTopicTitle) {
        return topicService.save(groupId, userId, newTopicTitle);
    }

    @PutMapping("/{topicId}")
    public Topic updateTopicById(@PathVariable("topicId") Long topicId, @RequestBody String newTopicTitle) {
        return topicService.changeTopicTitle(topicId, newTopicTitle);
    }

    @DeleteMapping("/{topicId}")
    public void deleteTopic(@PathVariable("topicId") Long topicId) {
        topicService.removeTopicById(topicId);
    }
}
