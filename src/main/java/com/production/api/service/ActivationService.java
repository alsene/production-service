package com.production.api.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ActivationService {

    // Générer un code numérique à 6 chiffres (ex: 482910)
    public String generateNumericCode() {
        SecureRandom random = new SecureRandom();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    // Alternative : Générer un jeton alphanumérique unique (UUID)
    public String generateUUIDCode() {
        return UUID.randomUUID().toString();
    }

    // Calculer la date d'expiration (ex: valide pendant 15 minutes)
    public LocalDateTime calculateExpiryDate(int expirationInMinutes) {
        return LocalDateTime.now().plusMinutes(expirationInMinutes);
    }

}