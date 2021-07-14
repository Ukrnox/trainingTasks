package forum.repositories;

import forum.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Modifying
    @Query(value = "UPDATE groups SET group_name = :newTitle WHERE id = :groupId", nativeQuery = true)
    void changeGroupTitle(@Param("newTitle") String newTitle, @Param("groupId") Long groupId);
}
