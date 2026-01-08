package es.deusto.sd.ecoembes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
// import es.deusto.sd.ecoembes.dto.FillLevelRecordDTO;

@Service
public class DumpsterService {

    private List<Dumpster> dumpsters = new ArrayList<>(); // Simula DB
    
    // Mapa para historial (ya que Dumpster Entity no tiene la lista)
    private Map<Long, List<FillLevelRecord>> fillHistoryMap = new HashMap<>();

    // --------------------------------------------------
    // CREATE
    // --------------------------------------------------
    public Dumpster createDumpster(Dumpster dumpster) {
        if (dumpster.getId() == 0) {
            dumpster.setId(dumpsters.size() + 1);
        }
        dumpsters.add(dumpster);
        fillHistoryMap.put(dumpster.getId(), new ArrayList<>());
        return dumpster;
    }

    // --------------------------------------------------
    // UPDATE (Fill Level)
    // --------------------------------------------------
    // AHORA RECIBE STRING EN LUGAR DE DATE
    public Dumpster updateDumpsterInfo(long id, float fillLevel, String date) {
        Dumpster dumpster = getDumpsterById(id);

        FillLevelRecord record = new FillLevelRecord();
        record.setDumpster(dumpster);
        record.setDate(date); // Guardamos el String directo
        record.setFillLevel(fillLevel);

        // Guardar en el mapa
        fillHistoryMap.computeIfAbsent(id, k -> new ArrayList<>()).add(record);
        
        return dumpster;
    }

    // --------------------------------------------------
    // GET BY ID
    // --------------------------------------------------
    public Dumpster getDumpsterById(long id) {
        return dumpsters.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dumpster with ID " + id + " not found"));
    }

    // --------------------------------------------------
    // GET BY POSTAL CODE & DATE
    // --------------------------------------------------
    public List<Dumpster> getDumpstersByPostalCode(String postalCode, String date) {
        return dumpsters.stream()
                .filter(d -> d.getLocation() != null && d.getLocation().contains(postalCode))
                .filter(d -> {
                    String lastUpdate = getLastUpdateDate(d.getId());
                    return sameDay(lastUpdate, date);
                })
                .collect(Collectors.toList());
    }

    // Helper para sacar última fecha (String)
    private String getLastUpdateDate(long dumpsterId) {
        List<FillLevelRecord> history = fillHistoryMap.get(dumpsterId);
        if (history == null || history.isEmpty()) return null;
        
        // Asumimos que el último insertado es el más reciente
        // Ojo: comparar Strings de fecha ("2025/01/02" > "2025/01/01") funciona si el formato es ISO (YYYY-MM-DD)
        return history.get(history.size() - 1).getDate();
    }

    // Comparación de Strings
    private boolean sameDay(String storedDate, String queryDate) {
        if (storedDate == null || queryDate == null) return false;
        // Comparamos si son iguales o si uno contiene al otro 
        // (por si uno es "2025-01-01" y el otro "2025-01-01T10:00")
        return storedDate.startsWith(queryDate) || queryDate.startsWith(storedDate);
    }

    // --------------------------------------------------
    // QUERY USAGE
    // --------------------------------------------------
    // AHORA RECIBE STRING EN LUGAR DE DATE
    public List<FillLevelRecord> queryUsage(long id, String startDate, String endDate) {
        getDumpsterById(id); // Check exists
        
        List<FillLevelRecord> history = fillHistoryMap.getOrDefault(id, new ArrayList<>());
        
        // Nota: La comparación de rangos con Strings solo funciona bien si usas formato ISO (YYYY-MM-DD)
        // Si usas DD/MM/YYYY, la lógica de mayor/menor alfabético no funciona para fechas.
        return history.stream()
                .filter(r -> r.getDate().compareTo(startDate) >= 0 && r.getDate().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
    }

    // --------------------------------------------------
    // DTO CONVERSION
    // --------------------------------------------------
    public DumpsterDTO toDTO(Dumpster dumpster) {
        // Mantenemos el DTO simple de 3 campos para que no falle tu constructor
        return new DumpsterDTO(
            dumpster.getId(),
            dumpster.getLocation(),
            dumpster.getMaxCapacity()
        );
    }
}