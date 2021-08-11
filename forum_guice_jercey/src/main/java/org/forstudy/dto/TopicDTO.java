package org.forstudy.dto;

public class TopicDTO {
    private String userId;
    private String groupId;

    public TopicDTO(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupId() {
        return groupId;
    }
}
