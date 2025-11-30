package es.deusto.sd.ecoembes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

@Service
public class AssignmentService {

    // Registro en memoria de asignaciones: dumpsterId -> lista de assignments
    private Map<Long, List<Assignment>> dumpsterAssignments = new HashMap<>();

    /**
     * Registra las asignaciones de un conjunto de dumpsters a un plant y empleado.
     */
    
    
    //FUNCION: ASSIGN DUMPSTERS TO RECYCLING PLANT
    public void assignDumpstersToPlant(List<Dumpster> dumpsters, RecyclingPlant plant, Employee employee) {
        for (Dumpster dumpster : dumpsters) {
            // Aquí puedes añadir lógica de capacidad si quieres
            Assignment newAssignment = new Assignment();
            newAssignment.setEmployee(employee);
            newAssignment.setRecyclingPlant(plant);
            newAssignment.setDumpsters(List.of(dumpster));
            
            // Añadir a la lista de assignments del dumpster
            dumpsterAssignments.compute(dumpster.getId(), (id, assignments) -> {
                if (assignments == null) {
                    return List.of(newAssignment);
                } else {
                    assignments.add(newAssignment);
                    return assignments;
                }
            });
            
            // También actualizar la lista de assignments en el dumpster
            List<Assignment> currentAssignments = dumpster.getAssignments();
            if (currentAssignments != null) {
                currentAssignments.add(newAssignment);
            } else {
                dumpster.setAssignments(List.of(newAssignment));
            }
        }
    }

    /**
     * Devuelve la lista de assignments a la que pertenece un dumpster.
     */
    public List<Assignment> getAssignments(Dumpster dumpster) {
        return dumpsterAssignments.getOrDefault(dumpster.getId(), List.of());
    }

    /**
     * Devuelve todas las plantas a las que un dumpster ha sido asignado.
     */
    public List<RecyclingPlant> getAssignedPlants(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
                .map(Assignment::getRecyclingPlant)
                .distinct()
                .toList();
    }

    /**
     * Devuelve los empleados que han hecho asignaciones a un dumpster.
     */
    public List<Employee> getAssignmentEmployees(Dumpster dumpster) {
        return getAssignments(dumpster).stream()
                .map(Assignment::getEmployee)
                .distinct()
                .toList();
    }
}
