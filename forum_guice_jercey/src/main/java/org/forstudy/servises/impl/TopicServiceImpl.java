package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.forstudy.entities.Group;
import org.forstudy.entities.Topic;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.TopicService;
import org.forstudy.servises.ValidationService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

public class TopicServiceImpl implements TopicService {

    private final EntityManager entityManager;
    private final ValidationService validationService;

    @Inject
    public TopicServiceImpl(EntityManager entityManager, ValidationService validationService) {
        this.entityManager = entityManager;
        this.validationService = validationService;
    }

    @Override
    @Transactional
    public List<Topic> findTopicsByGroupId(String groupId, String link) throws AppException {
        validationService.idValidation(groupId, link);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Topic> query = cb.createQuery(Topic.class);
        Root<Topic> e = query.from(Topic.class);
        query.where(cb.equal(e.get("group"), Long.parseLong(groupId)));
        return entityManager.createQuery(query).getResultList();
    }


    @Override
    public Topic findById(String topicId, String link) throws AppException {
        validationService.idValidation(topicId, link);
        Topic topic = entityManager.find(Topic.class, topicId);
        if (topic == null) {
            throw new AppException(400, "AppException",
                    "No topic with ID " + topicId, link + topicId);
        }
        return topic;
    }

    @Override
    @Transactional
    public Topic save(String groupId, String userId, Topic topic, String link) throws AppException {
        validationService.idValidation(groupId + userId, link);
        User user = entityManager.find(User.class, Long.parseLong(userId));
        Group group = entityManager.find(Group.class, Long.parseLong(groupId));
        Topic newTopic;
        if (user != null && group != null) {
            newTopic = new Topic();
            newTopic.setTitle(topic.getTitle());
            newTopic.setAuthor(user);
            newTopic.setGroup(group);
            newTopic.setDateOfCreation(LocalDateTime.now());
            entityManager.persist(newTopic);
        }
        else {
            throw new AppException(400, "AppException",
                    "Wrong group or user id", link);
        }
        return newTopic;
    }

    @Override
    @Transactional
    public Topic changeTopicTitle(String topicId, Topic newTopic, String link) throws AppException {
        validationService.idValidation(topicId, link);
        validationService.topicValidation(newTopic, link);
        Topic topic = entityManager.find(Topic.class, Long.parseLong(topicId));
        if (topic != null) {
            topic.setTitle(newTopic.getTitle());
        }
        else {
            throw new AppException(400, "AppException",
                    "No topic with id " + topicId, link);
        }
        return topic;
    }

    @Override
    @Transactional
    public void removeTopicById(String topicId, String link) throws AppException {
        validationService.idValidation(topicId, link);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Topic> delete = criteriaBuilder.
                createCriteriaDelete(Topic.class);
        Root<Topic> root = delete.from(Topic.class);
        delete.where(criteriaBuilder.equal(root.get("id"), Long.parseLong(topicId)));
        if (entityManager.createQuery(delete).executeUpdate() == 0) {
            throw new AppException(400, "AppException",
                    "No topic with ID " + topicId, link);
        }
    }
}
