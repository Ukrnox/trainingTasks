package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.servises.impl.GroupServiceImpl;

@ImplementedBy(GroupServiceImpl.class)
public interface GroupService {
    String findAll();

    String findById(String groupId);

    String createNewGroup(String newGroupTitle);

    String updateGroupTitle(String groupId, String newGroupTitle);

    void deleteGroupById(String groupId);
}
