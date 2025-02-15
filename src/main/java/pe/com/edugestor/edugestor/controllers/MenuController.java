package pe.com.edugestor.edugestor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/")
public class MenuController {
    @GetMapping()
    public String goMenuAdminView() {
        return "admin/main-menu";
    }
    
}
