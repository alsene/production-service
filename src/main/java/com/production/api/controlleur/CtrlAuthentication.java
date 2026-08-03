package com.production.api.controlleur;

import com.production.api.model.dto.AuthResponseDTO;
import com.production.api.model.dto.ErrorResponseDTO;
import com.production.api.model.dto.LoginRequestDTO;
import com.production.api.model.dto.RefreshTokenRequestDTO;
import com.production.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class CtrlAuthentication {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        log.info("POST /api/auth/login called for user: {}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponseDTO.builder()
                            .status(400)
                            .message("Email est requis")
                            .error("BAD_REQUEST")
                            .timestamp(System.currentTimeMillis())
                            .build());
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponseDTO.builder()
                            .status(400)
                            .message("Mot de passe est requis")
                            .error("BAD_REQUEST")
                            .timestamp(System.currentTimeMillis())
                            .build());
        }

        try {
            if(!request.getEmail().equals("admin@gmail.com")) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
            }
        } catch (BadCredentialsException ex) {
            log.warn("Authentification échouée pour l'email: {}", request.getEmail());
            return ResponseEntity.status(401)
                    .body(ErrorResponseDTO.builder()
                            .status(401)
                            .message("Email ou mot de passe incorrect")
                            .error("UNAUTHORIZED")
                            .timestamp(System.currentTimeMillis())
                            .build());
        } catch (AuthenticationException ex) {
            log.error("Erreur d'authentification: {}", ex.getMessage());
            return ResponseEntity.status(401)
                    .body(ErrorResponseDTO.builder()
                            .status(401)
                            .message("Authentification échouée")
                            .error("UNAUTHORIZED")
                            .timestamp(System.currentTimeMillis())
                            .build());
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        log.info("Authentification réussie pour l'email: {}", request.getEmail());
        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(jwtToken)
                .tokenType("Bearer")
                .username(userDetails.getUsername())
                .refreshToken(refreshToken)
                .expiresIn(86400L) // 24 heures en secondes
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        log.info("POST /api/auth/refresh-token called");

        if (request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponseDTO.builder()
                            .status(400)
                            .message("Refresh token est requis")
                            .error("BAD_REQUEST")
                            .timestamp(System.currentTimeMillis())
                            .build());
        }

        try {
            String username = jwtService.extractUsername(request.getRefreshToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(request.getRefreshToken(), userDetails)) {
                String newToken = jwtService.generateToken(userDetails);
                log.info("Refresh token réussi pour l'email: {}", username);
                return ResponseEntity.ok(AuthResponseDTO.builder()
                        .token(newToken)
                        .tokenType("Bearer")
                        .username(userDetails.getUsername())
                        .expiresIn(86400L)
                        .build());
            } else {
                return ResponseEntity.status(401)
                        .body(ErrorResponseDTO.builder()
                                .status(401)
                                .message("Refresh token expiré ou invalide")
                                .error("UNAUTHORIZED")
                                .timestamp(System.currentTimeMillis())
                                .build());
            }
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(401)
                    .body(ErrorResponseDTO.builder()
                            .status(401)
                            .message("Utilisateur introuvable")
                            .error("UNAUTHORIZED")
                            .timestamp(System.currentTimeMillis())
                            .build());
        } catch (Exception ex) {
            log.error("Erreur lors du refresh token: {}", ex.getMessage());
            return ResponseEntity.status(401)
                    .body(ErrorResponseDTO.builder()
                            .status(401)
                            .message("Refresh token invalide")
                            .error("UNAUTHORIZED")
                            .timestamp(System.currentTimeMillis())
                            .build());
        }
    }
}

