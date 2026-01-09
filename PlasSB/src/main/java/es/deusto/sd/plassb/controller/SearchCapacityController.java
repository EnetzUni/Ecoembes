package es.deusto.sd.plassb.controller;

import es.deusto.sd.plassb.dto.CapacityRequestDTO;
import es.deusto.sd.plassb.dto.CapacityResponseDTO;
import es.deusto.sd.plassb.service.DailyPlantCapacityService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capacity")
public class SearchCapacityController {

    private final DailyPlantCapacityService service;

    public SearchCapacityController(DailyPlantCapacityService service) {
        this.service = service;
    }

    /**
     * POST /api/capacity
     * Recibe CapacityRequestDTO (plantId + dateString) 
     * Devuelve CapacityResponseDTO (plantId + capacity + dateString)
     */
    @PostMapping
    public CapacityResponseDTO getCapacity(@RequestBody CapacityRequestDTO request) {
        // request.getDate() es String, así que pasa directo al servicio actualizado
        float capacity = service.getCapacityByPlantAndDate(request.getPlantId(), request.getDate())
                                .orElse(0f); 
        
        return new CapacityResponseDTO(request.getPlantId(), capacity, request.getDate());
    }
}