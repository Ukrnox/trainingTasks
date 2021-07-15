package forum.repositories;

import forum.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query(value = "SELECT * FROM topics WHERE group_id = :groupId", nativeQuery = true)
    List<Topic> findTopicByGroupId(@Param("groupId") Long groupId);

    @Query(value = "SELECT * FROM topics WHERE user_id = :userId", nativeQuery = true)
    List<Topic> findTopicByUserId(@Param("userId") Long groupId);

    @Modifying
    @Query(value = "DELETE FROM topics WHERE id = :topicId", nativeQuery = true)
    void removeTopicById(@Param("topicId") Long topicId);
}