package es.deusto.sd.ecoembes.facade;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.dto.RecyclingPlantDTO;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.service.DumpsterService;
import es.deusto.sd.ecoembes.service.RecyclingPlantService;
import es.deusto.sd.ecoembes.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ecoembes")
@Tag(name = "Ecoembes Controller", description = "Operations related to dumpsters, recycling plants, and assignments")
public class EcoembesController {

    private final DumpsterService dumpsterService;
    private final RecyclingPlantService plantService;
    private final AssignmentService assignmentService;

    public EcoembesController(DumpsterService dumpsterService,
                              RecyclingPlantService plantService,
                              AssignmentService assignmentService) {
        this.dumpsterService = dumpsterService;
        this.plantService = plantService;
        this.assignmentService = assignmentService;
    }

    // -----------------------
    // Dumpsters endpoints
    // -----------------------

    @Operation(summary = "Create a new dumpster", description = "Adds a new dumpster to the system")
    @PostMapping("/dumpsters")
    public ResponseEntity<DumpsterDTO> createDumpster(@RequestBody Dumpster dumpster) {
        Dumpster d = dumpsterService.createDumpster(dumpster);
        return new ResponseEntity<>(new DumpsterDTO(
                d.getId(),
                d.getLocation(),
                d.getMaxCapacity(),
                d.getFillLevel(),
                d.getLastUpdate(),
                null // fillHistory puede ser null por ahora
        ), HttpStatus.CREATED);
    }

    @Operation(summary = "Update dumpster info", description = "Updates the fill level and last update of a dumpster")
    @PutMapping("/dumpsters/{id}")
    public ResponseEntity<DumpsterDTO> updateDumpster(@PathVariable long id,
                                                      @RequestParam float fillLevel,
                                                      @RequestParam Date date) {
        Dumpster d = dumpsterService.updateDumpsterInfo(id, fillLevel, date);
        return new ResponseEntity<>(new DumpsterDTO(
                d.getId(),
                d.getLocation(),
                d.getMaxCapacity(),
                d.getFillLevel(),
                d.getLastUpdate(),
                null
        ), HttpStatus.OK);
    }

    @Operation(summary = "Get dumpsters by postal code and date", description = "Returns dumpsters in a given postal code on a specific date")
    @GetMapping("/dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getDumpstersByPostalCode(@RequestParam String postalCode,
                                                                      @RequestParam Date date) {
        List<DumpsterDTO> dtos = dumpsterService.getDumpstersByPostalCode(postalCode, date)
                .stream()
                .map(d -> new DumpsterDTO(d.getId(), d.getLocation(), d.getMaxCapacity(),
                        d.getFillLevel(), d.getLastUpdate(), null))
                .collect(Collectors.toList());

        if (dtos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // -----------------------
    // Recycling Plants endpoints
    // -----------------------

    @Operation(summary = "Get recycling plant capacity", description = "Returns the current capacity of a plant")
    @GetMapping("/plants/{id}/capacity")
    public ResponseEntity<Float> getPlantCapacity(@PathVariable long id) {
        try {
            float capacity = plantService.getCapacity(id);
            return new ResponseEntity<>(capacity, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // -----------------------
    // Assignments endpoints
    // -----------------------

    @Operation(summary = "Assign dumpsters to a plant", description = "Assigns dumpsters to a recycling plant by a given employee")
    @PostMapping("/assignments")
    public ResponseEntity<Void> assignDumpsters(@RequestParam List<Long> dumpsterIds,
                                                @RequestParam long plantId,
                                                @RequestParam long employeeId) {
        try {
            RecyclingPlant plant = plantService.getPlantById(plantId);
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
