package pl.balutek.authorization;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByLoginAndPasswordHash(String login, String passwordHash);
}
