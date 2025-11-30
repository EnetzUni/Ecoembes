package es.deusto.sd.ecoembes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.ecoembes.entity.DailyPlantCapacity;
import java.util.Date;
import java.util.List;

@Repository
public interface DailyPlantCapacityRepository extends JpaRepository<DailyPlantCapacity, Long> {
    List<DailyPlantCapacity> findByRecyclingPlantIdAndDate(long plantId, Date date);
}
