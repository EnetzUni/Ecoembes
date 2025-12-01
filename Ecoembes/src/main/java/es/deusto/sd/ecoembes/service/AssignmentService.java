package es.deusto.sd.ecoembes.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Service
public class AssignmentService {

    // Relación Dumpster -> Lista de Asignaciones
    private Map<Long, List<Assignment>> dumpsterAssignments = new HashMap<>();
    
    // NUEVO: Relación Assignment -> Planta (ya que no podemos guardarla en la entidad Assignment)
    private Map<Assignment, RecyclingPlant> assignmentPlantMap = new HashMap<>();

    /**
     * Registra las asignaciones de un conjunto de dumpsters a un plant y empleado.
     */
    public void assignDumpstersToPlant(List<Dumpster> dumpsters, RecyclingPlant plant, Employee employee) {
        // Creamos la asignación "original" (sin campo plant)
        Assignment newAssignment = new Assignment();
        newAssignment.setEmployee(employee);
        newAssignment.setDate(new Date());
        
        // Si Assignment tiene setDumpsters, lo usamos. Si no, lo gestionamos solo en el mapa.
        // Asumimos que Assignment volvió a tener 'private List<Dumpster> dumpsters' como estaba en tu archivo 'Assignment.java'
        if (dumpsters != null) {
            newAssignment.setDumpsters(new ArrayList<>(dumpsters));
        }

        // 1. Guardamos la relación con la planta en el Servicio
        assignmentPlantMap.put(newAssignment, plant);

        // 2. Actualizamos el mapa de contenedores
        for (Dumpster dumpster : dumpsters) {
            dumpsterAssignments.compute(dumpster.getId(), (id, assignments) -> {
                if (assignments == null) {
                    List<Assignment> list = new ArrayList<>();
                    list.add(newAssignment);
                    return list;
                } else {
                    assignments.add(newAssignment);
                    return assignments;
                }
            });
            
            // NOTA: Como Dumpster volvió al estado original, NO llamamos a dumpster.setAssignments()
        }
    }

    /**
     * Devuelve la lista de assignments a la que pertenece un dumpster.
     */
    public List<Assignment> getAssignments(Dumpster dumpster) {
        return dumpsterAssignments.getOrDefault(dumpster.getId(), new ArrayList<>());
    }

    /**
     * Devuelve todas las plantas a las que un dumpster ha sido asignado.
     * Recuperamos la planta usando el mapa auxiliar 'assignmentPlantMap'.
     */
    public List<RecyclingPlant> getAssignedPlants(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
                .map(assignment -> assignmentPlantMap.get(assignment)) // Buscamos en el mapa
                .filter(plant -> plant != null)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Devuelve los empleados que han hecho asignaciones a un dumpster.
     */
    public List<Employee> getAssignmentEmployees(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
                .map(Assignment::getEmployee)
                .filter(employee -> employee != null)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Devuelve un empleado a partir de su ID buscando en el historial de asignaciones
     */
    public Employee getEmployeeById(long employeeId) {
        return dumpsterAssignments.values().stream()
                .flatMap(List::stream)
                .map(Assignment::getEmployee)
                .filter(e -> e != null && e.getId() == employeeId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found in current assignments"));
    }
}