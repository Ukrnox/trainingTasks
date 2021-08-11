package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.entities.Group;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.impl.GroupServiceImpl;

import java.util.List;

@ImplementedBy(GroupServiceImpl.class)
public interface GroupService {
    List<Group> findAll();

    Group findById(String groupId, String link) throws AppException;

    Group createNewGroup(String newGroupTitle, String link);

    Group updateGroupTitle(String groupId, String newGroupTitle, String link) throws AppException;

    void deleteGroupById(String groupId, String link) throws AppException;
}
