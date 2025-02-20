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
    
    // Lista los elementos de profesor
    public List<Professor> listAllProfesor(){
        return this.professorRepository.findAll();
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
        
        Professor professorToUpdate = this.professorRepository.findById(entity.getIdProfessor()).orElse(null);

        if (professorToUpdate == null) 
            return null;

        professorToUpdate.setPerson(entity.getPerson());
        professorToUpdate.setSpecialty(entity.getSpecialty());

        return this.professorRepository.saveAndFlush(professorToUpdate);
    }
    
    // Listar Ultimos 5 Profesores registrados
    public List<Professor> listLastSixProfessor() {
        return professorRepository.findTop6Professors();
    }

    // Obtener profesor que se encuentra logeado por codigo de usuario
    public Professor getProfessorLog(String codUser){
        return professorRepository.getProfessorLogIn(codUser);
    }
    
}
