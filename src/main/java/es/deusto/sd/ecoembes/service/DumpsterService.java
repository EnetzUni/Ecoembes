package es.deusto.sd.ecoembes.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.dao.DumpsterRepository;
import es.deusto.sd.ecoembes.entity.Dumpster;

@Service
public class DumpsterService {

    private final DumpsterRepository dumpsterRepository;

    public DumpsterService(DumpsterRepository dumpsterRepository) {
        this.dumpsterRepository = dumpsterRepository;
    }

    
    //FUNCION: UPDATE DUMPESTER INFO
    public Dumpster updateDumpsterInfo(long id, float fillLevel, Date date) {
        Dumpster d = dumpsterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dumpster not found"));
        d.setFillLevel(fillLevel);
        d.setLastUpdate(date);
        return dumpsterRepository.save(d);
    }

    //FUNCION: CREATE DUMPSTER
    public Dumpster createDumpster(Dumpster dumpster) {
        return dumpsterRepository.save(dumpster);
    }

    
    //FUNCION: QUERY DUMPSTER USAGE
    public List<Dumpster> queryUsage(long id, Date start, Date end) {
        return dumpsterRepository.findById(id).stream()
                .filter(d -> !d.getLastUpdate().before(start) && !d.getLastUpdate().after(end))
                .collect(Collectors.toList());
    }

    //FUNCION: CHECK DUMPSTER STATUS
    public String getDumpsterStatus(float fillLevel) {
        if (fillLevel < 0.7f) return "GREEN";
        if (fillLevel < 0.95f) return "ORANGE";
        return "RED";
    }

    
    public List<Dumpster> getDumpstersByPostalCode(String postalCode, Date date) {
        return dumpsterRepository.findAll().stream()
                .filter(d -> d.getLocation().contains(postalCode) && d.getLastUpdate().equals(date))
                .collect(Collectors.toList());
    }
}

