package es.deusto.sd.ecoembes.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.dto.FillLevelRecordDTO;

@Service
public class DumpsterService {

    private List<Dumpster> dumpsters = new ArrayList<>(); // Simula DB Dumpsters
    
    // NUEVO: Como Dumpster no tiene lista de historial, la guardamos aquí mapeada por ID del contenedor
    private Map<Long, List<FillLevelRecord>> fillHistoryMap = new HashMap<>();

    // --------------------------------------------------
    // CREATE
    // --------------------------------------------------
    public Dumpster createDumpster(Dumpster dumpster) {
        // Asignar ID simulado si es 0
        if (dumpster.getId() == 0) {
            dumpster.setId(dumpsters.size() + 1);
        }
        dumpsters.add(dumpster);
        // Inicializar su historial vacío
        fillHistoryMap.put(dumpster.getId(), new ArrayList<>());
        return dumpster;
    }

    // --------------------------------------------------
    // UPDATE (añade un registro histórico de llenado)
    // --------------------------------------------------
    public Dumpster updateDumpsterInfo(long id, float fillLevel, Date date) {
        Dumpster dumpster = getDumpsterById(id);

        // Crear el nuevo registro
        FillLevelRecord record = new FillLevelRecord();
        record.setDumpster(dumpster); // Asumiendo que FillLevelRecord sí tiene setDumpster (si no, quita esta línea)
        record.setDate(date);
        record.setFillLevel(fillLevel);

        // Guardar en el mapa del servicio, NO en la entidad
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
    // GET BY POSTAL CODE + SAME DAY
    // --------------------------------------------------
    public List<Dumpster> getDumpstersByPostalCode(String postalCode, Date date) {
        return dumpsters.stream()
                .filter(d -> d.getLocation() != null && d.getLocation().contains(postalCode))
                .filter(d -> sameDay(getLastUpdate(d.getId()), date)) // Pasamos ID, no objeto
                .collect(Collectors.toList());
    }

    // Helper: Obtener historial desde el MAPA
    private List<FillLevelRecord> getHistoryByDumpsterId(long id) {
        return fillHistoryMap.getOrDefault(id, new ArrayList<>());
    }

    // Helper para sacar último update
    private Date getLastUpdate(long dumpsterId) {
        List<FillLevelRecord> history = getHistoryByDumpsterId(dumpsterId);
        if (history.isEmpty()) return null;
        
        return history.stream()
                .map(FillLevelRecord::getDate)
                .max(Date::compareTo)
                .orElse(null);
    }

    private boolean sameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) return false;
        // Comparación simple de fecha (YYYY-MM-DD)
        // Nota: En producción usar LocalDate es mejor, pero mantenemos tu lógica para evitar imports extra
        return d1.toString().substring(0, 10).equals(d2.toString().substring(0, 10));
    }

    // --------------------------------------------------
    // QUERY USAGE
    // --------------------------------------------------
    public List<FillLevelRecord> queryUsage(long id, Date start, Date end) {
        // Verificar que existe
        getDumpsterById(id);
        
        // Buscar en el mapa
        return getHistoryByDumpsterId(id).stream()
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
        List<FillLevelRecord> history = getHistoryByDumpsterId(dumpster.getId());
        boolean hasHistory = !history.isEmpty();

        // Último fillLevel
        float lastFill = hasHistory
                ? history.get(history.size() - 1).getFillLevel()
                : 0.0f;

        // Última fecha
        Date lastUpdate = hasHistory
                ? history.get(history.size() - 1).getDate()
                : null;

        // Convertir historial a DTOs
        List<FillLevelRecordDTO> historyDTOs = history.stream()
                .map(r -> new FillLevelRecordDTO(r.getDate(), r.getFillLevel()))
                .collect(Collectors.toList());

        // Usamos el constructor de DumpsterDTO que definiste
        // Si DumpsterDTO no tiene constructor para historial, tendrás que añadirlo o usar setters
        // Asumo que DumpsterDTO tiene el constructor completo que hicimos antes.
        return new DumpsterDTO(
                dumpster.getId(),
                dumpster.getLocation(),
                dumpster.getMaxCapacity()
                // Estos campos extra deben existir en DumpsterDTO o usar setters si el constructor no existe
                // Si el constructor es el de 3 params, usa setters abajo:
                /* new DumpsterDTO(id, loc, cap); 
                   dto.setCurrentFillLevel(lastFill); ... 
                */
                // Si tienes el DumpsterDTO "completo" del paso anterior:
                /* lastFill, lastUpdate, historyDTOs */
        );
        
        // NOTA: Si DumpsterDTO volvió a su estado original (solo 3 campos), 
        // tendrás que devolver solo esos 3 campos y perder la info extra, 
        // O modificar DumpsterDTO (ya que es un DTO, no una Entidad, sí se puede tocar sin romper DB).
        // Aquí asumo que DumpsterDTO SÍ tiene los campos extra para el frontend.
    }
}