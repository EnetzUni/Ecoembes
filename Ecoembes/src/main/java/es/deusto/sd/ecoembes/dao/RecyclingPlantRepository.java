package es.deusto.sd.ecoembes.dao;

import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecyclingPlantRepository extends JpaRepository<RecyclingPlant, Long> {
}