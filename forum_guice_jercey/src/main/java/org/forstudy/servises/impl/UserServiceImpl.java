package org.forstudy.servises.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.entities.User;
import org.forstudy.servises.UserService;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
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
    public String listOfAllUsers() {
        System.out.println("UserServiceImpl : list");
        List<User> users = entityManager.createQuery("FROM User").getResultList();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            for (User user : users) {
                JSONObject userJSON = makeUserJson(user);
                jsonArray.put(userJSON);
            }
            jsonObject.put("users", jsonArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public String findUserById(Long userId) {
        User user = entityManager.find(User.class, userId);
        String result = "";
        try {
            if (user != null) {
                result = makeUserJson(user).toString();
            }
            else {
                result = "No User with id " + userId;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String listOfAllUsersCriteria() {
//        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> userCriteria = criteriaBuilder.createQuery(User.class);
        Root<User> personRoot = userCriteria.from(User.class);
        userCriteria.select(personRoot);
        List<User> resultList = entityManager.createQuery(userCriteria)
                .getResultList();
//        entityManager.getTransaction().commit();
        return resultList.toString();
    }

    @Override
    @Transactional
    public String updateUserById(String userId, String login, String password) {
        String result;
        User user = entityManager.find(User.class, Long.parseLong(userId));
        if (user != null) {
            if (!checkLogin(login)) {
                user.setLogin(login);
                user.setPassword(password);
                try {
                    JSONObject userJSON = makeUserJson(user);
                    result = userJSON.toString();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    result = "";
                }
            }
            else {
                result = "The login: " + login + " is already exist!";
            }
        }
        else {
            result = "The login: " + login + " is already exist!";
        }
        return result;
    }

    @Override
    @Transactional
    public String save(String login, String password) {
        User newUserCreator = null;
        if (!checkLogin(login)) {
            newUserCreator = new User();
            newUserCreator.setLogin(login);
            newUserCreator.setPassword(password);
            newUserCreator.setRegistrationDate(LocalDateTime.now());
            entityManager.persist(newUserCreator);
        }
        System.out.println("UserServiceImpl : save");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = makeUserJson(newUserCreator);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Transactional
    public boolean checkLogin(String login) {
        List<User> users = entityManager.createQuery("FROM User").getResultList();
        boolean result = false;
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private JSONObject makeUserJson(User user) throws JSONException {
        JSONObject userJSON = new JSONObject();
        if (user != null) {
            userJSON.put("id", user.getId());
            userJSON.put("login", user.getLogin());
            userJSON.put("password", user.getPassword());
            userJSON.put("registrationDate", user.getRegistrationDate().toString());
        }
        return userJSON;
    }

    @Override
    @Transactional
    public void deleteUserById(String userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<User> delete = criteriaBuilder.
                createCriteriaDelete(User.class);
        Root<User> root = delete.from(User.class);
        delete.where(criteriaBuilder.equal(root.get("id"), userId));
        entityManager.createQuery(delete).executeUpdate();
    }
}
