package es.deusto.sd.ecoembes.facade;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ecoembes")
@Tag(name = "Ecoembes Controller", description = "Operations related to dumpsters, recycling plants, and assignments")
public class EcoembesController {

    private final AssignmentService assignmentService;

    public EcoembesController(
                              AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // -----------------------
    // Dumpsters endpoints
    // -----------------------

    //FUNCION: CREATE NEW DUMPSTER
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

    //FUNCION: UPDATE DUMPSTER INFO
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

    
    //FUNCION: CHECK DUMPSTER STATUS
    @Operation(summary = "Check dumpster status",description = "Returns dumpsters and status")
    	@GetMapping("/dumpsters")
    	public ResponseEntity<List<Map<String, Object>>> getDumpstersByPostalCode(
    	        @RequestParam String postalCode,
    	        @RequestParam Date date) {

    	    List<Map<String, Object>> results = dumpsterService.getDumpstersByPostalCode(postalCode, date)
    	            .stream()
    	            .map(d -> {
    	                Map<String, Object> dumpsterMap = new HashMap<>();

    	                dumpsterMap.put("id", d.getId());
    	                dumpsterMap.put("location", d.getLocation());
    	                dumpsterMap.put("maxCapacity", d.getMaxCapacity());
    	                dumpsterMap.put("fillLevel", d.getFillLevel());
    	                dumpsterMap.put("lastUpdate", d.getLastUpdate());
    	                dumpsterMap.put("status", dumpsterService.getDumpsterStatus(d.getFillLevel()));

    	                return dumpsterMap;
    	            })
    	            .toList();

    	    if (results.isEmpty()) {
    	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	    } else {
    	        return new ResponseEntity<>(results, HttpStatus.OK);
    	    }
    	}

    
    //FUNCION: QUERY DUMPSTER USAGE 
    @Operation(summary = "Query dumpster usage", description = "Returns the usage between two dates")
    @GetMapping("/dumpsters/{id}/usage")
    public ResponseEntity<List<FillLevelRecordDTO>> getUsage(
            @PathVariable long id,
            @RequestParam Date start,
            @RequestParam Date end) {

        try {
            List<FillLevelRecordDTO> history = dumpsterService
                    .getDumpsterById(id)
                    .getFillHistory()
                    .stream()
                    .filter(r -> !r.getDate().before(start) && !r.getDate().after(end))
                    .map(r -> new FillLevelRecordDTO(r.getDate(), r.getFillLevel()))
                    .toList();

            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // -----------------------
    // Recycling Plants endpoints
    // -----------------------

    //FUNCION: CHECK PLANT CAPACITY
    @Operation(summary = "Check recycling plant capacity", description = "Returns the current capacity of a plant")
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

    //FUNCION: ASSIGN DUMPSTERS TO A PLANT
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
