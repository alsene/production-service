package com.production.api.service;

import com.production.api.model.Lot;
import com.production.api.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvLot {
    private final LotRepository lotRepository;
    
    public Mono<Lot> getLot(){
        Lot lot = new Lot();
        log.info("Returning Lot: {}", lot);
        return Mono.just(lot);
    }

    /**
     * Save a product to the database
     */
    public Mono<Lot> saveLot(Lot lot) {
        return Mono.fromCallable(() -> {
            log.info("Saving Lot to database: {}", lot);
            Lot saved = lotRepository.save(lot);
            log.info("Lot saved with id: {}", saved.getId());
            return saved;
        });
    }

    /**
     * Get product by ID from database
     */
    public Mono<Lot>  getLotById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching Lot with id: {}", id);
            return lotRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lot not found with id: " + id));
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Lot>> getAllLots() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Lots from database");
            return lotRepository.findAll();
        });
    }
}