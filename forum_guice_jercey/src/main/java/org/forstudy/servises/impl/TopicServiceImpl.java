package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.entities.Group;
import org.forstudy.entities.Topic;
import org.forstudy.entities.User;
import org.forstudy.servises.TopicService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

public class TopicServiceImpl implements TopicService {

    private final EntityManager entityManager;

    @Inject
    public TopicServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public String findTopicByGroupId(String groupId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Topic> query = cb.createQuery(Topic.class);
        Root<Topic> e = query.from(Topic.class);
        query.where(cb.equal(e.get("group"), Long.parseLong(groupId)));
        List<Topic> topicsList = entityManager.createQuery(query).getResultList();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            for (Topic topic : topicsList) {
                JSONObject topicJson = makeTopicJson(topic);
                jsonArray.put(topicJson);
            }
            jsonObject.put("topics", jsonArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonArray.toString();
    }


    @Override
    public String findById(String topicId) {
        Topic topic = entityManager.find(Topic.class, topicId);
        String result = "";
        try {
            if (topic != null) {
                result = makeTopicJson(topic).toString();
            }
            else {
                result = "No Topic with id " + topicId;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional
    public String save(String groupId, String userId, String newTopicTitle) {
        User user = entityManager.find(User.class, Long.parseLong(userId));
        Group group = entityManager.find(Group.class, Long.parseLong(groupId));
        String result;
        if (user != null && group != null) {
            Topic newTopic = new Topic();
            newTopic.setTitle(newTopicTitle);
            newTopic.setAuthor(user);
            newTopic.setGroup(group);
            newTopic.setDateOfCreation(LocalDateTime.now());
            entityManager.persist(newTopic);
            JSONObject topicJSON;
            try {
                topicJSON = makeTopicJson(newTopic);
                result = topicJSON.toString();
            }
            catch (JSONException e) {
                e.printStackTrace();
                result = "";
            }
        }
        else {
            result = "Wrong request information!";
        }
        return result;
    }

    private JSONObject makeTopicJson(Topic newTopic) throws JSONException {
        JSONObject groupJSON = new JSONObject();
        if (newTopic != null) {
            groupJSON.put("id", newTopic.getId());
            groupJSON.put("login", newTopic.getTitle());
            groupJSON.put("dateOfCreation", newTopic.getDateOfCreation());
        }
        return groupJSON;
    }

    @Override
    @Transactional
    public String changeTopicTitle(String topicId, String newTopicTitle) {
        String result;
        Topic topic = entityManager.find(Topic.class, Long.parseLong(topicId));
        if (topic != null) {
            try {
                topic.setTitle(newTopicTitle);
                JSONObject topicJSON = makeTopicJson(topic);
                result = topicJSON.toString();
            }
            catch (JSONException e) {
                e.printStackTrace();
                result = "";
            }
        }
        else {
            result = "No Topic with id : " + topicId;
        }
        return result;
    }

    @Override
    @Transactional
    public void removeTopicById(String topicId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Topic> delete = criteriaBuilder.
                createCriteriaDelete(Topic.class);
        Root<Topic> root = delete.from(Topic.class);
        delete.where(criteriaBuilder.equal(root.get("id"), topicId));
        entityManager.createQuery(delete).executeUpdate();
    }
}
