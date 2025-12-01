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
     * Receives a CapacityRequestDTO (plantId + date) and returns a CapacityResponseDTO (plantId + capacity + date)
     */

    @PostMapping
    public CapacityResponseDTO getCapacity(@RequestBody CapacityRequestDTO request) {
        float capacity = service.getCapacityByPlantAndDate(request.getPlantId(), request.getDate())
                                .orElse(0f); // The capacity will be 0 if no capacity was found for that Plant and Date
        return new CapacityResponseDTO(request.getPlantId(), capacity, request.getDate());
    }
}
