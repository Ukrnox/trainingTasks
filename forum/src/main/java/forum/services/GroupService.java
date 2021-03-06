package forum.services;

import forum.entities.Group;
import forum.repositories.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findById(Long groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }

    @Transactional
    public Group createNewGroup(String newGroupTitle) {
        Group newGroup = new Group();
        newGroup.setName(newGroupTitle);
        return groupRepository.save(newGroup);
    }

    @Transactional
    public Group updateGroupTitle(Long groupId, String newTitle) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group != null) {
            group.setName(newTitle);
        }
        return group;
    }

    @Transactional
    public void deleteGroupById(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}