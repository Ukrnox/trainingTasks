package org.forstudy.dto;

import java.util.Map;

public class AllPostVotesDTO {
    private Map<String, Long> allPostVotes;

    public AllPostVotesDTO(Map<String, Long> allPostVotes) {
        this.allPostVotes = allPostVotes;
    }

    public Map<String, Long> getAllPostVotes() {
        return allPostVotes;
    }
}
