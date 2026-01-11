package es.deusto.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.dao.AssignmentRepository;
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Registra UNA asignación con VARIOS contenedores
     */
    public Assignment assignDumpstersToPlant(
            List<Dumpster> dumpsters,
            RecyclingPlant plant,
            Employee employee
    ) {
        String today = LocalDate.now().toString();

        Assignment assignment = new Assignment();
        assignment.setDate(today);
        assignment.setEmployee(employee);
        assignment.setRecyclingPlant(plant);
        assignment.setDumpsters(dumpsters); // todos de golpe
        assignmentRepository.save(assignment);

        return assignmentRepository.save(assignment);
    }

    /**
     * Devuelve todas las asignaciones
     */
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    /**
     * Buscar empleado por ID en el historial
     */
    public Employee getEmployeeById(long employeeId) {
        return assignmentRepository.findAll().stream()
                .map(Assignment::getEmployee)
                .filter(e -> e != null && e.getId() == employeeId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found in assignments history"));
    }
}
