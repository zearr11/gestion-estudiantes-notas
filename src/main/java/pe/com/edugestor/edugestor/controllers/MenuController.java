package pe.com.edugestor.edugestor.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.services.ProfessorService;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/")
public class MenuController {

    private final ProfessorService professorService;

    public MenuController(ProfessorService service){
        this.professorService = service;
    }


    @GetMapping()
    public String goMenuAdminView(Model model) {
        List<Professor> lstProfessor = this.professorService.listLast5();
        model.addAttribute("professors", lstProfessor);
        return "admin/main-menu";
    }
    
}
