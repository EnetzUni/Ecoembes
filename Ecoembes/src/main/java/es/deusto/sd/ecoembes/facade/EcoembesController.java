package es.deusto.sd.ecoembes.facade;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.ecoembes.dao.EmployeeRepository;
import es.deusto.sd.ecoembes.dao.RecyclingPlantRepository;
import es.deusto.sd.ecoembes.dao.AssignmentRepository;
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.service.AssignmentService;
import es.deusto.sd.ecoembes.service.DumpsterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ecoembes")
@Tag(name = "Ecoembes Controller", description = "Operations for Dumpsters and Assignments")
public class EcoembesController {

    private final DumpsterService dumpsterService;
    private final AssignmentService assignmentService;
    private final RecyclingPlantRepository recyclingPlantRepository;
    private final EmployeeRepository employeeRepository;
    private final AssignmentRepository assignmentRepository;

    public EcoembesController(DumpsterService dumpsterService, 
                              AssignmentService assignmentService,
                              RecyclingPlantRepository recyclingPlantRepository,
                              EmployeeRepository employeeRepository,
                              AssignmentRepository assignmentRepository) {
        this.dumpsterService = dumpsterService;
        this.assignmentService = assignmentService;
        this.recyclingPlantRepository = recyclingPlantRepository;
        this.employeeRepository = employeeRepository;
        this.assignmentRepository = assignmentRepository;
    }

    // --- 1. BUSCAR CONTENEDORES ---
    @Operation(summary = "Get Dumpsters", description = "Retrieve dumpsters, optionally filtered")
    @GetMapping("/dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getDumpsters(
            @RequestParam(name = "postalCode", required = false) String postalCode, 
            @RequestParam(name = "date", required = false) String date) {
        try {
            List<Dumpster> dumpsters = dumpsterService.getDumpstersByPostalCode(postalCode, date);
            List<DumpsterDTO> dtos = dumpsters.stream()
                    .map(dumpsterService::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- 2. ASIGNAR RUTAS (Nuevo: JSON con varios dumpsters) ---
    @Operation(summary = "Assign Dumpsters", description = "Assign dumpsters to a plant and employee")
    @PostMapping("/assignments")
    public ResponseEntity<Void> assignDumpsters(@RequestBody AssignRequest request) {
        try {
            RecyclingPlant plant = recyclingPlantRepository.findById(request.getPlantId())
                    .orElseThrow(() -> new RuntimeException("Planta no encontrada"));
            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            List<Dumpster> dumpsters = request.getDumpsterIds().stream()
                    .map(dumpsterService::getDumpsterById)
                    .collect(Collectors.toList());

            assignmentService.assignDumpstersToPlant(dumpsters, plant, employee);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // DTO para recibir JSON de asignación
    public static class AssignRequest {
        private List<Long> dumpsterIds;
        private long plantId;
        private long employeeId;

        public List<Long> getDumpsterIds() { return dumpsterIds; }
        public void setDumpsterIds(List<Long> dumpsterIds) { this.dumpsterIds = dumpsterIds; }

        public long getPlantId() { return plantId; }
        public void setPlantId(long plantId) { this.plantId = plantId; }

        public long getEmployeeId() { return employeeId; }
        public void setEmployeeId(long employeeId) { this.employeeId = employeeId; }
    }

    // --- 3. ACTUALIZAR NIVEL DE LLENADO ---
    @Operation(summary = "Update Fill Level", description = "Register a new fill level for a dumpster")
    @PutMapping("/dumpsters/{id}/fill-level")
    public ResponseEntity<?> updateFillLevel(@PathVariable("id") long id, @RequestBody FillRequest request) {
        try {
            Dumpster updatedDumpster = dumpsterService.updateDumpsterInfo(id, request.getFillLevel(), request.getDate());
            return ResponseEntity.ok(dumpsterService.toDTO(updatedDumpster));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // --- 4. CONSULTAR HISTORIAL ---
    @Operation(summary = "Get Usage History", description = "Get fill level history for a dumpster")
    @GetMapping("/dumpsters/{id}/usage")
    public ResponseEntity<List<FillLevelRecord>> getDumpsterUsage(
            @PathVariable("id") long id,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate) {
        try {
            List<FillLevelRecord> history = dumpsterService.queryUsage(id, startDate, endDate);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 5. LISTAR TODAS LAS PLANTAS ---
    @Operation(summary = "Get All Plants", description = "Retrieve a list of all recycling plants")
    @GetMapping("/plants")
    public ResponseEntity<List<RecyclingPlant>> getAllPlants() {
        return ResponseEntity.ok(recyclingPlantRepository.findAll());
    }

    // --- 6. LISTAR TODAS LAS ASIGNACIONES ---
    @Operation(summary = "Get All Assignments", description = "Retrieve a list of all route assignments")
    @GetMapping("/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentRepository.findAll());
    }

    // --- DTO para fill-level ---
    public static class FillRequest {
        private float fillLevel;
        private String date;

        public float getFillLevel() { return fillLevel; }
        public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }
}
