package es.deusto.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.dao.AssignmentRepository;
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Service
public class AssignmentService {

    // 1. Declaramos la variable del repositorio
    private final AssignmentRepository assignmentRepository;

    // 2. Inyectamos el repositorio en el constructor
    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Registra las asignaciones (Una por cada contenedor)
     */
    public void assignDumpstersToPlant(List<Dumpster> dumpsters, RecyclingPlant plant, Employee employee) {
        String today = LocalDate.now().toString();

        for (Dumpster d : dumpsters) {
            Assignment assignment = new Assignment();
            assignment.setDate(today);
            assignment.setEmployee(employee);
            assignment.setRecyclingPlant(plant);
            assignment.setDumpster(d); // Asignación 1 a 1

            // 3. Guardamos en Base de Datos usando la variable que inyectamos
            assignmentRepository.save(assignment);
        }
    }

    /**
     * Devuelve assignments de un contenedor (desde BD)
     */
    public List<Assignment> getAssignments(Dumpster dumpster) {
        return assignmentRepository.findByDumpster(dumpster);
    }

    /**
     * Devuelve Plantas históricas de un contenedor
     */
    public List<RecyclingPlant> getAssignedPlants(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
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
     * Buscar empleado por ID en el historial
     * (Nota: Este método es ineficiente en BD real, pero cumple la función lógica que tenías)
     */
    public Employee getEmployeeById(long employeeId) {
        // Buscamos en TODAS las asignaciones (cuidado si hay muchas)
        return assignmentRepository.findAll().stream()
                .map(Assignment::getEmployee)
                .filter(e -> e != null && e.getId() == employeeId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found in assignments history"));
    }
}