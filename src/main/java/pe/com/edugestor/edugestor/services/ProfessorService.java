package pe.com.edugestor.edugestor.services;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.repositories.ProfessorRepository;

@Service
public class ProfessorService {

    private ProfessorRepository professorRepository;
    
    public ProfessorService(ProfessorRepository repository){
        this.professorRepository = repository;
    }

    // Lista los elementos de la bd
    public List<Professor> listAll(){
        return professorRepository.findAll();
    }

    // Guarda la entidad en la bd
    public Professor createProfessor(Professor entity){
        return this.professorRepository.saveAndFlush(entity);
    }

    // Encuentra la entidad en base al id
    public Professor getProfessorByID(Long id){
        return this.professorRepository.findById(id).orElse(null);
    }

    // Actualiza un registro de la bd
    public Professor updateProfessor(Professor entity){
        
        Professor professorToUpdate = this.professorRepository.findById(entity.getId()).orElse(null);

        if (professorToUpdate == null) 
            return null;

        professorToUpdate.setName(entity.getName());
        professorToUpdate.setLastname(entity.getLastname());
        professorToUpdate.setAddress(entity.getAddress());
        professorToUpdate.setDateBirth(entity.getDateBirth());
        professorToUpdate.setEmail(entity.getEmail());
        professorToUpdate.setGender(entity.getGender());
        professorToUpdate.setNid(entity.getNid());
        professorToUpdate.setNumberPhone(entity.getNumberPhone());
        professorToUpdate.setSpecialty(entity.getSpecialty());

        return this.professorRepository.saveAndFlush(professorToUpdate);
    }
    
    public List<Professor> listLast5() {
        return professorRepository.findTop5ByOrderByIdDesc();
    }

}
