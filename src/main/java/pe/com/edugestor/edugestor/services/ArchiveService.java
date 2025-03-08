package pe.com.edugestor.edugestor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.edugestor.edugestor.models.Archive;
import pe.com.edugestor.edugestor.repositories.ArchiveRepository;

@Service
public class ArchiveService {
    
    @Autowired
    private ArchiveRepository archiveRepository;

    public Archive saveToArchive(Archive archiveToSave) {
        return archiveRepository.saveAndFlush(archiveToSave);
    }

    public Archive getArchiveByID(Long id) {
        return archiveRepository.findById(id).orElse(null);
    }
}
