package com.production.api.configuration;

import com.production.api.model.Utilisateur;
import com.production.api.repository.ProfilUtilisateurRepository;
import com.production.api.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityBeansConfig {

    private final UtilisateurRepository utilisateurRepository;
    private final ProfilUtilisateurRepository profilUtilisateurRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmail(username);
            if (utilisateur == null) {
                throw new UsernameNotFoundException("Utilisateur introuvable avec email: " + username);
            }

            var authorities = profilUtilisateurRepository.findByUtilisateurId(utilisateur.getId())
                    .stream()
                    .map(pu -> new SimpleGrantedAuthority("ROLE_" + pu.getProfil().getTypeProfil().name()))
                    .toList();

            UserDetails userDetails = User.withUsername(utilisateur.getEmail())
                    .password(utilisateur.getPassword())
                    .authorities(authorities.isEmpty() ? java.util.List.of(new SimpleGrantedAuthority("ROLE_USER")) : authorities)
                    .build();
            return userDetails;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

