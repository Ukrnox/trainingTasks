package forum.controllers;

import forum.entities.Group;
import forum.entities.Post;
import forum.entities.Topic;
import forum.entities.User;
import forum.services.GroupService;
import forum.services.PostService;
import forum.services.TopicService;
import forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/topics")
public class TopicController {

    TopicService topicService;
    GroupService groupService;
    UserService userService;
    PostService postService;

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

    @GetMapping("{topicId}")
    public @ResponseBody
    Topic getTopicByID(@PathVariable("topicId") Long topicId) {
        return topicService.findById(topicId);
    }

    @GetMapping("{topicId}/posts")
    public @ResponseBody
    List<Post> getAllPostsByTopicId(@PathVariable("topicId") Long topicId) {
        return postService.findPostsByTopicId(topicId);
    }

    @PostMapping
    public @ResponseBody
    Topic createNewTopic(@RequestParam Long groupId, @RequestParam Long userId, @RequestBody String newTopicTitle) {
        Group group = groupService.findById(groupId);
        User user = userService.findUserById(userId);
        Topic newTopicCreator = null;
        if (group != null && user != null) {
            newTopicCreator = new Topic();
            newTopicCreator.setAuthor(user);
            newTopicCreator.setGroup(group);
            newTopicCreator.setDateCreated(LocalDateTime.now());
            newTopicCreator.setTitle(newTopicTitle);
            topicService.save(newTopicCreator);
        }
        return newTopicCreator;
    }

    @PutMapping("{topicId}")
    public @ResponseBody
    Topic updateTopicById(@PathVariable("topicId") Long topicId, @RequestBody String newTopicTitle) {
        if (topicService.findById(topicId) != null) {
            topicService.changeTopicTitle(topicId, newTopicTitle);
        }
        return topicService.findById(topicId);
    }

    @DeleteMapping("{topicId}")
    public void deleteTopic(@PathVariable("topicId") Long topicId) {
        topicService.removeTopicById(topicId);
    }


}
