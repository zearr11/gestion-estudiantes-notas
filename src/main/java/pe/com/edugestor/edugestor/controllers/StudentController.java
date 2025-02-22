package pe.com.edugestor.edugestor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.models.Student;
import pe.com.edugestor.edugestor.models.User;
import pe.com.edugestor.edugestor.services.PersonService;
import pe.com.edugestor.edugestor.services.ProfessorService;
import pe.com.edugestor.edugestor.services.StudentService;
import pe.com.edugestor.edugestor.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/estudiantes")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private PersonService personService;
    @Autowired
    private UserService userService;

    private Student studentDefault = new Student(null, null, null, null);
    private Student studentSelected;
    private Person dataPersonStudent;

    @GetMapping()
    public String goViewStudents(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Professor professorLogIn = professorService.getProfessorLog(userDetails.getUsername());

        List<Student> studentsProff = new ArrayList<>();

        for (Student student : studentService.listAllStudents()) {
            for (int i = 0; student.getProfessor().size() > i; i++){
                if (student.getProfessor().get(i).getIdProfessor() == professorLogIn.getIdProfessor()){
                    studentsProff.add(student);
                    break;
                }
            }
        }

        model.addAttribute("students", studentsProff);
        return "professor/student-list";
    }

    @GetMapping("/nuevo")
    public String goViewNewStudent(Model model) {
        model.addAttribute("student", studentDefault);
        return "professor/student-add";
    }
    
    @PostMapping("/guardar")
    public String saveNewStudent(@ModelAttribute Student studentToSave) {

        // Obtener el Profesor que se encuentra logeado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User userStudent = new User(null, "E" + studentToSave.getPerson().getNid(), "E" + studentToSave.getPerson().getNid(), 
            "Estudiante", "Activo", null);
        studentToSave.getPerson().setUser(userStudent);

        // (Se agrega a person como lista en studenttosave)
        List<Person> person = new ArrayList<>(); person.add(studentToSave.getPerson());
        studentToSave.getPerson().getUser().setPersons(person);
        
        // Se agrega al profesor
        Professor professorLogIn = professorService.getProfessorLog(userDetails.getUsername());
        List<Professor> professors = new ArrayList<>();
        professors.add(professorLogIn);
        studentToSave.setProfessor(professors);

        // Se guarda en la bd
        userService.createUser(studentToSave.getPerson().getUser());
        personService.createPerson(studentToSave.getPerson());
        studentService.createStudent(studentToSave);

        return "redirect:/estudiantes";
    }

    @GetMapping("/editar/{id}")
    public String goViewEditStudent(@PathVariable Long id, Model model) {
        studentSelected = studentService.getStudentByID(id);

        if (studentSelected == null)
            return "redirect:/estudiantes";

        dataPersonStudent = studentSelected.getPerson();
        model.addAttribute("student", studentSelected);
        return "professor/student-edit";
    }

    @PostMapping("/actualizar")
    public String updateStudent(@ModelAttribute Student studentDataPerson) {

        Student studentToUpdate = studentSelected;

        studentToUpdate.setPerson(studentDataPerson.getPerson());
        studentToUpdate.getPerson().setIdPerson(dataPersonStudent.getIdPerson());

        studentToUpdate.getPerson().setUser(dataPersonStudent.getUser());

        studentToUpdate.getPerson().getUser().setCodUser("E" + studentDataPerson.getPerson().getNid());
        
        userService.updateUser(studentToUpdate.getPerson().getUser());
        personService.updatePerson(studentToUpdate.getPerson());
        studentService.updateStudent(studentToUpdate);
        
        return "redirect:/estudiantes";
    }
    
    
    
    
}
