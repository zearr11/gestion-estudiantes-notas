package pe.com.edugestor.edugestor.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import pe.com.edugestor.edugestor.models.Course;
import pe.com.edugestor.edugestor.models.Day;
import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.models.Section;
import pe.com.edugestor.edugestor.models.Student;
import pe.com.edugestor.edugestor.repositories.ArchiveExamRepository;
import pe.com.edugestor.edugestor.repositories.ArchiveRepository;
import pe.com.edugestor.edugestor.services.ArchiveExamService;
import pe.com.edugestor.edugestor.services.ArchiveService;
import pe.com.edugestor.edugestor.services.DayService;
import pe.com.edugestor.edugestor.services.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.com.edugestor.edugestor.services.ProfessorService;
import pe.com.edugestor.edugestor.services.StudentService;
import pe.com.edugestor.edugestor.services.UserService;


@Controller
public class AuthController {

    private final CourseController courseController;

    private final ArchiveService archiveService;

    private final ArchiveRepository archiveRepository;

    private final PersonService personService;

    private final ArchiveExamService archiveExamService;

    private final ArchiveExamRepository archiveExamRepository;

    @Autowired
    private DayService dayService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    AuthController(ArchiveExamRepository archiveExamRepository, ArchiveExamService archiveExamService, PersonService personService, ArchiveRepository archiveRepository, ArchiveService archiveService, CourseController courseController) {
        this.archiveExamRepository = archiveExamRepository;
        this.archiveExamService = archiveExamService;
        this.personService = personService;
        this.archiveRepository = archiveRepository;
        this.archiveService = archiveService;
        this.courseController = courseController;
    }

    @GetMapping("/login")
    public String goLoginView(@RequestParam(value = "error", required = false) String error, Model model) {

        if ("credenciales_invalidas".equals(error)) {
            model.addAttribute("loginError", "Usuario y/o contraseña inválidos.");
        }

        return "public/login";
    }

    @GetMapping("/")
    public String goMenuView(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();

        String storedPassword = userService.getUserByCod(userDetails.getUsername()).getPassword();
        String username = userDetails.getUsername();

        if (passwordEncoder.matches(username, storedPassword)){
            return "public/logged/userlog-change-pass";
        }
        else {
            if ("ROLE_Admin".equals(rol)) { 
                List<Professor> lstProfessor = professorService.listLastSixProfessor();
                List<Student> lstStudents = studentService.listLastSixStudents();
                model.addAttribute("professors", lstProfessor);
                model.addAttribute("students", lstStudents);
                return "admin/main-menu";
            } 
            else if ("ROLE_Profesor".equals(rol)) {
                model.addAttribute("sectionsByDay", loadMenu(username));
                return "professor/main-menu";
            } 
            else {
                model.addAttribute("sectionsByDay", loadMenu(username));
                return "student/main-menu";
            }
        }
        
    }

    private Map<String, List<Section>> loadMenu(String username) {
        Map<String, List<Section>> sectionsByDay = new LinkedHashMap<>();
        if (username.charAt(0) == 'D'){ // Docente
            
            Professor professor = this.professorService.getProfessorLog(username);

            if (professor != null) {
                List<Section> lstSections = new ArrayList<>();
                
                for (Course course : professor.getCourses()) {
                    List<Section> sectionsCourse = course.getSections();
                    for (Section sectionElement : sectionsCourse) {
                        lstSections.add(sectionElement);
                    }
                }

                List<Day> daysAll = this.dayService.findAll();
                for (Day dayWeek : daysAll) { // Itera sobre todos los días de la semana
                    List<Section> sectionsForDay = new ArrayList<>();

                    for (Section section : lstSections) { // Itera sobre todas las secciones del profesor
                        List<Day> sectionDays = section.getDay(); // Días en los que se imparte la sección

                        for (Day sectionDay : sectionDays) { // Itera sobre los días de la sección
                            if (dayWeek.getIdDay().equals(sectionDay.getIdDay())) { // Compara si el día coincide
                                sectionsForDay.add(section);
                            }
                        }
                    }
                    if (!sectionsForDay.isEmpty()) {
                        sectionsByDay.put(dayWeek.getNameDay(), sectionsForDay);
                    }
                }
            }

        }
        else { // Estudiante
            
            Student student = this.studentService.
                                getStudentByPerson(this.personService.
                                getPersonByUser(this.userService.
                                getUserByCod(username)));

            if (student != null) {
                List<Section> lstSections = student.getSections();
                List<Day> daysAll = this.dayService.findAll();
                for (Day dayWeek : daysAll) { // Itera sobre todos los días de la semana
                    List<Section> sectionsForDay = new ArrayList<>();

                    for (Section section : lstSections) { // Itera sobre todas las secciones del estudiante
                        List<Day> sectionDays = section.getDay(); // Días en los que se imparte la sección

                        for (Day sectionDay : sectionDays) { // Itera sobre los días de la sección
                            if (dayWeek.getIdDay().equals(sectionDay.getIdDay())) { // Compara si el día coincide
                                sectionsForDay.add(section);
                            }
                        }
                    }
                    if (!sectionsForDay.isEmpty()) {
                        sectionsByDay.put(dayWeek.getNameDay(), sectionsForDay);
                    }
                }
            }
        }
        return sectionsByDay;
    }
}
