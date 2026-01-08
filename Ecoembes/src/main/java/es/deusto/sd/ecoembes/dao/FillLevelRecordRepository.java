package es.deusto.sd.ecoembes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.ecoembes.entity.FillLevelRecord;

@Repository
public interface FillLevelRecordRepository extends JpaRepository<FillLevelRecord, Long> {

    // ERROR ANTERIOR: findByDumpsterIdAndDateBetween(long id, Date start, Date end);
    
    // CORRECCIÓN: Cambiamos Date por String
    List<FillLevelRecord> findByDumpsterIdAndDateBetween(long dumpsterId, String startDate, String endDate);
    
    // Si tuvieras otros métodos con fecha, cámbialos también a String.
}