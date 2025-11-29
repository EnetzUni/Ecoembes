package es.deusto.sd.ecoembes.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Dumpster;

@Service
public class DumpsterService {

    // Simulación de base de datos en memoria (en prototipo)
    private List<Dumpster> dumpsters;

    public Dumpster updateDumpsterInfo(long id, float fillLevel, Date date) {
        for (Dumpster d : dumpsters) {
            if (d.getId() == id) {
                d.setFillLevel(fillLevel);
                d.setDate(date);
                return d;
            }
        }
        throw new RuntimeException("Dumpster not found");
    }

    public Dumpster createDumpster(Dumpster dumpster) {
        dumpsters.add(dumpster);
        return dumpster;
    }

    public List<Dumpster> queryUsage(long id, Date start, Date end) {
        return dumpsters.stream()
                .filter(d -> d.getId() == id && d.getDate().after(start) && d.getDate().before(end))
                .collect(Collectors.toList());
    }

    public String getDumpsterStatus(float fillLevel) {
        if (fillLevel < 0.7f) return "GREEN";
        if (fillLevel < 0.95f) return "ORANGE";
        return "RED";
    }

    public List<Dumpster> getDumpstersByPostalCode(String postalCode, Date date) {
        return dumpsters.stream()
                .filter(d -> d.getLocation().contains(postalCode) && d.getDate().equals(date))
                .collect(Collectors.toList());
    }
}

