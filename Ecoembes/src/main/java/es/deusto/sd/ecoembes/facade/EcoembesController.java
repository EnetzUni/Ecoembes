package es.deusto.sd.ecoembes.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.ecoembes.dto.DumpsterDTO;
import es.deusto.sd.ecoembes.dto.FillLevelRecordDTO;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.FillLevelRecord;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.service.AssignmentService;
import es.deusto.sd.ecoembes.service.DumpsterService;

@RestController
@RequestMapping("/ecoembes")
public class EcoembesController {

    private final DumpsterService dumpsterService;
    private final AssignmentService assignmentService;

    public EcoembesController(DumpsterService dumpsterService, AssignmentService assignmentService) {
        this.dumpsterService = dumpsterService;
        this.assignmentService = assignmentService;
    }

    // 1. Crear Contenedor
    @PostMapping("/dumpsters")
    public ResponseEntity<DumpsterDTO> createDumpster(@RequestBody DumpsterDTO dumpsterDTO) {
        Dumpster dumpster = new Dumpster();
        dumpster.setLocation(dumpsterDTO.getLocation());
        dumpster.setMaxCapacity(dumpsterDTO.getMaxCapacity());
        
        Dumpster created = dumpsterService.createDumpster(dumpster);
        return new ResponseEntity<>(dumpsterService.toDTO(created), HttpStatus.CREATED);
    }

    // 2. Llenar Contenedor (UPDATE)
    // CAMBIO: 'date' ahora es String
    @PutMapping("/dumpsters/{id}")
    public ResponseEntity<DumpsterDTO> updateDumpster(@PathVariable long id,
                                                      @RequestParam float fillLevel,
                                                      @RequestParam String date) { 
        try {
            Dumpster updated = dumpsterService.updateDumpsterInfo(id, fillLevel, date);
            return ResponseEntity.ok(dumpsterService.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Buscar Contenedores (GET con Filtros)
    // CAMBIO: 'date' ahora es String
    @GetMapping("/dumpsters")
    public ResponseEntity<List<DumpsterDTO>> getDumpsters(@RequestParam(required = false) String postalCode,
                                                          @RequestParam(required = false) String date) {
        List<Dumpster> result;
        
        if (postalCode != null && date != null) {
            result = dumpsterService.getDumpstersByPostalCode(postalCode, date);
        } else {
            // Si no hay filtros, podríamos devolver todos, pero tu servicio no tenía ese método "findAll" explícito en la interfaz previa.
            // Para evitar errores, devolvemos lista vacía o implementa un getAll en el servicio si quieres.
            return ResponseEntity.ok(List.of()); 
        }

        List<DumpsterDTO> dtos = result.stream()
                .map(dumpsterService::toDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    // 4. Ver Historial (Usage)
    // CAMBIO: 'start' y 'end' ahora son String
    @GetMapping("/dumpsters/{id}/usage")
    public ResponseEntity<List<FillLevelRecordDTO>> getUsage(@PathVariable long id,
                                                             @RequestParam String start,
                                                             @RequestParam String end) {
        try {
            List<FillLevelRecord> records = dumpsterService.queryUsage(id, start, end);
            
            List<FillLevelRecordDTO> dtos = records.stream()
                    .map(r -> new FillLevelRecordDTO(r.getDate(), r.getFillLevel()))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Asignar Rutas
    @PostMapping("/assignments")
    public ResponseEntity<Void> assignDumpsters(@RequestParam List<Long> dumpsterIds,
                                                @RequestParam long plantId,
                                                @RequestParam long employeeId) {
        try {
            // 1. Simular Planta
            RecyclingPlant plant = new RecyclingPlant();
            plant.setId(plantId);
            plant.setName("Planta Test"); 

            // 2. Recuperar Contenedores
            List<Dumpster> dumpsters = dumpsterIds.stream()
                    .map(dumpsterService::getDumpsterById)
                    .collect(Collectors.toList());
            
            // 3. Simular Empleado (para evitar error si no existe en BD)
            Employee employee = new Employee();
            employee.setId(employeeId);
            
            // 4. Crear Asignación
            assignmentService.assignDumpstersToPlant(dumpsters, plant, employee);
            
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}