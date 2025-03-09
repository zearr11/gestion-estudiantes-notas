package pe.com.edugestor.edugestor.repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.User;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUser(User user);
}
