package forum.services;

import forum.entities.Post;
import forum.entities.User;
import forum.entities.Vote;
import forum.repositories.PostRepository;
import forum.repositories.UserRepository;
import forum.repositories.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository,
                       PostRepository postRepository,
                       UserRepository userRepository,
                       PlatformTransactionManager tm) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Vote> findVotesByUserID(Long userId) {
        return voteRepository.findVotesByUserId(userId);
    }

    public Vote findVotesByUserAndPostId(Long userId, Long postId) {
        return voteRepository.findVotesByUserAndPostId(userId, postId);
    }

    @Transactional
    public Vote changeVoteByUserAndPostId(Long postId, Long userId, String vote) {
        Vote votesByUserAndPostId = voteRepository.findVotesByUserAndPostId(userId, postId);
        if (votesByUserAndPostId != null &&
                (vote.equals("upVote") || vote.equals("downVote"))) {
            int downVote = vote.equals("downVote") ? 1 : 0;
            int upVote = vote.equals("upVote") ? 1 : 0;
            votesByUserAndPostId.setDownVotes(downVote);
            votesByUserAndPostId.setUpVotes(upVote);
        }
        return votesByUserAndPostId;
    }

    @Transactional
    public Vote save(Long postId, Long userId, String vote) {
        Post postById = postRepository.findById(postId).orElse(null);
        User userById = userRepository.findById(userId).orElse(null);
        Vote votesByUserAndPostId = voteRepository.findVotesByUserAndPostId(userId, postId);
        Vote newVote = null;
        if (postById != null &&
                userById != null &&
                votesByUserAndPostId == null &&
                (vote.equals("upVote") || vote.equals("downVote"))) {
            newVote = new Vote();
            newVote.setUpVotes(vote.equals("upVote") ? 1 : 0);
            newVote.setDownVotes(vote.equals("downVote") ? 1 : 0);
            newVote.setAuthor(userById);
            newVote.setPost(postById);
            voteRepository.save(newVote);
        }
        return newVote;
    }

    public List<Vote> findVoteByTopicId(Long topicID) {
        return voteRepository.findVoteByTopicId(topicID);
    }

    @Transactional
    public void removeVoteById(Long userId, Long postId) {
        Vote votesByUserAndPostId = voteRepository.findVotesByUserAndPostId(userId, postId);
        if (votesByUserAndPostId != null) {
            voteRepository.removeVoteById(votesByUserAndPostId.getId());
        }
    }

    public List<Vote> findVotesByPostId(Long postId) {
        return voteRepository.findVotesByPostId(postId);
    }

    public Map<String, Long> getAllVotes(Long postId) {
        return voteRepository.findVotesByPostId(postId).stream()
                .collect(Collectors.toMap(x -> x.getUpVotes() == 1 ? "upVotes" : "downVotes", n -> 1L, Long::sum));
    }
}
