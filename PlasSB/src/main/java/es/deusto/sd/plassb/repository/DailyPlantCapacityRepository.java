package es.deusto.sd.plassb.repository;

import es.deusto.sd.plassb.entity.DailyPlantCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DailyPlantCapacityRepository extends JpaRepository<DailyPlantCapacity, Long> {

    Optional<DailyPlantCapacity> findByRecyclingPlantIdAndDate(long recyclingPlantId, Date date);
}
