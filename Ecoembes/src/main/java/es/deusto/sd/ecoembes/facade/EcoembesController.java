package es.deusto.sd.ecoembes.facade;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.dto.FillLevelRecordDTO;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.service.AssignmentService;
import es.deusto.sd.ecoembes.service.DumpsterService; // Import necesario
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ecoembes")
@Tag(name = "Ecoembes Controller", description = "Operations related to dumpsters, recycling plants, and assignments")
public class EcoembesController {

    private final AssignmentService assignmentService;
    private final DumpsterService dumpsterService; // 1. Añadido el servicio faltante

    // 2. Inyección de dependencias actualizada
    public EcoembesController(AssignmentService assignmentService, DumpsterService dumpsterService) {
        this.assignmentService = assignmentService;
        this.dumpsterService = dumpsterService;
    }

    // -----------------------
    // Dumpsters endpoints
    // -----------------------

    // FUNCION: CREATE NEW DUMPSTER
    @Operation(summary = "Create a new dumpster", description = "Adds a new dumpster to the system")
    @PostMapping("/dumpsters")
    public ResponseEntity<DumpsterDTO> createDumpster(@RequestBody Dumpster dumpster) {
        Dumpster d = dumpsterService.createDumpster(dumpster);
        
        // CORRECCIÓN: Usamos el método toDTO del servicio.
        // El constructor manual new DumpsterDTO(...) fallaba porque Dumpster no tiene getFillLevel() directamente.
        return new ResponseEntity<>(dumpsterService.toDTO(d), HttpStatus.CREATED);
    }

    // FUNCION: UPDATE DUMPSTER INFO
    @Operation(summary = "Update dumpster info", description = "Updates the fill level and last update of a dumpster")
    @PutMapping("/dumpsters/{id}")
    public ResponseEntity<DumpsterDTO> updateDumpster(@PathVariable long id,
                                                      @RequestParam float fillLevel,
                                                      @RequestParam Date date) {
        Dumpster d = dumpsterService.updateDumpsterInfo(id, fillLevel, date);
        // CORRECCIÓN: Usamos toDTO para evitar errores de compilación y lógica duplicada
        return new ResponseEntity<>(dumpsterService.toDTO(d), HttpStatus.OK);
    }

    // FUNCION: CHECK DUMPSTER STATUS
    @Operation(summary = "Check dumpster status", description = "Returns dumpsters and status")
    @GetMapping("/dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getDumpstersByPostalCode(
            @RequestParam String postalCode,
            @RequestParam Date date) {

        // CORRECCIÓN: Simplificado para devolver DTOs directos usando el servicio
        // Esto evita el error de "Cannot infer type arguments" al mezclar Maps y Objects
        List<DumpsterDTO> results = dumpsterService.getDumpstersByPostalCode(postalCode, date)
                .stream()
                .map(dumpsterService::toDTO)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }

    // FUNCION: QUERY DUMPSTER USAGE 
    @Operation(summary = "Query dumpster usage", description = "Returns the usage between two dates")
    @GetMapping("/dumpsters/{id}/usage")
    public ResponseEntity<List<FillLevelRecordDTO>> getUsage(
            @PathVariable long id,
            @RequestParam Date start,
            @RequestParam Date end) {

        try {
            // Usamos queryUsage del servicio que ya filtra por fechas
            List<FillLevelRecordDTO> history = dumpsterService.queryUsage(id, start, end)
                    .stream()
                    .map(r -> new FillLevelRecordDTO(r.getDate(), r.getFillLevel()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // -----------------------
    // Assignments endpoints
    // -----------------------
    
    // NOTA: He eliminado getPlantCapacity porque PlantService ha sido borrado.

    // FUNCION: ASSIGN DUMPSTERS TO A PLANT
    @Operation(summary = "Assign dumpsters to a plant", description = "Assigns dumpsters to a recycling plant by a given employee")
    @PostMapping("/assignments")
    public ResponseEntity<Void> assignDumpsters(@RequestParam List<Long> dumpsterIds,
                                                @RequestParam long plantId,
                                                @RequestParam long employeeId) {
        try {
            // CORRECCIÓN: Como PlantService no existe, no podemos hacer plantService.getById(plantId).
            // Creamos una instancia "proxy" con el ID para que JPA pueda hacer la relación (o para simularlo).
            RecyclingPlant plant = new RecyclingPlant();
            plant.setId(plantId); 
            // Nota: En un sistema real necesitarías un Repository para verificar que la planta existe realmente.

            List<Dumpster> dumpsters = dumpsterIds.stream()
                    .map(dumpsterService::getDumpsterById)
                    .collect(Collectors.toList());
            
            Employee employee = assignmentService.getEmployeeById(employeeId);
            
            assignmentService.assignDumpstersToPlant(dumpsters, plant, employee);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    } 
}