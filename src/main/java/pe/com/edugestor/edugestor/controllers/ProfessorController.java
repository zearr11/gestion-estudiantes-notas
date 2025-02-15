package pe.com.edugestor.edugestor.controllers;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.services.ProfessorService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
@RequestMapping("/profesores")
public class ProfessorController {
    
    private final ProfessorService professorService;
    private Professor professorDefault = new Professor
    (null, null, null, null, null, null, 0, 0, null, null);

    public ProfessorController(ProfessorService service){
        this.professorService = service;
    }

    @GetMapping()
    public String goProfessorView() {

        /* 
        Professor entity = new Professor(
            null, "Juan Alfonso", "Perez Lopez", 
            LocalDate.of(1983, 2, 15), "Masculino", 
            "juanalfon23@gmail.com", 74682321, 988231761, 
            "Los Laureles 891", "Programación"
        );

        // Guardamos en la BD
        professorService.createProfessor(entity);
        */

        return "admin/professor-list";
    }

    @GetMapping("/nuevo")
    public String addNewProfessor(Model model) {
        model.addAttribute("professor", professorDefault);
        return "admin/professor-add";
    }

    @PostMapping("/guardar")
    public String saveProfessor(@ModelAttribute Professor professorToSave) {
        professorService.createProfessor(professorToSave);
        return "redirect:/profesores";
    }
    
    
    
}
