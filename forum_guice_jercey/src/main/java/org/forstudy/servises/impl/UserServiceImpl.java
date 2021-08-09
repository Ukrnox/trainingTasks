package org.forstudy.servises.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.forstudy.entities.User;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.UserService;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final EntityManager entityManager;

    @Inject
    public UserServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> listOfAllUsers() {
        return entityManager.createQuery("FROM User").getResultList();
    }

    @Override
    public User findUserById(String userId, String link) throws AppException {
        User user = entityManager.find(User.class, Long.parseLong(userId));
        if (user == null) {
            throw new AppException(400, "AppException",
                    "No user with ID " + userId, link + userId);
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUserById(String userId, User userFromJSON, String link) throws AppException {
        User user = entityManager.find(User.class, Long.parseLong(userId));
        if (user != null) {
            user.setLogin(userFromJSON.getLogin());
            user.setPassword(userFromJSON.getPassword());
        }
        return user;
    }

    @Override
    @Transactional
    public User save(User userFromJSON, String link) throws AppException {
        User newUserCreator = new User();
        newUserCreator.setLogin(userFromJSON.getLogin());
        newUserCreator.setPassword(userFromJSON.getPassword());
        newUserCreator.setRegistrationDate(LocalDateTime.now());
        entityManager.persist(newUserCreator);
        return newUserCreator;
    }

    @Override
    @Transactional
    public void deleteUserById(String userId, String link) throws AppException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<User> delete = criteriaBuilder.
                createCriteriaDelete(User.class);
        Root<User> root = delete.from(User.class);
        delete.where(criteriaBuilder.equal(root.get("id"), Long.parseLong(userId)));
        if (entityManager.createQuery(delete).executeUpdate() == 0) {
            throw new AppException(400, "AppException",
                    "No user with ID " + userId, link);
        }
    }
}
