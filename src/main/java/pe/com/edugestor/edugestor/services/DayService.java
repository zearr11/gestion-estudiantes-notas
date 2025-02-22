package pe.com.edugestor.edugestor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.Day;
import pe.com.edugestor.edugestor.repositories.DayRepository;

@Service
public class DayService {
    
    @Autowired
    private DayRepository dayRepository;

    public Day getDayById(Long id){
        return this.dayRepository.findById(id).orElse(null);
    }
}
