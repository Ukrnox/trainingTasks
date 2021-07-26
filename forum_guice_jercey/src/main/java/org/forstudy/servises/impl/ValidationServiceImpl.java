package org.forstudy.servises.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.forstudy.entities.Group;
import org.forstudy.entities.Post;
import org.forstudy.entities.Topic;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.exceptionhandling.ValidationException;
import org.forstudy.servises.ValidationService;

import javax.persistence.EntityManager;
import java.util.List;

public class ValidationServiceImpl implements ValidationService {

    private final EntityManager entityManager;

    @Inject
    public ValidationServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void userLoginAndPasswordValidation(User user, String link) throws AppException {
        if (user.getLogin() == null ||
                user.getPassword() == null ||
                user.getLogin().isEmpty() || user.getPassword().isEmpty()) {
            throw new ValidationException(400, "", "User login and password should not be empty", link);
        }
    }

    @Override
    public void topicValidation(Topic topic, String link) throws AppException {
        if (topic.getTitle() == null ||
                topic.getTitle().isEmpty()) {
            throw new ValidationException(400, "", "Topic title should not be empty", link);
        }
    }

    @Override
    public void postValidation(Post post, String link) throws AppException {
        if (post.getText() == null ||
                post.getText().isEmpty()) {
            throw new ValidationException(400, "", "Post text should not be empty", link);
        }
    }


    @Override
    @Transactional
    public void checkLogin(String login, String link) throws AppException {
        List<User> users = entityManager.createQuery("FROM User").getResultList();
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                throw new ValidationException(400, "", "Login " + login + " is already exist!", link);
            }
        }
    }

    @Override
    public void idValidation(String id, String link) throws AppException {
        try {
            Long.parseLong(id);
        }
        catch (NumberFormatException e) {
            throw new ValidationException(400, e.getClass().getSimpleName() + " " + e.getMessage(),
                    "Wrong id format", link);
        }
    }

    public boolean groupValidation(Group group) {
        boolean result = true;
        if (group.getName() == null ||
                group.getName().isEmpty()) {
            result = false;
        }
        return result;
    }
}
