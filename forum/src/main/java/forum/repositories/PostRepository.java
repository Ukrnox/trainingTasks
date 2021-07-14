package forum.repositories;

import forum.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT * FROM posts WHERE topic_id = :topicId", nativeQuery = true)
    List<Post> findPostsByTopicId(@Param("topicId") Long topicId);

    @Query(value = "SELECT * FROM posts WHERE user_id = :user_id", nativeQuery = true)
    List<Post> findPostsByUserId(@Param("user_id") Long userId);

    @Modifying
    @Query(value = "DELETE FROM posts WHERE id = :postId", nativeQuery = true)
    void removePostById(@Param("postId") Long postId);

    @Modifying
    @Query(value = "UPDATE posts SET text = :newText WHERE id = :postId", nativeQuery = true)
    void updatePostById(@Param("postId") Long postId, @Param("newText") String newText);
}
