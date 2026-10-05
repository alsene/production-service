package com.production.api.repository;

import com.production.api.model.StationLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationLotRepository extends JpaRepository<StationLot, Long> {

    @Query("SELECT sl FROM StationLot sl WHERE sl.station.id = :stationId")
    List<StationLot> findByStationId(Long stationId);

    @Query("SELECT sl FROM StationLot sl WHERE sl.lot.id = :lotId")
    List<StationLot> findByLotId(Long lotId);
}

