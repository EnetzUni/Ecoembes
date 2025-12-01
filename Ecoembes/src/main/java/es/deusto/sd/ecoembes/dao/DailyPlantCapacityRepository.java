package es.deusto.sd.ecoembes.dao;

import es.deusto.sd.ecoembes.entity.DailyPlantCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DailyPlantCapacityRepository extends JpaRepository<DailyPlantCapacity, Long> {

    Optional<DailyPlantCapacity> findByRecyclingPlantIdAndDate(long recyclingPlantId, Date date);
}
