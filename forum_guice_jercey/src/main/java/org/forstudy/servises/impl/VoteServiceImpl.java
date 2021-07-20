package org.forstudy.servises.impl;

import com.google.inject.persist.Transactional;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.forstudy.entities.*;
import org.forstudy.servises.VoteService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteServiceImpl implements VoteService {

    private final EntityManager entityManager;

    @Inject
    public VoteServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getAllVotes(long longPostId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = cb.createQuery(Vote.class);
        Root<Vote> e = query.from(Vote.class);
        query.where(cb.equal(e.get("post"), longPostId));
        List<Vote> votesList = entityManager.createQuery(query).getResultList();
        Map<String, Long> voteCollectionByPostId = votesList.stream()
                .collect(Collectors.toMap(x -> x.getUpVotes() == 1 ? "upVotes" : "downVotes", n -> 1L, Long::sum));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("upVotes", voteCollectionByPostId.get("upVotes"));
            jsonObject.put("downVotes", voteCollectionByPostId.get("downVotes"));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    @Transactional
    public String save(long longPostId, long longUserId, String vote) {
        User user = entityManager.find(User.class, longUserId);
        Post post = entityManager.find(Post.class, longPostId);
        Vote newVote = null;
        if (user != null && post != null &&
                (vote.equals("upVote") || vote.equals("downVote"))) {
            if (findVoteByUserAndPostId(longPostId, longUserId) == null) {
                newVote = new Vote();
                newVote.setUpVotes(vote.equals("upVote") ? 1 : 0);
                newVote.setDownVotes(vote.equals("downVote") ? 1 : 0);
                newVote.setAuthor(user);
                newVote.setPost(post);
                entityManager.persist(newVote);
            }
        }
        String result;
        try {
            result = makeVoteJson(newVote).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            result = "JSONException";
        }
        return result;
    }

    private JSONObject makeVoteJson(Vote vote) throws JSONException {
        JSONObject voteJSON = new JSONObject();
        if (vote != null) {
            voteJSON.put("id", vote.getId());
            voteJSON.put("upVote", vote.getUpVotes());
            voteJSON.put("downVote", vote.getDownVotes());
        }
        return voteJSON;
    }

    @Transactional
    private Vote findVoteByUserAndPostId(long longPostId, long longUserId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vote> query = cb.createQuery(Vote.class);
        Root<Vote> root = query.from(Vote.class);
        Predicate predicateForPostId
                = cb.equal(root.get("author"), longUserId);
        Predicate predicateForUserId
                = cb.equal(root.get("post"), longPostId);
        Predicate finalPredicate
                = cb.and(predicateForPostId, predicateForUserId);
        query.where(finalPredicate);
        return entityManager.createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void removeVoteById(long longPostId, long longUserId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Vote> delete = criteriaBuilder.
                createCriteriaDelete(Vote.class);
        Root<Vote> root = delete.from(Vote.class);
        Predicate predicateForPostId
                = criteriaBuilder.equal(root.get("author"), longUserId);
        Predicate predicateForUserId
                = criteriaBuilder.equal(root.get("post"), longPostId);
        Predicate finalPredicate
                = criteriaBuilder.and(predicateForPostId, predicateForUserId);
        delete.where(finalPredicate);
        entityManager.createQuery(delete).executeUpdate();
    }

    @Override
    @Transactional
    public String changeVoteByUserAndPostId(long longPostID, long longUserId, String vote) {
        Vote voteByUserAndPostId = findVoteByUserAndPostId(longPostID, longUserId);
        voteByUserAndPostId.setUpVotes(vote.equals("upVote") ? 1 : 0);
        voteByUserAndPostId.setDownVotes(vote.equals("downVote") ? 1 : 0);
        String result = null;
        try {
            result = makeVoteJson(voteByUserAndPostId).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
