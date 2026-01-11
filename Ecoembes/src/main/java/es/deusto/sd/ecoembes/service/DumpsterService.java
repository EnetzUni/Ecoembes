package es.deusto.sd.ecoembes.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import es.deusto.sd.ecoembes.dao.DumpsterRepository;
import es.deusto.sd.ecoembes.dao.FillLevelRecordRepository;
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;

@Service
public class DumpsterService {

    private final DumpsterRepository dumpsterRepository;
    private final FillLevelRecordRepository fillLevelRecordRepository;

    public DumpsterService(DumpsterRepository dumpsterRepository, FillLevelRecordRepository fillLevelRecordRepository) {
        this.dumpsterRepository = dumpsterRepository;
        this.fillLevelRecordRepository = fillLevelRecordRepository;
    }

    public Dumpster createDumpster(Dumpster dumpster) {
        return dumpsterRepository.save(dumpster);
    }

    public Dumpster updateDumpsterInfo(long id, float fillLevel, String date) {
        Dumpster dumpster = dumpsterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dumpster not found"));

        // 1. Guardar en el histórico (ESTO LO TENÍAS BIEN)
        FillLevelRecord record = new FillLevelRecord();
        record.setDumpster(dumpster);
        record.setDate(date);
        record.setFillLevel(fillLevel);
        fillLevelRecordRepository.save(record);

        // 2. ACTUALIZAR EL CONTENEDOR (ESTO FALTABA)
        // Si no haces esto, el contenedor sigue diciendo que está al 0% aunque hayas metido un registro del 100%
        dumpster.setFillLevel(fillLevel); 
        return dumpsterRepository.save(dumpster); // Guardamos el cambio en el propio contenedor
    }

    public Dumpster getDumpsterById(long id) {
        return dumpsterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dumpster not found"));
    }

    // --- AQUÍ ESTABA EL PROBLEMA DEL ERROR 500 ---
    public List<Dumpster> getDumpstersByPostalCode(String postalCode, String date) {
        // 1. Recuperamos todo
        List<Dumpster> allDumpsters = dumpsterRepository.findAll();

        // 2. Si no hay filtros, devolvemos la lista tal cual (Evita NullPointerException)
        if ((postalCode == null || postalCode.isBlank()) && (date == null || date.isBlank())) {
            return allDumpsters;
        }

        // 3. Si hay filtros, aplicamos lógica segura
        return allDumpsters.stream()
                .filter(d -> {
                    if (postalCode != null && !postalCode.isBlank()) {
                        return d.getLocation() != null && d.getLocation().contains(postalCode);
                    }
                    return true;
                })
                .filter(d -> {
                    if (date != null && !date.isBlank()) {
                        // Usamos findByDumpster_Id (convención segura de Spring Data)
                        List<FillLevelRecord> history = fillLevelRecordRepository.findByDumpsterIdAndDateBetween(d.getId(), date, date);
                        return !history.isEmpty();
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public List<FillLevelRecord> queryUsage(long id, String startDate, String endDate) {
        if (!dumpsterRepository.existsById(id)) throw new RuntimeException("Dumpster not found");
        return fillLevelRecordRepository.findByDumpsterIdAndDateBetween(id, startDate, endDate);
    }

    public DumpsterDTO toDTO(Dumpster d) {
        return new DumpsterDTO(
            d.getId(),
            d.getLocation(),
            d.getContainerCount(),
            d.getFillLevel(),
            d.getMaxCapacity()
        );
    }

}
