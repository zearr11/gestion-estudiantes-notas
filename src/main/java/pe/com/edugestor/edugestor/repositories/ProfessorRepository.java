package pe.com.edugestor.edugestor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.edugestor.edugestor.models.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findTop5ByOrderByIdDesc();
}
