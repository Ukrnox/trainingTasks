package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.servises.impl.PostServiceImpl;

@ImplementedBy(PostServiceImpl.class)
public interface PostService {
    String findPostsByTopicId(long topicId);

    String findPostById(long parseLong);

    String save(long longUserId, long longTopicId, String newPostText);

    String updatePostById(long longPostId, String newPostText);

    void removePostById(long longPostId);
}
