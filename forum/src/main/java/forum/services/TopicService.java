package forum.services;

import forum.entities.Group;
import forum.entities.Topic;
import forum.entities.User;
import forum.repositories.GroupRepository;
import forum.repositories.TopicRepository;
import forum.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;


    @Autowired
    public TopicService(TopicRepository topicRepository,
                        UserRepository userRepository,
                        GroupRepository groupRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public Topic save(Long groupId, Long userId, String newTopicTitle) {
        Group group = groupRepository.findById(groupId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        Topic newTopicCreator = null;
        if (group != null && user != null) {
            newTopicCreator = new Topic();
            newTopicCreator.setAuthor(user);
            newTopicCreator.setGroup(group);
            newTopicCreator.setDateCreated(LocalDateTime.now());
            newTopicCreator.setTitle(newTopicTitle);
            topicRepository.save(newTopicCreator);
        }
        return newTopicCreator;
    }

    public List<Topic> findTopicByGroupId(Long activeGroupId) {
        return topicRepository.findTopicByGroupId(activeGroupId);
    }

    public List<Topic> findTopicByUserId(Long userId) {
        return topicRepository.findTopicByUserId(userId);
    }

    public Topic findById(Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if (topic != null) {
            Hibernate.initialize(topic.getGroup());
        }
        return topic;
    }

    @Transactional
    public void removeTopicById(Long topicId) {
        topicRepository.removeTopicById(topicId);
    }

    @Transactional
    public Topic changeTopicTitle(Long topicId, String newTopicTitle) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if(topic!=null)
        {
            topic.setTitle(newTopicTitle);
        }
        return topic;
    }
}