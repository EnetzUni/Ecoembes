package es.deusto.sd.ecoembes.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // ESTA LÍNEA ES OBLIGATORIA PARA QUE FUNCIONE EL SERVICE
    List<Assignment> findByDumpster(Dumpster dumpster);
}