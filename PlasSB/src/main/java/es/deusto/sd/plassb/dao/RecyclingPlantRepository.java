package es.deusto.sd.plassb.dao;

import es.deusto.sd.plassb.entity.RecyclingPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecyclingPlantRepository extends JpaRepository<RecyclingPlant, Long> {
}