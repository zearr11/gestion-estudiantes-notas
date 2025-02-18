package pe.com.edugestor.edugestor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.services.PersonService;
import pe.com.edugestor.edugestor.services.ProfessorService;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/")
public class MenuController {
    
    private final ProfessorService professorService;

    public MenuController(ProfessorService serviceProfessor){
        this.professorService = serviceProfessor;
    }
    

    @GetMapping()
    public String goMenuAdminView(Model model) {
        List<Professor> lstProfessor = this.professorService.listLastFiveProfessor();

        model.addAttribute("professors", lstProfessor);

        return "admin/main-menu";
    }
    
}
