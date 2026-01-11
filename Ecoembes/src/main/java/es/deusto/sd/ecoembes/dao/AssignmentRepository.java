package es.deusto.sd.ecoembes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByDumpstersContaining(Dumpster dumpster);
}
