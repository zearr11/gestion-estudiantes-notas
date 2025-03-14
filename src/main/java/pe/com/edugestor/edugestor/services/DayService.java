package pe.com.edugestor.edugestor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.Day;
import pe.com.edugestor.edugestor.repositories.ArchiveExamRepository;
import pe.com.edugestor.edugestor.repositories.DayRepository;

@Service
public class DayService {

    private final ArchiveExamRepository archiveExamRepository;
    
    @Autowired
    private DayRepository dayRepository;

    DayService(ArchiveExamRepository archiveExamRepository) {
        this.archiveExamRepository = archiveExamRepository;
    }

    public Day getDayById(Long id){
        return this.dayRepository.findById(id).orElse(null);
    }

    public List<Day> findAll() {
        return this.dayRepository.findAll();
    }
}
