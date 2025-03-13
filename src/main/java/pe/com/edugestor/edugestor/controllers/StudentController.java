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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    private Student studentDefault = new Student(null, null, null, null, null);
    private Student studentSelected;
    private Person dataPersonStudent;

    @GetMapping()
    public String goViewStudents(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Professor professorLogIn = professorService.getProfessorLog(userDetails.getUsername());
        List<Student> studentsProffActives = new ArrayList<>();
        List<Student> studentsInactives = new ArrayList<>();
        for (Student student : studentService.listAllStudents()) {
            for (int i = 0; student.getProfessor().size() > i; i++){
                if (student.getProfessor().get(i).getIdProfessor() == professorLogIn.getIdProfessor()){
                    if (student.getPerson().getUser().getState().equals("Activo")){
                        studentsProffActives.add(student);
                    }
                    else{
                        studentsInactives.add(student);
                    }
                    break;
                }
            }
        }
        model.addAttribute("students", studentsProffActives);
        model.addAttribute("studentInactives", studentsInactives);
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
    
    
    @PostMapping("/cambiar-estado")
    public String changeState(@RequestParam("idStudentChange") Long idStudent) {
        System.out.println(idStudent);
        Student studentToChange = this.studentService.getStudentByID(idStudent);
        if (studentToChange.getPerson().getUser().getState().equals("Activo")){
            studentToChange.getPerson().getUser().setState("Inactivo");
        }
        else{
            studentToChange.getPerson().getUser().setState("Activo");
        }
        this.studentService.updateStudent(studentToChange);
        return "redirect:/estudiantes";
    }
    
    @GetMapping("/agregar-registrado")
    public String goViewAddStudentRegistered(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Professor professor = this.professorService.getProfessorLog(userDetails.getUsername());

        List<String> codUserStudent = new ArrayList<>();

        for (Student student : professor.getStudent()) {
            codUserStudent.add(student.getPerson().getUser().getCodUser());
        }

        model.addAttribute("codUserStudents", codUserStudent);

        return "professor/add-student-reg";
    }
    
    @PostMapping("/agregar-estudiante-registrado")
    public String addStudentRegistered(@RequestParam String codUser, RedirectAttributes redirect) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Professor professor = this.professorService.getProfessorLog(userDetails.getUsername());
        List<Student> studentsOfProfessor = professor.getStudent();
        Boolean studentExist = false;

        User userStudent = this.userService.getUserByCod(codUser);
        
        if (userStudent != null) { // Es un estudiante valido
            
            Person personStudent = this.personService.getPersonByUser(userStudent);
            Student studentData = this.studentService.getStudentByPerson(personStudent);

            for (Student studentProf : studentsOfProfessor) { // Verifica que el estudiante no tenga relacion con profesor
            
                if (studentProf.getIdStudent().equals(studentData.getIdStudent())) {
                    studentExist = true;
                    break;
                }

            }

            if (!studentExist) { // Estudiante no tiene relacion con profesor
                List<Professor> professorsToStudent = studentData.getProfessor();
                professorsToStudent.add(professor);
                studentData.setProfessor(professorsToStudent);
                
                this.studentService.updateStudent(studentData);
            }
            else { // Estudiante si tiene relacion con profesor
                redirect.addFlashAttribute("msgError", "El usuario ya se encuentra en el listado.");
                return "redirect:/estudiantes/agregar-registrado";
            }

        }
        else {
            redirect.addFlashAttribute("msgError", "El estudiante ingresado no se encuentra registrado");
            return "redirect:/estudiantes/agregar-registrado";
        }

        return "redirect:/estudiantes";
    }
    
    
}
