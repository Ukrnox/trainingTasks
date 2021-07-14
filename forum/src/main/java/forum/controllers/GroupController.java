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
    public @ResponseBody
    List<Group> getGroups() {
        return groupService.findAll();
    }

    @GetMapping("{groupId}")
    public @ResponseBody
    Group getGroupByID(@PathVariable("groupId") Long groupId) {
        return groupService.findById(groupId);
    }

    @PostMapping
    public @ResponseBody
    Group createNewGroup(@RequestParam String newGroupTitle) {
        Group newGroup = new Group();
        newGroup.setName(newGroupTitle);
        return groupService.createNewGroup(newGroup);
    }

    @GetMapping("{groupId}/topics")
    public @ResponseBody List<Topic> getAllTopicsByGroupID (@PathVariable("groupId") Long groupId) {
        return topicService.findTopicByGroupId(groupId);
    }

    @PutMapping("{groupId}")
    public @ResponseBody
    Group updateGroupTitleById(@PathVariable("groupId") Long groupId, @RequestParam String newGroupTitle) {
        Group groupForResponse = null;
        if (groupService.findById(groupId) != null) {
            groupService.updateGroupTitle(groupId, newGroupTitle);
            groupForResponse = groupService.findById(groupId);
        }
        return groupForResponse;
    }

    @DeleteMapping("{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteGroupById(groupId);
    }

}
