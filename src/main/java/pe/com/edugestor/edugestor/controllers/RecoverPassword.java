package pe.com.edugestor.edugestor.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.com.edugestor.edugestor.models.Person;
import pe.com.edugestor.edugestor.models.User;
import pe.com.edugestor.edugestor.services.EmailService;
import pe.com.edugestor.edugestor.services.PersonService;
import pe.com.edugestor.edugestor.services.UserService;

@Controller
@RequestMapping("/recuperar-clave")
public class RecoverPassword {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PersonService personService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public String goViewRecoverPassword() {
        return "public/not-log/recover-password";
    }

    Person dataPersonValidation;
    String codigoRecuperacion;

    @PostMapping("/envio-codigo")
    public String sendEmailWithCodeVerification(@RequestParam String codigoUsuario, Model model) {

        User usuario = userService.getUserByCod(codigoUsuario);
        
        if (usuario == null) {
            model.addAttribute("error", "El código de usuario no existe.");
            return "public/not-log/recover-password";
        }

        dataPersonValidation = personService.getPersonByUser(usuario);
        codigoRecuperacion = String.format("%06d", new Random().nextInt(999999));

        new Thread(() -> emailService.sendCodeRecuperation(dataPersonValidation.getEmail(), codigoRecuperacion)).start();

        return "public/not-log/verified-cod";
    }

    @PostMapping("/verificar-codigo")
    public String verificarCodigo(@RequestParam String codigoIngresado, Model model) {

        if (!codigoIngresado.equals(codigoRecuperacion)) {
            model.addAttribute("error", "El código ingresado es incorrecto.");
            return "public/not-log/verified-cod";
        }

        return "public/not-log/change-password-cod";
    }

    @PostMapping("/actualizar-password")
    public String actualizarPassword(@RequestParam String nuevaPassword,
                                     @RequestParam String confirmarPassword) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getAuthorities().iterator().next().getAuthority().equals("ROLE_ANONYMOUS")) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User userLoged = userService.getUserByCod(userDetails.getUsername());

            userLoged.setPassword(passwordEncoder.encode(nuevaPassword));
            userService.updateUser(userLoged);

            return "redirect:/logout";
        }

        User userVerified = dataPersonValidation.getUser();
        userVerified.setPassword(passwordEncoder.encode(nuevaPassword));
        userService.updateUser(userVerified);
        return "redirect:/login";
    }
}
