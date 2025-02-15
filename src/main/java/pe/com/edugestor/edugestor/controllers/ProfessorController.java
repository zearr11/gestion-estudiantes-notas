package pe.com.edugestor.edugestor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/profesores")
public class ProfessorController {
    @GetMapping()
    public String getMethodName() {
        return "admin/professor-list";
    }
    
}
