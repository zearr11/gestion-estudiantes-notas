package pe.com.edugestor.edugestor.services;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.Student;
import pe.com.edugestor.edugestor.repositories.StudentRepository;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    public StudentService(StudentRepository repositoryStudent){
        this.studentRepository = repositoryStudent;
    }

    public List<Student> listAllStudents(){
        return this.studentRepository.findAll();
    }

    public Student createStudent(Student entity){
        return this.studentRepository.saveAndFlush(entity);
    }

    public Student getStudentByID(Long id){
        return this.studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Student entity){
        Student studentToUpdate = this.studentRepository.findById(entity.getIdStudent()).orElse(null);

        if (studentToUpdate == null)
            return null;

        studentToUpdate.setPerson(entity.getPerson());
        studentToUpdate.setProfessor(entity.getProfessor());

        return this.studentRepository.saveAndFlush(studentToUpdate);
    }

    public List<Student> listLastSixStudents() {
        return studentRepository.findTop6Students();
    }

    public Student getStudentByPerson(Person person) {
        return this.studentRepository.findByPerson(person).orElse(null);
    }
}
