package pe.com.edugestor.edugestor.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.edugestor.edugestor.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCodUser(String codUser);
}
