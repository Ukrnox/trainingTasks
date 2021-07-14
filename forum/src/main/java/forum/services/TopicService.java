package forum.services;

import forum.entities.Topic;
import forum.repositories.TopicRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicService.class);

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Transactional
    public Topic save(Topic newTopic) {
        return topicRepository.save(newTopic);
    }

    public List<Topic> findTopicByGroupId(Long activeGroupId) {
        return topicRepository.findTopicByGroupId(activeGroupId);
    }

    public List<Topic> findTopicByUserId(Long userId) {
        return topicRepository.findTopicByUserId(userId);
    }

    public Topic findById(Long topicId) {
        Topic topic = null;
        Optional<Topic> byId = topicRepository.findById(topicId);
        if (byId.isPresent()) {
            topic = byId.get();
            Hibernate.initialize(topic.getGroup());
        }
        return topic;
    }

    @Transactional
    public void removeTopicById(Long topicId) {
        topicRepository.removeTopicById(topicId);
    }

    @Transactional
    public void changeTopicTitle(Long topicId, String newTopicTitle) {
        topicRepository.changeTopicTitle(topicId, newTopicTitle);
    }
}