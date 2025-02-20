package pe.com.edugestor.edugestor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.Professor;
import pe.com.edugestor.edugestor.models.User;
import pe.com.edugestor.edugestor.services.PersonService;
import pe.com.edugestor.edugestor.services.ProfessorService;
import pe.com.edugestor.edugestor.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/profesores")
public class ProfessorController {
    
    private ProfessorService professorService;
    private PersonService personService;
    private UserService userService;

    private Professor professorSelected;
    private Person dataProfessorSelected;
    private Professor professorDefault = new Professor(null, null, null, null, null);
    
    public ProfessorController(ProfessorService serviceProf, PersonService servicePer, UserService serviceUser){
        this.professorService = serviceProf;
        this.personService = servicePer;
        this.userService = serviceUser;
    }
     
    @GetMapping()
    public String goProfessorView(Model model) {
        model.addAttribute("professors", this.professorService.listAllProfesor());
        return "admin/professor-list";
    }
    
    @GetMapping("/nuevo")
    public String addNewProfessor(Model model) {
        model.addAttribute("professor", professorDefault);
        return "admin/professor-add";
    }
    
    @PostMapping("/guardar")
    public String saveProfessor(@ModelAttribute Professor professorToSave) {
        List<Person> persons = new ArrayList<>();
        persons.add(professorToSave.getPerson());
        User userToSave = new User(null, "D" + professorToSave.getPerson().getNid(), 
        "D" + professorToSave.getPerson().getNid(), "Profesor", "Activo", persons);
        professorToSave.getPerson().setUser(userToSave);

        userService.createUser(userToSave);
        personService.createPerson(professorToSave.getPerson());
        professorService.createProfessor(professorToSave);
        return "redirect:/profesores";
    }
    
    @GetMapping("/editar/{id}")
    public String goViewEditProfessor(@PathVariable Long id, Model model) {
        professorSelected = this.professorService.getProfessorByID(id);

        if (professorSelected == null)
            return "redirect:/profesores";
        
        dataProfessorSelected = professorSelected.getPerson();
        model.addAttribute("professor", professorSelected);
        return "admin/professor-edit";
    }
    
    @PostMapping("/actualizar")
    public String updateProfessor(@ModelAttribute Professor professorToUpdate) {
        
        dataProfessorSelected.getUser().setCodUser("D" + professorToUpdate.getPerson().getNid());
        professorToUpdate.setIdProfessor(professorSelected.getIdProfessor());
        professorToUpdate.getPerson().setIdPerson(dataProfessorSelected.getIdPerson());
        professorToUpdate.getPerson().setUser(dataProfessorSelected.getUser());

        this.userService.updateUser(professorToUpdate.getPerson().getUser());
        this.personService.updatePerson(professorToUpdate.getPerson());
        this.professorService.updateProfessor(professorToUpdate);

        return "redirect:/profesores";
    }
    
    
}
