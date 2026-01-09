package es.deusto.sd.ecoembes.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;

@Repository
public interface FillLevelRecordRepository extends JpaRepository<FillLevelRecord, Long> {
    // ESTA LÍNEA ES OBLIGATORIA (Ojo a los argumentos String)
    List<FillLevelRecord> findByDumpsterIdAndDateBetween(Long dumpsterId, String startDate, String endDate);
}