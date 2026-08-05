package sdmaker.ai.ai.Repos;



import org.springframework.data.jpa.repository.JpaRepository;
import sdmaker.ai.ai.Entites.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
