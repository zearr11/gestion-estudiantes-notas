package pe.com.edugestor.edugestor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import pe.com.edugestor.edugestor.models.Professor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.com.edugestor.edugestor.services.ProfessorService;



@Controller
public class AuthController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/login")
    public String goLoginView(@RequestParam(value = "error", required = false) String error, Model model) {

        System.out.println(error);

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
            model.addAttribute("professors", lstProfessor);
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
