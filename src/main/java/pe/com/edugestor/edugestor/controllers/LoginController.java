package pe.com.edugestor.edugestor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping()
    public String goLoginView() {
        return "login";
    }
    
}
