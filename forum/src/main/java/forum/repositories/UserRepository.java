package forum.repositories;

import forum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "UPDATE users SET user_login = :newLogin WHERE id = :userId", nativeQuery = true)
    void changeUserLogin(@Param("newLogin") String newLogin, @Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE users SET password = :newPassword WHERE id = :userId", nativeQuery = true)
    void changeUserPassword(@Param("newPassword") String newPassword, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM users WHERE id = :userId", nativeQuery = true)
    User findUserByUserId(@Param("userId") Long userId);
}