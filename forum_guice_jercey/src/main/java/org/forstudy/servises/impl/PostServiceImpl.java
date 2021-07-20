package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.entities.Post;
import org.forstudy.entities.Topic;
import org.forstudy.entities.User;
import org.forstudy.servises.PostService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class PostServiceImpl implements PostService {

    private final EntityManager entityManager;

    @Inject
    public PostServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String findPostsByTopicId(long topicId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> e = query.from(Post.class);
        query.where(cb.equal(e.get("topic"), topicId));
        List<Post> postsList = entityManager.createQuery(query).getResultList();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            for (Post post : postsList) {
                JSONObject topicJson = makePostJson(post);
                jsonArray.put(topicJson);
            }
            jsonObject.put("posts", jsonArray);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return jsonArray.toString();
    }

    @Override
    public String findPostById(long postId) {
        Post post = entityManager.find(Post.class, postId);
        String result = "";
        try {
            if (post != null) {
                result = makePostJson(post).toString();
            }
            else {
                result = "No Post with id " + postId;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional
    public String save(long longUserId, long longTopicId, String newPostText) {
        User user = entityManager.find(User.class, longUserId);
        Topic topic = entityManager.find(Topic.class, longTopicId);
        Post newPostCreator = null;
        if (user != null && topic != null) {
            newPostCreator = new Post();
            newPostCreator.setAuthor(user);
            newPostCreator.setTopic(topic);
            newPostCreator.setDateCreated(LocalDateTime.now());
            newPostCreator.setText(newPostText);
            entityManager.persist(newPostCreator);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = makePostJson(newPostCreator);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    @Transactional
    public String updatePostById(long longPostId, String newPostText) {
        String result;
        Post post = entityManager.find(Post.class, longPostId);
        if (post != null) {
            try {
                post.setText(newPostText);
                JSONObject postJSON = makePostJson(post);
                result = postJSON.toString();
            }
            catch (JSONException e) {
                e.printStackTrace();
                result = "";
            }
        }
        else {
            result = "No Post with id : " + longPostId;
        }
        return result;
    }

    @Override
    @Transactional
    public void removePostById(long longPostId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Post> delete = criteriaBuilder.
                createCriteriaDelete(Post.class);
        Root<Post> root = delete.from(Post.class);
        delete.where(criteriaBuilder.equal(root.get("id"), longPostId));
        entityManager.createQuery(delete).executeUpdate();
    }

    private JSONObject makePostJson(Post post) throws JSONException {
        JSONObject groupJSON = new JSONObject();
        if (post != null) {
            groupJSON.put("id", post.getId());
            groupJSON.put("login", post.getText());
        }
        return groupJSON;
    }
}
