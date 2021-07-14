package forum.services;

import forum.entities.Group;
import forum.repositories.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Group group = null;
        Optional<Group> byId = groupRepository.findById(groupId);
        if (byId.isPresent()) {
            group = byId.get();
        }
        return group;
    }

    @Transactional
    public Group createNewGroup(Group group) {
        return groupRepository.save(group);
    }

    @Transactional
    public void updateGroupTitle(Long groupId, String newTitle) {
        groupRepository.changeGroupTitle(newTitle, groupId);
    }

    @Transactional
    public void deleteGroupById(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}