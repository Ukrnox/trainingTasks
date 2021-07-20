package org.forstudy.servises;

import com.google.inject.ImplementedBy;
import org.forstudy.servises.impl.VoteServiceImpl;

@ImplementedBy(VoteServiceImpl.class)
public interface VoteService {
    String getAllVotes(long longPostId);

    String save(long longPostID, long longUserId, String vote);

    void removeVoteById(long longPostID, long longUserId);

    String changeVoteByUserAndPostId(long longPostID, long longUserId, String vote);
}
