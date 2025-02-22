package pe.com.edugestor.edugestor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.edugestor.edugestor.models.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    
}
