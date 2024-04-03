package com.example.delivery.repository;

import com.example.delivery.model.Weather;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    /**
     * Returns the station with given name and the latest timestamp
     * @param station The name of the station we're looking for, matches with ?1 in the query
     * @return Station with given name and latest timestamp
     */
    @Query(value = "SELECT w FROM Weather w WHERE w.station = ?1 AND w.timestamp = (SELECT MAX(w2.timestamp) FROM Weather w2 WHERE w2.station = ?1)")
    Weather findByLatestStation(String station);

    @Query(value = "SELECT w FROM Weather w WHERE w.station = ?1 AND w.timestamp = ?2")
    Weather findByTimeStation(String station, Timestamp time);
}