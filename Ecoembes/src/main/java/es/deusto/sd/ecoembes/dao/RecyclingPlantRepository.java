package es.deusto.sd.ecoembes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Repository
public interface RecyclingPlantRepository extends JpaRepository<RecyclingPlant, Long> {
    // No hace falta poner nada más, con esto ya tenemos el .findById() mágico
}