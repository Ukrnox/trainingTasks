package forum.services;

import forum.entities.Post;
import forum.entities.Topic;
import forum.entities.User;
import forum.entities.Vote;
import forum.repositories.PostRepository;
import forum.repositories.TopicRepository;
import forum.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class PostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, TopicRepository topicRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public Post findPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
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
            Set<Vote> votes = post.getVotes();
            for (Vote vote : votes) {
                Hibernate.initialize(vote.getAuthor());
            }
        }
        return postsByTopicId;
    }

    @Transactional
    public Post save(Long userId, Long topicId, String newPostText) {
        Post newPostCreator = null;
        User user = userRepository.findById(userId).orElse(null);
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if (user != null && topic != null) {
            newPostCreator = new Post();
            newPostCreator.setAuthor(user);
            newPostCreator.setTopic(topic);
            newPostCreator.setText(newPostText);
            newPostCreator.setDateCreated(LocalDateTime.now());
            postRepository.save(newPostCreator);
        }
        return newPostCreator;
    }

    public List<Post> findPostsByUserId(Long userId) {
        return postRepository.findPostsByUserId(userId);
    }

    @Transactional
    public void removePostById(Long postId) {
        postRepository.removePostById(postId);
    }

    @Transactional
    public Post updatePostById(Long postId, String newText) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post!=null)
        {
            post.setText(newText);
        }
        return post;
    }
}