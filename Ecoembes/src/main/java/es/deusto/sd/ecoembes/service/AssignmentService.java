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

    // Registro en memoria (Si usas JPA/H2, esto debería ser un Repository, pero mantenemos tu lógica)
    private Map<Long, List<Assignment>> dumpsterAssignments = new HashMap<>();

    /**
     * Registra las asignaciones de un conjunto de dumpsters a un plant y empleado.
     */
    public void assignDumpstersToPlant(List<Dumpster> dumpsters, RecyclingPlant plant, Employee employee) {
        // Creamos una única asignación (Ruta) que contiene varios dumpsters
        Assignment newAssignment = new Assignment();
        newAssignment.setEmployee(employee);
        newAssignment.setRecyclingPlant(plant); // Ahora funciona porque añadimos el campo a Assignment
        newAssignment.setDate(new Date());      // Asignamos fecha actual por defecto
        newAssignment.setDumpsters(new ArrayList<>(dumpsters)); // Usamos ArrayList mutable

        // Actualizamos las relaciones para cada contenedor
        for (Dumpster dumpster : dumpsters) {
            
            // 1. Actualizar mapa en memoria
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

            // 2. Actualizar la entidad Dumpster (Relación bidireccional)
            // Nota: Ahora Dumpster tiene getAssignments() gracias a la corrección
            if (dumpster.getAssignments() == null) {
                dumpster.setAssignments(new ArrayList<>());
            }
            dumpster.getAssignments().add(newAssignment);
        }
    }

    /**
     * Devuelve la lista de assignments a la que pertenece un dumpster.
     */
    public List<Assignment> getAssignments(Dumpster dumpster) {
        // Opción A: Sacarlo del mapa en memoria
        return dumpsterAssignments.getOrDefault(dumpster.getId(), new ArrayList<>());
        
        // Opción B (Si usaras JPA puro): return dumpster.getAssignments();
    }

    /**
     * Devuelve todas las plantas a las que un dumpster ha sido asignado historicamente.
     */
    public List<RecyclingPlant> getAssignedPlants(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
                .map(Assignment::getRecyclingPlant)
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