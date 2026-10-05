package com.production.api.service;

import com.production.api.model.SiloTypeProduit;
import com.production.api.model.StationLot;
import com.production.api.repository.StationLotRepository;
import com.production.api.util.EnrichirLibelle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvStationLot {
    private final StationLotRepository stationLotRepository;

    public Mono<StationLot> getStationLot() {
        StationLot stationLot = new StationLot();
        log.info("Returning StationLot: {}", stationLot);
        return Mono.just(stationLot);
    }

    public Mono<StationLot> saveStationLot(StationLot stationLot) {
        return Mono.fromCallable(() -> {
            log.info("Saving StationLot to database: {}", stationLot);
            EnrichirLibelle.libelleStationLot(stationLot);
            StationLot saved = stationLotRepository.save(stationLot);
            log.info("StationLot saved with id: {}", saved.getId());
            return saved;
        });
    }

    public Mono<StationLot> modifierStationLot(StationLot stationLot) {
        return Mono.fromCallable(() -> {
            if (stationLot.getId() == null) {
                throw new IllegalArgumentException("L'id du stationLot est requis pour la modification");
            }

            StationLot existing = stationLotRepository.findById(stationLot.getId())
                    .orElseThrow(() -> new IllegalArgumentException("StationLot not found with id: " + stationLot.getId()));

            existing.setStation(stationLot.getStation());
            existing.setLot(stationLot.getLot());
            EnrichirLibelle.libelleStationLot(existing);
            return stationLotRepository.save(existing);
        });
    }

    public Mono<Void> supprimerStationLot(Long id) {
        return Mono.fromCallable(() -> {
            StationLot existing = stationLotRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("StationLot not found with id: " + id));
            stationLotRepository.deleteById(existing.getId());
            return null;
        });
    }

    public Mono<StationLot> getStationLotById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching StationLot with id: {}", id);
            StationLot stationLot = stationLotRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("StationLot not found with id: " + id));
            EnrichirLibelle.libelleStationLot(stationLot);
            return stationLot;
        });
    }

    public Mono<List<StationLot>> findByStationId(String stationId) {
        return Mono.fromCallable(() -> {
            log.info("Fetching StationLot by station id: {}", stationId);
            List<StationLot> result = stationLotRepository.findByStationId(Long.valueOf(stationId));
            result.forEach(EnrichirLibelle::libelleStationLot);
            return result;
        });
    }

    public Mono<List<StationLot>> findByLotId(String lotId) {
        return Mono.fromCallable(() -> {
            log.info("Fetching StationLot by lot id: {}", lotId);
            List<StationLot> result = stationLotRepository.findByLotId(Long.valueOf(lotId));
            result.forEach(EnrichirLibelle::libelleStationLot);
            return result;
        });
    }

    public Mono<List<StationLot>> getAllStationLots() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all StationLots from database");
            List<StationLot> result = stationLotRepository.findAll();
            result.forEach(EnrichirLibelle::libelleStationLot);
            return result;
        });
    }
}

