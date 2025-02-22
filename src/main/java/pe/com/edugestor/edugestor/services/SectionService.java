package pe.com.edugestor.edugestor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.Section;
import pe.com.edugestor.edugestor.repositories.SectionRepository;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> listAllSection(){
        return this.sectionRepository.findAll();
    }

    public Section createSection(Section entity){
        return this.sectionRepository.saveAndFlush(entity);
    }

    public Section getSectiontByID(Long id){
        return this.sectionRepository.findById(id).orElse(null);
    }

    public Section updateSection(Section entity){

        Section sectionToSave = this.sectionRepository.findById(entity.getIdSection()).orElse(null);

        if (sectionToSave == null)
            return null;
        
        sectionToSave.setDay(entity.getDay());
        sectionToSave.setHourEnd(entity.getHourEnd());
        sectionToSave.setHourStart(entity.getHourStart());
        sectionToSave.setState(entity.getState());
        sectionToSave.setCourse(entity.getCourse());

        return this.sectionRepository.saveAndFlush(sectionToSave);

    }
}
