package pe.com.edugestor.edugestor.services;

import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.repositories.ProfessorRepository;

@Service
public class ProfessorService {

    private ProfessorRepository professorRepository;
    
    public ProfessorService(ProfessorRepository repository){
        this.professorRepository = repository;
    }

    // Guarda la entidad en la bd
    public Professor createProfessor(Professor entity){
        return this.professorRepository.saveAndFlush(entity);
    }

}
