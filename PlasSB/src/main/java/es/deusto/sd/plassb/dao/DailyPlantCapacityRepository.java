package es.deusto.sd.plassb.dao;

import es.deusto.sd.plassb.entity.DailyPlantCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyPlantCapacityRepository extends JpaRepository<DailyPlantCapacity, Long> {

    // CAMBIO IMPORTANTE: 
    // Antes: (long recyclingPlantId, Date date);
    // Ahora: (long recyclingPlantId, String date); <--- TIENE QUE SER STRING
    Optional<DailyPlantCapacity> findByRecyclingPlantIdAndDate(long recyclingPlantId, String date);
}