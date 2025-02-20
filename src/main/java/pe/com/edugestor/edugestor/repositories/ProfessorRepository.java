package pe.com.edugestor.edugestor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.com.edugestor.edugestor.models.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    //List<Professor> findTop5ByOrderByIdDesc();
    @Query(value = "SELECT * FROM professor ORDER BY id_professor DESC LIMIT 6", nativeQuery = true)
    List<Professor> findTop6Professors();

    @Query("SELECT p FROM Professor p " +
       "JOIN p.person e " + 
       "JOIN e.user u " + 
       "WHERE u.codUser = :codUser")
    Professor getProfessorLogIn(@Param("codUser") String codUser);
}
