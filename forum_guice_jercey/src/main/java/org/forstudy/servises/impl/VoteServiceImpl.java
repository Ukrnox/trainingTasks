package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.dto.AllPostVotesDTO;
import org.forstudy.entities.*;
import org.forstudy.exceptionhandling.AppException;
import org.forstudy.servises.ValidationService;
import org.forstudy.servises.VoteService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteServiceImpl implements VoteService {

    private final EntityManager entityManager;
    private final ValidationService validationService;

    @Inject
    public VoteServiceImpl(EntityManager entityManager, ValidationService validationService) {
        this.entityManager = entityManager;
        this.validationService = validationService;
    }

    @Override
    public AllPostVotesDTO getAllVotes(String postId, String link) throws AppException {
        validationService.idValidation(postId, link);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = cb.createQuery(Vote.class);
        Root<Vote> e = query.from(Vote.class);
        query.where(cb.equal(e.get("post"), Long.parseLong(postId)));
        List<Vote> votesList = entityManager.createQuery(query).getResultList();
        Map<String, Long> voteCollectionByPostId = votesList.stream()
                .collect(Collectors.toMap(x -> x.getUpVotes() == 1 ? "upVotes" : "downVotes", n -> 1L, Long::sum));
        return new AllPostVotesDTO(voteCollectionByPostId);
    }

    @Override
    @Transactional
    public Vote save(String postId, String userId, String vote, String link) throws AppException {
        validationService.idValidation(postId, link);
        validationService.idValidation(userId, link);
        long longUserId = Long.parseLong(userId);
        long longPostId = Long.parseLong(postId);
        User user = entityManager.find(User.class, longUserId);
        Post post = entityManager.find(Post.class, longPostId);
        Vote newVote = null;
        if (user != null && post != null &&
                (vote.equals("upVote") || vote.equals("downVote"))) {
            if (findVoteByUserAndPostId(longPostId, longUserId, link) == null) {
                newVote = new Vote();
                newVote.setUpVotes(vote.equals("upVote") ? 1 : 0);
                newVote.setDownVotes(vote.equals("downVote") ? 1 : 0);
                newVote.setAuthor(user);
                newVote.setPost(post);
                entityManager.persist(newVote);
            }
        }
        return newVote;
    }

    @Transactional
    private Vote findVoteByUserAndPostId(long longPostId, long longUserId, String link) throws AppException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = cb.createQuery(Vote.class);
        Root<Vote> root = query.from(Vote.class);
        query.where(getFinalPredForVoteByUserAndPostId(cb, root, longPostId, longUserId));
//        Vote vote = entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
//        if (vote == null) {
//            throw new AppException(400, "AppException",
//                    "No vote with user id " + longUserId + " and post id " + longPostId, link);
//
//        }
        return entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    private Predicate getFinalPredForVoteByUserAndPostId(CriteriaBuilder cb, Root<Vote> root, long longPostId, long longUserId) {
        Predicate predicateForUserId
                = cb.equal(root.get("author"), longUserId);
        Predicate predicateForPostId
                = cb.equal(root.get("post"), longPostId);
        return cb.and(predicateForPostId, predicateForUserId);
    }

    @Override
    @Transactional
    public void removeVoteById(String postId, String userId, String link) throws AppException {
        validationService.idValidation(postId, link);
        validationService.idValidation(userId, link);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Vote> delete = criteriaBuilder.
                createCriteriaDelete(Vote.class);
        Root<Vote> root = delete.from(Vote.class);
        delete.where(getFinalPredForVoteByUserAndPostId(criteriaBuilder, root, Long.parseLong(postId), Long.parseLong(userId)));
        if (entityManager.createQuery(delete).executeUpdate() == 0) {
            throw new AppException(400, "AppException",
                    "No vote with user id " + userId + " and post id " + postId, link);
        }
    }

    @Override
    @Transactional
    public Vote changeVoteByUserAndPostId(String postId, String userId, String vote, String link) throws AppException {
        validationService.idValidation(postId, link);
        validationService.idValidation(userId, link);
        Vote voteByUserAndPostId = findVoteByUserAndPostId(Long.parseLong(postId), Long.parseLong(userId), link);
        if (voteByUserAndPostId == null) {
            throw new AppException(400, "AppException",
                    "No vote with user id " + userId + " and post id " + postId, link);
        }
        voteByUserAndPostId.setUpVotes(vote.equals("upVote") ? 1 : 0);
        voteByUserAndPostId.setDownVotes(vote.equals("downVote") ? 1 : 0);
        return voteByUserAndPostId;
    }
}
