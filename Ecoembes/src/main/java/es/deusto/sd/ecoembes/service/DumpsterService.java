package es.deusto.sd.ecoembes.service;

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

    // --------------------------------------------------
    // CREATE
    // --------------------------------------------------
    public Dumpster createDumpster(Dumpster dumpster) {
        dumpsters.add(dumpster);
        return dumpster;
    }

    // --------------------------------------------------
    // UPDATE (añade un registro histórico de llenado)
    // --------------------------------------------------
    public Dumpster updateDumpsterInfo(long id, float fillLevel, Date date) {
        Dumpster dumpster = getDumpsterById(id);

        // Crear el nuevo registro
        FillLevelRecord record = new FillLevelRecord();
        record.setDumpster(dumpster);
        record.setDate(date);
        record.setFillLevel(fillLevel);

        dumpster.getFillHistory().add(record);
        return dumpster;
    }

    // --------------------------------------------------
    // GET BY ID
    // --------------------------------------------------
    public Dumpster getDumpsterById(long id) {
        return dumpsters.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dumpster not found"));
    }

    // --------------------------------------------------
    // GET BY POSTAL CODE + SAME DAY
    // --------------------------------------------------
    public List<Dumpster> getDumpstersByPostalCode(String postalCode, Date date) {
        return dumpsters.stream()
                .filter(d -> d.getLocation().contains(postalCode)
                        && sameDay(getLastUpdate(d), date))
                .collect(Collectors.toList());
    }

    // Helper para sacar último update desde FillHistory
    private Date getLastUpdate(Dumpster d) {
        if (d.getFillHistory().isEmpty()) return null;
        return d.getFillHistory()
                .stream()
                .map(FillLevelRecord::getDate)
                .max(Date::compareTo)
                .orElse(null);
    }

    private boolean sameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        return d1.toInstant().toString().substring(0, 10)
                .equals(d2.toInstant().toString().substring(0, 10));
    }

    // --------------------------------------------------
    // QUERY USAGE
    // --------------------------------------------------
    public List<FillLevelRecord> queryUsage(long id, Date start, Date end) {
        Dumpster dumpster = getDumpsterById(id);
        return dumpster.getFillHistory().stream()
                .filter(r -> !r.getDate().before(start) && !r.getDate().after(end))
                .collect(Collectors.toList());
    }

    // --------------------------------------------------
    // STATUS BASED ON LAST FILL LEVEL
    // --------------------------------------------------
    public String getDumpsterStatus(float fillLevel) {
        if (fillLevel < 0.7f) return "GREEN";
        if (fillLevel < 0.95f) return "ORANGE";
        return "RED";
    }

    // --------------------------------------------------
    // DTO CONVERSION
    // --------------------------------------------------
    public DumpsterDTO toDTO(Dumpster dumpster) {

        // Último fillLevel
        float lastFill = dumpster.getFillHistory().isEmpty()
                ? 0
                : dumpster.getFillHistory()
                        .get(dumpster.getFillHistory().size() - 1)
                        .getFillLevel();

        // Última fecha
        Date lastUpdate = dumpster.getFillHistory().isEmpty()
                ? null
                : dumpster.getFillHistory()
                        .get(dumpster.getFillHistory().size() - 1)
                        .getDate();

        // Convertir historial
        List<FillLevelRecordDTO> history = dumpster.getFillHistory().stream()
                .map(r -> new FillLevelRecordDTO(r.getDate(), r.getFillLevel()))
                .collect(Collectors.toList());

        return new DumpsterDTO(
                dumpster.getId(),
                dumpster.getLocation(),
                dumpster.getMaxCapacity(),
                lastFill,
                lastUpdate,
                history
        );
    }

}
