package es.deusto.sd.ecoembes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.ecoembes.entity.Dumpster;
import java.util.Date;
import java.util.List;

@Repository
public interface DumpsterRepository extends JpaRepository<Dumpster, Long> {
    List<Dumpster> findByLocationContainingAndLastUpdate(String postalCode, Date date);
    List<Dumpster> findByIdAndLastUpdateBetween(long id, Date start, Date end);
}
