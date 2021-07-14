package forum.services;

import forum.entities.Post;
import forum.entities.Vote;
import forum.repositories.PostRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post findPostById(Long postId) {
        Post post = null;
        Optional<Post> byId = postRepository.findById(postId);
        if (byId.isPresent()) {
            post = byId.get();
            Hibernate.initialize(post.getAuthor());
            Hibernate.initialize(post.getVotes());
        }
        return post;
    }

    @Transactional
    public List<Post> findPostsByTopicId(Long topicId) {
        List<Post> postsByTopicId = postRepository.findPostsByTopicId(topicId);
        for (Post post : postsByTopicId) {
            Hibernate.initialize(post.getAuthor());
            Hibernate.initialize(post.getVotes());
            for (Vote vote : post.getVotes()) {
                Hibernate.initialize(vote.getAuthor());
            }
        }
        return postsByTopicId;
    }

    @Transactional
    public Post save(Post newPost) {
        return postRepository.save(newPost);
    }

    public List<Post> findPostsByUserId(Long userId) {
        return postRepository.findPostsByUserId(userId);
    }

    @Transactional
    public void removePostById(Long postId) {
        postRepository.removePostById(postId);
    }

    @Transactional
    public void updatePostById(Long postId, String newText) {
        postRepository.updatePostById(postId, newText);
    }
}