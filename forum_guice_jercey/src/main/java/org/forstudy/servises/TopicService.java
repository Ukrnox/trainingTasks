package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.dto.TopicDTO;
import org.forstudy.entities.Topic;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.impl.TopicServiceImpl;

import java.util.List;

@ImplementedBy(TopicServiceImpl.class)
public interface TopicService {
    List<Topic> findTopicsByGroupId(String groupId, String link) throws AppException;

    Topic findById(String topicId, String link) throws AppException;

    Topic save(String groupId, String userId, Topic newTopic, String link) throws AppException;

    Topic changeTopicTitle(String topicId, Topic newTopic, String link) throws AppException;

    void removeTopicById(String topicId, String link) throws AppException;
}
