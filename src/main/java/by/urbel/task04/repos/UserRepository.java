package by.urbel.task04.repos;

import by.urbel.task04.entity.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDTO,Long> {
    Optional<UserDTO> findUserByEmail(String email);
}
