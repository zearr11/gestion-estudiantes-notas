package pe.com.edugestor.edugestor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.edugestor.edugestor.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT * FROM student ORDER BY id_student DESC LIMIT 6", nativeQuery = true)
    List<Student> findTop6Students();

}
