package es.deusto.sd.ecoembes.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.ecoembes.dao.EmployeeRepository;
import es.deusto.sd.ecoembes.dao.RecyclingPlantRepository;
import es.deusto.sd.ecoembes.dao.AssignmentRepository;
import es.deusto.sd.ecoembes.dao.DumpsterRepository; // Importante
import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.service.AssignmentService;
import es.deusto.sd.ecoembes.service.CapacityExternalService;
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
    // ✅ 1. AÑADIDO (Faltaba en tu código anterior)
    private final DumpsterRepository dumpsterRepository; 
    private final CapacityExternalService capacityExternalService;

    // ✅ 2. CONSTRUCTOR ACTUALIZADO (Con todos los repositorios)
    public EcoembesController(DumpsterService dumpsterService, 
                              AssignmentService assignmentService,
                              RecyclingPlantRepository recyclingPlantRepository,
                              EmployeeRepository employeeRepository,
                              AssignmentRepository assignmentRepository,
                              DumpsterRepository dumpsterRepository,
                              CapacityExternalService capacityExternalService) {
        this.dumpsterService = dumpsterService;
        this.assignmentService = assignmentService;
        this.recyclingPlantRepository = recyclingPlantRepository;
        this.employeeRepository = employeeRepository;
        this.assignmentRepository = assignmentRepository;
        this.dumpsterRepository = dumpsterRepository;
        this.capacityExternalService = capacityExternalService;
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

    // ❌ HE BORRADO EL MÉTODO 'assignDumpsters' QUE DABA CONFLICTO ❌
    
    // --- 2. CREAR ASIGNACIÓN (POST) ---
    // ✅ ESTE ES EL BUENO (Usa AssignmentRequest y gestiona la lista de IDs)
    @Operation(summary = "Create Assignment", description = "Assign dumpsters to a plant")
    @PostMapping("/assignments")
    public ResponseEntity<?> createAssignment(@RequestBody AssignmentRequest request) {
        try {
            // 1. Validar que vengan datos
            if (request.dumpsterIds == null || request.dumpsterIds.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ Error: No has enviado ningún contenedor (dumpsterIds vacío).");
            }

            // 2. Buscar los objetos en BD
            RecyclingPlant plant = recyclingPlantRepository.findById(request.recyclingPlantId)
                    .orElseThrow(() -> new RuntimeException("Planta no encontrada"));
            
            Employee employee = employeeRepository.findById(request.employeeId)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

            // Aquí usamos el repositorio que faltaba antes
            List<Dumpster> dumpsters = dumpsterRepository.findAllById(request.dumpsterIds);
            
            if(dumpsters.isEmpty()) {
                 return ResponseEntity.badRequest().body("❌ Error: Los IDs de contenedores no existen en BD.");
            }

            // 3. Llamar al servicio
            assignmentService.assignDumpstersToPlant(dumpsters, plant, employee);

            // ---------------------------------------------------------------
            // 2. NUEVO: CALCULAR TOTALES Y AVISAR A LA PLANTA EXTERNA
            // ---------------------------------------------------------------
            
            // Calcular basura total (Suma de los fillLevels de la lista)
            float totalWaste = (float) dumpsters.stream()
                .mapToDouble(d -> d.getFillLevel() * d.getMaxCapacity()) // Ojo: fillLevel suele ser % (0.0 - 1.0) o litros directos. Ajusta según tu lógica.
                // Si fillLevel es volumen directo: .mapToDouble(Dumpster::getFillLevel)
                .sum();

            int totalDumpsters = dumpsters.size();

            // Llamar al servicio externo (Socket o REST según la planta)
            // Usamos la fecha actual o la que venga en el request
            String date = LocalDate.now().toString();
            
            boolean notificado = capacityExternalService.notifyAssignment(
                plant.getId(), 
                date, 
                totalDumpsters, 
                totalWaste
            );

            if (notificado) {
                return ResponseEntity.ok("✅ Asignación creada y Planta notificada.");
            } else {
                return ResponseEntity.ok("⚠️ Asignación creada, pero falló la notificación a la planta.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // --- 3. ACTUALIZAR NIVEL DE LLENADO ---
    @Operation(summary = "Update Fill Level", description = "Register a new fill level for a dumpster")
    @PutMapping("/dumpsters/{id}/fill-level")
    public ResponseEntity<?> updateFillLevel(@PathVariable("id") long id, @RequestBody FillRequest request) {
        try {
            // Fíjate que el servicio ahora devuelve un Dumpster actualizado
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
    
    // --- 8. CREAR CONTENEDOR ---
    @Operation(summary = "Create Dumpster", description = "Create a new dumpster manually")
    @PostMapping("/dumpsters")
    public ResponseEntity<?> createDumpster(@RequestBody DumpsterDTO dto) { // Cambiamos a ResponseEntity<?> para devolver String de error
        try {
            // Imprimir en consola para ver si llega el dato
            System.out.println("Recibiendo petición POST Dumpster: " + dto.getLocation());

            Dumpster newDumpster = new Dumpster(
                dto.getLocation(), 
                dto.getMaxCapacity(), 
                dto.getContainerCount(), 
                dto.getFillLevel()
            );
            
            Dumpster saved = dumpsterService.createDumpster(newDumpster);
            
            return ResponseEntity.ok(dumpsterService.toDTO(saved));
            
        } catch (Exception e) {
            e.printStackTrace(); // MIRA LA CONSOLA DE ECLIPSE/INTELLIJ
            // Devolvemos el mensaje de error para que lo veas en Postman
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }
    // ==========================================
    // CLASES INTERNAS (DTOs Rápidos)
    // ==========================================

    // Clase para recibir el JSON del cliente (La que usa tu compañera)
    public static class AssignmentRequest {
        public Long recyclingPlantId; 
        public Long employeeId;
        public List<Long> dumpsterIds; 
    }

    // DTO para fill-level
    public static class FillRequest {
        private float fillLevel;
        private String date;

        public float getFillLevel() { return fillLevel; }
        public void setFillLevel(float fillLevel) { this.fillLevel = fillLevel; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }

    public DumpsterDTO toDTO(Dumpster d) {
        DumpsterDTO dto = new DumpsterDTO();
        dto.setId(d.getId());
        dto.setLocation(d.getLocation());
        dto.setMaxCapacity(d.getMaxCapacity());
        dto.setContainerCount(d.getContainerCount());
        dto.setFillLevel(d.getFillLevel());
        return dto;
    }
}