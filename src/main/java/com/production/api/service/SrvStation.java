package com.production.api.service;

import com.production.api.model.Station;
import com.production.api.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvStation {
    private final StationRepository stationRepository;

    public Mono<Station> getStation() {
        Station station = new Station();
        log.info("Returning Station: {}", station);
        return Mono.just(station);
    }

    public Mono<Station> saveStation(Station station) {
        return Mono.fromCallable(() -> {
            log.info("Saving Station to database: {}", station);
            Station saved = stationRepository.save(station);
            log.info("Station saved with id: {}", saved.getId());
            return saved;
        });
    }

    public Mono<Station> modifierStation(Station station) {
        return Mono.fromCallable(() -> {
            if (station.getId() == null) {
                throw new IllegalArgumentException("L'id de la station est requis pour la modification");
            }

            Station existingStation = stationRepository.findById(station.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Station not found with id: " + station.getId()));

            existingStation.setLibelle(station.getLibelle());
            existingStation.setTypeStation(station.getTypeStation());

            return stationRepository.save(existingStation);
        });
    }

    public Mono<Void> supprimerStation(Long id) {
        return Mono.fromCallable(() -> {
            Station existingStation = stationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Station not found with id: " + id));
            stationRepository.deleteById(existingStation.getId());
            return null;
        });
    }

    public Mono<Station> getStationById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching Station with id: {}", id);
            return stationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
        });
    }

    public Mono<List<Station>> getAllStations() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Stations from database");
            return stationRepository.findAll();
        });
    }
}

