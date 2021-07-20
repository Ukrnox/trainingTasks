package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.entities.Group;
import org.forstudy.servises.GroupService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.List;

public class GroupServiceImpl implements GroupService {

    private final EntityManager entityManager;

    @Inject
    public GroupServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public String findAll() {
        List<Group> groups = entityManager.createQuery("FROM Group ").getResultList();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            for (Group group : groups) {
                JSONObject groupJSON = makeGroupJson(group);
                jsonArray.put(groupJSON);
            }
            jsonObject.put("groups", jsonArray);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public String findById(String groupId) {
        Group group = entityManager.find(Group.class, Long.parseLong(groupId));
        String result = "";
        try {
            if (group != null) {
                result = makeGroupJson(group).toString();
            }
            else {
                result = "No Group with id " + groupId;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional
    public String createNewGroup(String newGroupTitle) {
        Group newGroup = new Group();
        newGroup.setName(newGroupTitle);
        entityManager.persist(newGroup);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = makeGroupJson(newGroup);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    @Transactional
    public String updateGroupTitle(String groupId, String newGroupTitle) {
        String result;
        Group group = entityManager.find(Group.class, Long.parseLong(groupId));
        if (group != null) {
            group.setName(newGroupTitle);
            try {
                JSONObject groupJSON = makeGroupJson(group);
                result = groupJSON.toString();
            }
            catch (JSONException e) {
                e.printStackTrace();
                result = "";
            }
        }
        else {
            result = "No Group with id : " + groupId;
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteGroupById(String groupId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Group> delete = criteriaBuilder.
                createCriteriaDelete(Group.class);
        Root<Group> root = delete.from(Group.class);
        delete.where(criteriaBuilder.equal(root.get("id"), groupId));
        entityManager.createQuery(delete).executeUpdate();
    }

    private JSONObject makeGroupJson(Group group) throws JSONException {
        JSONObject groupJSON = new JSONObject();
        if (group != null) {
            groupJSON.put("id", group.getId());
            groupJSON.put("login", group.getName());
        }
        return groupJSON;
    }
}
