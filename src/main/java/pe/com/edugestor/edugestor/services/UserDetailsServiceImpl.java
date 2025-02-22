package pe.com.edugestor.edugestor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import pe.com.edugestor.edugestor.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String codUser) throws UsernameNotFoundException {
        pe.com.edugestor.edugestor.models.User user = userRepository.findByCodUser(codUser)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User.withUsername(user.getCodUser())
            .password(user.getPassword())
            .roles(user.getRol())
            .build();
    }
    
}