package pe.com.edugestor.edugestor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.edugestor.edugestor.models.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
}
