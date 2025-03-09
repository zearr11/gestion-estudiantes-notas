package pe.com.edugestor.edugestor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendCodeRecuperation(String email, String codigoRecuperacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setSubject("EduGestión - Recuperación de Contraseña");
        mensaje.setText("Tu código de recuperación es: " + codigoRecuperacion);
        mailSender.send(mensaje);
    }
}
