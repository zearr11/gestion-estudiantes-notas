package pe.com.edugestor.edugestor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.models.Student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.com.edugestor.edugestor.services.ProfessorService;
import pe.com.edugestor.edugestor.services.StudentService;



@Controller
public class AuthController {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public String goLoginView(@RequestParam(value = "error", required = false) String error, Model model) {

        if (error != null) {
            model.addAttribute("loginError", true);
        }
        
        return "login";
    }

    @GetMapping("/")
    public String goMenuView(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();

        if ("ROLE_Admin".equals(rol)) { 
            List<Professor> lstProfessor = professorService.listLastSixProfessor();
            List<Student> lstStudents = studentService.listLastSixStudents();
            model.addAttribute("professors", lstProfessor);
            model.addAttribute("students", lstStudents);
            return "admin/main-menu";
        } 
        else if ("ROLE_Profesor".equals(rol)) {
            return "professor/main-menu";
        } 
        else {
            return "student/main-menu";
        }
        
    }
    
}
