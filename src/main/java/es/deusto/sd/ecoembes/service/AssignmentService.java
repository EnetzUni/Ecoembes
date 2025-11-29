package es.deusto.sd.ecoembes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Service
public class AssignmentService {

    // Mapa para registrar asignaciones (dumpsterId -> plantId)
    private Map<Long, Long> assignments = new HashMap<>();
    private Map<Long, Employee> assignmentByEmployee = new HashMap<>();

    public void assignDumpstersToPlant(List<Dumpster> dumpsters, RecyclingPlant plant, Employee employee) {
        for (Dumpster d : dumpsters) {
            if (d.getFillLevel() <= plant.getCapacity()) {
                assignments.put(d.getId(), plant.getId());
                assignmentByEmployee.put(d.getId(), employee);
            } else {
                throw new RuntimeException("Not enough capacity in plant");
            }
        }
    }

    public Long getAssignedPlant(long dumpsterId) {
        return assignments.get(dumpsterId);
    }

    public Employee getAssignmentEmployee(long dumpsterId) {
        return assignmentByEmployee.get(dumpsterId);
    }
}
