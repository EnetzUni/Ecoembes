package es.deusto.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    // Mapa: ID Contenedor -> Lista de Asignaciones
    private Map<Long, List<Assignment>> dumpsterAssignments = new HashMap<>();
    
    // YA NO NECESITAMOS 'assignmentPlantMap' porque Assignment tiene el campo RecyclingPlant.

    /**
     * Registra las asignaciones
     */
    public void assignDumpstersToPlant(List<Dumpster> dumpsters, RecyclingPlant plant, Employee employee) {
        // Crear asignación
        Assignment newAssignment = new Assignment();
        newAssignment.setEmployee(employee);
        
        // AHORA SI PODEMOS USAR ESTO DIRECTAMENTE:
        newAssignment.setRecyclingPlant(plant); 
        
        // Fecha como String (usamos fecha actual ISO)
        newAssignment.setDate(LocalDate.now().toString()); 
        
        newAssignment.setDumpsters(new ArrayList<>(dumpsters));

        // Actualizamos el mapa en memoria
        for (Dumpster dumpster : dumpsters) {
            dumpsterAssignments.compute(dumpster.getId(), (id, list) -> {
                if (list == null) list = new ArrayList<>();
                list.add(newAssignment);
                return list;
            });
        }
    }

    /**
     * Devuelve assignments de un contenedor
     */
    public List<Assignment> getAssignments(Dumpster dumpster) {
        return dumpsterAssignments.getOrDefault(dumpster.getId(), new ArrayList<>());
    }

    /**
     * Devuelve Plantas históricas de un contenedor
     */
    public List<RecyclingPlant> getAssignedPlants(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
                // AHORA SACAMOS LA PLANTA DIRECTAMENTE DE LA ENTIDAD ASSIGNMENT
                .map(Assignment::getRecyclingPlant) 
                .filter(plant -> plant != null)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Devuelve Empleados históricos
     */
    public List<Employee> getAssignmentEmployees(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
                .map(Assignment::getEmployee)
                .filter(employee -> employee != null)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Buscar empleado por ID en el historial (útil para el Controller)
     */
    public Employee getEmployeeById(long employeeId) {
        return dumpsterAssignments.values().stream()
                .flatMap(List::stream)
                .map(Assignment::getEmployee)
                .filter(e -> e != null && e.getId() == employeeId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found in assignments history"));
    }
}