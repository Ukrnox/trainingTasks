package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.forstudy.entities.Group;
import org.forstudy.exceptionhandling.AppException;
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
    public List<Group> findAll() {
        return entityManager.createQuery("FROM Group ").getResultList();
    }

    @Override
    public Group findById(String groupId, String link) throws AppException {
        Group group = entityManager.find(Group.class, Long.parseLong(groupId));
        if (group == null) {
            throw new AppException(400, "AppException",
                    "No Group with id " + groupId, link);
        }
        return group;
    }

    @Override
    @Transactional
    public Group createNewGroup(String newGroupTitle, String link) {
        Group newGroup = new Group();
        newGroup.setName(newGroupTitle);
        entityManager.persist(newGroup);
        return newGroup;
    }

    @Override
    @Transactional
    public Group updateGroupTitle(String groupId, String newGroupTitle, String link) throws AppException {
        Group group = entityManager.find(Group.class, Long.parseLong(groupId));
        if (group == null) {
            throw new AppException(400, "AppException",
                    "No group with ID " + groupId, link + groupId);
        }
        group.setName(newGroupTitle);
        return group;
    }

    @Override
    @Transactional
    public void deleteGroupById(String groupId, String link) throws AppException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Group> delete = criteriaBuilder.
                createCriteriaDelete(Group.class);
        Root<Group> root = delete.from(Group.class);
        delete.where(criteriaBuilder.equal(root.get("id"), Long.parseLong(groupId)));
        if (entityManager.createQuery(delete).executeUpdate() == 0) {
            throw new AppException(400, "AppException",
                    "No group with ID " + groupId, link + groupId);
        }
    }
}
