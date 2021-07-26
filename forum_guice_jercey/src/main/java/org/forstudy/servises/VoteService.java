package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.dto.AllPostVotesDTO;
import org.forstudy.entities.Vote;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.impl.VoteServiceImpl;

@ImplementedBy(VoteServiceImpl.class)
public interface VoteService {
    AllPostVotesDTO getAllVotes(String postId, String link) throws AppException;

    Vote save(String postID, String userId, String vote, String link) throws AppException;

    void removeVoteById(String postID, String userId, String link) throws AppException;

    Vote changeVoteByUserAndPostId(String postID, String userId, String vote, String link) throws AppException;
}
