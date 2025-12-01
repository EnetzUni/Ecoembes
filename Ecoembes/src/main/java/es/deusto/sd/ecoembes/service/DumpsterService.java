/*package es.deusto.sd.ecoembes.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.dto.FillLevelRecordDTO;

@Service
public class DumpsterService {

    private List<Dumpster> dumpsters = new ArrayList<>(); // Simula DB

    public Dumpster createDumpster(Dumpster dumpster) {
        dumpsters.add(dumpster);
        return dumpster;
    }

    public Dumpster updateDumpsterInfo(long id, float fillLevel, Date date) {
        Dumpster dumpster = getDumpsterById(id);
        dumpster.setFillLevel(fillLevel);
        dumpster.setLastUpdate(date);

        // Agregamos registro histórico como FillLevelRecord, no DTO
        FillLevelRecord record = new FillLevelRecord();
        record.setDumpster(dumpster);
        record.setDate(date);
        record.setFillLevel(fillLevel);
        dumpster.getFillHistory().add(record);

        return dumpster;
    }

    public Dumpster getDumpsterById(long id) {
        return dumpsters.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dumpster not found"));
    }

    public List<Dumpster> getDumpstersByPostalCode(String postalCode, Date date) {
        return dumpsters.stream()
                .filter(d -> d.getLocation().contains(postalCode) && sameDay(d.getLastUpdate(), date))
                .collect(Collectors.toList());
    }

    public List<Dumpster> queryUsage(long id, Date start, Date end) {
        Dumpster dumpster = getDumpsterById(id);
        return dumpster.getFillHistory().stream()
                .filter(r -> !r.getDate().before(start) && !r.getDate().after(end))
                .map(r -> dumpster) // devuelvo la entidad para mantener compatibilidad con tu código anterior
                .collect(Collectors.toList());
    }

    public String getDumpsterStatus(float fillLevel) {
        if (fillLevel < 0.7f) return "GREEN";
        if (fillLevel < 0.95f) return "ORANGE";
        return "RED";
    }

    // -----------------------
    // Convierte a DTO
    // -----------------------
    public DumpsterDTO toDTO(Dumpster dumpster) {
        List<FillLevelRecordDTO> history = dumpster.getFillHistory().stream()
                .map(r -> new FillLevelRecordDTO(r.getDate(), r.getFillLevel()))
                .collect(Collectors.toList());

        return new DumpsterDTO(
                dumpster.getId(),
                dumpster.getLocation(),
                dumpster.getMaxCapacity(),
                dumpster.getFillLevel(),
                dumpster.getLastUpdate(),
                history
        );
    }

    // -----------------------
    // Helper para comparar fechas ignorando hora
    // -----------------------
    private boolean sameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        return d1.getYear() == d2.getYear() &&
               d1.getMonth() == d2.getMonth() &&
               d1.getDate() == d2.getDate();
    }
}

*/