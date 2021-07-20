package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.servises.impl.TopicServiceImpl;

@ImplementedBy(TopicServiceImpl.class)
public interface TopicService {
    String findTopicByGroupId(String groupId);

    String findById(String topicId);

    String save(String groupId, String userId, String newTopicTitle);

    String changeTopicTitle(String topicId, String newTopicTitle);

    void removeTopicById(String topicId);
}
