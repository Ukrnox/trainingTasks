package forum.controllers;

import forum.entities.Group;
import forum.entities.Topic;
import forum.services.GroupService;
import forum.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final TopicService topicService;

    @Autowired
    public GroupController(GroupService groupService, TopicService topicService) {
        this.groupService = groupService;
        this.topicService = topicService;
    }

    @GetMapping
    public List<Group> getGroups() {
        return groupService.findAll();
    }

    @GetMapping("/{groupId}")
    public Group getGroupByID(@PathVariable("groupId") Long groupId) {
        return groupService.findById(groupId);
    }

    @PostMapping
    public Group createNewGroup(@RequestParam String newGroupTitle) {
        return groupService.createNewGroup(newGroupTitle);
    }

    @GetMapping("/{groupId}/topics")
    public List<Topic> getAllTopicsByGroupID(@PathVariable("groupId") Long groupId) {
        return topicService.findTopicByGroupId(groupId);
    }

    @PutMapping("/{groupId}")
    public Group updateGroupTitleById(@PathVariable("groupId") Long groupId, @RequestParam String newGroupTitle) {
        return groupService.updateGroupTitle(groupId, newGroupTitle);
    }

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteGroupById(groupId);
    }
}
