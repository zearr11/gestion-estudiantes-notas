package pe.com.edugestor.edugestor.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.models.User;
import pe.com.edugestor.edugestor.repositories.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repositoryUser, PasswordEncoder encoder){
        this.userRepository = repositoryUser;
        this.passwordEncoder = encoder;
    }

    public List<User> listAllUsers(){
        return this.userRepository.findAll();
    }

    public User createUser(User entity){
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return this.userRepository.saveAndFlush(entity);
    }

    public User getUserByID(Long id){
        return this.userRepository.findById(id).orElse(null);
    }

    public User getUserByCod(String cod){
        return this.userRepository.findByCodUser(cod).orElse(null);
    }

    public User updateUser(User entity){
        User userToUpdate = this.userRepository.findById(entity.getIdUser()).orElse(null);

        if (userToUpdate == null)
            return null;
        
        userToUpdate.setPassword(entity.getPassword());
        userToUpdate.setPersons(entity.getPersons());
        userToUpdate.setRol(entity.getRol());
        userToUpdate.setState(entity.getState());
        userToUpdate.setCodUser(entity.getCodUser());

        return this.userRepository.saveAndFlush(userToUpdate);
    }
    
}
