package forum.services;

import forum.entities.Vote;
import forum.repositories.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Transactional
    public List<Vote> findVotesByUserID(Long userId) {
        return voteRepository.findVotesByUserId(userId);
    }

    public Vote findVotesByUserAndPostId(Long userId, Long postId) {
        return voteRepository.findVotesByUserAndPostId(userId, postId);
    }

    @Transactional
    public void changeVoteById(Integer downVotes, Integer upVotes, Long voteId) {
        voteRepository.changeVoteById(downVotes, upVotes, voteId);
    }

    @Transactional
    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public List<Vote> findVoteByTopicId(Long topicID) {
        return voteRepository.findVoteByTopicId(topicID);
    }

    @Transactional
    public void removeVoteById(Long voteId) {
        voteRepository.removeVoteById(voteId);
    }

    public Vote findVoteById(Long voteId) {
        Optional<Vote> byId = voteRepository.findById(voteId);
        Vote vote = null;
        if (byId.isPresent()) {
            vote = byId.get();
        }
        return vote;
    }

    @Transactional
    public List<Vote> findVotesByPostId(Long postId) {
        return voteRepository.findVotesByPostId(postId);
    }
}
