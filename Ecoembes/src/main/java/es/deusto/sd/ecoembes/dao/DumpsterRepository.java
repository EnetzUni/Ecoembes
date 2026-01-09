package es.deusto.sd.ecoembes.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.deusto.sd.ecoembes.entity.Dumpster;

@Repository
public interface DumpsterRepository extends JpaRepository<Dumpster, Long> {
    // Método para buscar por localización (filtro parcial)
    // Containing hace que funcione como un LIKE %texto%
    List<Dumpster> findByLocationContaining(String location);
}