package com.production.api.util;

import com.production.api.model.Lot;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public final class JourJulien {
    public static String getJourJulien(){
        LocalDate date = LocalDate.now();
        int jourJulien = date.getDayOfYear();
        return String.valueOf(jourJulien);
    }
}
