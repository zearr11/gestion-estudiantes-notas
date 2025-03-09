package pe.com.edugestor.edugestor.services;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.User;
import pe.com.edugestor.edugestor.repositories.PersonRepository;

@Service
public class PersonService {
    
    private PersonRepository personRepository;

    public PersonService(PersonRepository repository){
        this.personRepository = repository;
    }

    // Obtener todos los registros de persona
    public List<Person> listAllPerson(){
        return this.personRepository.findAll();
    }

    // Obtener el registro de una persona por id
    public Person getPersonById(Long id){
        return this.personRepository.findById(id).orElse(null);
    }

    public Person getPersonByUser(User user){
        return this.personRepository.findByUser(user).orElse(null);
    }

    public Person createPerson(Person entity){
        return this.personRepository.saveAndFlush(entity);
    }

    public Person updatePerson(Person entity){

        Person personToUpdate = this.personRepository.findById(entity.getIdPerson()).orElse(null);

        if (personToUpdate == null)
            return null;

        personToUpdate.setAddress(entity.getAddress());
        personToUpdate.setDateBirth(entity.getDateBirth());
        personToUpdate.setEmail(entity.getEmail());
        personToUpdate.setGender(entity.getGender());
        personToUpdate.setLastname(entity.getLastname());
        personToUpdate.setName(entity.getName());
        personToUpdate.setTypeNid(entity.getTypeNid());
        personToUpdate.setNid(entity.getNid());
        personToUpdate.setNumberPhone(entity.getNumberPhone());
        personToUpdate.setProfessor(entity.getProfessor());
        
        return this.personRepository.saveAndFlush(personToUpdate);
    }
}
