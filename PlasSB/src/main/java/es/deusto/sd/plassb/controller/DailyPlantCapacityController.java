package es.deusto.sd.plassb.controller;

import es.deusto.sd.plassb.dto.CapacityRequestDTO;
import es.deusto.sd.plassb.dto.CapacityResponseDTO;
import es.deusto.sd.plassb.entity.DailyPlantCapacity;
import es.deusto.sd.plassb.service.DailyPlantCapacityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/capacity") // <--- Coincide con tu CAPACITY_URL
public class DailyPlantCapacityController {

    private final DailyPlantCapacityService service;

    public DailyPlantCapacityController(DailyPlantCapacityService service) {
        this.service = service;
    }

    // GET: Listar todas
    @GetMapping
    public List<DailyPlantCapacity> getAll() {
        return service.getAll();
    }

    // POST: Crear nuevo registro (Admin local)
    // Al recibir el JSON, Jackson mapeará automáticamente el campo "date" (String) al objeto.
    @PostMapping
    public DailyPlantCapacity create(@RequestBody DailyPlantCapacity capacity) {
        return service.save(capacity);
    }

    // POST: Consultar capacidad (Para Ecoembes)
    @PostMapping("/check")
    public ResponseEntity<CapacityResponseDTO> checkCapacity(@RequestBody CapacityRequestDTO request) {
        
        // request.getDate() ya devuelve un String (ej: "2025-11-15")
        Optional<Float> capacity = service.getCapacityByPlantAndDate(
                request.getPlantId(), 
                request.getDate()
        );

        if (capacity.isPresent()) {
            CapacityResponseDTO response = new CapacityResponseDTO(
                    request.getPlantId(), 
                    capacity.get(), 
                    request.getDate()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}