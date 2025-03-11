package pe.com.edugestor.edugestor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.edugestor.edugestor.models.UploadExam;

@Repository
public interface UploadExamRepository extends JpaRepository<UploadExam, Long> {
    
}
