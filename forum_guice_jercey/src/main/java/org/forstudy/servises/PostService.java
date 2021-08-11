package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.entities.Post;
import org.forstudy.entities.Topic;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.impl.PostServiceImpl;

import java.util.List;

@ImplementedBy(PostServiceImpl.class)
public interface PostService {
    List<Post> findPostsByTopicId(String topicId, String link) throws AppException;

    Post findPostById(String postId, String link) throws AppException;

    Post save(String userId, String topicId, Post newPost, String link) throws AppException;

    Post updatePostById(String postId, Post newPost, String link) throws AppException;

    void removePostById(String postId, String link) throws AppException;
}
