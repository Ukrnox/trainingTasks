package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.entities.Post;
import org.forstudy.entities.Topic;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.impl.ValidationServiceImpl;

@ImplementedBy(ValidationServiceImpl.class)
public interface ValidationService {
    void topicValidation(Topic topic, String link) throws AppException;

    void userLoginAndPasswordValidation(User user, String link) throws AppException;

    void postValidation(Post post, String link) throws AppException;

    void checkLogin(String login, String link) throws AppException;

    void idValidation(String id, String link) throws AppException;
}
