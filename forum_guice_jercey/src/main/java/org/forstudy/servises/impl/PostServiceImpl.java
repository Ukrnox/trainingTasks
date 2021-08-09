package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.forstudy.entities.Post;
import org.forstudy.entities.Topic;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
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
    public List<Post> findPostsByTopicId(String topicId, String link) throws AppException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> e = query.from(Post.class);
        query.where(cb.equal(e.get("topic"), Long.parseLong(topicId)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Post findPostById(String postId, String link) throws AppException {
        Post post = entityManager.find(Post.class, Long.parseLong(postId));
        if (post == null) {
            throw new AppException(400, "AppException",
                    "No post with ID " + postId, link);
        }
        return post;
    }

    @Override
    @Transactional
    public Post save(String userId, String topicId, Post newPost, String link) throws AppException {
        User user = entityManager.find(User.class, Long.parseLong(userId));
        Topic topic = entityManager.find(Topic.class, Long.parseLong(topicId));
        Post newPostCreator;
        if (user != null && topic != null) {
            newPostCreator = new Post();
            newPostCreator.setAuthor(user);
            newPostCreator.setTopic(topic);
            newPostCreator.setDateCreated(LocalDateTime.now());
            newPostCreator.setText(newPost.getText());
            entityManager.persist(newPostCreator);
        }
        else {
            throw new AppException(400, "AppException",
                    "Wrong topic or user id", link);
        }
        return newPostCreator;
    }

    @Override
    @Transactional
    public Post updatePostById(String postId, Post newPost, String link) throws AppException {
        Post post = entityManager.find(Post.class, Long.parseLong(postId));
        if (post != null) {
            post.setText(newPost.getText());
        }
        else {
            throw new AppException(400, "AppException",
                    "No post with id " + postId, link);
        }
        return post;
    }

    @Override
    @Transactional
    public void removePostById(String postId, String link) throws AppException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Post> delete = criteriaBuilder.
                createCriteriaDelete(Post.class);
        Root<Post> root = delete.from(Post.class);
        delete.where(criteriaBuilder.equal(root.get("id"), Long.parseLong(postId)));
        if (entityManager.createQuery(delete).executeUpdate() == 0) {
            throw new AppException(400, "AppException",
                    "No post with ID " + postId, link);
        }
    }
}
