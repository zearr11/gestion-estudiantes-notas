package pe.com.edugestor.edugestor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.services.ProfessorService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/profesores")
public class ProfessorController {
    
    private final ProfessorService professorService;
    private Professor professorSelected;
    private Professor professorDefault = new Professor
    (null, null, null, null, null, null, 0, 0, null, null);

    public ProfessorController(ProfessorService service){
        this.professorService = service;
    }

    @GetMapping()
    public String goProfessorView(Model model) {
        model.addAttribute("professors", this.professorService.listAll());
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

    @GetMapping("/editar/{id}")
    public String goViewEditProfessor(@PathVariable Long id, Model model) {
        professorSelected = this.professorService.getProfessorByID(id);
        
        if (professorSelected == null)
            return "redirect:/profesores";

        model.addAttribute("professor", professorSelected);
        return "admin/professor-edit";
    }
    
    @PostMapping("/actualizar")
    public String updateProfessor(@ModelAttribute Professor professorToUpdate) {
        professorToUpdate.setId(professorSelected.getId());
        this.professorService.updateProfessor(professorToUpdate);
        return "redirect:/profesores";
    }
    
    
}
