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
import pe.com.edugestor.edugestor.models.Course;
import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.models.Section;
import pe.com.edugestor.edugestor.models.Student;
import pe.com.edugestor.edugestor.models.User;
import pe.com.edugestor.edugestor.services.CourseService;
import pe.com.edugestor.edugestor.services.ProfessorService;
import pe.com.edugestor.edugestor.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/cursos")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private UserService userService;

    private Course courseDefault = new Course(null, null, null, null, null);

    @GetMapping()
    public String goViewCourses(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        String ruta;

        if (rol.equals("ROLE_Profesor")){
            Professor professorLogIn = professorService.getProfessorLog(userDetails.getUsername());
            List<Course> courses = new ArrayList<>();
            for (Course course : this.courseService.listAllCourses()) {
                if (course.getProfessor().getIdProfessor().equals(professorLogIn.getIdProfessor())){
                    courses.add(course);
                }
            }
            model.addAttribute("courses", courses);
            model.addAttribute("course", courseDefault);
            ruta = "professor/course";
        }
        else {
            User userLogIn = this.userService.getUserByCod(userDetails.getUsername());
            Student studentLogIn = userLogIn.getPersons().get(0).getStudents().get(0);
            List<Section> sections = new ArrayList<>();
            for (Section section : studentLogIn.getSections()) {
                sections.add(section);
            }
            model.addAttribute("sections", sections);
            ruta = "student/courses";
        }

        return ruta;
    }

    @PostMapping("/nuevo")
    public String saveCourse(@ModelAttribute Course courseToSave) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Professor professorLogIn = professorService.getProfessorLog(userDetails.getUsername());
        courseToSave.setProfessor(professorLogIn);

        courseService.createCourse(courseToSave);

        return "redirect:/cursos";
    }
    
    
}
